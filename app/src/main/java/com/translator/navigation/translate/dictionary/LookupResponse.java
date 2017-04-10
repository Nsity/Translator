package com.translator.navigation.translate.dictionary;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.util.List;

/**
 * Created by nsity on 11.04.17.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "def"
})
@JsonIgnoreProperties({"head"})
public final class LookupResponse {

    @JsonProperty("def")
    private List<Definition> definition = null;

    private boolean empty;

    @JsonProperty("def")
    public List<Definition> getDefinitions() {
        return definition;
    }

    @JsonProperty("def")
    public void setDefinition(final List<Definition> definition) {
        this.definition = definition;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public boolean isEmpty() {
        return definition == null || definition.size() == 0;
    }

    @Override
    public String toString() {
        return "LookupResponse{" +
                "definition=" + definition +
                '}';
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text",
            "pos",
            "ts",
            "tr"
    })
    public static final class Definition {

        @JsonProperty("text")
        private String text;
        @JsonProperty("pos")
        private String pos;
        @JsonProperty("ts")
        private String ts;
        @JsonProperty("tr")
        private List<Translation> translations = null;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @JsonProperty("pos")
        public String getPartOfSpeech() {
            return pos;
        }

        @JsonProperty("pos")
        public void setPos(final String pos) {
            this.pos = pos;
        }

        @JsonProperty("ts")
        public String getTs() {
            return ts;
        }

        @JsonProperty("ts")
        public void setTs(final String ts) {
            this.ts = ts;
        }

        @JsonProperty("tr")
        public List<Translation> getTranslations() {
            return translations;
        }

        @JsonProperty("tr")
        public void setTranslations(final List<Translation> translations) {
            this.translations = translations;
        }

        @Override
        public String toString() {
            return "Definition{" +
                    "text='" + text + '\'' +
                    ", pos='" + pos + '\'' +
                    ", ts='" + ts + '\'' +
                    ", translations=" + translations +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text",
            "tr"
    })
    public static final class Example {

        @JsonProperty("text")
        private String text;
        @JsonProperty("tr")
        private List<Tr_> tr = null;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @JsonProperty("tr")
        public List<Tr_> getTr() {
            return tr;
        }

        @JsonProperty("tr")
        public void setTr(final List<Tr_> tr) {
            this.tr = tr;
        }


        @Override
        public String toString() {
            return "Example{" +
                    "text='" + text + '\'' +
                    ", translations=" + tr +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text"
    })
    public static final class Meaning {

        @JsonProperty("text")
        private String text;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Meaning{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text",
            "pos",
            "gen"
    })
    public static final class Syn {

        @JsonProperty("text")
        private String text;
        @JsonProperty("pos")
        private String pos;
        @JsonProperty("gen")
        private String gen;
        @JsonProperty("asp")
        private String asp;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @JsonProperty("pos")
        public String getPos() {
            return pos;
        }

        @JsonProperty("pos")
        public void setPos(final String pos) {
            this.pos = pos;
        }

        @JsonProperty("gen")
        public String getGen() {
            return gen;
        }

        @JsonProperty("gen")
        public void setGen(final String gen) {
            this.gen = gen;
        }

        @JsonProperty("asp")
        public String getAsp() {
            return asp;
        }

        @JsonProperty("asp")
        public void setAsp(final String asp) {
            this.asp = asp;
        }

        @Override
        public String toString() {
            return "Syn{" +
                    "text='" + text + '\'' +
                    ", pos='" + pos + '\'' +
                    ", gen='" + gen + '\'' +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text",
            "pos",
            "gen",
            "syn",
            "mean",
            "ex",
            "asp"
    })
    public static final class Translation {

        @JsonProperty("text")
        private String text;
        @JsonProperty("pos")
        private String pos;
        @JsonProperty("gen")
        private String gen;
        @JsonProperty("syn")
        private List<Syn> syn = null;
        @JsonProperty("mean")
        private List<Meaning> meaning = null;
        @JsonProperty("ex")
        private List<Example> examples = null;
        @JsonProperty("asp")
        private String asp;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @JsonProperty("pos")
        public String getPartOfSpeech() {
            return pos;
        }

        @JsonProperty("pos")
        public void setPos(final String pos) {
            this.pos = pos;
        }

        @JsonProperty("gen")
        public String getGen() {
            return gen;
        }

        @JsonProperty("gen")
        public void setGen(final String gen) {
            this.gen = gen;
        }

        @JsonProperty("syn")
        public List<Syn> getSynonyms() {
            return syn;
        }

        @JsonProperty("syn")
        public void setSyn(final List<Syn> syn) {
            this.syn = syn;
        }

        @JsonProperty("mean")
        public List<Meaning> getMeanings() {
            return meaning;
        }

        @JsonProperty("mean")
        public void setMeaning(final List<Meaning> meaning) {
            this.meaning = meaning;
        }

        @JsonProperty("ex")
        public List<Example> getExamples() {
            return examples;
        }

        @JsonProperty("ex")
        public void setExamples(final List<Example> examples) {
            this.examples = examples;
        }

        @JsonProperty("asp")
        public String getAsp() {
            return asp;
        }

        @JsonProperty("asp")
        public void setAsp(final String asp) {
            this.asp = asp;
        }

        @Override
        public String toString() {
            return "Translation{" +
                    "text='" + text + '\'' +
                    ", pos='" + pos + '\'' +
                    ", gen='" + gen + '\'' +
                    ", syn=" + syn +
                    ", meaning=" + meaning +
                    ", examples=" + examples +
                    ", asp='" + asp + '\'' +
                    '}';
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonPropertyOrder({
            "text"
    })
    public static final class Tr_ {

        @JsonProperty("text")
        private String text;

        @JsonProperty("text")
        public String getText() {
            return text;
        }

        @JsonProperty("text")
        public void setText(final String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return "Tr_{" +
                    "text='" + text + '\'' +
                    '}';
        }
    }
}