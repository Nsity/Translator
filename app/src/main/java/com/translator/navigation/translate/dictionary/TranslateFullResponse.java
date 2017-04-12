package com.translator.navigation.translate.dictionary;

import com.translator.navigation.translate.dictionary.dictResult.Def;

import java.util.List;

/**
 * Created by fedorova on 11.04.2017.
 */

public class TranslateFullResponse {
    private List<Def> definitions = null;

    public List<Def> getDefinitions() {
        return definitions;
    }

    public void setDefinitions(List<Def> definitions) {
        this.definitions = definitions;
    }
}
