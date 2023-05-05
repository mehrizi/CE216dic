package org.mehrizi.crazydictionary;

import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.lzma.LZMACompressorInputStream;
import org.apache.commons.compress.compressors.xz.XZCompressorInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
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
import java.util.LinkedList;
import java.util.List;
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

    @FXML
    private Text progressBarText;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        if (true) {
//            try {
//                handleExtract();
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            } catch (ArchiveException e) {
//                throw new RuntimeException(e);
//            }
//            return;
//        }


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
        progressBarText.setText("Downloading...");

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                for (String fileUrl : urlsToDownload) {
                    URL url = null;
                    try {
                        url = new URL(fileUrl);
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                    String fileName = HelloApplication.dicPath + FilenameUtils.getName(url.getPath());

                    try {
                        FileUtils.copyURLToFile(url, new File(fileName));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Long bytes = null;
                    try {
                        bytes = Files.size(Path.of(fileName));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    downloadedSize += bytes.intValue();
                    downloadProgress.setProgress((double) downloadedSize / totalSize);
                }
                return null;
            }
        };


        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
            downloadButton.setDisable(true);
            fetchButton.setDisable(false);
            try {
                Files.deleteIfExists(Paths.get(HelloApplication.dicPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            progressBarText.setText("Download error! Please Try again");
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            try {
                handleExtract();
            } catch (IOException | ArchiveException e) {
                throw new RuntimeException(e);
            }
        });
        new Thread(task).start();

    }

    private void handleExtract() throws IOException, ArchiveException {

        progressBarText.setText("Extracting...");
        downloadProgress.setProgress(0);

        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                File folder = new File(HelloApplication.dicPath);
                File[] listOfFiles = folder.listFiles();

                for (int i = 0; i < listOfFiles.length; i++) {
                    if (listOfFiles[i].isFile()) {
                        String xzFilePath = listOfFiles[i].getPath();
                        String tarFilePath = xzFilePath.replace(".xz", "");

                        // First extract the xz file
                        InputStream fin = Files.newInputStream(Paths.get(xzFilePath));
                        BufferedInputStream in = new BufferedInputStream(fin);
                        OutputStream out = Files.newOutputStream(Paths.get(tarFilePath));
                        XZCompressorInputStream xzIn = new XZCompressorInputStream(in);
                        final byte[] buffer = new byte[8192];
                        int n = 0;
                        while (-1 != (n = xzIn.read(buffer))) {
                            out.write(buffer, 0, n);
                        }
                        out.close();
                        xzIn.close();

                        // Now lets get the tei file out
                        final InputStream is = new FileInputStream(new File(tarFilePath));
                        final TarArchiveInputStream debInputStream = (TarArchiveInputStream) new ArchiveStreamFactory().createArchiveInputStream("tar", is);
                        TarArchiveEntry entry = null;
                        while ((entry = (TarArchiveEntry) debInputStream.getNextEntry()) != null) {
                            if (entry.getName().endsWith(".tei")) {
                                String outPath = HelloApplication.dicPath + FilenameUtils.getName(entry.getName());
                                final OutputStream outputFileStream = new FileOutputStream(outPath);
                                IOUtils.copy(debInputStream, outputFileStream);
                                outputFileStream.close();
                            }
                        }
                        debInputStream.close();

                        // Lets delete archives
                        Files.deleteIfExists(Path.of(xzFilePath));
                        Files.deleteIfExists(Path.of(tarFilePath));

                        downloadProgress.setProgress((double) i / listOfFiles.length);

                    }
                }

                return null;
            }
        };


        task.setOnFailed(wse -> {
            wse.getSource().getException().printStackTrace();
            downloadButton.setDisable(true);
            fetchButton.setDisable(false);
            try {
                Files.deleteIfExists(Paths.get(HelloApplication.dicPath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            progressBarText.setText("Extraction error! Please Try again");
        });

        // If the task completed successfully, perform other updates here
        task.setOnSucceeded(wse -> {
            System.out.println("Done!");
        });
        new Thread(task).start();

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