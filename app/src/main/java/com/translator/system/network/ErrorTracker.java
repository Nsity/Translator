package com.translator.system.network;

import android.content.Context;

import com.translator.R;
import com.translator.system.CommonFunctions;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by nsity on 23.03.17.
 */

public class ErrorTracker {

    /**
     * Обработка ошибок
     * @param context
     * @param from - откуда пришла ошибка
     * @param response - ответ с ошибкой
     * @return
     */
    public static String getErrorDescription(Context context, String from, String response) {
        if (CommonFunctions.StringIsNullOrEmpty(response)) {
            return context.getString(R.string.error_occurred);
        }

        if (response.equalsIgnoreCase("{}")) {
            return context.getString(R.string.error_tracker_empty_json);
        }


        String jsonMessage;
        try {
            JSONObject jsonObject = new JSONObject(response);
            jsonMessage = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.par_message));
        } catch (JSONException e) {
            e.printStackTrace();
            return context.getString(R.string.error_occurred);
        }

        if (jsonMessage.equals(AsyncHttpResponse.MESSAGE)){
            return context.getString(R.string.error_tracker_timeout_error);
        }

        return (CommonFunctions.StringIsNullOrEmpty(jsonMessage)) ? context.getString(R.string.error_occurred) : jsonMessage;
    }

}
