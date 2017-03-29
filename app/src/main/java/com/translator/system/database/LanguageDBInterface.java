package com.translator.system.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.translator.R;
import com.translator.system.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by nsity on 21.03.17.
 */

public class LanguageDBInterface extends ADBWorker {

    public static final String LANGUAGE_TABLE_NAME = "LANGUAGE";

    public static final String LANGUAGE_COLUMN_NAME = "LANGUAGE_NAME";
    public static final String LANGUAGE_COLUMN_FULL_NAME = "LANGUAGE_FULL_NAME";

    public static final String LANGUAGE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
            + LANGUAGE_TABLE_NAME + "("
            + LANGUAGE_COLUMN_NAME + " VARCHAR(255) PRIMARY KEY, "
            + LANGUAGE_COLUMN_FULL_NAME + "  VARCHAR(255) " +
            ");";

    //----------------------------------------------------------------------------//

    public LanguageDBInterface(Context context) {
        super(context);
    }



    public int save(JSONObject jsonArray, boolean dropAllData) {
        if(dropAllData) {
            delete(LANGUAGE_TABLE_NAME, null, null);
        }
        return saveLanguages(jsonArray);
    }

    private int saveLanguages(JSONObject jsonArray) {
        if ((CommonFunctions.StringIsNullOrEmpty(jsonArray.toString())) || (jsonArray.length() == 0))
            return 0;

        try {
            ArrayList<ContentValues> langValues = new ArrayList<>();
            Iterator<String> keys = jsonArray.keys();
            while(keys.hasNext())
            {
                String key = keys.next();
                String value = jsonArray.getString(key);
                ContentValues cv = new ContentValues();

                cv.put(LANGUAGE_COLUMN_NAME, key);
                cv.put(LANGUAGE_COLUMN_FULL_NAME, value);

                //добавляем языки
                langValues.add(cv);
            }

            return insert(LANGUAGE_TABLE_NAME, ADBWorker.REPLACE, langValues);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public Cursor getLanguages() {
        String selectQuery = "SELECT * FROM " + LANGUAGE_TABLE_NAME;
        return getCursor(selectQuery, new String[]{ });
    }


}
