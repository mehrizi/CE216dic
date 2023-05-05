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
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DownloadLayoutController implements Initializable {
    @FXML
    private Button downloadButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Positioning
//        helpButton.

        downloadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleDownload();
            }
        });


    }

//    public static JSONObject getJson(URL url) {
//        String json = IOUtils.toString(url, Charset.forName("UTF-8"));
//        return new JSONObject(json);
//    }
    private void handleDownload()
    {

//        Translation result = new Translation(fromLanguageCombo.getValue().toString());
//        ArrayList<TranslatedItem> items = result.translate(inputWord.getText());
//        for(TranslatedItem item:items){
//            for (String word: item.translations)
//            {
//                translationResultList.getItems().add(item.getTargetLang()+':'+word);
//            }
//
//        }
//
    }
}