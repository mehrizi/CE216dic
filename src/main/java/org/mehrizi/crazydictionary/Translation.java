package org.mehrizi.crazydictionary;

import javafx.scene.control.TextField;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

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
            if (item == null || item.words.size() == 0) {
                untranslatedTargets.add(targetLang);
            } else
                translations.add(item);
        }

        // Now lets find items that has no direct translations
        for (String untranslated : untranslatedTargets) {
            boolean found = false;
            for (TranslatedItem t : translations) {
                Translation translateThrough = new Translation(t.getTargetLang());
                for (String w : t.words) {
                    TranslatedItem newTranslation = translateThrough.translate(w, untranslated);
                    if (newTranslation == null || newTranslation.words.size() == 0)
                        continue;

                    translations.add(newTranslation);
                    found = true;
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
        if (dicPath == null ||
                dicPath.equals("") || !Files.exists(Paths.get(dicPath)))
            return null;

        try {
            reader = new BufferedReader(new FileReader(dicPath, StandardCharsets.UTF_8));
            String line = reader.readLine();

            while (line != null) {
                String[] segments = line.split(":");
                if (word.equalsIgnoreCase(segments[0])) {
                    TranslatedItem item = new TranslatedItem(targetLang);
                    for (int i = 1; i < segments.length; i++) {
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

    private String getIndexPath(String targetLang) {
        return HelloApplication.dicPath + fromLang + "-" + targetLang + ".mmsdic.ind";
    }

    // returns null if no index file available
    // returns empty string if no file available
    private String getIndexedFile(String word, String targetLang) {
        String dicIndexPath = getIndexPath(targetLang);
        if (!Files.exists(Paths.get(dicIndexPath)))
            return null;

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

        for (Map.Entry<String, String> lang: Language.instance().langs.entrySet()) {
            if (!lang.getKey().equals(fromLang)) {
                targetLanguages.add(lang.getKey());
            }
        }
        return targetLanguages;
    }

    public void saveTranslations(String toLang, String word, ArrayList<TextField> items) throws IOException {
        String fileToEdit = getIndexedFile(word, toLang);
        String indexPath = getIndexPath(toLang);

        boolean needToCreateDicFile = false;
        if (fileToEdit == null) {// no index file
            FileWriter indexFile = new FileWriter(indexPath);
            indexFile.write(word.charAt(0) + ":0" + System.lineSeparator());
            indexFile.close();
            fileToEdit = HelloApplication.dicPath + fromLang + "-" + toLang + "-0.mmsdic";
            needToCreateDicFile = true;

        } else if (fileToEdit.equals("")) { // there is an index file but no mmsdic file
            BufferedReader reader;
            int i = 0;
            try {
                reader = new BufferedReader(new FileReader(indexPath));
                String line = reader.readLine();

                while (line != null) {
                    i++;
                    line = reader.readLine();
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Files.write(Paths.get(indexPath), (word.charAt(0) + ":" + String.valueOf(i)).getBytes(), StandardOpenOption.APPEND);
            fileToEdit = HelloApplication.dicPath + fromLang + "-" + toLang + "-" + String.valueOf(i) + ".mmsdic";
            needToCreateDicFile = true;
        }

        // now in any of case above lets create the file
        // we do this so rest of the operation can be the same for cases there is a dic file
        if (needToCreateDicFile){
            File file = new File(fileToEdit);
            file.createNewFile();
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(fileToEdit), StandardCharsets.UTF_8);
            ArrayList<String> newLines = new ArrayList<>();

            boolean wordFound = false;
            for (String line : lines) {
                String[] segments = line.split(":");

                if (segments[0].equalsIgnoreCase(word)) { //if word found
                    wordFound = true;
                    ArrayList<String> newSegments = new ArrayList<>();
                    newSegments.add(word);
                    for (TextField item :items) {
                        if (!item.getText().equals(""))
                        newSegments.add(item.getText());
                    }
                    newLines.add(String.join(":", newSegments));
                }
                else newLines.add(line);
            }
            if (!wordFound)
            {
                ArrayList<String> segments = new ArrayList<>();
                segments.add(word);
                for (TextField item: items) {
                    if (!item.getText().equals(""))
                        segments.add(item.getText());
                }
                newLines.add(String.join(":", segments));
            }

            // now lets write to the file
            FileWriter writer = new FileWriter(fileToEdit,StandardCharsets.UTF_8);
            for(String line: newLines) {
                writer.write(line + System.lineSeparator());
            }
            writer.close();
        } catch (IOException ex) {
            System.out.format("I/O error: %s%n", ex);
        }

    }
}
