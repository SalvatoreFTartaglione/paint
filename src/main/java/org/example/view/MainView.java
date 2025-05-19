package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;

import java.io.IOException;

public class MainView {
    private final MainController controller;

    public MainView(MainController controller) {
        this.controller = controller;
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/MainView.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            stage.setTitle("Paint");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
