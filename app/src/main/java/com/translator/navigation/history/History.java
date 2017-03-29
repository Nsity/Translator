package com.translator.navigation.history;

import android.content.Context;
import android.database.Cursor;

import com.translator.navigation.Translation;
import com.translator.navigation.Translations;
import com.translator.system.Language;
import com.translator.system.database.LanguageDBInterface;
import com.translator.system.database.TranslationDBInterface;

import java.util.ArrayList;

/**
 * Created by nsity on 29.03.17.
 */

public class History extends Translations {


    public History(Context context) {
        super(context);
    }

    @Override
    public void loadFromDB() {
        Cursor cursor = db.getHistory();
        setTranslations(cursor);
    }

    @Override
    public void delete() {
        db.deleteHistory();
        loadFromDB();
    }
}
