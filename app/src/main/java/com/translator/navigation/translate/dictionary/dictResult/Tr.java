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
    private ArrayList<Syn> synonyms = null;
    private ArrayList<Mean> meanings = null;
    private ArrayList<Ex> examples = null;

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

    public ArrayList<Ex> getExamples() {
        return examples;
    }

    public void setExamples(ArrayList<Ex> examples) {
        this.examples = examples;
    }

    public ArrayList<Syn> getSynonyms() {
        return synonyms;
    }

    public void setSynonyms(ArrayList<Syn> synonyms) {
        this.synonyms = synonyms;
    }

    public ArrayList<Mean> getMeanings() {
        return meanings;
    }

    public void setMeanings(ArrayList<Mean> meanings) {
        this.meanings = meanings;
    }
}
