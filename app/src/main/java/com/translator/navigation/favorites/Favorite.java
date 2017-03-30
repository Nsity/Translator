package com.translator.navigation.favorites;

import android.content.Context;
import android.database.Cursor;

import com.translator.navigation.Translation;
import com.translator.navigation.Translations;
import com.translator.system.CommonFunctions;

/**
 * Created by nsity on 29.03.17.
 */

public class Favorite extends Translations {


    public Favorite(Context context) {
        super(context);
    }

    @Override
    protected void loadFromDB() {
        Cursor cursor = db.getFavorite();
        setTranslations(cursor);

    }

    @Override
    protected void delete() {
        db.deleteFavorite();
        loadFromDB();
    }

    @Override
    protected void deleteItem(int i) {
        db.deleteFavoriteItem(arrayList.get(i).getId());
        loadFromDB();
    }

    @Override
    protected void search(String query) {
        Cursor cursor;
        if(CommonFunctions.StringIsNullOrEmpty(query)) {
            cursor = db.getFavorite();
        } else {
            cursor = db.searchInFavorite(query);
        }
        setTranslations(cursor);
    }
}
