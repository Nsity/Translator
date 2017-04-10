package com.translator.navigation.translate.dictionary;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.system.CommonFunctions;

import java.security.Guard;
import java.util.Iterator;
import java.util.List;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.text.Spanned.SPAN_EXCLUSIVE_INCLUSIVE;

/**
 * Created by nsity on 11.04.17.
 */

public class DictionaryConstructor {

        private static final String EMPTY = "";
        private static final String SPACE = " ";
        private static final String DOT = ".";
        private static final String COMMA = ",";
        private static final String TIRET = "—";
        private static final String OPEN_PARENTHESIS = "(";
        private static final String CLOSE_PARENTHESIS = ")";
        private static final String OPEN_BRACE = "[";
        private static final String CLOSE_BRACE = "]";

        private static int secondaryItemsColor;

        /**
         * Прикрепляет к {@code parent} набор {@link android.view.View},
         * которые визуализируют объект {@link LookupResponse}
         */
        public static void makeLookupResponse(@NonNull final Context context, @NonNull final LinearLayout parent,
                                              @NonNull final LookupResponse response) {

            secondaryItemsColor = context.getResources().getColor(R.color.colorGray6);

            final LayoutInflater inflater = LayoutInflater.from(context);

            //Части речи
            for (final LookupResponse.Definition definition : response.getDefinitions()) {

                final TextView textViewPartOfSpeech = (TextView) inflater
                        .inflate(R.layout.component_dict_item_part_or_speech, null);
                parent.addView(textViewPartOfSpeech);

                textViewPartOfSpeech.setText(definition.getPartOfSpeech());

                //Варианты перевода
                if (definition.getTranslations() != null) {
                    makeTranslations(parent, inflater, definition.getTranslations());
                }
            }
        }

        private static void makeTranslations(@NonNull final LinearLayout parent, @NonNull final LayoutInflater inflater,
                                             @NonNull final List<LookupResponse.Translation> translations) {

            int translationCount = 0;

            for (final LookupResponse.Translation translation : translations) {

                translationCount++;

                String count = Integer.toString(translationCount);

                final TextView textViewDefinitions = (TextView) inflater
                        .inflate(R.layout.component_dict_item_definition, null);
                parent.addView(textViewDefinitions);

                final SpannableStringBuilder stringBuilderDefinitions = new SpannableStringBuilder();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    stringBuilderDefinitions
                            .append(count, new StyleSpan(BOLD), SPAN_EXCLUSIVE_EXCLUSIVE)
                            .append(DOT, new StyleSpan(BOLD), SPAN_EXCLUSIVE_EXCLUSIVE)
                            .append(SPACE);
                }

                if (translation.getText() != null) {
                    stringBuilderDefinitions.append(translation.getText());

                    if (translation.getGen() != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stringBuilderDefinitions.append(SPACE)
                                    .append(translation.getGen(), new StyleSpan(ITALIC),
                                            SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        final int genStart = stringBuilderDefinitions.length() - translation.getGen().length();
                        final int genEnd = stringBuilderDefinitions.length();

                        stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (translation.getAsp() != null) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            stringBuilderDefinitions.append(SPACE)
                                    .append(translation.getAsp(), new StyleSpan(ITALIC),
                                            SPAN_EXCLUSIVE_EXCLUSIVE);
                        }

                        final int aspStart = stringBuilderDefinitions.length() - translation.getAsp().length();
                        final int aspEnd = stringBuilderDefinitions.length();

                        stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                aspStart, aspEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }

                if (translation.getSynonyms() != null) {
                    for (final LookupResponse.Syn syn : translation.getSynonyms()) {

                        if (syn.getText() != null) {
                            stringBuilderDefinitions.append(COMMA).append(SPACE)
                                    .append(syn.getText());

                            if (syn.getGen() != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    stringBuilderDefinitions.append(SPACE)
                                            .append(syn.getGen(), new StyleSpan(ITALIC),
                                                    SPAN_EXCLUSIVE_EXCLUSIVE);
                                }

                                final int genStart = stringBuilderDefinitions.length() - syn.getGen().length();
                                final int genEnd = stringBuilderDefinitions.length();

                                stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                        genStart, genEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                            }

                            if (syn.getAsp() != null) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    stringBuilderDefinitions.append(SPACE)
                                            .append(syn.getAsp(), new StyleSpan(ITALIC),
                                                    SPAN_EXCLUSIVE_EXCLUSIVE);
                                }

                                final int aspStart = stringBuilderDefinitions.length() - syn.getAsp().length();
                                final int aspEnd = stringBuilderDefinitions.length();

                                stringBuilderDefinitions.setSpan(new ForegroundColorSpan(secondaryItemsColor),
                                        aspStart, aspEnd, SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }
                    }
                }

                textViewDefinitions.setText(stringBuilderDefinitions);

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

        private static void makeMeanings(@NonNull final LinearLayout parent,
                                         @NonNull final LayoutInflater inflater, @NonNull final List<LookupResponse.Meaning> meanings) {

            final TextView textViewMeaning = (TextView) inflater
                    .inflate(R.layout.component_dict_item_meaning, null);
            parent.addView(textViewMeaning);

            final StringBuilder stringBuilderMeanings = new StringBuilder(OPEN_PARENTHESIS);

            for (final Iterator<LookupResponse.Meaning> iterator = meanings.iterator();
                 iterator.hasNext(); ) {

                final LookupResponse.Meaning meaning = iterator.next();

                stringBuilderMeanings.append(meaning.getText());

                if (iterator.hasNext()) {
                    stringBuilderMeanings.append(COMMA).append(SPACE);
                }
            }

            stringBuilderMeanings.append(CLOSE_PARENTHESIS);

            textViewMeaning.setText(stringBuilderMeanings.toString());
        }

        private static void makeExamples(@NonNull final LinearLayout parent,
                                         @NonNull final LayoutInflater inflater, @NonNull final List<LookupResponse.Example> examples) {

            final SpannableStringBuilder stringBuilderExample = new SpannableStringBuilder();

            for (final LookupResponse.Example example : examples) {

                final TextView textViewExample = (TextView) inflater
                        .inflate(R.layout.component_dict_item_example, null);
                parent.addView(textViewExample);

                stringBuilderExample.clear();

                stringBuilderExample
                        .append(example.getText());

                if (example.getTr() != null) {
                    stringBuilderExample.append(SPACE).append(TIRET).append(SPACE)
                            .append(example.getTr().get(0).getText());
                }

                stringBuilderExample.setSpan(new StyleSpan(ITALIC), 0, stringBuilderExample.length(),
                        SPAN_EXCLUSIVE_EXCLUSIVE);

                textViewExample.setText(stringBuilderExample);
            }
        }

        /**
         * Возвращает строку и информацией о транскрипции слова.
         */
        public static CharSequence formatDefinition(@NonNull final LookupResponse response) {

            if (response.getDefinitions() != null) {

                final SpannableStringBuilder formattedDefinition = new SpannableStringBuilder();

                formattedDefinition
                        .append(response.getDefinitions().get(0).getText());

                if (!CommonFunctions.StringIsNullOrEmpty(response.getDefinitions().get(0).getTs())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        formattedDefinition
                                .append(SPACE)
                                .append(OPEN_BRACE,
                                        new ForegroundColorSpan(secondaryItemsColor), SPAN_EXCLUSIVE_INCLUSIVE)
                                .append(response.getDefinitions().get(0).getTs())
                                .append(CLOSE_BRACE);
                    }
                }

                return formattedDefinition;
            }
            return EMPTY;
        }

        private DictionaryConstructor() {
        }
}