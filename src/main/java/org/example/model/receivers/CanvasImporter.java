package org.example.model.receivers;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.factory.concreteCreator.EllipseCreator;
import org.example.model.factory.concreteCreator.LineCreator;
import org.example.model.factory.concreteCreator.PolygonCreator;
import org.example.model.factory.concreteCreator.RectangleCreator;
import org.example.model.factory.concreteCreator.TextCreator;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.utils.SerializationUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * Receiver in charge of loading a drawing from a text file and putting its shapes on the canvas.
 * <p>
 * Loading is all-or-nothing: the whole file is parsed first and the canvas is touched only
 * if every line is valid, so a corrupted file can never leave a half-loaded drawing behind.
 */
public class CanvasImporter {

    /** First line of every canvas file. */
    public static final String HEADER = "My custom canvas";

    private final DrawingContextDTO contextDTO;

    /**
     * Creates an importer bound to the canvas held by the given context.
     *
     * @param contextDTO the drawing context that provides the canvas pane
     */
    public CanvasImporter(DrawingContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }

    /**
     * Replaces the content of the canvas with the shapes stored in the given file.
     *
     * @param file the file to read
     * @throws IOException if the file cannot be read or its content is not a valid canvas file;
     *                     in that case the canvas is left untouched
     */
    public void importAsText(File file) throws IOException {
        List<ShapeInterface> shapes = parse(file);
        contextDTO.getDrawingPane().getChildren().clear();
        for (ShapeInterface shape : shapes) {
            Node node = shape.getNode();
            node.setUserData(shape);
            contextDTO.getDrawingPane().getChildren().add(node);
        }
    }

    /**
     * Parses a canvas file without touching the canvas.
     *
     * @param file the file to read
     * @return the shapes in z-order (back to front)
     * @throws IOException if the file cannot be read or contains an invalid line
     */
    public List<ShapeInterface> parse(File file) throws IOException {
        List<ShapeInterface> shapes = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
            String header = reader.readLine();
            if (!HEADER.equals(header)) {
                throw new IOException("Not correct file format. The file must start with \"" + HEADER + "\".");
            }
            String line;
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    shapes.add(parseLine(line));
                } catch (IOException e) {
                    throw new IOException("Line " + lineNumber + ": " + e.getMessage(), e);
                } catch (RuntimeException e) {
                    throw new IOException("Line " + lineNumber + ": malformed shape definition ("
                            + e.getMessage() + ")", e);
                }
            }
        }
        return shapes;
    }

    private ShapeInterface parseLine(String line) throws IOException {
        List<String> parts = SerializationUtils.splitUnescaped(line, ';');
        String type = parts.get(0);
        switch (type) {
            case "Line":
                requireFields(type, parts, 8);
                return parseLineShape(parts);
            case "Rectangle":
                requireFields(type, parts, 9);
                return parseRectangle(parts);
            case "Ellipse":
                requireFields(type, parts, 9);
                return parseEllipse(parts);
            case "Text":
                requireFields(type, parts, 10);
                return parseText(parts);
            case "Polygon":
                requireFields(type, parts, 8);
                return parsePolygon(parts);
            default:
                throw new IOException("unknown shape type \"" + type + "\"");
        }
    }

    private static void requireFields(String type, List<String> parts, int expected) throws IOException {
        if (parts.size() != expected) {
            throw new IOException(type + " expects " + expected + " fields but found " + parts.size());
        }
    }

    private ShapeInterface parseLineShape(List<String> p) {
        Color stroke = Color.web(p.get(1));
        double endX = Double.parseDouble(p.get(2));
        double endY = Double.parseDouble(p.get(3));
        double startX = Double.parseDouble(p.get(4));
        double startY = Double.parseDouble(p.get(5));
        double strokeWidth = Double.parseDouble(p.get(6));
        double angle = Double.parseDouble(p.get(7));

        LineCreator creator = new LineCreator();
        creator.creationOfShape(stroke, null, (int) strokeWidth, 0, 0, null, 0.0, 0.0, angle);
        ShapeInterface shape = creator.getShape();
        shape.setStrokeColor(stroke);
        shape.draw(new Double[]{startX, startY, endX, endY});
        return shape;
    }

    private ShapeInterface parseRectangle(List<String> p) {
        Color stroke = Color.web(p.get(1));
        Color fill = Color.web(p.get(2));
        double strokeWidth = Double.parseDouble(p.get(3));
        double width = Double.parseDouble(p.get(4));
        double height = Double.parseDouble(p.get(5));
        double x = Double.parseDouble(p.get(6));
        double y = Double.parseDouble(p.get(7));
        double angle = Double.parseDouble(p.get(8));

        RectangleCreator creator = new RectangleCreator();
        creator.creationOfShape(stroke, fill, (int) strokeWidth, width, height, null, 0.0, 0.0, angle);
        ShapeInterface shape = creator.getShape();
        shape.draw(x, y);
        return shape;
    }

    private ShapeInterface parseEllipse(List<String> p) {
        Color stroke = Color.web(p.get(1));
        Color fill = Color.web(p.get(2));
        double strokeWidth = Double.parseDouble(p.get(3));
        double radiusX = Double.parseDouble(p.get(4));
        double radiusY = Double.parseDouble(p.get(5));
        double centerX = Double.parseDouble(p.get(6));
        double centerY = Double.parseDouble(p.get(7));
        double angle = Double.parseDouble(p.get(8));

        EllipseCreator creator = new EllipseCreator();
        creator.creationOfShape(stroke, fill, (int) strokeWidth, radiusX, radiusY, null, 0.0, 0.0, angle);
        ShapeInterface shape = creator.getShape();
        shape.draw(centerX, centerY);
        return shape;
    }

    private ShapeInterface parseText(List<String> p) {
        String text = SerializationUtils.unescape(p.get(1));
        Color stroke = Color.web(p.get(2));
        Color fill = Color.web(p.get(3));
        double x = Double.parseDouble(p.get(4));
        double y = Double.parseDouble(p.get(5));
        double fontSize = Double.parseDouble(p.get(6));        // stored in the "stroke width" slot
        double scaleX = Double.parseDouble(p.get(7));
        double scaleY = Double.parseDouble(p.get(8));
        double angle = Double.parseDouble(p.get(9));

        TextCreator creator = new TextCreator();
        creator.creationOfShape(stroke, fill, (int) fontSize, x, y, text, scaleX, scaleY, angle);
        ShapeInterface shape = creator.getShape();
        shape.draw(x, y);
        return shape;
    }

    private ShapeInterface parsePolygon(List<String> p) throws IOException {
        Color stroke = Color.web(p.get(1));
        Color fill = Color.web(p.get(2));
        double strokeWidth = Double.parseDouble(p.get(3));
        double scaleX = Double.parseDouble(p.get(4));
        double scaleY = Double.parseDouble(p.get(5));
        String pointsField = p.get(6).trim();
        double angle = Double.parseDouble(p.get(7));

        if (!pointsField.startsWith("[") || !pointsField.endsWith("]")) {
            throw new IOException("Polygon points must be enclosed in square brackets");
        }
        String[] values = pointsField.substring(1, pointsField.length() - 1).split(",");
        if (values.length < 6 || values.length % 2 != 0) {
            throw new IOException("Polygon needs an even number of coordinates (at least 3 points)");
        }
        Double[] points = new Double[values.length];
        for (int i = 0; i < values.length; i++) {
            points[i] = Double.parseDouble(values[i].trim());
        }

        PolygonCreator creator = new PolygonCreator();
        creator.creationOfShape(stroke, fill, (int) strokeWidth, 0, 0, null, scaleX, scaleY, angle);
        ShapeInterface shape = creator.getShape();
        shape.draw(points);
        return shape;
    }
}
