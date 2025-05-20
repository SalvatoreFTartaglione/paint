package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.model.commands.UndoManager;

import java.io.IOException;

public class MainView {
    private final MainController controller;
    private final UndoManager undoManager;
    private MainViewController mainViewController;

    public MainView(MainController controller, UndoManager undoManager) {
        this.controller = controller;
        this.undoManager = undoManager;
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/MainView.fxml"));
            Parent root = loader.load();

            //assegnazione controller
            mainViewController = loader.getController();
            mainViewController.setUndoManager(undoManager);
            //mainViewController.setController(controller);

            Scene scene = new Scene(root);
            stage.setTitle("Paint");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MainViewController getMainViewController() {
        return mainViewController;
    }
}
