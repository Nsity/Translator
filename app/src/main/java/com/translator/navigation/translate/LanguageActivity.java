package com.translator.navigation.translate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.translator.R;
import com.translator.system.Preferences;

import java.util.ArrayList;

/**
 * Created by fedorova on 21.03.2017.
 */

public class LanguageActivity extends AppCompatActivity {

    public static final int INPUT_LANG = 1;
    public static final int TRANSLATION_LANG = 2;

    public static final String ACTION = "action";
    public static final String SELECTED_LANG = "selected_lang";

    private ArrayList<Language> arrayList;
    private ListView languagesListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_language);

        languagesListView = (ListView) findViewById(R.id.languages_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            setTitle();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }

        setAdapter();
    }


    private void setAdapter() {
        final Intent intent = getIntent();
        if(intent == null) {
            return;
        }

        //выбираем язык, который нужно отметить в списке
        String selectedLang = Preferences.get(Preferences.input_lang, getApplicationContext());

        switch (intent.getIntExtra(ACTION, 0)) {
            case INPUT_LANG:
                selectedLang = Preferences.get(Preferences.input_lang, getApplicationContext());
                break;
            case TRANSLATION_LANG:
                selectedLang = Preferences.get(Preferences.translation_lang, getApplicationContext());
                break;
        }

        //получаем все языки
        Languages languages = new Languages(getApplicationContext());
        arrayList = languages.getLanguages();

        //создаем адаптер и устанавливаем его
        final LanguageAdapter languagesAdapter = new LanguageAdapter(getApplicationContext(), arrayList, selectedLang);
        languagesListView.setAdapter(languagesAdapter);

        //выбор языка
        languagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                languagesAdapter.update(arrayList.get(i).getName());

                intent.putExtra(SELECTED_LANG, arrayList.get(i).getName());
                setResult(RESULT_OK, intent);
                onBackPressed();

               /* switch (intent.getIntExtra(ACTION, 0)) {
                    case INPUT_LANG:
                        Preferences.set(Preferences.input_lang, arrayList.get(i).getName(), getApplicationContext());
                        languagesAdapter.update(arrayList.get(i).getName());
                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                    case TRANSLATION_LANG:
                        Preferences.set(Preferences.translation_lang, arrayList.get(i).getName(), getApplicationContext());
                        languagesAdapter.update(arrayList.get(i).getName());

                        setResult(RESULT_OK, intent);
                        onBackPressed();
                        break;
                }*/
            }
        });
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

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }
}
