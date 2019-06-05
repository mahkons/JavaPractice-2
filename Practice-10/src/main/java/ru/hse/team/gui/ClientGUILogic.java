package ru.hse.team.gui;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;

import ru.hse.team.SimpleFtpClient;

/**
 * User interface for moving through directories and downloading files.
 * File is downloaded on Enter key pressing
 */
public class ClientGUILogic {

    private SimpleFtpClient client;
    private final Scene scene;
    private final Stage primaryStage;

    public ClientGUILogic(String hostName, String port, Stage primaryStage) {
        this.primaryStage = primaryStage;
        client = new SimpleFtpClient(hostName, Integer.parseInt(port));

        FileTreeItem rootItem = new FileTreeItem(new FileItem(".", true), client);
        rootItem.setExpanded(true);

        var tree = new TreeView<>(rootItem);

        tree.setOnKeyPressed(event -> {
            if (event.getCode() != KeyCode.ENTER) {
                return;
            }
            var selectedItems = tree.getSelectionModel().getSelectedItems();

            for (TreeItem<FileItem> item : selectedItems) {
                if (item.getValue().isDirectory()) {
                    item.setExpanded(true);
                    continue;
                }
                FileChooser chooser = new FileChooser();
                File fileToSave = chooser.showSaveDialog(primaryStage);
                if (fileToSave != null) {
                    try (var output = new FileOutputStream(fileToSave)) {
                        output.write(getFile(item.getValue()).getBytes(StandardCharsets.UTF_8));
                    } catch (IOException e) {
                        //AAAA
                        e.printStackTrace();
                    }
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

    /**
     * Shows screen on stage provided in constructor.
     */
    public void show() {
        primaryStage.setScene(scene);
    }

}
