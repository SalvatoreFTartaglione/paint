package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.model.MessageModel;
import org.example.view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        MessageModel model = new MessageModel();
        MainController controller = new MainController(model);
        MainView view = new MainView(controller);
        view.start(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
