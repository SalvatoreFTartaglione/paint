package org.example;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.controller.MainController;
import org.example.model.MessageModel;
import org.example.model.commands.UndoManager;
import org.example.model.shape.AbstractShape;
import org.example.model.shape.EllipseCreator;
import org.example.model.shape.LineCreator;
import org.example.model.shape.RectangleCreator;
import org.example.view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        System.out.println("Hello World");
        MessageModel model = new MessageModel();
        MainController controller = new MainController(model);
        UndoManager undoManager = new UndoManager(); // crea il gestore Undo
        MainView view = new MainView(controller, undoManager);
        view.start(stage);
/*

        AbstractShape rectangle = new RectangleCreator();
        AbstractShape ellipse = new EllipseCreator();
        AbstractShape line = new LineCreator();

        rectangle.creationOfShape(Color.BLUE, Color.RED, 5, 7.0, 10.0);
        ellipse.creationOfShape(Color.RED, Color.BLUE, 5, 7.0, 10.0);
        line.creationOfShape(Color.RED, Color.BLUE, 5, 7.0, 10.0);

        System.out.println(rectangle.getShape().toString());
        System.out.println(ellipse.getShape().toString());
        System.out.println(line.getShape().toString());
*/


    }

    public static void main(String[] args) {
        launch(args);
    }
}
