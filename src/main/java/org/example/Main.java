package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.model.invoker.Invoker;
import org.example.view.MainView;
import org.example.controller.MainViewController;

/**
 * The Main class serves as the entry point for the JavaFX application.
 * It initializes and starts the primary stage of the application by
 * integrating the controller, view, and invoker for managing commands.
 *
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application by initializing the main controller, command invoker,
     * and view, and then sets up the primary stage.
     *
     * @param stage the primary stage for this application, onto which the application
     *              scene can be set.
     */
    @Override
    public void start(Stage stage) {

        MainViewController controller = new MainViewController();
        Invoker invoker = new Invoker(); // handles undo
        MainView view = new MainView(controller, invoker);
        view.start(stage);

    }

    /**
     * The main entry point for the JavaFX application. This method is responsible
     * for launching the application and delegating control to the JavaFX runtime.
     *
     * @param args an array of command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
