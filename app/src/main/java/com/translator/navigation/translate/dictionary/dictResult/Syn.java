package com.translator.navigation.translate.dictionary.dictResult;

/**
 * Created by fedorova on 11.04.2017.
 */

public class Syn {

    private String text;
    private String pos; //часть речи
    private String gen; //род

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }
}
