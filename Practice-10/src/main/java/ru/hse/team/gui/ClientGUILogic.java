package ru.hse.team.gui;

import javafx.scene.Scene;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

import ru.hse.team.SimpleFtpClient;

public class ClientGUILogic {

    private SimpleFtpClient client;
    private final Scene scene;
    private final Stage primaryStage;

    public ClientGUILogic(String hostIp, String port, Stage primaryStage) {
        this.primaryStage = primaryStage;
        client = new SimpleFtpClient();

        FileTreeItem rootItem = new FileTreeItem(new FileItem(".", true), client);
        rootItem.setExpanded(true);

        var tree = new TreeView<>(rootItem);
        tree.getSelectionModel().selectedItemProperty().addListener((observable, old_val, new_val) -> {
            if (observable.getValue().getValue().isDirectory()) {
                return;
            }

            FileChooser chooser = new FileChooser();
            File fileToSave = chooser.showSaveDialog(primaryStage);
            if (fileToSave != null) {
                try (var output = new FileOutputStream(fileToSave)) {
                    output.write(getFile(observable.getValue().getValue()).getBytes(StandardCharsets.UTF_8));
                } catch (IOException e) {
                    //AAAA
                    e.printStackTrace();
                }
            }

        });
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        scene = new Scene(root, ClientGUI.PREF_WIDTH, ClientGUI.PREF_HEIGHT);
    }

    private String getFile(FileItem file) {
        String message = "";
        try {
            message = client.executeGet(file.getName());
        } catch (IOException e) {
            //AAAA
            e.printStackTrace();
        }
        int pos = message.indexOf(' ');
        return message.substring(pos + 1);
    }

    public void show() {
        primaryStage.setScene(scene);
    }

}
