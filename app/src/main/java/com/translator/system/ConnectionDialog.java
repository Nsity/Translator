package com.translator.system;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;

import com.translator.R;

/**
 * Created by nsity on 23.03.17.
 */

public class ConnectionDialog {

    public static void showNoConnectionDialog(final Context context)
    {
        if(context == null)
            return;

        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setMessage(R.string.no_connection);
            builder.setTitle(R.string.no_connection_title);

            builder.setPositiveButton(R.string.text_settings, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            });

            builder.setNegativeButton(R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
