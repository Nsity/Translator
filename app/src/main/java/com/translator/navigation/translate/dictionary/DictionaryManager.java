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

                        TranslateFullResponse translateFullResponse = convert(context, response);


                        callBack.onSuccess(translateFullResponse);
                    }

                    @Override
                    public void onFailure(ResponseObject object) {
                        callBack.onFail(ErrorTracker.getErrorDescription(context, getClass().getName(),
                                String.valueOf(object.getResponse())));
                    }
                });
    }

    private static TranslateFullResponse convert(Context context, JSONObject jsonObject) {

        TranslateFullResponse translateFullResponse = new TranslateFullResponse();
        translateFullResponse.setDef(new ArrayList<Def>());

        try {
            JSONArray defArray = jsonObject.getJSONArray(context.getString(R.string.par_def)); //массив словарных статей
            for (int i = 0; i < defArray.length(); i++) {
                JSONObject defObject = defArray.getJSONObject(i);

                Def def = new Def();
                def.setText(CommonFunctions.getFieldString(defObject, context.getString(R.string.par_text)));
                def.setPos(CommonFunctions.getFieldString(defObject, context.getString(R.string.par_pos))); //часть речи
                def.setNum(CommonFunctions.getFieldString(defObject, context.getString(R.string.par_num))); //число
                def.setGen(CommonFunctions.getFieldString(defObject, context.getString(R.string.par_gen))); //род существительного
                def.setTs(CommonFunctions.getFieldString(defObject, context.getString(R.string.par_ts))); //транскрипция
               // def.anm = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_anm)); //одушевленное

                //--------------------------
                ArrayList<Tr> trWordArray = new ArrayList<>();
                try {
                    JSONArray trArray = defObject.getJSONArray(context.getString(R.string.par_tr)); //массив переводов
                    for (int j = 0; j < trArray.length(); j++) {
                        JSONObject trObject = trArray.getJSONObject(j);

                        Tr tr = new Tr();

                        tr.setText(CommonFunctions.getFieldString(trObject, context.getString(R.string.par_text)));
                        tr.setGen(CommonFunctions.getFieldString(trObject, context.getString(R.string.par_gen))); //род существительного
                        tr.setPos(CommonFunctions.getFieldString(trObject, context.getString(R.string.par_pos))); //часть речи


                        //--------------------------
                        ArrayList<Syn> synWordArray = new ArrayList<>();
                        try {
                            JSONArray synArray = trObject.getJSONArray(context.getString(R.string.par_syn)); //массив синонимов
                            for (int k = 0; k < synArray.length(); k++) {
                                JSONObject synObject = synArray.getJSONObject(k);

                                Syn syn = new Syn();

                                syn.setText(CommonFunctions.getFieldString(synObject, context.getString(R.string.par_text)));
                                syn.setGen(CommonFunctions.getFieldString(synObject, context.getString(R.string.par_gen))); //род существительного
                                syn.setPos(CommonFunctions.getFieldString(synObject, context.getString(R.string.par_pos))); //часть речи

                                synWordArray.add(syn);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tr.syn = synWordArray;
                        //--------------------------

                        ArrayList<Mean> meanWordArray = new ArrayList<>();
                        try {
                            JSONArray meanArray = trObject.getJSONArray(context.getString(R.string.par_mean)); //массив значений
                            for (int k = 0; k < meanArray.length(); k++) {
                                JSONObject meanObject = meanArray.getJSONObject(k);

                                Mean mean = new Mean();

                                mean.setText(CommonFunctions.getFieldString(meanObject, context.getString(R.string.par_text)));
                                meanWordArray.add(mean);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tr.mean = meanWordArray;

                        //--------------------------

                        //--------------------------

                        ArrayList<Ex> exWordArray = new ArrayList<>();
                        try {
                            JSONArray exArray = trObject.getJSONArray(context.getString(R.string.par_ex)); //массив значений
                            for (int k = 0; k < exArray.length(); k++) {
                                JSONObject exObject = exArray.getJSONObject(k);

                                Ex ex = new Ex();

                                ex.setText(CommonFunctions.getFieldString(exObject, context.getString(R.string.par_text)));
                                exWordArray.add(ex);

                                ArrayList<Tr> trExWordArray = new ArrayList<>();

                                try {
                                    JSONArray trExArray = exObject.getJSONArray(context.getString(R.string.par_tr));
                                    for (int u = 0; u < trExArray.length(); u++) {
                                        JSONObject trExObject = trExArray.getJSONObject(u);

                                        Tr trEx = new Tr();
                                        trEx.setText(CommonFunctions.getFieldString(trExObject, context.getString(R.string.par_text)));
                                        trExWordArray.add(trEx);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                ex.setTranslations(trExWordArray);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        tr.ex = exWordArray;


                        trWordArray.add(tr);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                def.setTranstations(trWordArray);

                //--------------------------

                translateFullResponse.getDef().add(def);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return translateFullResponse;
    }

}
