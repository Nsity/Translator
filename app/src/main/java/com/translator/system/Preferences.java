package com.translator.system;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nsity on 21.03.17.
 */


public class Preferences {

    public static final String input_lang = "input_lang";
    public static final String translation_lang = "translation_lang";
    private static final String accountType = "com.translator";


    private static SharedPreferences sPref;

    public static void set(String fieldName, String value, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);

        sPref.edit().putString(fieldName, value).apply();
    }

    public static void set(String fieldName, boolean value, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);

        sPref.edit().putBoolean(fieldName, value).apply();

    }

    public static String get(String fieldName, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);

        return sPref.getString(fieldName, "");
    }

    public static boolean getBoolean(String fieldName, boolean defaultValue, Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);

        return sPref.getBoolean(fieldName, defaultValue);
    }

    public static void clear(Context context){
        if (sPref == null)
            sPref = context.getSharedPreferences(accountType, Context.MODE_PRIVATE);

        sPref.edit().clear().apply();
    }
}
