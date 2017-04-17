package com.translator.navigation.translate;

import android.content.Context;
import android.database.Cursor;

import com.translator.navigation.Translation;
import com.translator.navigation.translate.dictionary.dictResult.Tr;
import com.translator.system.database.CacheDBInterface;
import com.translator.system.database.TranslationDBInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by nsity on 17.04.17.
 */

public class Cache {

    private CacheDBInterface db;
    private ArrayList<Translation> arrayList;
    private Context context;

    private Comparator<Translation> comparator = new Comparator<Translation>() {
        public int compare(Translation translation1, Translation translation2) {
            if(translation1.getInputLang().equals(translation2.getInputLang()) &&
                    translation1.getInputText().equals(translation2.getInputText()) &&
                    translation1.getTranslationLang().equals(translation2.getTranslationLang())) {
                return 0;
            }
            return -1;
        }
    };

    public Cache(Context context) {
        this.context = context;
        this.db = new CacheDBInterface(context);
        this.arrayList = new ArrayList<>();
        loadFromDB();
    }


    public void loadFromDB() {
        arrayList.clear();

        Cursor cursor = db.getCache();

        arrayList.clear();
        if (cursor.moveToFirst()) {
            do {

                Translation translation = new Translation(context);
                translation.setId(cursor.getInt(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_ID)));
                translation.setInputText(cursor.getString(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_INPUT_TEXT)));
                translation.setTranslationText(cursor.getString(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_TRANSLATION_TEXT)));
                translation.setInputLang(cursor.getString(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_INPUT_LANG)));
                translation.setTranslationLang(cursor.getString(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_TRANSLATION_LANG)));
                translation.setFavorite(cursor.getInt(cursor.getColumnIndex(CacheDBInterface.CACHE_COLUMN_IN_FAVORITE)) != 0);

                arrayList.add(translation);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.closeDB();
    }


    /**
     * добавление перевода в кэш
     * @param translation - перевод, который нужно добавить
     */
    public void add(Translation translation) {
        /*if(arrayList.size() > CacheDBInterface.CACHE_SIZE && arrayList.size() > 0) {
            arrayList.remove(0);
        }

        int i = Collections.binarySearch(arrayList, translation, comparator);
        if(i >= 0) {
            arrayList.remove(i);
        }

        arrayList.add(translation);*/
        db.saveTranslation(translation);
        loadFromDB();
    }

     /**
     * обновление кэша
     * @param translation - перевод, который нужно обновить в кэше
     */
    public void updateFavorite(Translation translation) {

        int i = Collections.binarySearch(arrayList, translation, comparator);
        if(i >= 0) {

            Translation translationInCache = arrayList.get(i);
            translationInCache.setFavorite(translation.isFavorite());

            arrayList.remove(i);
            arrayList.add(i, translationInCache);

            db.updateTranslation(translationInCache);
        }
    }

    public void updateAll() {
        try {
            TranslationDBInterface translationDBInterface = new TranslationDBInterface(context);
            for (int i = 0; i < arrayList.size(); i++) {
                Translation translationInCache = arrayList.get(i);
                boolean inFavorite = translationDBInterface.checkFavorite(translationInCache);

                if(inFavorite != translationInCache.isFavorite()) {
                    translationInCache.setFavorite(inFavorite);

                    arrayList.remove(i);
                    arrayList.add(i, translationInCache);

                    db.updateTranslation(translationInCache);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * поиск в кэше
     * @param translation - перевод, который нужно найти
     * @return - перевод в кэше или, если не найден, то null
     */
    public Translation find(Translation translation) {

        int i = Collections.binarySearch(arrayList, translation, comparator);
        if(i >= 0) {
            return arrayList.get(i);
        } else {
            return null;
        }
    }
}
