package com.translator.system.database;

import android.content.Context;
import android.database.CharArrayBuffer;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by nsity on 20.03.17.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "TRANSLATOR";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(LanguageDBInterface.LANGUAGE_TABLE_CREATE);
        database.execSQL(TranslationDBInterface.TRANSLATION_TABLE_CREATE);
        database.execSQL(DictionaryDBInterface.DICTIONARY_TABLE_CREATE);
        database.execSQL(CacheDBInterface.CACHE_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LanguageDBInterface.LANGUAGE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TranslationDBInterface.TRANSLATION_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + DictionaryDBInterface.DICTIONARY_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CacheDBInterface.CACHE_TABLE_NAME);


        onCreate(db);
    }
}
