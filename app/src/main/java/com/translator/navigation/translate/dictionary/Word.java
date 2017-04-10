package com.translator.navigation.translate.dictionary;

/**
 * Created by nsity on 11.04.17.
 */

public class Word {

    private String text;
    private String pos;
    private String num;
    private String gen;

    public Word() {
    }

    public String getText() {
        return text;
    }

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }
}
