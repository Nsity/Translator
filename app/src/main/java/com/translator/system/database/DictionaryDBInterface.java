package com.translator.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.translator.system.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nsity on 13.04.17.
 */

public class DictionaryDBInterface extends ADBWorker {

    public static final String DICTIONARY_TABLE_NAME = "DICTIONARY";

    public static final String DICTIONARY_COLUMN_ID = " DICTIONARY_ID";
    public static final String DICTIONARY_COLUMN_LANG_FROM = "DICTIONARY_LANG_FROM";
    public static final String DICTIONARY_COLUMN_LANG_TO = "DICTIONARY_LANG_TO";

    public static final String DICTIONARY_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + DICTIONARY_TABLE_NAME + "("
            + DICTIONARY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DICTIONARY_COLUMN_LANG_FROM + " VARCHAR(255), "
            + DICTIONARY_COLUMN_LANG_TO + "  VARCHAR(255) " +
            ");";

    //----------------------------------------------------------------------------//

    public DictionaryDBInterface(Context context) {
        super(context);
    }

    public int save(JSONArray jsonArray, boolean dropAllData) {
        if(dropAllData) {
            delete(DICTIONARY_TABLE_NAME, null, null);
        }
        return saveLanguages(jsonArray);
    }

    private int saveLanguages(JSONArray jsonArray) {
        if ((CommonFunctions.StringIsNullOrEmpty(jsonArray.toString())) || (jsonArray.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> langValues = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                String langPair = jsonArray.getString(i);

                if(!CommonFunctions.StringIsNullOrEmpty(langPair)) {
                    String[] langs = langPair.split("-");
                    ContentValues cv = new ContentValues();

                    cv.put(DICTIONARY_COLUMN_LANG_FROM, langs[0]);
                    cv.put(DICTIONARY_COLUMN_LANG_TO, langs[1]);

                    langValues.add(cv);
                }
            }

            return insert(DICTIONARY_TABLE_NAME, ADBWorker.REPLACE, langValues);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public Cursor getLanguages() {
        String selectQuery = "SELECT * FROM " + DICTIONARY_TABLE_NAME;
        return getCursor(selectQuery, new String[]{ });
    }

}
