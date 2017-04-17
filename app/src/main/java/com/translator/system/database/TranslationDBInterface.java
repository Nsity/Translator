package com.translator.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.translator.navigation.Translation;
import com.translator.system.CommonFunctions;

/**
 * Created by nsity on 29.03.17.
 */

public class TranslationDBInterface extends ADBWorker {

    public static final String TRANSLATION_TABLE_NAME = "TRANSLATION";

    public static final String TRANSLATION_COLUMN_ID = "TRANSLATION_ID";
    public static final String TRANSLATION_COLUMN_INPUT_TEXT = "TRANSLATION_INPUT_TEXT";
    public static final String TRANSLATION_COLUMN_TRANSLATION_TEXT = "TRANSLATION_TRANSLATION_TEXT";
    public static final String TRANSLATION_COLUMN_INPUT_LANG = "TRANSLATION_INPUT_LANG";
    public static final String TRANSLATION_COLUMN_TRANSLATION_LANG = "TRANSLATION_TRANSLATION_LANG";
    public static final String TRANSLATION_COLUMN_IN_HISTORY = "TRANSLATION_IN_HISTORY";
    public static final String TRANSLATION_COLUMN_IN_FAVORITE = "TRANSLATION_IN_FAVORITE";

    public static final String TRANSLATION_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + TRANSLATION_TABLE_NAME + "("
            + TRANSLATION_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TRANSLATION_COLUMN_INPUT_TEXT + " TEXT, "
            + TRANSLATION_COLUMN_TRANSLATION_TEXT + "  TEXT, "
            + TRANSLATION_COLUMN_INPUT_LANG + " VARCHAR(255), "
            + TRANSLATION_COLUMN_IN_HISTORY + " BOOLEAN, "
            + TRANSLATION_COLUMN_IN_FAVORITE + " BOOLEAN, "
            + TRANSLATION_COLUMN_TRANSLATION_LANG + " VARCHAR(255) " +
            ");";

    //----------------------------------------------------------------------------//


    public TranslationDBInterface(Context context) {
        super(context);
    }


    public boolean checkFavorite(Translation translation) {
        String selectQuery = "SELECT * FROM " + TRANSLATION_TABLE_NAME + " WHERE " + TRANSLATION_COLUMN_INPUT_TEXT +
                " =? AND " + TRANSLATION_COLUMN_INPUT_LANG + " =? AND " +
                TRANSLATION_COLUMN_TRANSLATION_TEXT + " =? AND " + TRANSLATION_COLUMN_TRANSLATION_LANG + " =? AND " +
                TRANSLATION_COLUMN_IN_FAVORITE + " = 1";

        Cursor cursor = getCursor(selectQuery, new String[]{translation.getInputText(),
                translation.getInputLang(), translation.getTranslationText(), translation.getTranslationLang()});

        boolean result = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }
        closeDB();

        return result;
    }


    public long saveTranslation(Translation translation) {

        delete(TRANSLATION_TABLE_NAME, TRANSLATION_COLUMN_INPUT_TEXT + " =? AND " + TRANSLATION_COLUMN_INPUT_LANG + " =? AND " +
                TRANSLATION_COLUMN_TRANSLATION_TEXT + " =? AND " + TRANSLATION_COLUMN_TRANSLATION_LANG + " =? ",
                new String[] { translation.getInputText(), translation.getInputLang(), translation.getTranslationText(), translation.getTranslationLang() } );

        ContentValues cv = new ContentValues();

        cv.put(TRANSLATION_COLUMN_INPUT_TEXT, translation.getInputText());
        cv.put(TRANSLATION_COLUMN_TRANSLATION_TEXT, translation.getTranslationText());
        cv.put(TRANSLATION_COLUMN_INPUT_LANG, translation.getInputLang());
        cv.put(TRANSLATION_COLUMN_TRANSLATION_LANG, translation.getTranslationLang());
        cv.put(TRANSLATION_COLUMN_IN_HISTORY, translation.isInHistory());
        cv.put(TRANSLATION_COLUMN_IN_FAVORITE, translation.isFavorite());

        return insert(TRANSLATION_TABLE_NAME, cv);
    }


    public long updateTranslation(Translation translation) {
        ContentValues cv = new ContentValues();

        cv.put(TRANSLATION_COLUMN_INPUT_TEXT, translation.getInputText());
        cv.put(TRANSLATION_COLUMN_TRANSLATION_TEXT, translation.getTranslationText());
        cv.put(TRANSLATION_COLUMN_INPUT_LANG, translation.getInputLang());
        cv.put(TRANSLATION_COLUMN_TRANSLATION_LANG, translation.getTranslationLang());
        cv.put(TRANSLATION_COLUMN_IN_HISTORY, translation.isInHistory());
        cv.put(TRANSLATION_COLUMN_IN_FAVORITE, translation.isFavorite());


        return update(TRANSLATION_TABLE_NAME, cv, TRANSLATION_COLUMN_ID + " =?", new String[] { String.valueOf(translation.getId()) });
    }

    public Cursor getHistory() {
        String selectQuery = "SELECT * FROM " + TRANSLATION_TABLE_NAME + " WHERE " + TRANSLATION_COLUMN_IN_HISTORY + " =? ORDER BY " + TRANSLATION_COLUMN_ID + " DESC";
        return getCursor(selectQuery, new String[]{ "1" });
    }

    public Cursor searchInHistory(String query) {
        String selectQuery = "SELECT * FROM " + TRANSLATION_TABLE_NAME + " WHERE " + TRANSLATION_COLUMN_IN_HISTORY + " =? AND" +
                " ( " + TRANSLATION_COLUMN_TRANSLATION_TEXT + " LIKE '%" + query + "%' OR " +
                TRANSLATION_COLUMN_INPUT_TEXT + " LIKE '%" + query + "%') " +
                " ORDER BY " + TRANSLATION_COLUMN_ID + " DESC";
        return getCursor(selectQuery, new String[]{ "1" });
    }


    public Cursor searchInFavorite(String query) {
        String selectQuery = "SELECT * FROM " + TRANSLATION_TABLE_NAME + " WHERE " + TRANSLATION_COLUMN_IN_FAVORITE + " =? AND" +
                " ( " + TRANSLATION_COLUMN_TRANSLATION_TEXT + " LIKE '%" + query + "%' OR " +
                TRANSLATION_COLUMN_INPUT_TEXT + " LIKE '%" + query + "%') " +
                " ORDER BY " + TRANSLATION_COLUMN_ID + " DESC";
        return getCursor(selectQuery, new String[]{ "1" });
    }

    public Cursor getFavorite() {
        String selectQuery = "SELECT * FROM " + TRANSLATION_TABLE_NAME + " WHERE " + TRANSLATION_COLUMN_IN_FAVORITE + " =? ORDER BY " + TRANSLATION_COLUMN_ID + " DESC";
        return getCursor(selectQuery, new String[]{ "1" });
    }


    public void deleteHistory() {
        ContentValues cv = new ContentValues();
        cv.put(TRANSLATION_COLUMN_IN_HISTORY, false);

        update(TRANSLATION_TABLE_NAME, cv, null, null);

        delete(TRANSLATION_TABLE_NAME, TRANSLATION_COLUMN_IN_FAVORITE + " =? AND " +
                TRANSLATION_TABLE_NAME + " = ?", new String[] {"1", "1"});
    }


    public void deleteHistoryItem(int id) {
        ContentValues cv = new ContentValues();
        cv.put(TRANSLATION_COLUMN_IN_HISTORY, false);

        update(TRANSLATION_TABLE_NAME, cv, TRANSLATION_COLUMN_ID + " =?", new String[] { String.valueOf(id) });

        delete(TRANSLATION_TABLE_NAME, TRANSLATION_COLUMN_IN_FAVORITE + " =? AND " +
                TRANSLATION_TABLE_NAME + " = ?", new String[] {"1", "1"});
    }


    public void deleteFavoriteItem(int id) {
        ContentValues cv = new ContentValues();
        cv.put(TRANSLATION_COLUMN_IN_FAVORITE, false);

        update(TRANSLATION_TABLE_NAME, cv, TRANSLATION_COLUMN_ID + " =?", new String[] { String.valueOf(id) });

        delete(TRANSLATION_TABLE_NAME, TRANSLATION_COLUMN_IN_FAVORITE + " =? AND " +
                TRANSLATION_TABLE_NAME + " = ?", new String[] {"1", "1"});
    }


    public void deleteFavorite() {
        ContentValues cv = new ContentValues();
        cv.put(TRANSLATION_COLUMN_IN_FAVORITE, false);

        update(TRANSLATION_TABLE_NAME, cv, null, null);

        delete(TRANSLATION_TABLE_NAME, TRANSLATION_COLUMN_IN_FAVORITE + " =? AND " +
                TRANSLATION_TABLE_NAME + " = ?", new String[] {"1", "1"});
    }
}
