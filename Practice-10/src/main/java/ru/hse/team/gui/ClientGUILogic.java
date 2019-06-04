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

        TreeItem<String> rootItem = new TreeItem<>("/");
        rootItem.setExpanded(true);

        try {
            client.executeList("./src/main/java/ru/hse/team");
        } catch (IOException e) {
            e.printStackTrace();
        }

        TreeView<String> tree = new TreeView<> (rootItem);
        StackPane root = new StackPane();
        root.getChildren().add(tree);
        scene = new Scene(root, ClientGUI.PREF_WIDTH, ClientGUI.PREF_HEIGHT);
    }


    public void show(Stage primaryStage) {
        primaryStage.setScene(scene);
    }

}
