package com.translator.system;

import java.util.HashMap;

/**
 * Created by nsity on 21.03.17.
 */

public class Language {

    private String name;
    private String fullName;


   private static HashMap<String, String> hashMap = new HashMap<>();


    public static String getLanguageDisplay(String language) {
        return CommonFunctions.setFirstLetterUpperCase(hashMap.get(language));
    }


    public static void setUpLanguages() {
        if(hashMap == null) {
            hashMap = new HashMap<>();
        }

        hashMap.put("az", "азербайджанский");
        hashMap.put("sq", "албанский");
        hashMap.put("am", "амхарский");
        hashMap.put("en", "английский");
        hashMap.put("ar", "арабский");
        hashMap.put("hy", "армянский");
        hashMap.put("af", "африкаанс");
        hashMap.put("eu", "баскский");
        hashMap.put("ba", "башкирский");
        hashMap.put("be","белорусский");
        hashMap.put("bn","бенгальский");
        hashMap.put("bg","болгарский");
        hashMap.put("bs","боснийский");
        hashMap.put("cy","валлийский");
        hashMap.put("hu","венгерский");
        hashMap.put("vi","вьетнамский");
        hashMap.put("ht","гаитянский (креольский)");
        hashMap.put("gl","галисийский");
        hashMap.put("nl","голландский");
        hashMap.put("mrj","горномарийский");
        hashMap.put("el","греческий");
        hashMap.put("ka","грузинский");
        hashMap.put("gu","гуджарати");
        hashMap.put("da","датский");
        hashMap.put("he","иврит");
        hashMap.put("yi","идиш");
        hashMap.put("id","индонезийский");
        hashMap.put("ga","ирландский");
        hashMap.put("it","итальянский");
        hashMap.put("is","исландский");
        hashMap.put("es","испанский");
        hashMap.put("kk","казахский");
        hashMap.put("kn","каннада");
        hashMap.put("ca","каталанский");
        hashMap.put("ky","киргизский");
        hashMap.put("zh","китайский");
        hashMap.put("ko","корейский");
        hashMap.put("xh","коса");
        hashMap.put("la","латынь");
        hashMap.put("lv","латышский");
        hashMap.put("lt","литовский");
        hashMap.put("lb","люксембургский");
        hashMap.put("mg","малагасийский");
        hashMap.put("ms","малайский");
        hashMap.put("ml","малаялам");
        hashMap.put("mt","мальтийский");
        hashMap.put("mk","македонский");
        hashMap.put("mi","маори");
        hashMap.put("mr","маратхи");
        hashMap.put("mhr","марийский");
        hashMap.put("mn","монгольский");
        hashMap.put("de","немецкий");
        hashMap.put("ne","непальский");
        hashMap.put("no","норвежский");
        hashMap.put("pa","панджаби");
        hashMap.put("pap","папьяменто");
        hashMap.put("fa","персидский");
        hashMap.put("pl","польский");
        hashMap.put("pt","португальский");
        hashMap.put("ro","румынский");
        hashMap.put("ru","русский");
        hashMap.put("ceb","себуанский");
        hashMap.put("sr","сербский");
        hashMap.put("si","сингальский");
        hashMap.put("sk","словацкий");
        hashMap.put("sl","словенский");
        hashMap.put("sw","суахили");
        hashMap.put("su","сунданский");
        hashMap.put("tg","таджикский");
        hashMap.put("th","тайский");
        hashMap.put("tl","тагальский");
        hashMap.put("ta","тамильский");
        hashMap.put("tt","татарский");
        hashMap.put("te","телугу");
        hashMap.put("tr","турецкий");
        hashMap.put("udm","удмуртский");
        hashMap.put("uz","узбекский");
        hashMap.put("uk","украинский");
        hashMap.put("ur","урду");
        hashMap.put("fi","финский");
        hashMap.put("fr","французский");
        hashMap.put("hi","хинди");
        hashMap.put("hr","хорватский");
        hashMap.put("cs","чешский");
        hashMap.put("sv","шведский");
        hashMap.put("gd","шотландский");
        hashMap.put("et","эстонский");
        hashMap.put("eo","эсперанто");
        hashMap.put("jv","яванский");
        hashMap.put("ja","японский");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
