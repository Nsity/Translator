package com.translator.navigation;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.translator.R;
import com.translator.system.CommonFunctions;
import com.translator.system.network.AsyncHttpResponse;
import com.translator.system.network.CallBack;
import com.translator.system.network.ErrorTracker;
import com.translator.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fedorova on 21.03.2017.
 */

public class TranslationManager {

    /**
     * API Перевод текста
     * @param context
     * @param inputText - текст, который нужно перевести
     * @param lang - направление текста
     * @param callBack
     */
    public static void translate(final Context context, String inputText,
                                 String lang, final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_translate);
        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));
        params.put(context.getString(R.string.par_text), inputText);
        params.put(context.getString(R.string.par_lang), lang);

        String url = context.getString(R.string.main_http) + method;

        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE,
                new CallBack<ResponseObject>() {
            @Override
            public void onSuccess(ResponseObject object) {
                if (!(object.getResponse() instanceof JSONObject)) {
                    return;
                }
                try {
                    JSONObject response = (JSONObject) object.getResponse();
                    if(CommonFunctions.getFieldInt(response,
                            context.getString(R.string.par_code)) == 200) {
                            ArrayList<String> result =
                                    getTranslations(response.getJSONArray(context.getString(R.string.par_text)));
                        if(result != null) {
                            callBack.onSuccess(result);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(ResponseObject object) {
                callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                        String.valueOf(object.getResponse())));
            }
        });
    }


    /**
     * Получение списка переводов
     * @param jsonArr - перевод JSONArray
     * @return - массив переводов
     */
    private static ArrayList<String> getTranslations(JSONArray jsonArr) {
        if(CommonFunctions.StringIsNullOrEmpty(jsonArr.toString())) {
            return null;
        }
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < jsonArr.length(); i++){
            try {
                result.add(jsonArr.get(i).toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    public static void detect(final Context context, String inputText, String hint,
                              final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_detect);

        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));
        params.put(context.getString(R.string.par_text), inputText);

        if(!CommonFunctions.StringIsNullOrEmpty(hint))
            params.put(context.getString(R.string.par_hint), hint);

        String url = context.getString(R.string.main_http) + method;


        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE,
                new CallBack<ResponseObject>() {
            @Override
            public void onSuccess(ResponseObject object) {
                if (!(object.getResponse() instanceof JSONObject)) {


                    return;
                }

                JSONObject response = (JSONObject) object.getResponse();
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(ResponseObject object) {
                callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                        String.valueOf(object.getResponse())));
            }
        });
    }

    public static void getLanguages(final Context context, String lang, final CallBack callBack) {
        String method = context.getResources().getString(R.string.api_get_langs);

        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));

        if(lang != null)
            params.put(context.getString(R.string.par_ui), lang);

        String url = context.getString(R.string.main_http) + method;

        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE,
                new CallBack<ResponseObject>() {
            @Override
            public void onSuccess(ResponseObject object) {
                if (!(object.getResponse() instanceof JSONObject)) {
                    return;
                }

                JSONObject response = (JSONObject) object.getResponse();
                callBack.onSuccess(response);
            }

            @Override
            public void onFailure(ResponseObject object) {
                callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                        String.valueOf(object.getResponse())));
            }
        });
    }



}
