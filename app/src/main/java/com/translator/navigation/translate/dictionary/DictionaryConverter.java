package com.translator.navigation.translate.dictionary;

import android.content.Context;

import com.translator.R;
import com.translator.navigation.translate.dictionary.dictResult.Def;
import com.translator.navigation.translate.dictionary.dictResult.Ex;
import com.translator.navigation.translate.dictionary.dictResult.Mean;
import com.translator.navigation.translate.dictionary.dictResult.Syn;
import com.translator.navigation.translate.dictionary.dictResult.Tr;
import com.translator.system.CommonFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by fedorova on 12.04.2017.
 */

public class DictionaryConverter {

    public static TranslateFullResponse convert(Context context, JSONObject jsonObject) {

        TranslateFullResponse translateFullResponse = new TranslateFullResponse();
        translateFullResponse.setDefinitions(new ArrayList<Def>());

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

                def.setTranslations(getTranslations(context, defObject));

                //--------------------------

                translateFullResponse.getDefinitions().add(def);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return translateFullResponse;
    }


    private static ArrayList<Tr> getTranslations(Context context, JSONObject defObject) {
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

                tr.setSynonyms(getSynonyms(context, trObject));
                tr.setMeanings(getMeanings(context, trObject));
                tr.setExamples(getExamples(context, trObject));

                trWordArray.add(tr);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return trWordArray;
    }


    private static ArrayList<Syn> getSynonyms(Context context, JSONObject trObject) {
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
        return synWordArray;
    }

    private static ArrayList<Mean> getMeanings(Context context, JSONObject trObject) {
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

        return meanWordArray;
    }


    private static ArrayList<Ex> getExamples(Context context, JSONObject trObject) {
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

        return exWordArray;
    }
}
