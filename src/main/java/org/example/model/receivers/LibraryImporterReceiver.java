package org.example.model.receivers;

import javafx.scene.paint.Color;
import org.example.model.shapeCreators.*;
import org.example.model.shape.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LibraryImporterReceiver {

    private FavoriteManagerReceiver favoriteManagerReceiver;

    public LibraryImporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

    public void importLibrary(File file) throws IOException {
        Map<String, ShapeInterface> importedMap = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null || !header.equals("My Custom Library")) {
                throw new IOException("Not valid file or Unknown format.");
            }

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                String[] parts = line.split(":", 2);
                if (parts.length != 2) continue;

                String key = parts[0].trim();
                String value = parts[1].trim();

                ShapeInterface shape = parseShape(value);
                if (shape != null) {
                    importedMap.put(key, shape);
                }
            }
        }

        favoriteManagerReceiver.setMapFavorites(importedMap);
    }

    private ShapeInterface parseShape(String value) {
        String[] tokens = value.split(";");

        String type = tokens[0];
        double scaleX = 0.0;
        double scaleY = 0.0;

        switch (type) {
            case "Rectangle":
                Color rectStroke = Color.web(tokens[1]);
                Color rectFill = Color.web(tokens[2]);
                double strokeWidth = Double.parseDouble(tokens[3]);
                double width = Double.parseDouble(tokens[4]);
                double height = Double.parseDouble(tokens[5]);
                double x = Double.parseDouble(tokens[6]);
                double y = Double.parseDouble(tokens[7]);
                double angleR = Double.parseDouble(tokens[8]);
                AbstractShape rectangle = new RectangleCreator();
                rectangle.creationOfShape(rectStroke, rectFill, (int) strokeWidth, width, height, null, 0.0, 0.0, angleR);
                return rectangle.getShape();

            case "Ellipse":
                Color ellStroke = Color.web(tokens[1]);
                Color ellFill = Color.web(tokens[2]);
                double ellStrokeWidth = Double.parseDouble(tokens[3]);
                double radiusX = Double.parseDouble(tokens[4]);
                double radiusY = Double.parseDouble(tokens[5]);
                double centerX = Double.parseDouble(tokens[6]);
                double centerY = Double.parseDouble(tokens[7]);
                double angleE = Double.parseDouble(tokens[8]);
                AbstractShape ellipse = new EllipseCreator();
                ellipse.creationOfShape(ellStroke, ellFill, (int) ellStrokeWidth, radiusX, radiusY, null, 0.0, 0.0, angleE);
                return ellipse.getShape();

            case "Line":
                Color lineStroke = Color.web(tokens[1]);
                double endX = Double.parseDouble(tokens[2]);
                double endY = Double.parseDouble(tokens[3]);
                double startX = Double.parseDouble(tokens[4]);
                double startY = Double.parseDouble(tokens[5]);
                double lineStrokeWidth = Double.parseDouble(tokens[6]);
                double angleL = Double.parseDouble(tokens[7]);
                AbstractShape line = new LineCreator();
                line.creationOfShape(lineStroke, null, (int) lineStrokeWidth, 0, 0, null, 0.0, 0.0, angleL);
                line.getShape().setPoints(new Double[]{startX, startY, endX, endY});
                return line.getShape();

            case "Text":
                String content = tokens[1];
                Color textStroke = Color.web(tokens[2]);
                Color textFill = Color.web(tokens[3]);
                double textX = Double.parseDouble(tokens[4]);
                double textY = Double.parseDouble(tokens[5]);
                double textStrokeWidth = Double.parseDouble(tokens[6]);
                scaleX = Double.parseDouble(tokens[7]);
                scaleY = Double.parseDouble(tokens[8]);
                double angleT = Double.parseDouble(tokens[9]);
                AbstractShape text = new TextCreator();
                text.creationOfShape(textStroke, textFill, (int) textStrokeWidth, textX, textY, content, scaleX, scaleY, angleT);
                return text.getShape();

            case "Polygon":
                Color polyStroke = Color.web(tokens[1]);
                Color polyFill = Color.web(tokens[2]);
                double polyStrokeWidth = Double.parseDouble(tokens[3]);
                scaleX = Double.parseDouble(tokens[4]);
                scaleY = Double.parseDouble(tokens[5]);

                String pointsStr = tokens[6].trim();
                if (!pointsStr.startsWith("[") || !pointsStr.endsWith("]")) return null;
                pointsStr = pointsStr.substring(1, pointsStr.length() - 1);
                String[] pointTokens = pointsStr.split(",");
                Double[] pointsP = new Double[pointTokens.length];
                for (int i = 0; i < pointTokens.length; i++) {
                    pointsP[i] = Double.parseDouble(pointTokens[i].trim());
                }

                double angleP = Double.parseDouble(tokens[7]);
                AbstractShape polygon = new PolygonCreator();
                polygon.creationOfShape(polyStroke, polyFill, (int) polyStrokeWidth, 0, 0, null, scaleX, scaleY, angleP);
                polygon.getShape().setPoints(pointsP);
                return polygon.getShape();


            default:
                return null;
        }
    }

}