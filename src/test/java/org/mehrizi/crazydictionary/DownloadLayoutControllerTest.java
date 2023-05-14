package org.mehrizi.crazydictionary;

//import com.sun.javafx.fxml.builder.JavaFXSceneBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DownloadLayoutControllerTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void handleFetch() throws IOException {

        Map<Integer, ArrayList<String>> retObj =  DownloadLayoutController.handleFetch(DownloadLayoutController.apiURL);
        ArrayList<String> items = retObj.get(retObj.keySet().toArray()[0]);
        Boolean isMoreThan30dictionariesFound = items.size()>30;

        assertTrue(isMoreThan30dictionariesFound);

    }

    @Test
    void handleFetchSizeCheck() throws IOException {

        Map<Integer, ArrayList<String>> retObj =  DownloadLayoutController.handleFetch(DownloadLayoutController.apiURL);
        Integer totalSize = (Integer) retObj.keySet().toArray()[0];

        assertTrue(totalSize>40*1024*1024);

    }

    @Test
    void performanceCheckForTyping() {
        // here we translate some words successively to see the performance and time
        Long last = System.currentTimeMillis();
        Long maxLatency = 0L;
        //  We go over five words
        String[] words = {"He", "Hel", "Hell", "Hello"};

        for (int i = 0; i < words.length; i++) {
            ArrayList<TranslatedItem> dummyObj = translate(words[i], "eng");
            maxLatency = Math.max(maxLatency, last - System.currentTimeMillis());
            last = System.currentTimeMillis();
        }

        assertTrue(maxLatency<50);



    }
    @Test
    void performanceCheckForDifferentWords() {
        // here we translate some words successively to see the performance and time
        Long last = System.currentTimeMillis();
        Long maxLatency = 0L;
        //  We go over five words
        String[] words = {"hello", "father", "mother", "joke"};

        for (int i = 0; i < words.length; i++) {
            ArrayList<TranslatedItem> dummyObj = translate(words[i], "eng");
            maxLatency = Math.max(maxLatency, last - System.currentTimeMillis());
            last = System.currentTimeMillis();
        }

        assertTrue(maxLatency<50);

    }

    @Test
    void checkIfAddingNewLanguageIncreaseDownloadSize() throws IOException {

        Map<Integer, ArrayList<String>> retObj =  DownloadLayoutController.handleFetch(DownloadLayoutController.apiURL);
        Integer totalSize1 = (Integer) retObj.keySet().toArray()[0];

        Language.instance().langs.put("ara","Arabic");

        Map<Integer, ArrayList<String>> retObj2 =  DownloadLayoutController.handleFetch(DownloadLayoutController.apiURL);
        Integer totalSize2 = (Integer) retObj2.keySet().toArray()[0];

        Language.instance().langs.remove("ara");
        assertTrue(totalSize2>totalSize1);
    }

    private ArrayList<TranslatedItem> translate(String word, String fromLang) {
        Translation testObject = new Translation(fromLang);
        testObject.translate(word);
        // Now we test for at least one translation for each language
        return testObject.translations;

    }

}