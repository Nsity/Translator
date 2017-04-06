package com.translator.navigation.translate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.DictionaryManager;
import com.translator.navigation.Translation;
import com.translator.navigation.TranslationManager;
import com.translator.navigation.translation.favorites.OnShowTranslationInterface;
import com.translator.system.CommonFunctions;
import com.translator.system.Preferences;
import com.translator.system.Snackbar;
import com.translator.system.database.TranslationDBInterface;
import com.translator.system.network.CallBack;
import com.translator.system.network.Server;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class TranslateFragment extends Fragment implements OnShowTranslationInterface {

    private EditText inputEditText;
    private TextView resultTextView, errorTextView, errorDescriptionTextView, inputLangTextView,
            translationLangTextView, dictionaryTextView;
    private ImageButton switchImageButton;

    public static final int LANG_REQUEST_CODE = 2;
    private View rootView;

    private Languages languages;
    private ImageButton clearButton;

    private RelativeLayout translationLayout;
    private LinearLayout errorLayout;

    private ImageButton favoriteButton;

    private boolean showTranslation = false;
    private Chache chache;

    public static final int DISABLED_FAVORITE = R.color.colorGray7;
    public static final int ACTIVE_FAVORITE =  R.color.colorPrimary;

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

        translationLayout = (RelativeLayout) rootView.findViewById(R.id.translation_layout);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);

        errorTextView = (TextView) rootView.findViewById(R.id.error_text);
        errorDescriptionTextView = (TextView) rootView.findViewById(R.id.error_description_text);
        favoriteButton = (ImageButton) rootView.findViewById(R.id.favorite_button);


        //кэш
        chache = new Chache();

        TextView yandexTranslateTextView = (TextView) rootView.findViewById(R.id.yandex_translate);

        //ссылка на Яндекс.Переводчик
        yandexTranslateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://translate.yandex.ru/"));
                startActivity(Intent.createChooser(browseIntent, getString(R.string.browser)));
            }
        });

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //если поле не пустое
                if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString()) ||
                        CommonFunctions.StringIsNullOrEmpty(resultTextView.getText().toString())) {
                    return;
                }
                updateFavoriteButton(!checkFavorite());

                Translation translation = getCurrentTranslation();

                translation.setFavorite(checkFavorite());
                translation.setInHistory(true);

                translation.save();

            }
        });


        Button tryAgainButton = (Button) rootView.findViewById(R.id.try_again_button);
        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Server.isOnline(getActivity())) {
                    return;
                }

                hideError();

                translateText(inputEditText.getText().toString());
            }
        });


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

        updateFavoriteButton(false);


        //при нажатии делаем выделение
        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    clearButton.setColorFilter(getResources().getColor(R.color.listSelector));
                    return false;
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    clearButton.setColorFilter(getResources().getColor(R.color.colorGray7)); // or null
                    return false;
                }
                return false;
            }

        });

        //очищаем поле ввода
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideError();

                inputEditText.setText("");
                clearButton.setVisibility(View.GONE);

                clearTranslation();
            }
        });


        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    inputEditText.clearFocus();
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //если поле не пустое
                    if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString()) ||
                            CommonFunctions.StringIsNullOrEmpty(resultTextView.getText().toString())) {
                        return;
                    }
                    //сохраняем перевод в историю
                    Translation translation = getCurrentTranslation();

                    translation.setFavorite(checkFavorite());
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

                    translateText(inputEditText.getText().toString());

                    return true;
                }
                return false;
            }
        });


        //отслеживаем изменения в поле ввода
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if(showTranslation) {
                    showTranslation = false;
                    return;
                }

                // Прописываем то, что надо выполнить после изменения текста
                //убираем лишние пробелы проблемы
                final String inputText = inputEditText.getText().toString().trim();

                if (inputText.equals("")) {
                    clearTranslation();
                    Server.getHttpClient().cancelRequests(getActivity(), true);
                    return;
                }

                //определяем исходный язык
                TranslationManager.detect(getActivity(), inputText, Preferences.get(Preferences.input_lang,
                        getActivity()), new CallBack<String>() {
                    @Override
                    public void onSuccess(String lang) {
                        //если не удалось определить язык или
                        //язык тот же, то просто переводим
                        if (CommonFunctions.StringIsNullOrEmpty(lang) ||
                                lang.equals(Preferences.get(Preferences.input_lang, getActivity()))) {
                            translateText(inputText);
                            return;
                        }

                        //если язык совпадает с языком перевода, то меняем их
                        if (lang.equals(Preferences.get(Preferences.translation_lang, getActivity()))) {
                            switchLanguages();
                        }
                        setInputLang(lang);

                        translateText(inputText);
                    }

                    @Override
                    public void onFail(String message) {
                        clearTranslation();

                        //показываем ошибку
                        Snackbar.showLongMessage(getActivity(), rootView, message, Snackbar.SNACKBAR_FAIL);

                    }
                });

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
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString()) ||
                    CommonFunctions.StringIsNullOrEmpty(resultTextView.getText().toString())) {
                return;
            }
            //если добавили/убрали из избранного
            updateFavoriteButton(new TranslationDBInterface(getActivity()).checkFavorite(getCurrentTranslation()));
        }
    }


    /**
     * метод для проверки состояния кнопки Избранное
     * @return - в избранном или нет
     */
    private boolean checkFavorite() {
        return (boolean)favoriteButton.getTag();
    }

    /**
     * метод для очистки превода
     */
    private void clearTranslation() {
        resultTextView.setText("");

        updateFavoriteButton(false);
        translationLayout.setVisibility(View.GONE);
    }

    /**
     * показ ошибки
     * @param error - название ошибки
     * @param errorDescription - описание
     */
    private void showError(String error, String errorDescription) {
        translationLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorTextView.setText(error);
        errorDescriptionTextView.setText(errorDescription);

    }


    /**
     * добавляет/удаляет из Избранного
     * @param isFavorite - избранное
     */
    private void updateFavoriteButton(boolean isFavorite) {
        if(isFavorite) {
            favoriteButton.setColorFilter(ContextCompat.getColor(getActivity(), ACTIVE_FAVORITE));
            favoriteButton.setTag(true);
        } else {
            favoriteButton.setColorFilter(ContextCompat.getColor(getActivity(), DISABLED_FAVORITE));
            favoriteButton.setTag(false);
        }

        //обновляем кэш, когда перевод добавляется/удаляется из Избранного
        Translation translation = getCurrentTranslation();
        translation.setFavorite(checkFavorite());
        chache.update(translation);
    }

    /**
     * скрытие ошибки
     */
    private void hideError() {
        translationLayout.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
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
                    switchLanguages();

                    String inputText = inputEditText.getText().toString().trim();
                    inputEditText.setText(resultTextView.getText().toString().trim());

                    try {
                        inputEditText.setSelection(resultTextView.getText().toString().trim().length());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    translateText(inputEditText.getText().toString());
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


    private Translation getCurrentTranslation() {
        Translation translation = new Translation(getActivity());

        translation.setInputText(inputEditText.getText().toString().trim());
        translation.setInputLang(Preferences.get(Preferences.input_lang, getActivity()));
        translation.setTranslationText(resultTextView.getText().toString().trim());
        translation.setTranslationLang(Preferences.get(Preferences.translation_lang, getActivity()));

        return translation;
    }


    private void setTranslation(Translation translation) {
        translationLayout.setVisibility(View.VISIBLE);

        //записываем результат
        if(translation.getInputText().equals(inputEditText.getText().toString())) {
            resultTextView.setText(translation.getTranslationText());
        }

        updateFavoriteButton(translation.isFavorite());
    }


    /**
     * метод для перевода текста
     * @param inputText - текст, который необходимо перевести
     */
    private void translateText(final String inputText) {
        if(CommonFunctions.StringIsNullOrEmpty(inputText)) {
            return;
        }
        hideError();

        final Translation translation = new Translation(getActivity());
        translation.setInputText(inputText);
        translation.setInputLang(Preferences.get(Preferences.input_lang, getActivity()));
        translation.setTranslationLang(Preferences.get(Preferences.translation_lang, getActivity()));


        //ищем перевод в кэше
        Translation translationInChache = chache.findInChache(translation);
        if(translationInChache != null) {
            setTranslation(translationInChache);
            return;
        }

        //нет соединения с Интернетом
        if(!Server.isOnline(getActivity())) {
            clearTranslation();
            showError(getString(R.string.connection_error), getString(R.string.check_internet_connection_and_try_again));
            return;
        }


        TranslationManager.translate(getActivity(), translation, new CallBack<Translation>() {
            @Override
            public void onSuccess(Translation result) {
                //если поле ввода пустое, то очищаем результат
                if(CommonFunctions.StringIsNullOrEmpty(inputEditText.getText().toString())) {
                    clearTranslation();
                    return;
                }

                setTranslation(result);

                //добавляем перевод в кэш
                chache.add(result);
                //lookupInDictionary(translation);
            }

            @Override
            public void onFail(String message) {
                //очищаем поле перевода
                clearTranslation();

                //показываем ошибку
                Snackbar.showLongMessage(getActivity(), rootView, message, Snackbar.SNACKBAR_FAIL);
            }
        });
    }





    private void lookupInDictionary(Translation translation) {
        hideError();

        //нет соединения с Интернетом
        if(!Server.isOnline(getActivity())) {
            clearTranslation();
            showError(getString(R.string.connection_error), getString(R.string.check_internet_connection_and_try_again));
            return;
        }

        DictionaryManager.lookup(getActivity(), translation, new CallBack<Translation>() {
            @Override
            public void onSuccess(Translation result) {
            }

            @Override
            public void onFail(String message) {
            }
        });

    }


    /**
     * открытие перевода с других страниц
     * @param translation - перевод
     */
    @Override
    public void showTranslation(Translation translation) {
        showTranslation = true;

        translationLayout.setVisibility(View.VISIBLE);

        resultTextView.setText(translation.getTranslationText());
        inputEditText.setText(translation.getInputText());

        setTranslationLang(translation.getTranslationLang());
        setInputLang(translation.getInputLang());

        updateFavoriteButton(translation.isFavorite());

    }
}
