package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {
    @FXML
    private ComboBox fromLanguageCombo;

    @FXML
    private Button helpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Positioning
//        helpButton.
        fromLanguageCombo.getItems().addAll(FXCollections.observableArrayList(Language.getAvailableLanguages()));
        fromLanguageCombo.setVisibleRowCount(3);
        fromLanguageCombo.getSelectionModel().selectFirst();
        fromLanguageCombo.setValue("EN");


    }

//    public MainLayoutController(){
//                fromLanguageCombo.getItems().addAll(FXCollections.observableArrayList(Language.getAvailableLanguages()));
//        fromLanguageCombo.setVisibleRowCount(3);
//        fromLanguageCombo.getSelectionModel().selectFirst();
//        fromLanguageCombo.setValue("EN");
//
//    }
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }
}