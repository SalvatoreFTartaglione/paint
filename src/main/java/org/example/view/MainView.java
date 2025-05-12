package org.example.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.example.controller.MainController;

public class MainView {
    private final MainController controller;

    public MainView(MainController controller) {
        this.controller = controller;
    }

    public void start(Stage stage) {
        Label label = new Label(controller.getMessage());
        StackPane root = new StackPane(label);
        Scene scene = new Scene(root, 400, 200);
        stage.setTitle("JavaFX MVC Example");
        stage.setScene(scene);
        stage.show();
    }
}
