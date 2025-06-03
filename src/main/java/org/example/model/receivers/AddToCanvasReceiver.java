package org.example.model.receivers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.factory.concreteCreator.*;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.product.ShapeInterface;

import java.util.ArrayList;
import java.util.List;

/**
 * The AddToCanvasReceiver class is responsible for managing the addition of various shapes
 * to a drawing canvas. It provides functionality for creating and configuring different
 * shapes, translating and clamping shapes within defined boundaries, and managing preview
 * states for shapes like lines and polygons*/
public class AddToCanvasReceiver {

    private final DrawingContextDTO drawingDTO;
    private static final List<Circle> previewCirclesLine = new ArrayList<>();
    private static final List<Circle> previewCirclesPolygon = new ArrayList<>();
    private static final List<Line> previewLines = new ArrayList<>();
    private boolean canAdd = false;

    /**
     * Constructor for the AddToCanvasReceiver class. This initializes the receiver
     * with the specified drawing context.
     *
     * @param drawingDTO the DrawingContextDTO object containing the necessary context
     *                   and settings for drawing operations.
     */
    public AddToCanvasReceiver(DrawingContextDTO drawingDTO) {
        this.drawingDTO = drawingDTO;
    }


    /**
     * Adds a shape to the drawing pane based on the selected shape option.
     *
     * The method determines the type of shape to add, such as an ellipse, rectangle, line, text, polygon, or a
     * favorite shape. It handles the configuration and drawing logic for each shape, clears any previous preview
     * elements, and performs actions such as clamping positions and translating points where necessary.
     *
     * For certain shapes like lines and polygons, it also manages additional preview elements and conditions
     * for finalizing the shape creation.
     *
     * @return the created and added shape as a {@code ShapeInterface} instance, or {@code null} if no shape can be added
     */
    public ShapeInterface addShape() {

        ShapeInterface shape = null;
        ShapeOption selectedShape = drawingDTO.getShapeOption();
        canAdd = false;

        switch (selectedShape.getName()) {

            case "Ellipse": {
                //Clear previous polygon/line points
                this.clearPreview(true, true);

                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractEllipse = createEllipse(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 100, 50, 0);
                shape = abstractEllipse.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                canAdd = true;

                break;
            }

            case "Rectangle": {
                //Clear previous polygon/line points
                this.clearPreview(true, true);

                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractRectangle = createRectangle(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 120, 60, 0);
                shape = abstractRectangle.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                canAdd = true;

                break;
            }

            case "Line": {
                //Clear previous preview of polygons
                this.clearPreview(false, true);

                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                Circle c0 = new Circle(localCoords.getX(), localCoords.getY() ,8, Color.GREEN);
                if (drawingDTO.getStartPoint() == null) {
                    drawingDTO.getDrawingPane().getChildren().removeAll(previewCirclesLine);
                    previewCirclesLine.clear();
                    drawingDTO.setStartPoint(localCoords);
                    // Preview first point
                    drawingDTO.getDrawingPane().getChildren().add(c0);
                    previewCirclesLine.add(c0);
                    canAdd = false;
                    return null;
                } else {
                    AbstractShape abstractLine = createLine(drawingDTO.getBorderColor(), 9, 0);
                    shape = abstractLine.getShape();
                    Double[] points = {
                            localCoords.getX(), localCoords.getY(),
                            drawingDTO.getStartPoint().getX(), drawingDTO.getStartPoint().getY()
                    };
                    // Preview second point
                    Circle c1 = new Circle(localCoords.getX(), localCoords.getY() ,8, Color.GREEN);
                    // Preview first point
                    drawingDTO.getDrawingPane().getChildren().add(c1);
                    previewCirclesLine.add(c1);
                    shape.draw(points);
                    // Remove previews after 500ms from add
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewCirclesLine);
                        previewCirclesLine.clear();
                    }));
                    timeline.play();
                    // Reset start point for line
                    drawingDTO.setStartPoint(null);
                    canAdd = true;
                }

                break;
            }

            case "Text": {
                //Clear previous polygon/line points
                this.clearPreview(true, true);

                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractText = createText(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), drawingDTO.getFontSize(), 40, 20, drawingDTO.getText(), 0);
                shape = abstractText.getShape();
                shape.draw(localCoords.getX(), localCoords.getY());
                canAdd = true;
                break;
            }

            case "Polygon": {
                //Clear previous line points to add LineShape
                this.clearPreview(true, false);

                // show preview of the points
                // if the polygon points list is empty set the first point to red
                if (drawingDTO.getPolygonPoints().size() == 1) {
                    Circle c = new Circle(drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getX(), drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getY(), 8, Color.RED);
                    drawingDTO.getDrawingPane().getChildren().add(c);
                    previewCirclesPolygon.add(c);
                }
                else {
                    // get the two last points
                    int lastIndex = drawingDTO.getPolygonPoints().size() - 1;
                    Point2D last = drawingDTO.getPolygonPoints().get(lastIndex);
                    Point2D previous = drawingDTO.getPolygonPoints().get(lastIndex - 1);
                    //create line as a visual aid
                    Line connectingLine = new Line(previous.getX(), previous.getY(), last.getX(), last.getY());
                    connectingLine.setStroke(Color.BLUE);
                    connectingLine.setStrokeWidth(3);
                    // get last point from the list of points
                    Circle c = new Circle(drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getX(), drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getY(), 8, Color.BLUE);
                    drawingDTO.getDrawingPane().getChildren().add(c);
                    drawingDTO.getDrawingPane().getChildren().add(connectingLine);
                    previewCirclesPolygon.add(c);
                    previewLines.add(connectingLine);
                }
                canAdd = false;
                // Check if the polygon is ready to get drawn
                if (drawingDTO.canCreatePolygon()) {
                    List<Point2D> points = drawingDTO.getPolygonPoints();
                    // Convert List<Point2D> to List<Double> (extracting X and Y values)
                    List<Double> doubleList = new ArrayList<>();
                    for (Point2D point : points) {
                        doubleList.add(point.getX());
                        doubleList.add(point.getY());
                    }
                    // Convert List<Double> to Double[]
                    Double[] doublePoints = doubleList.toArray(new Double[0]);
                    // Draw the polygon on the canvas
                    // Invoke the polygon creator
                    AbstractShape abstractPolygon = createPolygon(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40, 0);
                    shape = abstractPolygon.getShape();
                    shape.draw(doublePoints);
                    // Remove previews after 500ms from add
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewCirclesPolygon);
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewLines);
                        // Clear preview list
                        previewCirclesPolygon.clear();
                        previewLines.clear();
                    }));
                    timeline.play();
                    // Clear array of polygon points
                    drawingDTO.clearPolygonPoints();
                    canAdd = true;
                    break;
                }
                //Clear previous line points
                this.clearPreview(true, false);
                break;
            }
            case "Favorite": {
                ShapeInterface favoriteShape = drawingDTO.getShape();
                //Clear previous polygon/line points
                this.clearPreview(true, true);
                if (favoriteShape != null) {
                    ShapeInterface clonedShape = favoriteShape.cloneShape();
                    Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());

                    Double[] points = clonedShape.getPoints();

                    if (points != null && points.length >= 2) {
                        Point2D shapeCenter = computeCenter(points);
                        double dx = localCoords.getX() - shapeCenter.getX();
                        double dy = localCoords.getY() - shapeCenter.getY();
                        Double[] translated = translatePoints(points, dx, dy);
                        Double[] clamped = clampPointsIntoPane(translated);
                        clonedShape.draw(clamped);
                    } else {
                        Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), clonedShape.getWidth(), clonedShape.getHeight());
                        clonedShape.draw(clampedCoords.getX(), clampedCoords.getY());
                    }

                    shape = clonedShape;
                    canAdd = true;
                }
                break;
            }

        }
        if (shape != null) {
            shape.getNode().setUserData(shape);
            drawingDTO.getDrawingPane().getChildren().add(shape.getNode());
            return shape;
        }
        else {
            return null;
        }
    }

    /**
     * Computes the geometric center (centroid) of a set of points.
     *
     * The method assumes that the input array consists of alternating x and y coordinates,
     * with each adjacent pair representing a single point in a 2D*/
    private Point2D computeCenter(Double[] points) {
        double sumX = 0;
        double sumY = 0;
        int n = points.length / 2;

        for (int i = 0; i < points.length; i += 2) {
            sumX += points[i];
            sumY += points[i + 1];
        }
        return new Point2D(sumX / n, sumY / n);
    }


    /**
     * Translates a set of 2D points by a specified offset in the x and y directions.
     *
     * The method assumes the input array contains alternating x and y coordinates,
     * with each pair representing a single point in a 2D space.
     *
     * @param points an array of {@code Double} values representing the coordinates
     *               of points to be translated. The array must have an even length,
     *               as it consists of alternating x and y values.
     * @param dx     the translation offset to apply to the x-coordinates.
     * @param dy     the translation offset to apply to the y-coordinates.
     * @return a new array of {@code Double} values containing the translated points.
     *         The array has the same length as the input and consists of alternating
     *         x and y coordinates.
     */
    private Double[] translatePoints(Double[] points, double dx, double dy) {
        Double[] result = new Double[points.length];
        for (int i = 0; i < points.length; i += 2) {
            result[i] = points[i] + dx;
            result[i + 1] = points[i + 1] + dy;
        }
        return result;
    }


    /**
     * Clamps the given x and y coordinates to ensure that a shape of the specified width
     * and height remains within the visible boundaries of a drawing pane, accommodating
     * for zoom levels and additional margins.
     *
     * @param x           the x-coordinate of the shape's position to be clamped.
     * @param y           the y-coordinate of the shape's position to be clamped.
     * @param shapeWidth  the width of the shape that needs to be clamped.
     * @param shapeHeight the height of the shape that needs to be clamped.
     * @return a {@code Point2D} object containing the clamped x and y coordinates.
     */
    private Point2D clampPosition(double x, double y, double shapeWidth, double shapeHeight) {
        double zoom = drawingDTO.getDrawingPane().getScaleX();
        double paneWidth = drawingDTO.getDrawingPane().getPrefWidth() * zoom;
        double paneHeight = drawingDTO.getDrawingPane().getPrefHeight() * zoom;

        double halfWidth = shapeWidth / 2;
        double halfHeight = shapeHeight / 2;

        double extraMargin = 42;

        double minX = (halfWidth + extraMargin) / zoom;
        double minY = (halfHeight + extraMargin) / zoom;
        double maxX = (paneWidth - halfWidth - extraMargin) / zoom;
        double maxY = (paneHeight - halfHeight - extraMargin) / zoom;

        double clampedX = Math.max(minX, Math.min(x, maxX));
        double clampedY = Math.max(minY, Math.min(y, maxY));

        return new Point2D(clampedX, clampedY);
    }

    /**
     * Adjusts the given array of 2D points to ensure they fit within the visible
     * bounds of the drawing pane, accounting for the current zoom level and an
     * additional margin.
     *
     * The method calculates the visible boundaries of the drawing pane based on
     * its dimensions and zoom level, and ensures all points are translated into
     * these boundaries without exceeding them.
     *
     * @param points an array of {@code Double} values representing the coordinates
     *               of points to be clamped. The array must have an even length,
     *               consisting of alternating x and y values.
     * @return a new array of {@code Double} values representing the adjusted
     *         coordinates of the points, ensuring they fit within the visible
     *         bounds of the drawing pane.
     */
    private Double[] clampPointsIntoPane(Double[] points) {
        double zoom = drawingDTO.getDrawingPane().getScaleX();
        double paneWidth = drawingDTO.getDrawingPane().getPrefWidth() * zoom;
        double paneHeight = drawingDTO.getDrawingPane().getPrefHeight() * zoom;

        double extraMargin = 42;
        double minX = extraMargin / zoom;
        double minY = extraMargin / zoom;
        double maxX = (paneWidth - extraMargin) / zoom;
        double maxY = (paneHeight - extraMargin) / zoom;
        double shapeMinX = Double.POSITIVE_INFINITY;
        double shapeMinY = Double.POSITIVE_INFINITY;
        double shapeMaxX = Double.NEGATIVE_INFINITY;
        double shapeMaxY = Double.NEGATIVE_INFINITY;

        for (int i = 0; i < points.length; i += 2) {
            double x = points[i];
            double y = points[i + 1];
            shapeMinX = Math.min(shapeMinX, x);
            shapeMinY = Math.min(shapeMinY, y);
            shapeMaxX = Math.max(shapeMaxX, x);
            shapeMaxY = Math.max(shapeMaxY, y);
        }
        double dx = 0, dy = 0;
        if (shapeMinX < minX) dx = minX - shapeMinX;
        else if (shapeMaxX > maxX) dx = maxX - shapeMaxX;

        if (shapeMinY < minY) dy = minY - shapeMinY;
        else if (shapeMaxY > maxY) dy = maxY - shapeMaxY;

        return translatePoints(points, dx, dy);
    }


    /**
     * Adds a cloned shape to the drawing pane.
     *
     * This method takes a {@code ShapeInterface} instance, retrieves its corresponding
     * {@code Node}, and adds it to the drawing pane. The node is configured with certain
     * properties, such as picking on bounds and moving to the front of the drawing pane.
     *
     * @param shape the {@code ShapeInterface} to be cloned and added to the drawing pane.
     **/
    public void addClonedShape(ShapeInterface shape) {
        Node node = shape.getNode();
        node.setUserData(shape);
        node.setPickOnBounds(true);
        drawingDTO.getDrawingPane().getChildren().add(node);
        node.toFront();
    }

    /**
     * Creates a rectangle shape with the specified properties.
     *
     * @param strokeColor the color to be used for the border (stroke) of the rectangle
     * @param fillColor the color to be used to fill the rectangle
     * @param borderWidth the width of the rectangle's border (stroke)
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param angle the rotation angle for the rectangle in degrees*/
    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0, angle);
        return rectangle;
    }

    /**
     * Creates an ellipse shape with the specified properties.
     *
     * @param strokeColor the color to be used for the border (stroke) of the ellipse
     * @param fillColor the color to be used to fill the ellipse
     * @param borderWidth the width of the border (stroke) of the ellipse
     * @param width the horizontal radius of the ellipse
     * @param height the vertical radius of the ellipse
     * @param angle the rotation angle for the ellipse in degrees
     * @return an instance of {@code AbstractShape} representing the created ellipse
     */
    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0, angle);
        return ellipse;
    }

    /**
     * Creates a line shape with the specified properties.
     *
     * This method creates and configures an instance of a line shape using the provided
     * stroke color, stroke width, and rotation angle. The created line is an implementation
     * of the {@code AbstractShape} class.
     *
     * @param strokeColor the color to be used for the border (stroke) of the line
     * @param strokeWidth the width of the line's stroke
     * @param angle the rotation angle of the line in degrees
     * @return an instance of {@code AbstractShape} representing the created line
     */
    private AbstractShape createLine(Color strokeColor, double strokeWidth, double angle){
        AbstractShape line = new LineCreator();
        line.creationOfShape(strokeColor, null, (int) strokeWidth, 0, 0, null, 1.0, 1.0, angle);
        return line;
    }

    /**
     * Creates a text shape with the specified properties.
     *
     * @param strokeColor the color to be used for the border (stroke) of the text
     * @param fillColor the color to be used to fill the text
     * @param borderWidth the width of the text's border (stroke)
     * @param width the width of the text shape
     * @param height the height of the text shape
     * @param content the textual content to be displayed in the shape
     * @param angle the rotation angle for the text in degrees
     * @return an instance of {@code AbstractShape} representing the created text shape
     */
    private AbstractShape createText(Color strokeColor, Color fillColor, int borderWidth, double width, double height, String content, double angle) {
        AbstractShape textCreator = new TextCreator();
        textCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, content, 1.0, 1.0, angle);
        return textCreator;
    }

    /**
     * Creates a polygon shape with the specified properties.
     *
     * @param strokeColor the color to be used for the border (stroke) of the polygon
     * @param fillColor the color to be used to fill the polygon
     * @param borderWidth the width of the polygon's border (stroke)
     * @param width the width of the polygon
     * @param height the height of the polygon
     * @param angle the rotation angle for the polygon in degrees
     * @return an instance of {@code AbstractShape} representing the created polygon
     */
    private AbstractShape createPolygon(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape polygonCreator = new PolygonCreator();
        polygonCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 1.0, 1.0, angle);
        return polygonCreator;
    }

    /**
     * Clears the preview elements from the drawing pane for lines and/or polygons.
     *
     * Depending on the specified parameters, this method removes any preview circles
     * and lines related to line or polygon previews from the drawing pane and resets
     * associated data structures.
     *
     * @param linePreview    a boolean indicating whether to clear the preview elements
     *                       related to lines. If {@code true},*/
    private void clearPreview(boolean linePreview, boolean polygonPreview) {
        if (linePreview) {
            drawingDTO.setStartPoint(null);
            drawingDTO.getDrawingPane().getChildren().removeAll(previewCirclesLine);
            previewCirclesLine.clear();
        }
        if (polygonPreview) {
            drawingDTO.getDrawingPane().getChildren().removeAll(previewCirclesPolygon);
            drawingDTO.getDrawingPane().getChildren().removeAll(previewLines);
            previewCirclesPolygon.clear();
            previewLines.clear();
            drawingDTO.resetPolygonPoints();
        }
    }
}
