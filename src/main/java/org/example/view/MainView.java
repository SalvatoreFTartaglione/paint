package org.example.view;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainViewController;
import org.example.model.commands.UndoManager;
import org.example.model.shape.ShapeInterface;

import java.io.IOException;

public class MainView {
    private final UndoManager undoManager;
    private MainViewController mainViewController;

    public MainView(MainViewController controller, UndoManager undoManager) {
        this.mainViewController = controller;
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
}
