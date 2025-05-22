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


    public Load(Pane pane) {
        this.pane = pane;
    }

    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file == null) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 0) continue;

                String shapeType = parts[0];
                ShapeInterface shape = null;

                switch (shapeType) {
                    case "Line":
                        if (parts.length != 7) continue; // Line + 6 params

                        Color strokeColorL = Color.web(parts[1]);
                        double endX = Double.parseDouble(parts[2]);
                        double endY = Double.parseDouble(parts[3]);
                        double startX = Double.parseDouble(parts[4]);
                        double startY = Double.parseDouble(parts[5]);
                        double strokeWidthL = Double.parseDouble(parts[6]);

                        LineCreator lineCreator = new LineCreator();
                        lineCreator.creationOfShape(strokeColorL, (Color) null, (int) strokeWidthL, 0, 0); // width/height not used
                        shape = lineCreator.getShape();
                        shape.setStrokeColor(strokeColorL);
                        shape.draw(new Double[]{startX, startY, endX, endY});
                        break;

                    case "Rectangle":
                        if (parts.length != 8) continue; // Rectangle + 7 params

                        Color strokeColorR = Color.web(parts[1]);
                        Color fillColorR = Color.web(parts[2]);
                        double strokeWidthR = Double.parseDouble(parts[3]);
                        double widthR = Double.parseDouble(parts[4]);
                        double heightR = Double.parseDouble(parts[5]);
                        double xR = Double.parseDouble(parts[6]);
                        double yR = Double.parseDouble(parts[7]);

                        RectangleCreator rectangleCreator = new RectangleCreator();
                        rectangleCreator.creationOfShape(strokeColorR, fillColorR, (int) strokeWidthR, widthR, heightR);
                        shape = rectangleCreator.getShape();
                        shape.draw(xR, yR);
                        break;

                    case "Ellipse":
                        if (parts.length != 8) continue; // Ellipse + 7 params

                        Color strokeColorE = Color.web(parts[1]);
                        Color fillColorE = Color.web(parts[2]);
                        double strokeWidthE = Double.parseDouble(parts[3]);
                        double radiusX = Double.parseDouble(parts[4]);
                        double radiusY = Double.parseDouble(parts[5]);
                        double centerX = Double.parseDouble(parts[6]);
                        double centerY = Double.parseDouble(parts[7]);

                        EllipseCreator ellipseCreator = new EllipseCreator();
                        ellipseCreator.creationOfShape(strokeColorE, fillColorE, (int) strokeWidthE, radiusX, radiusY);
                        shape = ellipseCreator.getShape();
                        shape.draw(centerX, centerY);
                        break;

                    default:
                        System.err.println(": " + shapeType);
                        continue;
                }
                if (shape != null) {
                    Node n = shape.getNode();
                    n.setUserData(shape);
                    pane.getChildren().add(n);
                }else {
                    System.err.println("Shape is null: " + shape.getNode());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}


