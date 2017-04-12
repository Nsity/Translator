package com.translator.navigation.translate;

import android.util.Log;

import com.translator.navigation.Translation;

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

    public void add(Translation translation) {
        if(arrayList.size() > COUNT) {
            arrayList.remove(0);
        }

        arrayList.add(translation);
    }


    public void update(Translation translation) {
        for (int i=0; i < arrayList.size();i++) {
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
