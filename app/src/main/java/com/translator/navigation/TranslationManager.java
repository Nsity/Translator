package com.translator.navigation;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.translator.R;
import com.translator.system.CommonFunctions;
import com.translator.system.network.AsyncHttpResponse;
import com.translator.system.network.CallBack;
import com.translator.system.network.ResponseObject;

import org.json.JSONObject;

/**
 * Created by fedorova on 21.03.2017.
 */

public class TranslationManager {

    public static void translate(Context context, String inputText, String lang, final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_translate);

        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));
        params.put(context.getString(R.string.par_text), inputText);
        params.put(context.getString(R.string.par_lang), lang);

        String url = context.getString(R.string.main_http) + method;


        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>() {
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
                Log.i("TAG", String.valueOf(object.getResponse()));
                callBack.onSuccess(object.getResponse());
            }
        });
    }


    public static void detect(Context context, String inputText, String hint, final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_detect);

        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));
        params.put(context.getString(R.string.par_text), inputText);

        if(!CommonFunctions.StringIsNullOrEmpty(hint))
            params.put(context.getString(R.string.par_hint), hint);

        String url = context.getString(R.string.main_http) + method;


        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>() {
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
                Log.i("TAG", String.valueOf(object.getResponse()));
                callBack.onSuccess(object.getResponse());
            }
        });
    }

    public static void getLanguages(Context context, String lang, final CallBack callBack) {
        String method = context.getResources().getString(R.string.api_get_langs);

        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));

        if(lang != null)
            params.put(context.getString(R.string.par_ui), lang);

        String url = context.getString(R.string.main_http) + method;

        new AsyncHttpResponse(context, url, params,AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE, new CallBack<ResponseObject>() {
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
                Log.i("TAG", String.valueOf(object.getResponse()));
                callBack.onSuccess(object.getResponse());
            }
        });
    }



}
