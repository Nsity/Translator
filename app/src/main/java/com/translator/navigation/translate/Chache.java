package com.translator.navigation.translate;

import android.content.Context;
import android.util.Log;

import com.translator.navigation.Translation;
import com.translator.system.database.TranslationDBInterface;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by nsity on 07.04.17.
 */

public class Chache implements Serializable {

    private ArrayList<Translation> arrayList;

    private static final int COUNT = 1000;

    public Chache() {
        arrayList = new ArrayList<>();
    }


    /**
     * поиск в кэше
     * @param translation - перевод, котоый нужно найти в кэше
     * @return - перевод в кэше или, если не найден, то null
     */
    public Translation findInChache(Translation translation) {

        for (Translation translationInChache: arrayList) {
            if(translation.getInputLang().equals(translationInChache.getInputLang()) &&
                    translation.getInputText().equals(translationInChache.getInputText()) &&
                    translation.getTranslationLang().equals(translationInChache.getTranslationLang())) {
                return translationInChache;
            }
        }

        return null;
    }

    /**
     * добавление перевода в кэш
     * @param translation - перевод, который нужно добавить
     */
    public void add(Translation translation) {
        if(arrayList.size() > COUNT) {
            arrayList.remove(0);
        }

        arrayList.add(translation);
    }


    public void update(Context context) {
        TranslationDBInterface db = new TranslationDBInterface(context);

        for (int i = 0; i < arrayList.size(); i++) {
            Translation translationInChache = arrayList.get(i);
            boolean inFavorite = db.checkFavorite(translationInChache);
            translationInChache.setFavorite(inFavorite);

            arrayList.remove(i);
            arrayList.add(i, translationInChache);
        }
    }


    /**
     * оновление кэша
     * @param translation - перевод, который нужно обновить в кэше
     */
    public void update(Translation translation) {
        for (int i = 0; i < arrayList.size(); i++) {
            Translation translationInChache = arrayList.get(i);

            if(translation.getInputLang().equals(translationInChache.getInputLang()) &&
                    translation.getInputText().equals(translationInChache.getInputText()) &&
                    translation.getTranslationLang().equals(translationInChache.getTranslationLang())) {
                arrayList.remove(i);
                arrayList.add(i, translation);
            }
        }
    }
}
