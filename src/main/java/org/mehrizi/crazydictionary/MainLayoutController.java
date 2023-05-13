package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    public static MainLayoutController instance;
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

        instance = this;
        fromLanguageCombo.getItems().addAll(FXCollections.observableArrayList(Language.instance().langs.values()));
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
//                if (event.getCode() == KeyCode.ENTER)
                    handleTranslation();
            }
        });


    }

    public void setData(String fromLang,String word){
        fromLanguageCombo.setValue(fromLang);
        inputWord.setText(word);
        handleTranslation();
    }
    private void handleTranslation()
    {
        translationResultList.getItems().clear();
        String fromLang = Language.getShortForm(fromLanguageCombo.getValue().toString());
        String wordToTranslate = inputWord.getText();
        Translation result = new Translation(fromLang);
        result.translate(wordToTranslate);
        if (result.translations.size() == 0)
            translationResultList.getItems().add("Nothing found!");
        for(TranslatedItem item:result.translations){
            int i = 0;
            for (String word: item.words)
            {
                if (i==0){
                    HBox hBox = new HBox();
                    Button editBtn = new Button("edit");
                    editBtn.setMaxHeight(18);
                    editBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent event) {
                            EditLayoutController.item = item;
                            EditLayoutController.fromLang = fromLang;
                            EditLayoutController.word = wordToTranslate;
                            try {
                                HelloApplication.myApp.showEditWindow(HelloApplication.myStage);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });


                    Pane spacer = new Pane();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    spacer.setMinSize(10, 1);

                    Label txt = new Label();
                    txt.setText(Language.instance().langs.get(item.getTargetLang()));

                    hBox.getChildren().addAll(txt,spacer,editBtn);
                    translationResultList.getItems().add(hBox);// +  );
                }

                translationResultList.getItems().add("       "+word);
                i++;
            }

        }

    }
}