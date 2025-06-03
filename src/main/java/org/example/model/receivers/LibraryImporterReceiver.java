package org.example.model.receivers;

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

/**
 * The LibraryImporterReceiver class is responsible for importing a library of shapes
 * from a file. It parses the file, converts the data into shape objects, and stores
 * them as favorites using the FavoriteManagerReceiver.
 *
 * This class supports importing various shapes such as rectangles, ellipses,
 * lines, text, and polygons. The file should adhere to a specific format,
 * with the first line being a header: "My Custom Library."
 *
 * If the file is invalid or its format is not recognized, an IOException is thrown.
 */
public class LibraryImporterReceiver {

    private FavoriteManagerReceiver favoriteManagerReceiver;

    /**
     * Constructs a LibraryImporterReceiver instance with a specified FavoriteManagerReceiver.
     *
     * @param favoriteManagerReceiver the FavoriteManagerReceiver used to manage favorite shapes
     */
    public LibraryImporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

    /**
     * Imports and processes a custom library file to populate a map of shapes.
     * This method reads the content of the provided file, validates its format, parses shapes,
     * and updates the map maintained by the favoriteManagerReceiver.
     *
     * @param file the file to be imported and processed; it should be in the expected "My Custom Library" format.
     * @throws IOException if the file format is invalid, cannot be read, or any input/output error occurs.
     */
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

    /**
     * Parses the provided string value into a specific shape based on its type.
     * The type of the shape is determined by the first token in the input string.
     * Depending on the identified type, it delegates processing to the appropriate
     * parsing method.
     *
     * @param value the string representation of the shape, where the first token
     *              specifies the shape type and subsequent tokens provide its attributes.
     * @return a ShapeInterface instance corresponding to the parsed type and attributes,
     *         or null if the type is unknown or unsupported.
     */
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

    /**
     * Parses a rectangle's attributes from the provided array of tokens and creates a rectangle shape.
     * The tokens array must follow the expected structure, where each element corresponds to specific
     * attributes such as stroke color, fill color, stroke width, dimensions, position, and angle.
     *
     * @param tokens an array of strings representing the attributes of the rectangle.
     *               The expected order is:
     *               [type, stroke color, fill color, stroke width, width, height, x-position, y-position, angle].
     * @return a ShapeInterface instance representing the parsed rectangle with the specified attributes.
     */
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

    /**
     * Parses an ellipse's attributes from the provided array of tokens and creates an ellipse shape.
     * The tokens array must follow the expected structure, where each element corresponds to specific
     * attributes such as stroke color, fill color, stroke width, radii, position, and angle.
     *
     * @param tokens an array of strings representing the attributes of the ellipse.
     *               The expected order is:
     *               [type, stroke color, fill color, stroke width, radius X, radius Y,
     *               x-position, y-position, angle].
     * @return a ShapeInterface instance representing the parsed ellipse with the specified attributes.
     */
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

    /**
     * Parses a line's attributes from the provided array of tokens and creates a line shape.
     * The tokens array should follow the expected structure, where each element corresponds to
     * specific attributes such as stroke color, start and end coordinates, stroke width, and angle.
     *
     * @param tokens an array of strings representing the attributes of the line.
     *               The expected order is:
     *               [type, stroke color, end X, end Y, start X, start Y, stroke width, angle].
     * @return a ShapeInterface instance representing the parsed line with the specified attributes.
     */
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

    /**
     * Parses a text shape's attributes from the provided array of tokens and creates a text shape.
     * The tokens array must follow the expected structure, where each element corresponds to specific
     * attributes such as text content, stroke color, fill color, position, stroke width, scale factors, and angle.
     *
     * @param tokens an array of strings representing the attributes of the text shape.
     *               The expected order is:
     *               [type, content, stroke color, fill color, x-position, y-position,
     *               stroke width, scale X, scale Y, angle].
     * @return a ShapeInterface instance representing the parsed text shape with*/
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

    /**
     * Parses a polygon's attributes from the provided array of tokens and creates a polygon shape.
     * The tokens array must follow the expected structure, where each element corresponds to specific
     * attributes such as stroke color, fill color, stroke width, scaling factors, points, and angle.
     *
     * @param tokens an array of strings representing the attributes of the polygon.
     *               The expected order is:
     *               [type, stroke color, fill color, stroke width, scale X, scale Y, points, angle].
     *               - `points` should be a comma-separated list enclosed in square brackets,
     *                 representing the coordinates of the polygon's vertices.
     * @return a ShapeInterface instance representing the parsed polygon with the specified attributes,
     *         or null if the input is invalid or improperly formatted.
     */
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
