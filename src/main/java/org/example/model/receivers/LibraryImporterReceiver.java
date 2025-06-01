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
        Map importedMap = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String header = reader.readLine();
            if (header == null || !header.equals("My Custom Library")) {
                throw new IOException("File non valido o formato non riconosciuto.");
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
        String[] tokens = value.split(";", 2);
        if (tokens.length < 2) return null;

        String type = tokens[0];
        String[] args = tokens[1].split(";");
        double scaleX = 0.0;
        double scaleY = 0.0;

        switch (type) {
            case "Rectangle":
                Color rectStroke = Color.web(args[0]);
                Color rectFill = Color.web(args[1]);
                double strokeWidth = Double.parseDouble(args[2]);
                double width = Double.parseDouble(args[3]);
                double height = Double.parseDouble(args[4]);
                double x = Double.parseDouble(args[5]);
                double y = Double.parseDouble(args[6]);
                double angleR = Double.parseDouble(args[7]);
                AbstractShape rectangle = new RectangleCreator();
                rectangle.creationOfShape(rectStroke, rectFill, (int) strokeWidth, width, height, null, 0.0, 0.0, angleR);
                return rectangle.getShape();

            case "Ellipse":
                Color ellStroke = Color.web(args[0]);
                Color ellFill = Color.web(args[1]);
                double ellStrokeWidth = Double.parseDouble(args[2]);
                double radiusX = Double.parseDouble(args[3]);
                double radiusY = Double.parseDouble(args[4]);
                double centerX = Double.parseDouble(args[5]);
                double centerY = Double.parseDouble(args[6]);
                double angleE = Double.parseDouble(args[7]);
                AbstractShape ellipse = new EllipseCreator();
                ellipse.creationOfShape(ellStroke, ellFill, (int) ellStrokeWidth, radiusX, radiusY, null, 0.0, 0.0, angleE);
                return ellipse.getShape();

            case "Line":
                Color lineStroke = Color.web(args[0]);
                double endX = Double.parseDouble(args[1]);
                double endY = Double.parseDouble(args[2]);
                double startX = Double.parseDouble(args[3]);
                double startY = Double.parseDouble(args[4]);
                double lineStrokeWidth = Double.parseDouble(args[5]);
                double angleL = Double.parseDouble(args[6]);
                AbstractShape line = new LineCreator();
                line.creationOfShape(lineStroke, null, (int) lineStrokeWidth, 0, 0, null, 0.0, 0.0, angleL);
                //line.getShape().setPoints(new Double[]{startX, startY, endX, endY});
                return line.getShape();

            case "Text":
                String content = args[0];
                Color textStroke = Color.web(args[1]);
                Color textFill = Color.web(args[2]);
                double textX = Double.parseDouble(args[3]);
                double textY = Double.parseDouble(args[4]);
                double textStrokeWidth = Double.parseDouble(args[5]);
                scaleX = Double.parseDouble(args[6]);
                scaleY = Double.parseDouble(args[7]);
                double angleT = Double.parseDouble(args[8]);
                AbstractShape text = new TextCreator();
                text.creationOfShape(textStroke, textFill, (int) textStrokeWidth, textX, textY, content, scaleX, scaleY, angleT);
                return text.getShape();

            case "Polygon":
                Color polyStroke = Color.web(args[0]);
                Color polyFill = Color.web(args[1]);
                double polyStrokeWidth = Double.parseDouble(args[2]);
                Double.parseDouble(args[3]);
                scaleY = Double.parseDouble(args[4]);
                String pointsStr = args[5].trim();
                if (!pointsStr.startsWith("[") || !pointsStr.endsWith("]")) return null;
                pointsStr = pointsStr.substring(1, pointsStr.length() - 1);
                String[] pointTokens = pointsStr.split(",");
                Double[] pointsP = new Double[pointTokens.length];
                for (int i = 0; i < pointTokens.length; i++) {
                    pointsP[i] = Double.parseDouble(pointTokens[i].trim());
                }
                double angleP = Double.parseDouble(args[6]);
                AbstractShape polygon = new PolygonCreator();
                polygon.creationOfShape(polyStroke, polyFill, (int) polyStrokeWidth, 0, 0, null, scaleX, scaleY, angleP);
                //polygon.getShape().setPoints(pointsP);
                return polygon.getShape();

            default:
                return null;
        }
    }

}