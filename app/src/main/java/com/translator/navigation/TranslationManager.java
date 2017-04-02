package com.translator.navigation;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.translator.R;
import com.translator.system.CommonFunctions;
import com.translator.system.Preferences;
import com.translator.system.database.LanguageDBInterface;
import com.translator.system.database.TranslationDBInterface;
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
     * @param translation - перевод
     * @param callBack
     */
    public static void translate(final Context context, final Translation translation, final CallBack callBack) {

        String method = context.getResources().getString(R.string.api_translate);
        RequestParams params = new RequestParams();
        params.put(context.getString(R.string.par_key), context.getString(R.string.api_key));
        params.put(context.getString(R.string.par_text), translation.getInputText());
        params.put(context.getString(R.string.par_lang), translation.getInputLang() + "-" + translation.getTranslationLang());

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


                    if(CommonFunctions.getFieldInt(response, context.getString(R.string.par_code)) == 200) {
                        ArrayList<String> result = getTranslations(response.getJSONArray(context.getString(R.string.par_text)));

                        translation.setTranslationText(listToStr(result).trim());

                        //TODO
                        translation.setFavorite(new TranslationDBInterface(context).checkFavorite(translation));

                        if(result != null) {
                            callBack.onSuccess(translation);
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
     * переводит список переводов в строку
     * @param arr - список переводов
     * @return - строка
     */
    private static String listToStr(ArrayList<String> arr) {
        String resStr = "";
        for (String str: arr) {
            resStr += str + "\n";
        }

        return resStr;
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
                    callBack.onFail(context.getString(R.string.error_occurred));
                    return;
                }

                JSONObject response = (JSONObject) object.getResponse();

                try {
                    if (CommonFunctions.getFieldInt(response,
                            context.getString(R.string.par_code)) == 200) {
                        String result = response.getString(context.getString(R.string.par_lang));
                        if (result != null) {
                            callBack.onSuccess(result);
                        }
                    }
                } catch (JSONException e) {
                    callBack.onFail(context.getString(R.string.error_occurred));
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

    public static void getLanguages(final Context context, final String lang, final CallBack callBack) {
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
                    callBack.onFail(context.getString(R.string.error_occurred));
                    return;
                }

                JSONObject response = (JSONObject) object.getResponse();

                try {
                    JSONObject result = response.getJSONObject(context.getString(R.string.par_langs));

                    LanguageDBInterface db = new LanguageDBInterface(context);
                    db.save(result, true);

                    callBack.onSuccess();

                } catch (JSONException e) {
                    e.printStackTrace();
                    callBack.onFail(context.getString(R.string.error_occurred));
                }
            }

            @Override
            public void onFailure(ResponseObject object) {
                callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                        String.valueOf(object.getResponse())));
            }
        });
    }



}
