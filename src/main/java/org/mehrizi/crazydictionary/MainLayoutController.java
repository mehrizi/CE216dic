package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML
    private ComboBox fromLanguageCombo;

    @FXML
    private Button helpButton;

    @FXML
    private Button searchButton;

    @FXML
    private ListView translationResultList;

    @FXML
    private TextField inputWord;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        System.out.println(Charset.defaultCharset());


        fromLanguageCombo.getItems().addAll(FXCollections.observableArrayList(Language.getAvailableLanguages()));
        fromLanguageCombo.setVisibleRowCount(6);
        fromLanguageCombo.getSelectionModel().selectFirst();
        fromLanguageCombo.setValue("English");

        searchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleTranslation();
            }
        });

        inputWord.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)
                    handleTranslation();
            }
        });


    }

    private void handleTranslation()
    {
        translationResultList.getItems().clear();
        String fromLang = Language.getShortForm(fromLanguageCombo.getValue().toString());
        Translation result = new Translation(fromLang);
        result.translate(inputWord.getText());
        if (result.translations.size() == 0)
            translationResultList.getItems().add("Nothing found!");
        for(TranslatedItem item:result.translations){
            int i = 0;
            for (String word: item.words)
            {
                if (i==0)
                    translationResultList.getItems().add(item.getFullTargetLang() );
                translationResultList.getItems().add("       "+word);
                i++;
            }

        }

    }
}