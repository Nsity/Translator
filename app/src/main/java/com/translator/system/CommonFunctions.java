package com.translator.system;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.View;

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


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static void showProgress(final boolean show, Context context, final View mFormView, final View mProgressView) {
        int shortAnimTime = context.getResources().getInteger(android.R.integer.config_shortAnimTime);

        mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mFormView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }


}
