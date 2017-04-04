package com.translator.navigation;

import android.content.ContentValues;
import android.content.Context;

import com.translator.system.database.TranslationDBInterface;

import java.io.Serializable;

/**
 * Created by nsity on 20.03.17.
 */

public class Translation implements Serializable {

    private int id;
    private boolean isFavorite;
    private String inputText;
    private String translationText;
    private String inputLang;
    private String translationLang;
    private String fullTranslation;
    private boolean inHistory;


    private TranslationDBInterface db;
    private Context context;

    public Translation(Context context) {
        this.context = context;
        this.db = new TranslationDBInterface(context);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public String getInputText() {
        return inputText;
    }

    public void setInputText(String inputText) {
        this.inputText = inputText;
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText;
    }

    public String getInputLang() {
        return inputLang;
    }

    public void setInputLang(String inputLang) {
        this.inputLang = inputLang;
    }

    public String getTranslationLang() {
        return translationLang;
    }

    public void setTranslationLang(String translationLang) {
        this.translationLang = translationLang;
    }

    public String getFullTranslation() {
        return fullTranslation;
    }

    public void setFullTranslation(String fullTranslation) {
        this.fullTranslation = fullTranslation;
    }

    public boolean isInHistory() {
        return inHistory;
    }

    public void setInHistory(boolean inHistory) {
        this.inHistory = inHistory;
    }


    public long save() {
        return db.saveTranslation(this);
    }


    public long update() {
        return db.updateTranslation(this);
    }
}
