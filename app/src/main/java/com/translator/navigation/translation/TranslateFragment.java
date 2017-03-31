package com.translator.navigation.translation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ButtonBarLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.TranslationManager;
import com.translator.system.CommonFunctions;
import com.translator.system.ConnectionDialog;
import com.translator.system.Preferences;
import com.translator.system.Snackbar;
import com.translator.system.network.CallBack;
import com.translator.system.network.Server;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class TranslateFragment extends Fragment {

    //https://github.com/rmtheis/yandex-translator-java-api
    private EditText inputEditText;
    private TextView resultTextView;

    private TextView inputLangTextView, translationLangTextView;
    private ImageButton switchImageButton;

    public static final int LANG_REQUEST_CODE = 2;
    private View rootView;

    private Languages languages;
    private ImageButton clearButton;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //работа с элементами тулбара
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        inputLangTextView = (TextView) toolbar.findViewById(R.id.input_lang);
        translationLangTextView = (TextView) toolbar.findViewById(R.id.translation_lang);
        switchImageButton = (ImageButton) toolbar.findViewById(R.id.switch_lang);

        setUpActionButtonsOnToolbar();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_translate, container, false);

        inputEditText = (EditText) rootView.findViewById(R.id.input_text);
        resultTextView = (TextView) rootView.findViewById(R.id.result_text);
        clearButton = (ImageButton) rootView.findViewById(R.id.clear_button);


        //убираем клавиатуру и фокус с поля ввода, когда происходит нажатие за его пределами
        LinearLayout touchInterceptor = (LinearLayout) rootView.findViewById(R.id.touch_interceptor);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (inputEditText.isFocused()) {
                        Rect outRect = new Rect();
                        inputEditText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            inputEditText.clearFocus();
                            // Hide keyboard
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });


        //загружаем все языки из БД
        languages = new Languages(getActivity());

        //нужно для того, чтобы поле было многострочным, но оторажалась кнопка Готово
        inputEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
        inputEditText.setRawInputType(InputType.TYPE_CLASS_TEXT);


        //при нажатии делаем выделение
        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    clearButton.setColorFilter(getResources().getColor(R.color.listSelector));
                    return false;
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    clearButton.setColorFilter(Color.BLACK); // or null
                    return false;
                }
                return false;
            }

        });

        //очищаем поле ввода
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputEditText.setText("");
                resultTextView.setText("");
                clearButton.setVisibility(View.GONE);
            }
        });


        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    //если поле не пустое
                    if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString())) {
                        return;
                    }
                    //сохраняем перевод в историю
                    //TODO сделать глобальным, чтобы потом сохранять в избранное или как???
                    Translation translation = new Translation(getActivity());
                    translation.setInputText(inputEditText.getText().toString().trim());
                    translation.setInputLang(Preferences.get(Preferences.input_lang, getActivity()));
                    translation.setTranslationText(resultTextView.getText().toString().trim());
                    translation.setTranslationLang(Preferences.get(Preferences.translation_lang, getActivity()));
                    translation.setFavorite(false);
                    translation.setInHistory(true);

                    translation.save();

                }
            }
        });


        inputEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    inputEditText.clearFocus();
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });


        //отслеживаем изменения в поле ввода
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {

                //TODO добавить кэш и ошибки исправить
                // Прописываем то, что надо выполнить после изменения текста
                Log.i("TAG", inputEditText.getText().toString());
                String inputText = inputEditText.getText().toString();

                if(inputText.equals("")) {
                    resultTextView.setText("");
                    Server.getHttpClient().cancelRequests(getActivity(), true);
                }

                if(!inputText.equals("")) {

                    translateText(inputText);

                    TranslationManager.detect(getActivity(), inputText, Preferences.get(Preferences.input_lang, getActivity()), new CallBack<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {
                            try {
                                String lang = result.getString(getString(R.string.par_lang));
                                if(lang.equals(Preferences.get(Preferences.translation_lang, getActivity()))) {
                                    switchLanguages();
                                }
                                setInputLang(lang);


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFail(String message) {
                            resultTextView.setText("");

                        }
                    });
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //показываем или скрываем кнопку Очистить
                if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString())) {
                    clearButton.setVisibility(View.GONE);
                } else {
                    clearButton.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null)
            return;

        if (requestCode == LANG_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {

                String inputLang = Preferences.get(Preferences.input_lang, getActivity());
                String translationLang = Preferences.get(Preferences.translation_lang, getActivity());

                //меняем выбранный язык - если он совпадает с другим, то другой заменяем на старый
                String selectedLang = data.getStringExtra(LanguageActivity.SELECTED_LANG);
                if(!CommonFunctions.StringIsNullOrEmpty(selectedLang)) {
                    switch (data.getIntExtra(LanguageActivity.ACTION, 0)) {
                        case LanguageActivity.INPUT_LANG:
                            if(selectedLang.equals(translationLang)) {
                                Preferences.set(Preferences.translation_lang, inputLang, getActivity());
                                setTranslationLangTextView();
                            }
                            Preferences.set(Preferences.input_lang, selectedLang, getActivity());
                            setInputLangTextView();
                            break;
                        case LanguageActivity.TRANSLATION_LANG:
                            if(selectedLang.equals(inputLang)) {
                                Preferences.set(Preferences.input_lang, translationLang, getActivity());
                                setInputLangTextView();
                            }
                            Preferences.set(Preferences.translation_lang, selectedLang, getActivity());
                            setTranslationLangTextView();
                            break;

                    }
                }

                //переводим текст
                translateText(inputEditText.getText().toString());
            }
        }

    }


    private void setInputLangTextView() {
        String inputLang = Preferences.get(Preferences.input_lang, getActivity());
        inputLangTextView.setText(languages.findFullName(inputLang));
    }

    private void setTranslationLangTextView() {
        String translationLang = Preferences.get(Preferences.translation_lang, getActivity());
        translationLangTextView.setText(languages.findFullName(translationLang));
    }


    private void setUpActionButtonsOnToolbar() {

        if(inputEditText != null) {
            setInputLangTextView();
            inputLangTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LanguageActivity.class);
                    intent.putExtra(LanguageActivity.ACTION, LanguageActivity.INPUT_LANG);
                    startActivityForResult(intent, LANG_REQUEST_CODE);
                }
            });
        }

        if(translationLangTextView != null) {
            setTranslationLangTextView();
            translationLangTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LanguageActivity.class);
                    intent.putExtra(LanguageActivity.ACTION, LanguageActivity.TRANSLATION_LANG);
                    startActivityForResult(intent, LANG_REQUEST_CODE);
                }
            });

        }

        if(switchImageButton != null) {
            switchImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   //TODO
                    switchLanguages();

                    String inputText = inputEditText.getText().toString();

                   // inputEditText.setText("");
                    inputEditText.setText(resultTextView.getText().toString());
                    resultTextView.setText(inputText);
                   /* if(!CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString())) {
                        translateText(resultTextView.getText().toString());
                    }*/
                }
            });
        }
    }


    private void setInputLang(String lang) {
        Preferences.set(Preferences.input_lang, lang, getActivity());
        setInputLangTextView();
    }

    private void setTranslationLang(String lang) {
        Preferences.set(Preferences.translation_lang, lang, getActivity());
        setTranslationLangTextView();
    }

    private void switchLanguages() {
        String inputLangDisplay = inputLangTextView.getText().toString();
        inputLangTextView.setText(translationLangTextView.getText().toString());
        translationLangTextView.setText(inputLangDisplay);

        String inputLang = Preferences.get(Preferences.input_lang, getActivity());
        String translationLang = Preferences.get(Preferences.translation_lang, getActivity());
        Preferences.set(Preferences.input_lang, translationLang, getActivity());
        Preferences.set(Preferences.translation_lang, inputLang, getActivity());
    }


    /**
     * Метод для перевода текста
     * @param inputText - текст, который необходимо перевести
     */
    private void translateText(final String inputText) {
        if(CommonFunctions.StringIsNullOrEmpty(inputText)) {
            return;
        }

        //направление перевода
        String langPair = Preferences.get(Preferences.input_lang, getActivity()) + "-" +
                Preferences.get(Preferences.translation_lang, getActivity());

        //нет соединения с Интернетом
        if(!Server.isOnline(getActivity())) {
            ConnectionDialog.showNoConnectionDialog(getActivity());
            return;
        }

        TranslationManager.translate(getActivity(), inputText, langPair, new CallBack<ArrayList<String>>() {
            @Override
            public void onSuccess(ArrayList<String> result) {
                //если поле ввода пустое, то очищаем результат
                if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString())) {
                    resultTextView.setText("");
                    return;
                }
                //записываем результат
                resultTextView.setText(listToStr(result));
            }

            @Override
            public void onFail(String message) {
                //очищаем поле перевода
                resultTextView.setText("");

                //показываем ошибку
                Snackbar.showLongMessage(getActivity(), rootView, message, Snackbar.SNACKBAR_FAIL);
            }
        });
    }


    /**
     * Переводит список переводов в строку
     * @param arr - список переводов
     * @return - строка
     */
    private String listToStr(ArrayList<String> arr) {
        String resStr = "";
        for (String str: arr) {
            resStr += str + "\n";
        }

        return resStr;
    }
}
