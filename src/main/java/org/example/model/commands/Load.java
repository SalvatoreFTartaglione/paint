package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.example.model.shape.*;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class Load implements CommandInterface {

    private final Pane pane;

    /**
     * Constructs a Load command that will load shapes into the given drawing pane.
     *
     * @param pane the pane where the shapes will be loaded
     */
    public Load(Pane pane) {
        this.pane = pane;
    }

    /**
     * Executes the load operation by opening a .txt file and reconstructing shapes defined in it.
     * <p>
     * The method uses a FileChooser to select the file, parses shape data, recreates the corresponding
     * shapes, and adds them to the canvas.
     * </p>
     *
     * @param node unused in this context, required by the interface
     */
    @Override
    public void executeCommand(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        try (Scanner in = new Scanner(new BufferedReader(new FileReader(file)))) {

            Color strokeColor;
            Color fillingColor = null;
            int strokeColorWidth;
            double width, height, x, y;
            ShapeInterface shape;

            in.useLocale(Locale.US);
            in.useDelimiter(";|\\n");

            while (in.hasNext()) {
                String shapeType = in.next();
                // Distinction accounts for fillingColor absence in Line
                if (shapeType.equals("Line")) {
                    strokeColor = Color.web(in.next());
                    strokeColorWidth = Integer.parseInt(in.next());
                    width = Double.parseDouble(in.next());               // in LineShape the attribute has a different name
                    height = Double.parseDouble(in.next());              // in LineShape the attribute has a different name
                    x = Double.parseDouble(in.next());                   // in LineShape the attribute has a different name
                    y = Double.parseDouble(in.next());                   // in LineShape the attribute has a different name
                }
                else {
                    strokeColor = Color.web(in.next());
                    fillingColor = Color.web(in.next());
                    strokeColorWidth = Integer.parseInt(in.next());
                    width = Double.parseDouble(in.next());
                    height = Double.parseDouble(in.next());
                    x = Double.parseDouble(in.next());                   // in Ellipse the attribute has a different name
                    y = Double.parseDouble(in.next());                   // in Ellipse the attribute has a different name
                }

                switch (shapeType) {
                    case "Rectangle":
                        // Creating the shape with the loaded attributes
                        RectangleCreator rectangleCreator = new RectangleCreator();
                        rectangleCreator.creationOfShape(strokeColor, fillingColor, strokeColorWidth, width, height);
                        // Gets the specific shape from ShapeCreator
                        shape = rectangleCreator.getShape();
                        // Draw the shape
                        shape.draw(x, y);
                        break;

                    case "Ellipse":
                        // Creating the shape with the loaded attributes
                        EllipseCreator ellipseCreator = new EllipseCreator();
                        ellipseCreator.creationOfShape(strokeColor, fillingColor, strokeColorWidth, width, height);
                        // Gets the specific shape from ShapeCreator
                        shape = ellipseCreator.getShape();
                        // Draw the shape
                        shape.draw(x, y);
                        break;

                    case "Line":
                        // Creating the shape with the loaded attributes
                        LineCreator lineCreator = new LineCreator();
                        lineCreator.creationOfShape(strokeColor, fillingColor, strokeColorWidth, width, height);        // only stroke and filling are not null
                        // Gets the specific shape from ShapeCreator
                        shape = lineCreator.getShape();
                        // Draw the shape
                        Double[] points = new Double[] {x, y, width, height};               // StartX, StartY, EndX, EndY
                        shape.draw(points);
                        break;
                    default:
                        continue;   // ignores unknown formats
                }

                Node node1 = (Node) shape;
                pane.getChildren().add(node1);
                //DragMod dm = new DragMod(node1, anchorPane, this.getcTaker(), this.getController());
                //d.makeDraggable();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


