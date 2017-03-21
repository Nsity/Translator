package com.translator.navigation.translation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.TranslationManager;
import com.translator.system.network.CallBack;

import org.json.JSONException;
import org.json.JSONObject;

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



        inputEditText = (EditText) rootView.findViewById(R.id.input_text);
        resultTextView = (TextView) rootView.findViewById(R.id.result_text);


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        inputLangTextView = (TextView) toolbar.findViewById(R.id.input_lang);
        translationLangTextView = (TextView) toolbar.findViewById(R.id.translation_lang);


        inputLangTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LanguageActivity.class);
                intent.putExtra(LanguageActivity.ACTION, LanguageActivity.INPUT_LANG);
                startActivityForResult(intent, LANG_REQUEST_CODE);
            }
        });

        translationLangTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), LanguageActivity.class);
                intent.putExtra(LanguageActivity.ACTION, LanguageActivity.TRANSLATION_LANG);
                startActivityForResult(intent, LANG_REQUEST_CODE);
            }
        });




        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                // Прописываем то, что надо выполнить после изменения текста

                String inputText = inputEditText.getText().toString();

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
}
