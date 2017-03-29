package com.translator.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.translator.R;
import com.translator.system.CommonFunctions;
import com.translator.system.Preferences;
import com.translator.system.network.CallBack;
import com.translator.system.network.Server;

import java.util.Locale;

/**
 * Created by nsity on 21.03.17.
 */

public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //установка default input lang
        if(CommonFunctions.StringIsNullOrEmpty(Preferences.get(Preferences.input_lang, getApplicationContext()))) {
            Preferences.set(Preferences.input_lang, Locale.getDefault().getLanguage(), getApplicationContext());
        }

        //установка default translation lang
        if(CommonFunctions.StringIsNullOrEmpty(Preferences.get(Preferences.translation_lang, getApplicationContext()))) {
            Preferences.set(Preferences.translation_lang, "en", getApplicationContext());
        }


        //если нет Интернета, то просто загружаем главную
        if(!Server.isOnline(getApplicationContext())) {
            showSplash();
            return;
        }

        //иначе получаем список поддерживаемых языков
        loadLanguages();
    }


    private void showSplash() {
        int SPLASH_TIME_OUT = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                openMainActivity();
            }
        }, SPLASH_TIME_OUT);
    }


    private void openMainActivity() {
        Intent i = new Intent(SplashActivity.this, MainActivity.class);
        //i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(i);

        // close this activity
        finish();
        overridePendingTransition(0, 0);
    }


    private void loadLanguages() {
        TranslationManager.getLanguages(getApplicationContext(), Preferences.get(Preferences.input_lang, getApplicationContext()), new CallBack() {
            @Override
            public void onSuccess() {
                showSplash();
            }

            @Override
            public void onFail(String message) {
                showSplash();
            }
        });
    }
}
