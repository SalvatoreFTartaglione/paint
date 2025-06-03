package org.example.model.commands.receivers;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.*;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.product.ShapeInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;
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
            String currentKey = null;
            StringBuilder shapeBuffer = null;

            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;

                if (line.startsWith(" ") || line.trim().startsWith("|")) {
                    if (shapeBuffer != null) {
                        shapeBuffer.append(" ").append(line.trim());
                    }
                } else {
                    if (shapeBuffer != null && currentKey != null) {
                        ShapeInterface shape = parseShape(shapeBuffer.toString().trim());
                        if (shape != null) {
                            importedMap.put(currentKey, shape);
                        }
                    }

                    String[] parts = line.split(":", 2);
                    if (parts.length != 2) continue;

                    currentKey = parts[0].trim();
                    shapeBuffer = new StringBuilder(parts[1].trim());
                }
            }

            // Gestione ultimo elemento
            if (shapeBuffer != null && currentKey != null) {
                ShapeInterface shape = parseShape(shapeBuffer.toString().trim());
                if (shape != null) {
                    importedMap.put(currentKey, shape);
                }
            }
        }

        favoriteManagerReceiver.setMapFavorites(importedMap);
    }

    private ShapeInterface parseShape(String value) {

        String[] tokens = value.split(";");

        String type = tokens[0];

        switch (type) {
            case "Rectangle":
                return parseRectangle(tokens);
            case "Ellipse":
                return parseEllipse(tokens);
            case "Line":
                return parseLine(tokens);
            case "Text":
                return parseText(tokens);
            case "Polygon":
                return parsePolygon(tokens);
            default:
                return null;
        }
    }

    private ShapeInterface parseRectangle(String[] tokens) {
        Color stroke = Color.web(tokens[1]);
        Color fill = Color.web(tokens[2]);
        int strokeWidth = (int) Double.parseDouble(tokens[3]);
        double width = Double.parseDouble(tokens[4]);
        double height = Double.parseDouble(tokens[5]);
        double x = Double.parseDouble(tokens[6]);
        double y = Double.parseDouble(tokens[7]);
        double angle = Double.parseDouble(tokens[8]);

        AbstractShape rect = new RectangleCreator();
        rect.creationOfShape(stroke, fill, strokeWidth, width, height, null, 0.0, 0.0, angle);
        rect.getShape().draw(x, y);
        return rect.getShape();
    }

    private ShapeInterface parseEllipse(String[] tokens) {
        Color stroke = Color.web(tokens[1]);
        Color fill = Color.web(tokens[2]);
        int strokeWidth = (int) Double.parseDouble(tokens[3]);
        double radiusX = Double.parseDouble(tokens[4]);
        double radiusY = Double.parseDouble(tokens[5]);
        double centerX = Double.parseDouble(tokens[6]);
        double centerY = Double.parseDouble(tokens[7]);
        double angle = Double.parseDouble(tokens[8]);

        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(stroke, fill, strokeWidth, radiusX, radiusY, null, 0.0, 0.0, angle);
        ellipse.getShape().draw(centerX, centerY);
        return ellipse.getShape();
    }

    private ShapeInterface parseLine(String[] tokens) {
        Color stroke = Color.web(tokens[1]);
        double endX = Double.parseDouble(tokens[2]);
        double endY = Double.parseDouble(tokens[3]);
        double startX = Double.parseDouble(tokens[4]);
        double startY = Double.parseDouble(tokens[5]);
        int strokeWidth = (int) Double.parseDouble(tokens[6]);
        double angle = Double.parseDouble(tokens[7]);

        AbstractShape line = new LineCreator();
        line.creationOfShape(stroke, null, strokeWidth, 0, 0, null, 0.0, 0.0, angle);
        ShapeInterface shape = line.getShape();
        shape.setPoints(new Double[]{startX, startY, endX, endY});
        return shape;
    }

    private ShapeInterface parseText(String[] tokens) {
        String content = tokens[1];
        Color stroke = Color.web(tokens[2]);
        Color fill = Color.web(tokens[3]);
        double x = Double.parseDouble(tokens[4]);
        double y = Double.parseDouble(tokens[5]);
        int strokeWidth = (int) Double.parseDouble(tokens[6]);
        double scaleX = Double.parseDouble(tokens[7]);
        double scaleY = Double.parseDouble(tokens[8]);
        double angle = Double.parseDouble(tokens[9]);

        AbstractShape text = new TextCreator();
        text.creationOfShape(stroke, fill, strokeWidth, x, y, content, scaleX, scaleY, angle);
        return text.getShape();
    }

    private ShapeInterface parsePolygon(String[] tokens) {
        Color stroke = Color.web(tokens[1]);
        Color fill = Color.web(tokens[2]);
        int strokeWidth = (int) Double.parseDouble(tokens[3]);
        double scaleX = Double.parseDouble(tokens[4]);
        double scaleY = Double.parseDouble(tokens[5]);

        String pointsStr = tokens[6].trim();
        if (!pointsStr.startsWith("[") || !pointsStr.endsWith("]")) return null;

        pointsStr = pointsStr.substring(1, pointsStr.length() - 1);
        String[] pointTokens = pointsStr.split(",");
        Double[] points = new Double[pointTokens.length];
        for (int i = 0; i < pointTokens.length; i++) {
            points[i] = Double.parseDouble(pointTokens[i].trim());
        }

        double angle = Double.parseDouble(tokens[7]);

        AbstractShape polygon = new PolygonCreator();
        polygon.creationOfShape(stroke, fill, strokeWidth, 0, 0, null, scaleX, scaleY, angle);
        polygon.getShape().setPoints(points);
        return polygon.getShape();
    }
}
