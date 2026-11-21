package org.example.model.receivers;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.EllipseCreator;
import org.example.model.factory.concreteCreator.LineCreator;
import org.example.model.factory.concreteCreator.PolygonCreator;
import org.example.model.factory.concreteCreator.RectangleCreator;
import org.example.model.factory.concreteCreator.TextCreator;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.utils.SerializationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Receiver that imports a shape library file (see {@link LibraryExporterReceiver}) and replaces the
 * current favorites with its content.
 * <p>
 * The import is all-or-nothing: favorites are replaced only if the whole file is valid.
 */
public class LibraryImporterReceiver {

    private final FavoriteManagerReceiver favoriteManagerReceiver;

    /**
     * Creates an importer that stores the imported shapes through the given receiver.
     *
     * @param favoriteManagerReceiver the receiver that manages the favorite shapes
     */
    public LibraryImporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

    /**
     * Reads a library file and replaces the current favorites with its content.
     *
     * @param file the library file
     * @throws IOException if the file cannot be read or is not a valid library; in that case the
     *                     current favorites are left untouched
     */
    public void importLibrary(File file) throws IOException {
        favoriteManagerReceiver.setMapFavorites(parse(file));
    }

    /**
     * Parses a library file without touching the current favorites.
     *
     * @param file the library file
     * @return the shapes in file order, indexed by name
     * @throws IOException if the file cannot be read or contains an invalid entry
     */
    public Map<String, ShapeInterface> parse(File file) throws IOException {
        Map<String, ShapeInterface> importedMap = new LinkedHashMap<>();

        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (header == null || !header.equals(LibraryExporterReceiver.HEADER)) {
                throw new IOException("Not valid file or Unknown format.");
            }

            String line;
            int lineNumber = 1;
            String currentKey = null;
            int currentLine = 0;
            StringBuilder shapeBuffer = null;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.isEmpty()) continue;

                // Continuation of the previous entry
                if (line.startsWith(" ") || line.trim().startsWith("|")) {
                    if (shapeBuffer != null) {
                        shapeBuffer.append(" ").append(line.trim());
                    }
                    continue;
                }

                // A new entry begins: flush the previous one
                flush(importedMap, currentKey, shapeBuffer, currentLine);

                int separator = SerializationUtils.indexOfUnescaped(line, ':');
                if (separator < 0) {
                    throw new IOException("Line " + lineNumber + ": expected \"name: shape\"");
                }
                currentKey = SerializationUtils.unescape(line.substring(0, separator)).trim();
                currentLine = lineNumber;
                shapeBuffer = new StringBuilder(line.substring(separator + 1).trim());
            }

            flush(importedMap, currentKey, shapeBuffer, currentLine);
        }

        return importedMap;
    }

    private void flush(Map<String, ShapeInterface> target, String key, StringBuilder buffer, int line) throws IOException {
        if (key == null || buffer == null) {
            return;
        }
        try {
            ShapeInterface shape = parseShape(buffer.toString().trim());
            target.put(key, shape);
        } catch (IOException e) {
            throw new IOException("Line " + line + ": " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new IOException("Line " + line + ": malformed shape definition (" + e.getMessage() + ")", e);
        }
    }

    private ShapeInterface parseShape(String value) throws IOException {
        List<String> tokens = SerializationUtils.splitUnescaped(value, ';');
        String type = tokens.get(0);

        switch (type) {
            case "Rectangle":
                requireFields(type, tokens, 9);
                return parseRectangle(tokens);
            case "Ellipse":
                requireFields(type, tokens, 9);
                return parseEllipse(tokens);
            case "Line":
                requireFields(type, tokens, 8);
                return parseLine(tokens);
            case "Text":
                requireFields(type, tokens, 10);
                return parseText(tokens);
            case "Polygon":
                requireFields(type, tokens, 8);
                return parsePolygon(tokens);
            default:
                throw new IOException("unknown shape type \"" + type + "\"");
        }
    }

    private static void requireFields(String type, List<String> tokens, int expected) throws IOException {
        if (tokens.size() != expected) {
            throw new IOException(type + " expects " + expected + " fields but found " + tokens.size());
        }
    }

    private ShapeInterface parseRectangle(List<String> t) {
        Color stroke = Color.web(t.get(1));
        Color fill = Color.web(t.get(2));
        int strokeWidth = (int) Double.parseDouble(t.get(3));
        double width = Double.parseDouble(t.get(4));
        double height = Double.parseDouble(t.get(5));
        double x = Double.parseDouble(t.get(6));
        double y = Double.parseDouble(t.get(7));
        double angle = Double.parseDouble(t.get(8));

        AbstractShape rect = new RectangleCreator();
        rect.creationOfShape(stroke, fill, strokeWidth, width, height, null, 0.0, 0.0, angle);
        rect.getShape().draw(x, y);
        return rect.getShape();
    }

    private ShapeInterface parseEllipse(List<String> t) {
        Color stroke = Color.web(t.get(1));
        Color fill = Color.web(t.get(2));
        int strokeWidth = (int) Double.parseDouble(t.get(3));
        double radiusX = Double.parseDouble(t.get(4));
        double radiusY = Double.parseDouble(t.get(5));
        double centerX = Double.parseDouble(t.get(6));
        double centerY = Double.parseDouble(t.get(7));
        double angle = Double.parseDouble(t.get(8));

        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(stroke, fill, strokeWidth, radiusX, radiusY, null, 0.0, 0.0, angle);
        ellipse.getShape().draw(centerX, centerY);
        return ellipse.getShape();
    }

    private ShapeInterface parseLine(List<String> t) {
        Color stroke = Color.web(t.get(1));
        double endX = Double.parseDouble(t.get(2));
        double endY = Double.parseDouble(t.get(3));
        double startX = Double.parseDouble(t.get(4));
        double startY = Double.parseDouble(t.get(5));
        int strokeWidth = (int) Double.parseDouble(t.get(6));
        double angle = Double.parseDouble(t.get(7));

        AbstractShape line = new LineCreator();
        line.creationOfShape(stroke, null, strokeWidth, 0, 0, null, 0.0, 0.0, angle);
        ShapeInterface shape = line.getShape();
        shape.setPoints(new Double[]{startX, startY, endX, endY});
        return shape;
    }

    private ShapeInterface parseText(List<String> t) {
        String content = SerializationUtils.unescape(t.get(1));
        Color stroke = Color.web(t.get(2));
        Color fill = Color.web(t.get(3));
        double x = Double.parseDouble(t.get(4));
        double y = Double.parseDouble(t.get(5));
        int strokeWidth = (int) Double.parseDouble(t.get(6));
        double scaleX = Double.parseDouble(t.get(7));
        double scaleY = Double.parseDouble(t.get(8));
        double angle = Double.parseDouble(t.get(9));

        AbstractShape text = new TextCreator();
        text.creationOfShape(stroke, fill, strokeWidth, x, y, content, scaleX, scaleY, angle);
        return text.getShape();
    }

    private ShapeInterface parsePolygon(List<String> t) throws IOException {
        Color stroke = Color.web(t.get(1));
        Color fill = Color.web(t.get(2));
        int strokeWidth = (int) Double.parseDouble(t.get(3));
        double scaleX = Double.parseDouble(t.get(4));
        double scaleY = Double.parseDouble(t.get(5));

        String pointsStr = t.get(6).trim();
        if (!pointsStr.startsWith("[") || !pointsStr.endsWith("]")) {
            throw new IOException("Polygon points must be enclosed in square brackets");
        }
        String[] pointTokens = pointsStr.substring(1, pointsStr.length() - 1).split(",");
        if (pointTokens.length < 6 || pointTokens.length % 2 != 0) {
            throw new IOException("Polygon needs an even number of coordinates (at least 3 points)");
        }
        Double[] points = new Double[pointTokens.length];
        for (int i = 0; i < pointTokens.length; i++) {
            points[i] = Double.parseDouble(pointTokens[i].trim());
        }
        double angle = Double.parseDouble(t.get(7));

        AbstractShape polygon = new PolygonCreator();
        polygon.creationOfShape(stroke, fill, strokeWidth, 0, 0, null, scaleX, scaleY, angle);
        polygon.getShape().setPoints(points);
        return polygon.getShape();
    }
}
