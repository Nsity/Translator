package com.translator.navigation;

/**
 * Created by nsity on 20.03.17.
 */

public class Translation {

    private int id;
    private boolean isFavorite;
    private String inputText;
    private String translationText;
    private String inputLang;
    private String translationLang;
    private String fullTranslation;


    public Translation(int id) {
        this.id = id;
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
}
