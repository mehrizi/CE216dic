package org.mehrizi.crazydictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Translation {
    private String fromLang;
    public ArrayList<TranslatedItem> translations = new ArrayList<>();

    public Translation(String fromLang) {
        this.fromLang = fromLang;
    }

    public void translate(String word) {
        ArrayList<String> untranslatedTargets = new ArrayList<>();
        for (String targetLang : targetLangs()) {
            TranslatedItem item = translate(word, targetLang);
            if (item==null || item.words.size()==0) {
                untranslatedTargets.add(targetLang);
            }
            else
                translations.add(item);
        }

        // Now lets find items that has no direct translations
        for (String untranslated:untranslatedTargets){
            boolean found=false;
            for (TranslatedItem t:translations){
                Translation translateThrough = new Translation(t.getTargetLang());
                for(String w:t.words){
                    TranslatedItem newTranslation = translateThrough.translate(w,untranslated);
                    if (newTranslation.words.size() == 0)
                        continue;

                    translations.add(newTranslation);
                    found=true;
                    break;
                }
                if (found)
                    break;
            }

        }
    }

    public TranslatedItem translate(String word, String targetLang) {
        BufferedReader reader;

        String dicPath = getIndexedFile(word, targetLang);
        if (dicPath.equals("") || !Files.exists(Paths.get(dicPath)))
            return null;

        try {
            reader = new BufferedReader(new FileReader(dicPath,StandardCharsets.UTF_8));
            String line = reader.readLine();

            while (line != null) {
                String[] segments = line.split(":");
                if (word.equalsIgnoreCase(segments[0])) {
                    TranslatedItem item = new TranslatedItem(targetLang);
                    for (int i = 1; i < segments.length; i++)
                    {
//                        byte[] charset = segments[i].getBytes(StandardCharsets.UTF_8);
//                        String result = new String(charset, StandardCharsets.UTF_8);
//                        item.addWord(result);
                        item.addWord(segments[i]);
                    }

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
        if (!Files.exists(Paths.get(dicIndexPath)))
            return "";

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
