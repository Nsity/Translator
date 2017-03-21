package com.translator.navigation.translation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.animation.AnimationUtils;

import com.translator.R;

/**
 * Created by fedorova on 21.03.2017.
 */

public class LanguageActivity extends AppCompatActivity {

    public static final int INPUT_LANG = 1;
    public static final int TRANSLATION_LANG = 2;

    public static final String ACTION = "action";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            setTitle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

    }


    private void setTitle() {
        Intent intent = getIntent();
        if (intent != null) {

            switch (intent.getIntExtra(ACTION, 0)) {
                case INPUT_LANG:
                    getSupportActionBar().setTitle(R.string.language_text);
                    break;
                case TRANSLATION_LANG:
                    getSupportActionBar().setTitle(R.string.language_translation);
                    break;
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
    }
}
