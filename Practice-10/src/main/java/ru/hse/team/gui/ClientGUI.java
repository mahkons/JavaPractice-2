package ru.hse.team.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ClientGUI extends Application {

    private static int MIN_WIDTH = 200;
    private static int MIN_HEIGHT = 200;

    public static int PREF_WIDTH = 600;
    public static int PREF_HEIGHT = 400;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("FTP server");
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);

        primaryStage.setScene(new Scene(createContent(primaryStage)));
        primaryStage.show();
    }

    private Pane createContent(Stage primaryStage) {
        var pane = new GridPane();
        pane.setPrefSize(PREF_WIDTH, PREF_HEIGHT);

        var host = new TextField("host");
        pane.add(host, 0, 0);
        var port = new TextField("port");
        pane.add(port, 0, 1);



        var connectButton = new Button("connect");
        connectButton.setOnAction(event -> {
            String hostIP = host.getText();
            String portValue = port.getText();

            if (isIPV4(hostIP) && checkPort(portValue)) {
                var fileTree = new ClientGUILogic(hostIP, portValue);
                fileTree.show(primaryStage);
            }
        });
        pane.add(connectButton, 0, 2);

        return pane;
    }



    private static boolean isIPV4(String ipAddress) {
        if (ipAddress == null) {
            return false;
        }
        String ipPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
        Pattern pattern = Pattern.compile(ipPattern);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    private static boolean checkPort(String portValue) {
        if (portValue == null) {
            return false;
        }
        int port = Integer.parseInt(portValue);
        if (port < 0 || port >= 65536) {
            return false;
        }
        return true;
    }



    public static void main(String[] args) {
        launch(args);
    }
}
