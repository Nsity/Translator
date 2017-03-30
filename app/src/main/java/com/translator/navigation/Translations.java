package com.translator.navigation;

import android.content.Context;
import android.database.Cursor;

import com.translator.system.database.TranslationDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 29.03.17.
 */

public abstract class Translations {

    protected TranslationDBInterface db;
    protected Context context;

    protected ArrayList<Translation> arrayList;

    public Translations(Context context) {
        this.db = new TranslationDBInterface(context);
        this.arrayList = new ArrayList<>();
        this.context = context;
        loadFromDB();
    }

    protected void setTranslations(Cursor cursor) {
        if (cursor == null)
            return;

        arrayList.clear();
        if (cursor.moveToFirst()) {
            do {

                Translation translation = new Translation(context);
                translation.setId(cursor.getInt(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_ID)));
                translation.setInputText(cursor.getString(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_INPUT_TEXT)));
                translation.setTranslationText(cursor.getString(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_TRANSLATION_TEXT)));
                translation.setInputLang(cursor.getString(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_INPUT_LANG)));
                translation.setTranslationLang(cursor.getString(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_TRANSLATION_LANG)));
                translation.setInHistory(cursor.getInt(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_IN_HISTORY)) != 0);
                translation.setFavorite(cursor.getInt(cursor.getColumnIndex(TranslationDBInterface.TRANSLATION_COLUMN_IN_FAVORITE)) != 0);

                arrayList.add(translation);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();
    }


    protected abstract void loadFromDB();
    protected abstract void delete();

    protected abstract void deleteItem(int i);

    protected abstract void search(String query);

    public ArrayList<Translation> getTranslations() {
        return arrayList;
    }

}
