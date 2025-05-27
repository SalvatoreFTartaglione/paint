package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.commands.Invoker;
import org.example.view.MainView;
import org.example.controller.MainViewController;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        MainViewController controller = new MainViewController();
        Invoker invoker = new Invoker(); // crea il gestore Undo
        MainView view = new MainView(controller, invoker);
        view.start(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
