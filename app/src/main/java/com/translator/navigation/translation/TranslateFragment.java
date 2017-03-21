package com.translator.navigation.translation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.TranslationManager;
import com.translator.system.database.HistoryDBInterface;
import com.translator.system.network.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.spec.ECField;

/**
 * Created by nsity on 18.03.17.
 */

public class TranslateFragment extends Fragment {

    //https://github.com/rmtheis/yandex-translator-java-api
    private EditText inputEditText;
    private TextView resultTextView;

    private TextView inputLangTextView, translationLangTextView;

    public static final int LANG_REQUEST_CODE = 2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_translate, container, false);

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
                            //
                            // Hide keyboard
                            //
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                    }
                }
                return false;
            }
        });


        inputEditText = (EditText) rootView.findViewById(R.id.input_text);
        resultTextView = (TextView) rootView.findViewById(R.id.result_text);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        inputLangTextView = (TextView) toolbar.findViewById(R.id.input_lang);
        translationLangTextView = (TextView) toolbar.findViewById(R.id.translation_lang);

        addActionOnToolbar();


        inputEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {


                    //save translation in history
                    Translation translation = new Translation();
                    translation.setInputText(inputEditText.getText().toString());
                    translation.setInputLang("");
                    translation.setTranslationText(resultTextView.getText().toString());
                    translation.setTranslationLang("");

                    HistoryDBInterface historyDBInterface = new HistoryDBInterface(getActivity());
                    historyDBInterface.saveTranslation(translation);


                }
            }
        });

        inputEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {

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


        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Прописываем то, что надо выполнить после изменения текста

                String inputText = inputEditText.getText().toString();


                if(inputText.equals("")) {
                    resultTextView.setText("");
                }

                if(!inputText.equals("")) {
                    TranslationManager.translate(getActivity(), inputText, "ru-en", new CallBack<JSONObject>() {
                        @Override
                        public void onSuccess(JSONObject result) {

                            try {
                                resultTextView.setText(result.getJSONArray("text").toString());
                            } catch (JSONException e) {
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
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }




    private void addActionOnToolbar() {
        if(inputEditText != null)
            inputLangTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LanguageActivity.class);
                    intent.putExtra(LanguageActivity.ACTION, LanguageActivity.INPUT_LANG);
                    startActivityForResult(intent, LANG_REQUEST_CODE);
                }
            });

        if(translationLangTextView != null)
            translationLangTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), LanguageActivity.class);
                    intent.putExtra(LanguageActivity.ACTION, LanguageActivity.TRANSLATION_LANG);
                    startActivityForResult(intent, LANG_REQUEST_CODE);
                }
            });
    }



}
