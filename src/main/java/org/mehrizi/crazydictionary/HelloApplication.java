package org.mehrizi.crazydictionary;

import java.io.RandomAccessFile;
import java.net.URL;
import java.nio.file.Files;

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

    public static String dicPath = System.getenv("SystemDrive")+"/mehrizi-dictionaries/";
    @Override
    public void start(Stage stage) throws IOException {


        Path dictionariesPath = Paths.get(dicPath);
        if (!Files.exists(dictionariesPath))
        {
            showDictionaryDownloadWindow(stage);
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dictionary-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 640, 440);

        stage.setTitle("Crazy 11 Dic!");
        stage.setScene(scene);
        stage.show();

    }

    public void showDictionaryDownloadWindow(Stage stage) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("download-view.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 640, 440);

        stage.setTitle("Crazy 11 Dic!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}