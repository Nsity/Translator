package com.translator.navigation.translate.dictionary.dictResult;

import com.translator.navigation.translate.dictionary.dictResult.Ex;
import com.translator.navigation.translate.dictionary.dictResult.Mean;
import com.translator.navigation.translate.dictionary.dictResult.Syn;

import java.util.ArrayList;

/**
 * Created by fedorova on 11.04.2017.
 */

public class Tr {

    private String text;
    private String pos;
    private String gen;
    public ArrayList<Syn> syn = null;
    public ArrayList<Mean> mean = null;
    public ArrayList<Ex> ex = null;

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
