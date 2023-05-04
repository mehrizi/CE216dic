package org.mehrizi.crazydictionary;

import javafx.application.Application;
import javafx.beans.value.ObservableListValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 640, 440);
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());

//        ComboBox fromLanguageCombo = (ComboBox) scene.lookup("#fromLanguageCombo");

//        ObservableList<String> hobbies = FXCollections.observableArrayList("EN","FR","TR");

//        fromLanguageCombo.getItems().addAll(FXCollections.observableArrayList(Language.getAvailableLanguages()));
//        fromLanguageCombo.setVisibleRowCount(3);
//        fromLanguageCombo.getSelectionModel().selectFirst();
//        fromLanguageCombo.setValue("EN");

        stage.setTitle("Crazy 11 Dic!");
        stage.setScene(scene);
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}