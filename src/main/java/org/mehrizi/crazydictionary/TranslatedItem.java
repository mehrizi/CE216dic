package org.mehrizi.crazydictionary;

import java.util.ArrayList;

public class TranslatedItem {

    private String targetLang;

    public ArrayList<String> translations = new ArrayList<>();

    public String getTargetLang() {
        return targetLang;
    }
    public TranslatedItem(String targetLang)
    {
        this.targetLang = targetLang;
    }

    public void addTranslation(String word)
    {
        translations.add(word);
    }


}
