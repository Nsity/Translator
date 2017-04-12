package com.translator.navigation.translate.dictionary;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.translate.dictionary.dictResult.Def;
import com.translator.navigation.translate.dictionary.dictResult.Ex;
import com.translator.navigation.translate.dictionary.dictResult.Mean;
import com.translator.navigation.translate.dictionary.dictResult.Syn;
import com.translator.navigation.translate.dictionary.dictResult.Tr;
import com.translator.system.CommonFunctions;
import com.translator.system.Preferences;
import com.translator.system.network.AsyncHttpResponse;
import com.translator.system.network.CallBack;
import com.translator.system.network.ErrorTracker;
import com.translator.system.network.ResponseObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fedorova on 06.04.2017.
 */

public class DictionaryManager {

    public static void lookup(final Context context, final Translation translation, final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_lookup);
        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.yandex_dictionary_api_key));
        params.put(context.getString(R.string.par_text), translation.getInputText());
        params.put(context.getString(R.string.par_lang), translation.getInputLang() + "-" + translation.getTranslationLang());


        String ui_lang = Preferences.get(Preferences.ui_lang, context);
        if(!CommonFunctions.StringIsNullOrEmpty(ui_lang)) {
            if(ui_lang.equals("en") || ui_lang.equals("ru") || ui_lang.equals("uk") || ui_lang.equals("tr")) {
                params.put(context.getString(R.string.par_ui), ui_lang);
            }
        }

        String url = context.getString(R.string.yandex_dictionary_http) + method;

        new AsyncHttpResponse(context, url, params, AsyncHttpResponse.CALL_POST_JSON_HTTP_RESPONSE,
                new CallBack<ResponseObject>() {
                    @Override
                    public void onSuccess(ResponseObject object) {
                        if (!(object.getResponse() instanceof JSONObject)) {
                            return;
                        }
                        JSONObject response = (JSONObject) object.getResponse();
                        Log.i("TAG", response.toString());

                        TranslateFullResponse translateFullResponse = DictionaryConverter.convert(context, response);
                        translation.setFullTranslation(translateFullResponse);

                        callBack.onSuccess(translation);
                    }

                    @Override
                    public void onFailure(ResponseObject object) {
                        callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                                String.valueOf(object.getResponse())));
                    }
                });
    }



}
