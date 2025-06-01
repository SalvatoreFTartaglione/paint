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
                AbstractShape abstractEllipse = createEllipse(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 100, 50);
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
                AbstractShape abstractRectangle = createRectangle(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 120, 60);
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
                    AbstractShape abstractLine = createLine(drawingDTO.getBorderColor(), 9);
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
                AbstractShape abstractText = createText(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), drawingDTO.getFontSize(), 40, 20, drawingDTO.getText());
                shape = abstractText.getShape();
                shape.draw(localCoords.getX(), localCoords.getY());
                canAdd = true;
                break;
            }

            case "Polygon": {
                // show preview of the points
                // get last point from the list of points
                Circle c = new Circle( drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getX(), drawingDTO.getPolygonPoints().get(drawingDTO.getPolygonPoints().size() - 1).getY(), 4, Color.BLUE );
                drawingDTO.getDrawingPane().getChildren().add(c);
                previewCircles.add(c);
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
                    AbstractShape abstractPolygon = createPolygon(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40);
                    shape = abstractPolygon.getShape();
                    shape.draw(doublePoints);
                    // Remove previews after 500ms from add
                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(500), e -> {
                        drawingDTO.getDrawingPane().getChildren().removeAll(previewCircles);
                        // Clear preview list
                        previewCircles.clear();
                    }));
                    timeline.play();
                    // Clear array of polygon points
                    drawingDTO.clearPolygonPoints();
                    // Reset start point for line
                    drawingDTO.setStartPoint(null);
                    canAdd = true;
                }
            }
            case "Favorite": {
                ShapeInterface favoriteShape = drawingDTO.getShape();
                if (favoriteShape != null) {
                   Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                   favoriteShape.draw(localCoords.getX(), localCoords.getY());
                   shape = favoriteShape;
                   break;
                }
            }

        }
        if (shape != null && canAdd) {
            shape.getNode().setUserData(shape);
            drawingDTO.getDrawingPane().getChildren().add(shape.getNode());
            return shape;
        }
        else {
            return null;
        }
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


    public void addClonedShape(ShapeInterface shape) {
        Node node = shape.getNode();
        node.setUserData(shape);
        node.setPickOnBounds(true);
        drawingDTO.getDrawingPane().getChildren().add(node);
        node.toFront();
    }

    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0);
        return rectangle;
    }

    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0);
        return ellipse;
    }

    private AbstractShape createLine(Color strokeColor, double strokeWidth){
        AbstractShape line = new LineCreator();
        line.creationOfShape(strokeColor, null, (int) strokeWidth, 0, 0, null, 1.0, 1.0);
        return line;
    }

    private AbstractShape createText(Color strokeColor, Color fillColor, int borderWidth, double width, double height, String content) {
        AbstractShape textCreator = new TextCreator();
        textCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, content, 1.0, 1.0);
        return textCreator;
    }

    private AbstractShape createPolygon(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape polygonCreator = new PolygonCreator();
        polygonCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 1.0, 1.0);
        return polygonCreator;
    }
}
