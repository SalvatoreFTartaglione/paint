package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.commands.UndoManager;
import org.example.view.MainView;
import org.example.controller.MainViewController;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        MainViewController controller = new MainViewController();
        UndoManager undoManager = new UndoManager(); // crea il gestore Undo
        MainView view = new MainView(controller, undoManager);
        view.start(stage);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
