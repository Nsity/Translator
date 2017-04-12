package com.translator.navigation.translate.dictionary;

import android.content.Context;
import android.database.Cursor;

import com.translator.system.database.DictionaryDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 13.04.17.
 */

public class DictionaryPairs {

    private Context context;
    private DictionaryDBInterface db;
    private ArrayList<DictionaryPair> arrayList;

    public DictionaryPairs(Context context) {
        this.context = context;
        this.db = new DictionaryDBInterface(context);
        this.arrayList = new ArrayList<>();
        loadFromDB();
    }

    public void loadFromDB() {
        Cursor cursor = db.getLanguages();

        if (cursor == null)
            return;

        arrayList.clear();
        if (cursor.moveToFirst()){
            do {
                String langFrom = cursor.getString(cursor.getColumnIndex(DictionaryDBInterface.DICTIONARY_COLUMN_LANG_FROM));
                String langTo = cursor.getString(cursor.getColumnIndex(DictionaryDBInterface.DICTIONARY_COLUMN_LANG_TO));


                DictionaryPair langPair = new DictionaryPair();
                langPair.setLangFrom(langFrom);
                langPair.setLangTo(langTo);

                arrayList.add(langPair);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();
    }


    public ArrayList<DictionaryPair> getPairs() {
        return arrayList;
    }


    public boolean findPair(String langFrom, String langTo) {
        for (DictionaryPair dictionaryPair: arrayList) {
            if(dictionaryPair.getLangFrom().equals(langFrom) && dictionaryPair.getLangTo().equals(langTo)) {
                return true;
            }
        }

        return false;
    }
}
