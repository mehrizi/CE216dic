package org.mehrizi.crazydictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Translation {
    private String fromLang;

    public Translation(String fromLang) {
        this.fromLang = fromLang;
    }

    public ArrayList<TranslatedItem> translate(String word) {
        ArrayList<TranslatedItem> translations = new ArrayList<TranslatedItem>();

        for (String targetLang : targetLangs()) {
            translate(word, targetLang);
            translations.add(translate(word, targetLang));
        }


        return translations;
    }

    private TranslatedItem translate(String word, String targetLang) {
        BufferedReader reader;

        String dicPath = getIndexedFile(word, targetLang);
        if (!Files.exists(Paths.get(dicPath)))
            return translateThrough(word, targetLang);

        try {
            reader = new BufferedReader(new FileReader(dicPath));
            String line = reader.readLine();

            while (line != null) {
                String[] segments = line.split(":");
                if (word.equalsIgnoreCase(segments[0])) {
                    TranslatedItem item = new TranslatedItem(targetLang);
                    for (int i = 1; i < segments.length; i++)
                        item.addTranslation(segments[i]);

                    return item;
                }
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        TranslatedItem item = new TranslatedItem(targetLang);
        return item;

    }

    private String getIndexedFile(String word, String targetLang) {
        String dicIndexPath = HelloApplication.dicPath + fromLang + "-" + targetLang + ".mmsdic.ind";
        Character firstChar = word.toLowerCase().charAt(0);
        BufferedReader reader;

        try {
            reader = new BufferedReader(new FileReader(dicIndexPath));
            String line = reader.readLine();

            while (line != null) {
                if (firstChar.equals(line.charAt(0))) {
                    String[] segments = line.split(":");
                    return HelloApplication.dicPath + fromLang + "-" + targetLang + "-" + segments[1] + ".mmsdic";
                }

                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    private TranslatedItem translateThrough(String word, String targetLang) {
        // @todo handle through other dics
        return new TranslatedItem(targetLang);

    }

    public ArrayList<String> targetLangs() {
        ArrayList<String> targetLanguages = new ArrayList<>();
        int i = 0;
        for (String lang : Language.getAvailableLanguages(true)) {
            if (lang != fromLang) {
                targetLanguages.add(lang);
            }
        }
        return targetLanguages;
    }
}
