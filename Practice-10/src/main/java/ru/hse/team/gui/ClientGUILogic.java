package ru.hse.team.gui;

import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import ru.hse.team.SimpleFtpClient;

import java.io.IOException;

public class ClientGUILogic {

    private SimpleFtpClient client;
    private final Scene scene;

    public ClientGUILogic(String hostIp, String port){
        client = new SimpleFtpClient();
        try {
            client.connect();
        } catch (IOException e) {
            //"AAA"
            e.printStackTrace();
        }

        FileTreeItem rootItem = new FileTreeItem(new FileItem(".", true), client);
        rootItem.setExpanded(true);

        var tree = new TreeView<>(rootItem);
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        scene = new Scene(root, ClientGUI.PREF_WIDTH, ClientGUI.PREF_HEIGHT);
    }


    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
    }

}
