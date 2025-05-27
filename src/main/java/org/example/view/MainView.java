package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainViewController;
import org.example.model.commands.Invoker;

import java.io.IOException;

public class MainView {
    private final Invoker invoker;
    private MainViewController mainViewController;

    public MainView(MainViewController controller, Invoker invoker) {
        this.mainViewController = controller;
        this.invoker = invoker;
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/MainView.fxml"));
            Parent root = loader.load();

            //assegnazione controller
            mainViewController = loader.getController();
            mainViewController.setUndoManager(invoker);
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
