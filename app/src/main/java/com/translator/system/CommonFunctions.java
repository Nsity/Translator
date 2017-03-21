package com.translator.system;

/**
 * Created by nsity on 18.03.17.
 */

public class CommonFunctions {


    public static boolean StringIsNullOrEmpty(String string) {
        return (string == null) || (string.isEmpty());
    }

    public static String setFirstLetterUpperCase(String str) {
        if(StringIsNullOrEmpty(str))
            return str;
        return str.substring(0,1).toUpperCase() + str.substring(1).toLowerCase();
    }
}
