package org.example.model.commands.receivers;

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

public class AddToCanvasReceiver {

    private final DrawingContextDTO drawingDTO;
    private static final List<Circle> previewCirclesLine = new ArrayList<>();
    private static final List<Circle> previewCirclesPolygon = new ArrayList<>();
    private static final List<Line> previewLines = new ArrayList<>();
    private boolean canAdd = false;

    public AddToCanvasReceiver(DrawingContextDTO drawingDTO) {
        this.drawingDTO = drawingDTO;
    }


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


    private Double[] translatePoints(Double[] points, double dx, double dy) {
        Double[] result = new Double[points.length];
        for (int i = 0; i < points.length; i += 2) {
            result[i] = points[i] + dx;
            result[i + 1] = points[i + 1] + dy;
        }
        return result;
    }


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



    public void addClonedShape(ShapeInterface shape) {
        Node node = shape.getNode();
        node.setUserData(shape);
        node.setPickOnBounds(true);
        drawingDTO.getDrawingPane().getChildren().add(node);
        node.toFront();
    }

    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0, angle);
        return rectangle;
    }

    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0, angle);
        return ellipse;
    }

    private AbstractShape createLine(Color strokeColor, double strokeWidth, double angle){
        AbstractShape line = new LineCreator();
        line.creationOfShape(strokeColor, null, (int) strokeWidth, 0, 0, null, 1.0, 1.0, angle);
        return line;
    }

    private AbstractShape createText(Color strokeColor, Color fillColor, int borderWidth, double width, double height, String content, double angle) {
        AbstractShape textCreator = new TextCreator();
        textCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, content, 1.0, 1.0, angle);
        return textCreator;
    }

    private AbstractShape createPolygon(Color strokeColor, Color fillColor, int borderWidth, double width, double height, double angle) {
        AbstractShape polygonCreator = new PolygonCreator();
        polygonCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 1.0, 1.0, angle);
        return polygonCreator;
    }

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
