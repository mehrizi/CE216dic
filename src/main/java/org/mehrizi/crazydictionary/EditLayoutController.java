package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;

public class EditLayoutController implements Initializable {

    public static TranslatedItem item;
    public static String word;
    public static String fromLang;

    @FXML
    private VBox vBox;

    @FXML
    private Text explanationText;

    @FXML
    private Button cancelBtn;
    @FXML
    private Button saveBtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        explanationText.setText("You are editing definitions for "+word+" from language "+Language.getLongForm(fromLang) +" to "+item.getFullTargetLang());
        ArrayList<TextField> fields = new ArrayList<>();
        for (String translationText:item.words){
            TextField txtField = new TextField(translationText);
            fields.add(txtField);
            vBox.getChildren().add(txtField);
        }

        Button addNewButton = new Button("Add");
        addNewButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                TextField txtField = new TextField();
                fields.add(txtField);
                vBox.getChildren().remove(addNewButton);
                vBox.getChildren().add(txtField);
                vBox.getChildren().add(addNewButton);

            }
        });
        vBox.getChildren().add(addNewButton);

        cancelBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    HelloApplication.myApp.showDictionaryWindow(HelloApplication.myStage);
                    MainLayoutController.instance.setData(Language.getLongForm(fromLang),word);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        saveBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
               Translation Obj = new Translation(fromLang);
                try {
                    Obj.saveTranslations(item.getTargetLang(), word,fields);
                    HelloApplication.myApp.showDictionaryWindow(HelloApplication.myStage);
                    MainLayoutController.instance.setData(Language.getLongForm(fromLang),word);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });






    }

}