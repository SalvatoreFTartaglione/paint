package org.example.model.receivers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shapeCreators.*;
import org.example.model.shape.*;

import java.util.ArrayList;
import java.util.List;

public class AddToCanvasReceiver {

    private final DrawingContextDTO drawingDTO;
    private static final List<Circle> previewCircles = new ArrayList<>();
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
                drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                previewCircles.clear();
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractEllipse = createEllipse(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 100, 50, 0);
                shape = abstractEllipse.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                canAdd = true;
                break;
            }

            case "Rectangle": {
                drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                previewCircles.clear();
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractRectangle = createRectangle(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 120, 60, 0);
                shape = abstractRectangle.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                canAdd = true;
                break;
            }

            case "Line": {
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                Circle c0 = new Circle(localCoords.getX(), localCoords.getY() ,4, Color.RED);
                if (drawingDTO.getStartPoint() == null) {
                    drawingDTO.setStartPoint(localCoords);
                    // Preview first point
                    drawingDTO.getDrawingPane().getChildren().add(c0);
                    previewCircles.add(c0);
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
                    Circle c1 = new Circle(localCoords.getX(), localCoords.getY() ,4, Color.RED);
                    // Preview first point
                    drawingDTO.getDrawingPane().getChildren().add(c1);
                    previewCircles.add(c1);
                    shape.draw(points);
                    // Remove previews after 500ms from add
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                        // Clear preview list
                        previewCircles.clear();
                    }));
                    timeline.play();
                    // Reset start point for line
                    drawingDTO.setStartPoint(null);
                    canAdd = true;
                }
                break;
            }

            case "Text": {
                drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                previewCircles.clear();
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractText = createText(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), drawingDTO.getFontSize(), 40, 20, drawingDTO.getText(), 0);
                shape = abstractText.getShape();
                shape.draw(localCoords.getX(), localCoords.getY());
                canAdd = true;
                break;
            }

            case "Polygon": {
                Circle c = new Circle( drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getX(), drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getY(), 4, Color.BLUE );
                drawingDTO.getDrawingPane().getChildren().add(c);
                previewCircles.add(c);
                canAdd = false;
                if (drawingDTO.canCreatePolygon()) {
                    List<Point2D> points = drawingDTO.getPolygonPoints();
                    List<Double> doubleList = new ArrayList<>();
                    for (Point2D point : points) {
                        doubleList.add(point.getX());
                        doubleList.add(point.getY());
                    }
                    Double[] doublePoints = doubleList.toArray(new Double[0]);
                    AbstractShape abstractPolygon = createPolygon(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40, 0);
                    shape = abstractPolygon.getShape();
                    shape.draw(doublePoints);
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                        previewCircles.clear();
                    }));
                    timeline.play();
                    drawingDTO.clearPolygonPoints();
                    drawingDTO.setStartPoint(null);
                    canAdd = true;
                    break;
                }
                break;
            }
            case "Favorite": {
                ShapeInterface favoriteShape = drawingDTO.getShape();
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
}
