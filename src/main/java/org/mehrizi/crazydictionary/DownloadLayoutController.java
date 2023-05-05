package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class DownloadLayoutController implements Initializable {

    private String apiURL = "https://freedict.org/freedict-database.json";
    private ArrayList<String> urlsToDownload = new ArrayList<>();
    private Integer totalSize = 0;
    private Integer downloadedSize = 0;

    @FXML
    private Button downloadButton;

    @FXML
    private Button fetchButton;

    @FXML
    private Text messageText;

    @FXML
    private ProgressBar downloadProgress;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        downloadButton.setDisable(true);
        downloadButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    handleDownload();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        fetchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    handleFetch();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });


    }

    private void handleDownload() throws IOException {

        downloadButton.setDisable(true);
        fetchButton.setDisable(true);
        Files.createDirectories(Paths.get(HelloApplication.dicPath));

        for (String fileUrl : urlsToDownload) {
            URL url = new URL(fileUrl);
            String fileName = HelloApplication.dicPath+ FilenameUtils.getName(url.getPath());

            FileUtils.copyURLToFile(url, new File(fileName));
            Long bytes = Files.size(Path.of(fileName));
            downloadedSize +=  bytes.intValue();
            downloadProgress.setProgress((double) downloadedSize/totalSize);

        }
    }

    private void handleFetch() throws IOException {

        // First lets get json from API
        fetchButton.setDisable(true);
        URL url = new URL(apiURL);
        String json = IOUtils.toString(url, Charset.forName("UTF-8"));
        JSONArray apiResult = new JSONArray(json);

        // Now lets build items to download
        String[] availableLangs = Language.getAvailableLanguages(true);
        for (int i = 0; i < apiResult.length(); i++) {
            JSONObject item = apiResult.getJSONObject(i);
            if (!item.has("name"))
                continue;
            String name = (String) item.get("name");

            // iterating over needed languages to see if the item has one of these lang
            for (String lang : availableLangs) {
                if (name.contains(lang)) {
                    // first let the correct src package
                    JSONArray releases = (JSONArray) item.get("releases");
                    for (int j = 0; j < releases.length(); j++) {
                        JSONObject release = (JSONObject) releases.get(j);

                        String platform = (String) release.get("platform");
                        if (platform.equals("src")) {
                            urlsToDownload.add((String) release.get("URL"));
                            totalSize += Integer.parseInt(release.get("size").toString());
                        }
                    }

                }
            }
        }

        messageText.setText("Total of " + urlsToDownload.size() +
                " dictionaries found with total download size of " + FileUtils.byteCountToDisplaySize(totalSize) +
                ". Do you want to download?");

        downloadButton.setDisable(false);
        fetchButton.setDisable(false);

    }
}