package org.mehrizi.crazydictionary;

import java.util.ArrayList;

public class Translation {
    private String fromLang;

    public Translation(String fromLang) {
        this.fromLang = fromLang;
    }

    public ArrayList<TranslatedItem> translate(String word) {
        ArrayList<TranslatedItem> translations = new ArrayList<TranslatedItem>();

        for(String targetLang: targetLangs()){
            TranslatedItem item = new TranslatedItem(targetLang);
            item.addTranslation("Sample 1");
            item.addTranslation("Sample 2");
            translations.add(item);
        }


        return translations;

        
    }

    public ArrayList<String> targetLangs() {
        ArrayList<String> targetLanguages = new ArrayList<>();
        int i = 0;
        for (String lang : Language.getAvailableLanguages()) {
            if (lang != fromLang) {
                targetLanguages.add(lang);
            }
        }
        return targetLanguages;
    }
}
