package ru.hse.team;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TicTacToeApplication extends Application {

    private static final int GRID_SIZE = 3;
    private static final int CELL_LENGTH = 200;

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Hello world!");
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch();
    }

    private Parent createContent() {
        var root = new BorderPane();

        var center = new GridPane();
        center.setPrefSize(GRID_SIZE);

        return root;
    }


}
