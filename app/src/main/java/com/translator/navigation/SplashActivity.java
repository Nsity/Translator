package com.translator.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.translate.Languages;
import com.translator.system.CommonFunctions;
import com.translator.system.Preferences;
import com.translator.system.network.CallBack;
import com.translator.system.network.Server;

import java.util.Locale;

/**
 * Created by nsity on 21.03.17.
 */

public class SplashActivity extends AppCompatActivity {


    private LinearLayout errorLayout;
    private TextView appNameTextView, errorTextView, errorDescriptionTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Button tryAgainButton = (Button) findViewById(R.id.try_again_button);
        errorLayout = (LinearLayout) findViewById(R.id.error_layout);
        appNameTextView = (TextView) findViewById(R.id.app_name);
        errorTextView = (TextView) findViewById(R.id.error_text);
        errorDescriptionTextView = (TextView) findViewById(R.id.error_description_text);

        tryAgainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Server.isOnline(getApplicationContext())) {
                    return;
                }

                hideError();

                loadLanguages();
            }
        });

        String ui_lang = Preferences.get(Preferences.ui_lang, getApplicationContext());

        Preferences.set(Preferences.ui_lang, Locale.getDefault().getLanguage(), getApplicationContext());

        //установка default input lang
        if(CommonFunctions.StringIsNullOrEmpty(Preferences.get(Preferences.input_lang, getApplicationContext()))) {
            Preferences.set(Preferences.input_lang, Locale.getDefault().getLanguage(), getApplicationContext());
        }

        //установка default translation lang
        if(CommonFunctions.StringIsNullOrEmpty(Preferences.get(Preferences.translation_lang, getApplicationContext()))) {
            if(Locale.getDefault().getLanguage().equals("en")) {
                Preferences.set(Preferences.translation_lang, "ru", getApplicationContext());
            } else {
                Preferences.set(Preferences.translation_lang, "en", getApplicationContext());
            }

        }

        //получаем список загруженных языков
        int langCount = new Languages(getApplicationContext()).getLanguages().size();

        //если нет Интернета и не загружены языки, то показываем ошибку
        if(!Server.isOnline(getApplicationContext()) && langCount == 0) {
            showError(getString(R.string.connection_error), getString(R.string.check_internet_connection_and_try_again));
            return;
        }

        if(!Locale.getDefault().getLanguage().equals(ui_lang)) {
            loadLanguages();
            return;
        }

        if(langCount != 0) {
            //показываем экран загрузки
            showSplash();
        } else {
            //иначе получаем список поддерживаемых языков
            loadLanguages();
        }

    }


    /**
     * показ ошибки
     * @param error - название ошибки
     * @param errorDescription - описание
     */
    private void showError(String error, String errorDescription) {
        appNameTextView.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        errorTextView.setText(error);
        errorDescriptionTextView.setText(errorDescription);
    }


    /**
     * скрытие ошибки
     */
    private void hideError() {
        appNameTextView.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
    }


    /**
     * показ экрана загрузки
     */
    private void showSplash() {
        int SPLASH_TIME_OUT = 1500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, SPLASH_TIME_OUT);
    }


    /**
     * открытие основного окна
     */
    private void openMainActivity() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);

        // close this activity
        finish();
        overridePendingTransition(0, 0);
    }


    /**
     * метод для загрузки поддерживаемых языков
     */
    private void loadLanguages() {
        TranslationManager.getLanguages(getApplicationContext(), Locale.getDefault().getLanguage(), new CallBack() {
            @Override
            public void onSuccess() {
                openMainActivity();
            }

            @Override
            public void onFail(String message) {
                showError(getString(R.string.error_occurred), getString(R.string.try_again_later));
            }
        });
    }
}
