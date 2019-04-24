package ru.hse.team;

import javafx.application.Application;
import javafx.stage.Stage;

public class MyFxApp extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Hello world!");
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
