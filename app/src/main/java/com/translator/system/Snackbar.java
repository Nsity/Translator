package com.translator.system;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.system.Preferences;


/**
 * Created by fedorova on 25.10.2016.
 */
public class Snackbar {

    public static final String MESSAGE = "message";
    public static final String TYPE = "type";

    public static final int SNACKBAR_SUCCESS = 1;
    public static final int SNACKBAR_FAIL = 0;
    public static final int SNACKBAR_INFO = 2;

    public static void showLongMessage(Context context, View view, String message, int type) {
        if (view == null || context == null || message == null)
            return;

        showLong(context, view, message, type);
    }

    public static void showShortMessage(Context context, View view, String message, int type) {
        if (view == null || context == null || message == null)
            return;

        showShort(context, view, message, type);
    }

    private static void showShort(Context context, View view, String message, int type) {
        try {
            android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(view, message, android.support.design.widget.Snackbar.LENGTH_SHORT);
            showSnackbar(context, snackbar, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showLong(Context context, View view, String message, int type) {
        try {
            android.support.design.widget.Snackbar snackbar = android.support.design.widget.Snackbar.make(view, message, android.support.design.widget.Snackbar.LENGTH_LONG);

            showSnackbar(context, snackbar, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void showSnackbar(Context context, android.support.design.widget.Snackbar snackbar, int type) {
        final View snackbarView = snackbar.getView();
        TextView snackTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackTextView.setMaxLines(7);

        switch (type) {
            case SNACKBAR_SUCCESS:
                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.green_color_7));
                snackTextView.setTextColor(context.getResources().getColor(android.R.color.white));
                break;
            case SNACKBAR_FAIL:
                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.red_color_7));
                snackTextView.setTextColor(context.getResources().getColor(android.R.color.white));
                break;
            case SNACKBAR_INFO:
                snackbarView.setBackgroundColor(context.getResources().getColor(R.color.blue_color_7));
                snackTextView.setTextColor(context.getResources().getColor(android.R.color.white));
                break;
        }

        snackbar.show();
    }
}
