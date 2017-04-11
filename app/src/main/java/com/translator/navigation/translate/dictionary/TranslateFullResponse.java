package com.translator.navigation.translate.dictionary;

import com.translator.navigation.translate.dictionary.dictResult.Def;

import java.util.List;

/**
 * Created by fedorova on 11.04.2017.
 */

public class TranslateFullResponse {
    private List<Def> def = null;

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }
}
