package org.mehrizi.crazydictionary;

public class Language {

    public static String[] getAvailableLanguages() {
        String[] arr = {"English", "French", "German", "Turkish", "Italian", "Swedish", "Greek"};

        return arr;
    }

    public static String[] getAvailableLanguages(Boolean shortForm) {
        if (!shortForm)
            return getAvailableLanguages();

        String[] arr = {"eng", "fra", "deu", "tur", "ita", "swe", "ell"};

        return arr;
    }

    public static String getShortForm(String longForm) {
        String[] longForms=getAvailableLanguages();
        for (int i=0; i<longForms.length;i++){
            if (longForm.equals(longForms[i]))
                return getAvailableLanguages(true)[i];

        }
        return "";
    }
}
