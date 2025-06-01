package org.example.model.receivers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.shapeCreators.*;
import org.example.model.shape.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CanvasImporter {

    private Pane canvas;

    public CanvasImporter(Pane canvas) {
        this.canvas = canvas;
    }

    public void importAsText(File file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 0) continue;

                String shapeType = parts[0];
                ShapeInterface shape = null;

                switch (shapeType) {
                    case "Line":
                        if (parts.length != 8) continue; // Line + 6 params
                        Color strokeColorL = Color.web(parts[1]);
                        double endX = Double.parseDouble(parts[2]);
                        double endY = Double.parseDouble(parts[3]);
                        double startX = Double.parseDouble(parts[4]);
                        double startY = Double.parseDouble(parts[5]);
                        double strokeWidthL = Double.parseDouble(parts[6]);
                        double angleL = Double.parseDouble(parts[7]);

                        LineCreator lineCreator = new LineCreator();
                        lineCreator.creationOfShape(strokeColorL, (Color) null, (int) strokeWidthL, 0, 0, null, 0.0, 0.0, angleL);
                        shape = lineCreator.getShape();
                        shape.setStrokeColor(strokeColorL);
                        shape.draw(new Double[]{startX, startY, endX, endY});
                        break;

                    case "Rectangle":
                        if (parts.length != 9) continue; // Rectangle + 7 params
                        Color strokeColorR = Color.web(parts[1]);
                        Color fillColorR = Color.web(parts[2]);
                        double strokeWidthR = Double.parseDouble(parts[3]);
                        double widthR = Double.parseDouble(parts[4]);
                        double heightR = Double.parseDouble(parts[5]);
                        double xR = Double.parseDouble(parts[6]);
                        double yR = Double.parseDouble(parts[7]);
                        double angleR = Double.parseDouble(parts[8]);

                        RectangleCreator rectangleCreator = new RectangleCreator();
                        rectangleCreator.creationOfShape(strokeColorR, fillColorR, (int) strokeWidthR, widthR, heightR, null, 0.0, 0.0, angleR);
                        shape = rectangleCreator.getShape();
                        shape.draw(xR, yR);
                        break;

                    case "Ellipse":
                        if (parts.length != 9) continue;
                        Color strokeColorE = Color.web(parts[1]);
                        Color fillColorE = Color.web(parts[2]);
                        double strokeWidthE = Double.parseDouble(parts[3]);
                        double radiusX = Double.parseDouble(parts[4]);
                        double radiusY = Double.parseDouble(parts[5]);
                        double centerX = Double.parseDouble(parts[6]);
                        double centerY = Double.parseDouble(parts[7]);
                        double angleE = Double.parseDouble(parts[8]);

                        EllipseCreator ellipseCreator = new EllipseCreator();
                        ellipseCreator.creationOfShape(strokeColorE, fillColorE, (int) strokeWidthE, radiusX, radiusY, null, 0.0, 0.0, angleE);
                        shape = ellipseCreator.getShape();
                        shape.draw(centerX, centerY);
                        break;

                    case "Text":
                        if (parts.length != 10) continue;
                        String textT = parts[1];
                        Color strokeColorT = Color.web(parts[2]);
                        Color fillColorT = Color.web(parts[3]);
                        double xT = Double.parseDouble(parts[4]);
                        double yT = Double.parseDouble(parts[5]);
                        double fontSize = Double.parseDouble(parts[6]);        // aka strokeWidth
                        double scaleX = Double.parseDouble(parts[7]);
                        double scaleY = Double.parseDouble(parts[8]);
                        double angleT = Double.parseDouble(parts[9]);

                        TextCreator textCreator = new TextCreator();
                        textCreator.creationOfShape(strokeColorT, fillColorT, (int) fontSize, xT, yT, textT, scaleX, scaleY, angleT);
                        shape = textCreator.getShape();
                        shape.draw(xT, yT);
                        break;

                    case "Polygon":
                        if (parts.length != 8) continue;
                        Color strokeColorP = Color.web(parts[1]);
                        Color fillColorP = Color.web(parts[2]);
                        double strokeWidthP = Double.parseDouble(parts[3]);
                        double scaleXP = Double.parseDouble(parts[4]);
                        double scaleYP = Double.parseDouble(parts[5]);
                        double angleP = Double.parseDouble(parts[7]);
                        String pointStr = parts[6].replace("[", "").replace("]", "");
                        String[] pointValues = pointStr.split(",");
                        Double[] pointsP = new Double[pointValues.length];
                        for (int i = 0; i < pointValues.length; i++) {
                            pointsP[i] = Double.parseDouble(pointValues[i].trim());
                        }

                        PolygonCreator polygonCreator = new PolygonCreator();
                        polygonCreator.creationOfShape(strokeColorP, fillColorP, (int) strokeWidthP, 0, 0, null, scaleXP, scaleYP, angleP);
                        shape = polygonCreator.getShape();
                        shape.draw(pointsP);
                        break;

                    default:
                        System.err.println(": " + shapeType);
                        continue;
                }
                if (shape != null) {
                    Node n = shape.getNode();
                    n.setUserData(shape);
                    canvas.getChildren().add(n);
                }else {
                    throw new NullPointerException("Shape is null: " + shape);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
