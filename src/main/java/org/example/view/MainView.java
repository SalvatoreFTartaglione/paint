package org.example.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.controller.MainViewController;
import org.example.model.invoker.Invoker;

import javafx.scene.image.Image;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * The MainView class is responsible for initializing and displaying the main user interface of the application.
 * It loads the FXML layout, sets up the scene, and manages the interaction between the MainViewController and other components.
 */
public class MainView {
    private final Invoker invoker;
    private MainViewController mainViewController;

    /**
     * Constructs a MainView instance and initializes it with the given controller and invoker.
     *
     * @param controller the MainViewController responsible for managing the main view logic and interactions
     * @param invoker the Invoker instance used to handle undo/redo operations or command execution
     */
    public MainView(MainViewController controller, Invoker invoker) {
        this.mainViewController = controller;
        this.invoker = invoker;
    }

    /**
     * Starts the primary stage of the JavaFX application by initializing and displaying the main view.
     * This method loads the FXML layout, attaches the corresponding controller, and sets up the scene
     * with the specified primary stage.
     *
     * @param stage the primary stage for this JavaFX application. The application's main scene will be
     *              set onto this stage and the stage will be displayed.
     */
    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/view/MainView.fxml"));
            Parent root = loader.load();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/org/example/images/paintBrush.png")));
            // The controller is instantiated by the FXML loader
            mainViewController = loader.getController();
            mainViewController.setUndoManager(invoker);

            Scene scene = new Scene(root);
            stage.setTitle("Paint");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            // Without its layout the application is unusable: fail loudly instead of showing nothing
            throw new UncheckedIOException("Unable to load the main view", e);
        }
    }
}
