package com.translator.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.translator.navigation.Translation;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by nsity on 17.04.17.
 */

public class CacheDBInterface extends ADBWorker {

    public static final int CACHE_SIZE = 1000;


    public static final String CACHE_TABLE_NAME = "CACHE";

    public static final String CACHE_COLUMN_ID = "CACHE_ID";
    public static final String CACHE_COLUMN_INPUT_TEXT = "CACHE_INPUT_TEXT";
    public static final String CACHE_COLUMN_TRANSLATION_TEXT = "CACHE_TRANSLATION_TEXT";
    public static final String CACHE_COLUMN_INPUT_LANG = "CACHE_INPUT_LANG";
    public static final String CACHE_COLUMN_TRANSLATION_LANG = "CACHE_TRANSLATION_LANG";
    public static final String CACHE_COLUMN_IN_FAVORITE = "CACHE_IN_FAVORITE";

    public static final String CACHE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + CACHE_TABLE_NAME + "("
            + CACHE_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CACHE_COLUMN_INPUT_TEXT + " TEXT, "
            + CACHE_COLUMN_TRANSLATION_TEXT + "  TEXT, "
            + CACHE_COLUMN_INPUT_LANG + " VARCHAR(255), "
            + CACHE_COLUMN_IN_FAVORITE + " BOOLEAN, "
            + CACHE_COLUMN_TRANSLATION_LANG + " VARCHAR(255) " +
            ");";

    public CacheDBInterface(Context context) {
        super(context);
    }

    public void updateTranslation(Translation translation) {
        ContentValues cv = new ContentValues();
        cv.put(CACHE_COLUMN_IN_FAVORITE, translation.isFavorite());

        update(CACHE_TABLE_NAME, cv, CACHE_COLUMN_ID + " =?", new String[] {String.valueOf(translation.getId())} );
    }


    public long saveTranslation(Translation translation) {
        if(getCacheSize() >= CACHE_SIZE) {
            delete(CACHE_TABLE_NAME, CACHE_COLUMN_ID + " = (SELECT  " +
                    CACHE_COLUMN_ID + " FROM " + CACHE_TABLE_NAME + " LIMIT 1) ", new String[]{});
        }

        delete(CACHE_TABLE_NAME, CACHE_COLUMN_INPUT_TEXT + " =? AND "
                        + CACHE_COLUMN_INPUT_LANG + " =? AND " +
                        CACHE_COLUMN_TRANSLATION_TEXT + " =? AND " + CACHE_COLUMN_TRANSLATION_LANG + " =? ",
                new String[] { translation.getInputText(), translation.getInputLang(),
                        translation.getTranslationText(), translation.getTranslationLang() } );

        ContentValues cv = new ContentValues();

        cv.put(CACHE_COLUMN_INPUT_TEXT, translation.getInputText());
        cv.put(CACHE_COLUMN_TRANSLATION_TEXT, translation.getTranslationText());
        cv.put(CACHE_COLUMN_INPUT_LANG, translation.getInputLang());
        cv.put(CACHE_COLUMN_TRANSLATION_LANG, translation.getTranslationLang());
        cv.put(CACHE_COLUMN_IN_FAVORITE, translation.isFavorite());

        return insert(CACHE_TABLE_NAME, cv);
    }


    private int getCacheSize() {
        String selectQuery = "SELECT * FROM " + CACHE_TABLE_NAME;

        Cursor cursor = getCursor(selectQuery, new String[]{ });
        int count = 0;
        if(cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }

        closeDB();

        return count;
    }



    public Cursor getCache() {
        String selectQuery = "SELECT * FROM " + CACHE_TABLE_NAME;

        return getCursor(selectQuery, new String[]{ });
    }




}
