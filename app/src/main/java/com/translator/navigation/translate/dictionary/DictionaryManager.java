package com.translator.navigation.translate.dictionary;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.RequestParams;
import com.translator.R;
import com.translator.navigation.Translation;
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
import java.util.concurrent.Exchanger;

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

                       // translation.setDictionaryResponse(JsonUtils.serialize(response.toString()));

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
        translateFullResponse.def = new ArrayList<>();

        try {
            JSONArray defArray = jsonObject.getJSONArray(context.getString(R.string.par_def)); //массив словарных статей
            for (int i = 0; i < defArray.length(); i++) {
                JSONObject defObject = defArray.getJSONObject(i);

                Def def = new Def();
                def.text = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_text));
                def.pos = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_pos)); //часть речи
                def.num = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_num)); //число
                def.gen = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_gen)); //род существительного
                def.anm = CommonFunctions.getFieldString(defObject, context.getString(R.string.par_anm)); //одушевленное

                //--------------------------
                ArrayList<Tr> trWordArray = new ArrayList<>();
                try {
                    JSONArray trArray = defObject.getJSONArray(context.getString(R.string.par_tr)); //массив переводов
                    for (int j = 0; j < trArray.length(); j++) {
                        JSONObject trObject = trArray.getJSONObject(j);

                        Tr tr = new Tr();

                        tr.text = CommonFunctions.getFieldString(trObject, context.getString(R.string.par_text));
                        tr.gen =  CommonFunctions.getFieldString(trObject, context.getString(R.string.par_gen)); //род существительного
                        tr.pos = CommonFunctions.getFieldString(trObject, context.getString(R.string.par_pos)); //часть речи


                        //--------------------------
                        ArrayList<Syn> synWordArray = new ArrayList<>();
                        try {
                            JSONArray synArray = trObject.getJSONArray(context.getString(R.string.par_syn)); //массив синонимов
                            for (int k = 0; k < synArray.length(); k++) {
                                JSONObject synObject = synArray.getJSONObject(k);

                                Syn syn = new Syn();

                                syn.text = CommonFunctions.getFieldString(synObject, context.getString(R.string.par_text));
                                syn.gen =  CommonFunctions.getFieldString(synObject, context.getString(R.string.par_gen)); //род существительного
                                syn.pos = CommonFunctions.getFieldString(synObject, context.getString(R.string.par_pos)); //часть речи

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

                                mean.text = CommonFunctions.getFieldString(meanObject, context.getString(R.string.par_text));
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

                                ex.text = CommonFunctions.getFieldString(exObject, context.getString(R.string.par_text));
                                exWordArray.add(ex);

                                ArrayList<Tr> trExWordArray = new ArrayList<>();

                                try {
                                    JSONArray trExArray = exObject.getJSONArray(context.getString(R.string.par_tr));
                                    for (int u = 0; u < trExArray.length(); u++) {
                                        JSONObject trExObject = trExArray.getJSONObject(u);

                                        Tr trEx = new Tr();
                                        trEx.text = CommonFunctions.getFieldString(trExObject, context.getString(R.string.par_text));
                                        trExWordArray.add(trEx);
                                    }
                                } catch (Exception e){
                                    e.printStackTrace();
                                }

                                ex.tr = trExWordArray;
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
                def.tr = trWordArray;

                //--------------------------

                translateFullResponse.def.add(def);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return translateFullResponse;
    }


   /* private static ArrayList<Def> convert(Context context, JSONObject jsonObject) {
        ArrayList<Def> arrayList = new ArrayList<>();
        try {

            JSONArray defArray = jsonObject.getJSONArray(context.getString(R.string.par_def)); //массив словарных статей
            for (int i = 0; i < defArray.length(); i++) {
                JSONObject defObject = defArray.getJSONObject(i);

                Def def = new Def();
                def.setWord(getObject(context, defObject));

                //--------------------------
                ArrayList<Word> trWordArray = new ArrayList<>();
                try {
                    JSONArray trArray = defObject.getJSONArray(context.getString(R.string.par_tr)); //массив переводов
                    for (int j = 0; j < trArray.length(); j++) {
                        JSONObject trObject = trArray.getJSONObject(j);
                        Word trWord = getObject(context, trObject);
                        trWordArray.add(trWord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                def.setTrArray(trWordArray);

                //--------------------------
                ArrayList<Word> sunWordArray = new ArrayList<>();
                try {
                    JSONArray sunArray = defObject.getJSONArray(context.getString(R.string.par_sun)); //массив синонимов
                    for (int j = 0; j < sunArray.length(); j++) {
                        JSONObject sunObject = sunArray.getJSONObject(j);
                        Word sunWord = getObject(context, sunObject);
                        sunWordArray.add(sunWord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                def.setSunArray(sunWordArray);

                //--------------------------
                ArrayList<Word> meanWordArray = new ArrayList<>();
                try {
                    JSONArray meanArray = defObject.getJSONArray(context.getString(R.string.par_mean)); //массив значений
                    for (int j = 0; j < meanArray.length(); j++) {
                        JSONObject meanObject = meanArray.getJSONObject(j);
                        Word meanWord = getObject(context, meanObject);
                        meanWordArray.add(meanWord);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                def.setMeanArray(meanWordArray);

                //--------------------------
                ArrayList<Word> exWordArray = new ArrayList<>();
                try {
                    JSONArray exArray = defObject.getJSONArray(context.getString(R.string.par_ex)); //массив примеров
                    for (int j = 0; j < exArray.length(); j++) {
                        JSONObject exObject = exArray.getJSONObject(j);
                        Word exWord = getObject(context, exObject);
                        exWordArray.add(exWord);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                def.setExArray(exWordArray);

                arrayList.add(def);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }*/


    private static Word getObject(Context context, JSONObject jsonObject) {
        String text = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.par_text));
        String pos = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.par_pos)); //часть речи
        String num = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.par_num)); //число
        String gen = CommonFunctions.getFieldString(jsonObject, context.getString(R.string.par_gen)); //род существительного


        Word word = new Word();
        word.setText(text);
        word.setPos(pos);
        word.setNum(num);
        word.setGen(gen);

        return word;
    }
}
