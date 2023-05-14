package org.mehrizi.crazydictionary;

import org.apache.commons.lang.StringEscapeUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TranslationTest {

    @Test
    void ifHelloExistsToAllOthers() {

        ArrayList<TranslatedItem> translations = translate("hello", "eng");
        assertEquals(Language.instance().langs.size() - 1, translations.size()); //-1 is for excluding from eng-eng
    }

    @Test
    void ifHelloExistsCaseInsensitiveInAllOthers() {

        ArrayList<TranslatedItem> translations = translate("Hello", "eng");
        assertEquals(Language.instance().langs.size() - 1, translations.size()); //-1 is for excluding from eng-eng
        translations = translate("HeLLo", "eng");
        assertEquals(Language.instance().langs.size() - 1, translations.size()); //-1 is for excluding from eng-eng
        translations = translate("heLlo", "eng");
        assertEquals(Language.instance().langs.size() - 1, translations.size()); //-1 is for excluding from eng-eng
        translations = translate("HELLO", "eng");
        assertEquals(Language.instance().langs.size() - 1, translations.size()); //-1 is for excluding from eng-eng
    }


    @Test
    void checkTurkishToGreek() {

        ArrayList<TranslatedItem> translations = translate("baba", "tur");
        Boolean foundInGreek = false;
        for (TranslatedItem item : translations) {
            if (item.getTargetLang().equals("ell"))
                foundInGreek = true;
        }
        assertEquals(foundInGreek, true);
    }

    @Test
    void checkGreekAlphabets() {

        ArrayList<TranslatedItem> translations = translate("baba", "tur");
        Boolean foundInGreekTrueLetters = false;
        for (TranslatedItem item : translations) {
            if (item.getTargetLang().equals("ell")) {
                for (String word : item.words) {
                    if (word.equalsIgnoreCase(StringEscapeUtils.unescapeJava("\\u03bc\\u03c0\\u03b1\\u03bc\\u03c0\\u03ac\\u03c2")))
                        foundInGreekTrueLetters = true;
                }

            }
        }
        assertEquals(foundInGreekTrueLetters, true);
    }


    @Test
    void performanceCheckForlanguageStwitch() {
        // here we translate some words successively to see the performance and time
        Long last = System.currentTimeMillis();
        Long maxLatency = 0L;
        //  We go over five words
        String[] words = {"eng", "fre", "tur"};

        for (int i = 0; i < words.length; i++) {
            ArrayList<TranslatedItem> dummyObj = translate("baba", words[i]);
            maxLatency = Math.max(maxLatency, last - System.currentTimeMillis());
            last = System.currentTimeMillis();
        }

        assertTrue(maxLatency<50);

    }


    private ArrayList<TranslatedItem> translate(String word, String fromLang) {
        Translation testObject = new Translation(fromLang);
        testObject.translate(word);
        // Now we test for at least one translation for each language
        return testObject.translations;

    }

}