package com.translator.navigation.translate.dictionary.dictResult;

import java.util.ArrayList;

/**
 * Created by nsity on 10.04.17.
 */

public class Def {

    private String text;
    private String pos; //часть речи
    private String gen; //род
    private String num;
    private String ts; //транскрипция
   // public String anm; // одушевленное

    private ArrayList<Tr> transtations = null;

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

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public ArrayList<Tr> getTranstations() {
        return transtations;
    }

    public void setTranstations(ArrayList<Tr> transtations) {
        this.transtations = transtations;
    }

    public String getTs() {
        return ts;
    }

    public void setTs(String ts) {
        this.ts = ts;
    }
}
