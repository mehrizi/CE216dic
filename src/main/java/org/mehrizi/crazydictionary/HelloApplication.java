package org.mehrizi.crazydictionary;

import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Files;

//import itkach.slob.Slob;
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HelloApplication extends Application {

//    Path dictionariesPath = Paths.get("C:/mehrizi-dictionaries");
    @Override
    public void start(Stage stage) throws IOException {

//        String testSlobName = "freedict-eng-tur.slob";
//        ClassLoader classLoader = getClass().getClassLoader();
//        URL resource = classLoader.getResource(testSlobName);
//        RandomAccessFile f = new RandomAccessFile(resource.getFile(), "r");
//        Slob s = new Slob(f.getChannel(), testSlobName);
//
//        Slob.Blob earthBlob = Slob.find("earth", s).next();

//        System.out.println(s.);

//        if (!Files.exists(dictionariesPath))
//        {
//            showDictionaryDownloadWindow(stage);
//            return;
//        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dictionary-view.fxml"));

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

    public void showDictionaryDownloadWindow(Stage stage) throws IOException{

    }

    public static void main(String[] args) {
        launch();
    }
}