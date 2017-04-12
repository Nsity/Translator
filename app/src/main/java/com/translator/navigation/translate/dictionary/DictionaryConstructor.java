package com.translator.navigation.translate.dictionary;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.translate.dictionary.dictResult.Def;
import com.translator.navigation.translate.dictionary.dictResult.Ex;
import com.translator.navigation.translate.dictionary.dictResult.Mean;
import com.translator.navigation.translate.dictionary.dictResult.Syn;
import com.translator.navigation.translate.dictionary.dictResult.Tr;
import com.translator.system.CommonFunctions;


import java.util.ArrayList;

import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;

/**
 * Created by nsity on 11.04.17.
 */

public class DictionaryConstructor {

    private static final String SPACE = " ";
    private static final String COMMA = ",";
    private static final String TIRET = "—";
    private static final String OPEN_PARENTHESIS = "(";
    private static final String CLOSE_PARENTHESIS = ")";
    private static final String OPEN_BRACE = "[";
    private static final String CLOSE_BRACE = "]";

    /**
     * формирует словарную статью
     *
     * @param context  - контекст
     * @param parent   - родительский вид
     * @param response - полный перевод
     */
    public static void makeLookupResponse(Context context, LinearLayout parent, TranslateFullResponse response) {
        if(response.getDefinitions() == null || response.getDefinitions().size() == 0) {
            return;
        }

        final LayoutInflater inflater = LayoutInflater.from(context);

        //части речи
        for (int i = 0; i < response.getDefinitions().size(); i++) {
            Def def = response.getDefinitions().get(i);

            RelativeLayout partOfSpeechLayout = (RelativeLayout) inflater.inflate(R.layout.dict_item_part_of_speech, null);

            TextView partOfSpeechTextView = (TextView) partOfSpeechLayout.findViewById(R.id.definition_part_of_speech);
            TextView definitionTextView = (TextView) partOfSpeechLayout.findViewById(R.id.definition);

            partOfSpeechTextView.setText(def.getPos());

            SpannableStringBuilder stringBuilderDefinition = new SpannableStringBuilder();
            stringBuilderDefinition.append(def.getText());

            //род существительного
            if (!CommonFunctions.StringIsNullOrEmpty(def.getGen())) {
                stringBuilderDefinition.append(SPACE).append(def.getGen());

                int genStart = def.getText().length() + 1;
                int genEnd = genStart + def.getGen().length();

                stringBuilderDefinition.setSpan(new StyleSpan(ITALIC), genStart,
                        genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                stringBuilderDefinition.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGray6)),
                        genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);

            }


            //транскрипция
            if (!CommonFunctions.StringIsNullOrEmpty(def.getTs())) {
                stringBuilderDefinition.append(SPACE).append(OPEN_BRACE).append(def.getTs());

                int genStart = def.getText().length() + 1;
                int genEnd = genStart + def.getTs().length() + 2;

                if (!CommonFunctions.StringIsNullOrEmpty(def.getGen())) {
                    genStart += def.getGen().length() + 1;
                    genEnd += def.getGen().length() + 1;
                }

                stringBuilderDefinition.append(CLOSE_BRACE);

                stringBuilderDefinition.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGray6)),
                        genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
            }


            definitionTextView.setText(stringBuilderDefinition);

            if (i != 0) {
                partOfSpeechLayout.setPadding(0, 16, 0, 0);
            }

            parent.addView(partOfSpeechLayout);

            //Варианты перевода
            if (def.getTranslations() != null) {
                makeTranslations(context, parent, inflater, def.getTranslations());
            }
        }

        makeLicense(context, parent, inflater);
    }

    /**
     * @param context - контекст
     * @param parent - родительский вид
     * @param inflater
     * @param translations - переводы
     */
    private static void makeTranslations(Context context, LinearLayout parent, LayoutInflater inflater, ArrayList<Tr> translations) {

        int translationCount = 0;

        for (final Tr translation : translations) {

            translationCount++;

            String count = Integer.toString(translationCount);

            RelativeLayout definitionLayout = (RelativeLayout) inflater.inflate(R.layout.dict_item_definition, null);

            //счетчик
            TextView countTextView = (TextView) definitionLayout.findViewById(R.id.count);
            countTextView.setText(count);

            if (translations.size() == 1) {
                countTextView.setVisibility(View.INVISIBLE);
            } else {
                countTextView.setVisibility(View.VISIBLE);
            }

            TextView synonymsTextView = (TextView) definitionLayout.findViewById(R.id.synonyms);

            SpannableStringBuilder stringBuilderDefinitions = new SpannableStringBuilder();


            if (!CommonFunctions.StringIsNullOrEmpty(translation.getText())) {
                stringBuilderDefinitions.append(translation.getText());

                if (!CommonFunctions.StringIsNullOrEmpty(translation.getGen())) {

                    stringBuilderDefinitions.append(SPACE).append(translation.getGen());

                    int genStart = translation.getText().length() + 1;
                    int genEnd = genStart + translation.getGen().length();

                    stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), genStart,
                            genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                    stringBuilderDefinitions.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGray6)),
                            genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                }

                    /*if (translation.getAsp() != null) {
                        stringBuilderDefinitions.append(SPACE)
                                .append(translation.getAsp(), new StyleSpan(ITALIC),
                                        SPAN_EXCLUSIVE_EXCLUSIVE);

                        final int aspStart = stringBuilderDefinitions.length() - translation.getAsp().length();
                        final int aspEnd = stringBuilderDefinitions.length();

                        stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                aspStart, aspEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                    }*/
            }

            if (translation.getSynonyms() != null) {
                for (Syn syn : translation.getSynonyms()) {

                    if (!CommonFunctions.StringIsNullOrEmpty(syn.getText())) {
                        stringBuilderDefinitions.append(COMMA).append(SPACE)
                                .append(syn.getText());

                        if (!CommonFunctions.StringIsNullOrEmpty(syn.getGen())) {

                            stringBuilderDefinitions.append(SPACE).append(syn.getGen());

                            int genStart = stringBuilderDefinitions.length() - syn.getGen().length();
                            int genEnd = stringBuilderDefinitions.length();

                            stringBuilderDefinitions.setSpan(new StyleSpan(ITALIC), genStart,
                                    genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                            stringBuilderDefinitions.setSpan(new ForegroundColorSpan(context.getResources().getColor(R.color.colorGray6)),
                                    genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                           /* if (syn.getAsp() != null) {
                                stringBuilderDefinitions.append(SPACE)
                                        .append(syn.getAsp(), new StyleSpan(ITALIC),
                                                SPAN_EXCLUSIVE_EXCLUSIVE);

                                final int aspStart = stringBuilderDefinitions.length() - syn.getAsp().length();
                                final int aspEnd = stringBuilderDefinitions.length();

                                stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                        aspStart, aspEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                            }*/
                    }
                }
            }

            synonymsTextView.setText(stringBuilderDefinitions);

            parent.addView(definitionLayout);

            //Синонимы на исходном языке
            if (translation.getMeanings() != null) {
                makeMeanings(parent, inflater, translation.getMeanings());
            }

            //Примеры употребления
            if (translation.getExamples() != null) {
                makeExamples(parent, inflater, translation.getExamples());
            }
        }
    }

    /**
     * @param parent
     * @param inflater
     * @param meanings - значения
     */
    private static void makeMeanings(LinearLayout parent, LayoutInflater inflater, ArrayList<Mean> meanings) {
        if (meanings.size() == 0) {
            return;
        }

        TextView meaningTextView = (TextView) inflater.inflate(R.layout.dict_item_meaning, null);

        StringBuilder stringBuilderMeanings = new StringBuilder(OPEN_PARENTHESIS);

        stringBuilderMeanings.append(meanings.get(0).getText());

        for (int i = 1; i < meanings.size(); i++) {
            stringBuilderMeanings.append(COMMA).append(SPACE)
                    .append(meanings.get(i).getText());
        }

        stringBuilderMeanings.append(CLOSE_PARENTHESIS);

        meaningTextView.setText(stringBuilderMeanings.toString());

        parent.addView(meaningTextView);
    }

    /**
     * @param parent
     * @param inflater
     * @param examples - примеры
     */
    private static void makeExamples(LinearLayout parent, LayoutInflater inflater, ArrayList<Ex> examples) {

        final SpannableStringBuilder stringBuilderExample = new SpannableStringBuilder();

        for (final Ex example : examples) {

            final TextView exampleTextView = (TextView) inflater.inflate(R.layout.dict_item_example, null);
            parent.addView(exampleTextView);

            stringBuilderExample.clear();

            stringBuilderExample.append(example.getText());

            if (example.getTranslations() != null) {
                stringBuilderExample.append(SPACE).append(TIRET).append(SPACE)
                        .append(example.getTranslations().get(0).getText());
                for (int i = 1; i < example.getTranslations().size(); i++) {
                    stringBuilderExample.append(COMMA).append(SPACE)
                            .append(example.getTranslations().get(i).getText());
                }
            }

            exampleTextView.setText(stringBuilderExample);
        }
    }


    private static void makeLicense(final Context context, LinearLayout parent, LayoutInflater inflater) {
        TextView licenseTextView = (TextView) inflater.inflate(R.layout.dict_item_license, null);

        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://tech.yandex.ru/dictionary/"));
                context.startActivity(Intent.createChooser(browseIntent, context.getString(R.string.browser)));
            }
        });

        parent.addView(licenseTextView);
    }
}