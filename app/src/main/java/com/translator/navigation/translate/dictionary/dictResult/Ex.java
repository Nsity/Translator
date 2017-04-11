package com.translator.navigation.translate.dictionary.dictResult;

import java.util.ArrayList;

/**
 * Created by fedorova on 11.04.2017.
 */

public class Ex {

    private String text;
    private ArrayList<Tr> translations = null;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<Tr> getTranslations() {
        return translations;
    }

    public void setTranslations(ArrayList<Tr> translations) {
        this.translations = translations;
    }
}
