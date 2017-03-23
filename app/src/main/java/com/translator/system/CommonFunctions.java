package com.translator.system;

import org.json.JSONObject;

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



    public static String getFieldString(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getString(field);
        } catch (Exception e) {
            return "";
        }
    }


    public static boolean getFieldBoolean(JSONObject jsonObject, String field) {
        try {
            return !jsonObject.isNull(field) && (jsonObject.getString(field).equals("1") || (jsonObject.getString(field).equals("-1")));
        } catch (Exception e) {
            return false;
        }
    }

    public static int getFieldInt(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getInt(field);
        } catch (Exception e) {
            return 0;
        }
    }

    public static Double getFieldDouble(JSONObject jsonObject, String field) {
        try {
            return jsonObject.getDouble(field);
        } catch (Exception e) {
            return 0.0;
        }
    }


}
