package com.translator.navigation.translation;

import android.content.Context;
import android.database.Cursor;

import com.translator.system.Language;
import com.translator.system.database.LanguageDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class Languages {


    private LanguageDBInterface db;

    private ArrayList<Language> arrayList;

    public Languages(Context context) {
        this.db = new LanguageDBInterface(context);
        this.arrayList = new ArrayList<>();

        loadFromDB();
    }

    private void loadFromDB() {
        Cursor cursor = db.getLanguages();

        if (cursor == null)
            return;

        arrayList.clear();
        if (cursor.moveToFirst()){
            do{
                String langName = cursor.getString(cursor.getColumnIndex(LanguageDBInterface.LANGUAGE_COLUMN_NAME));
                String langFullName = cursor.getString(cursor.getColumnIndex(LanguageDBInterface.LANGUAGE_COLUMN_FULL_NAME));

                Language language = new Language();
                language.setName(langName);
                language.setFullName(langFullName);

                arrayList.add(language);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();

    }

    public ArrayList<Language> getLanguages() {
        return arrayList;
    }

}
