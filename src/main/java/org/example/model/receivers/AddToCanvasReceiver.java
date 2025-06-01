package org.example.model.receivers;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.*;

public class AddToCanvasReceiver {

    private final DrawingContextDTO drawingDTO;

    public AddToCanvasReceiver(DrawingContextDTO drawingDTO) {
        this.drawingDTO = drawingDTO;
    }


    public ShapeInterface addShape() {
        ShapeInterface shape = null;
        ShapeOption selectedShape = drawingDTO.getShapeOption();

        switch (selectedShape.getName()) {
            case "Ellipse": {
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractEllipse = createEllipse(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40);
                shape = abstractEllipse.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                break;
            }
            case "Rectangle": {
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractRectangle = createRectangle(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40);
                shape = abstractRectangle.getShape();
                Point2D clampedCoords = clampPosition(localCoords.getX(), localCoords.getY(), shape.getWidth(), shape.getHeight());
                shape.draw(clampedCoords.getX(), clampedCoords.getY());
                break;
            }
            case "Line": {
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                if (drawingDTO.getStartPoint() == null) {
                    drawingDTO.setStartPoint(localCoords);
                    return null;  // incomplete line, non aggiungere
                } else {
                    LineCreator lineCreator = new LineCreator();
                    lineCreator.creationOfShape(drawingDTO.getBorderColor(), null, 5, 0, 0, null, 0.0, 0.0);
                    shape = lineCreator.getShape();
                    Double[] points = {
                            localCoords.getX(), localCoords.getY(),
                            drawingDTO.getStartPoint().getX(), drawingDTO.getStartPoint().getY()
                    };
                    shape.draw(points);
                    drawingDTO.setStartPoint(null);
                }
                break;
            }
            case "Text": {
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                AbstractShape abstractText = createText(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), drawingDTO.getFontSize(), 40, 20, drawingDTO.getText());
                shape = abstractText.getShape();
                shape.draw(localCoords.getX(), localCoords.getY());
                break;
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

        if (shape != null) {
            shape.getNode().setUserData(shape);
            drawingDTO.getDrawingPane().getChildren().add(shape.getNode());
        }

        return shape;
    }

    private Point2D clampPosition(double x, double y, double shapeWidth, double shapeHeight) {
        double zoom = drawingDTO.getDrawingPane().getScaleX(); // Scala uniforme X/Y
        double paneWidth = drawingDTO.getDrawingPane().getPrefWidth() * zoom;
        double paneHeight = drawingDTO.getDrawingPane().getPrefHeight() * zoom;

        double halfWidth = shapeWidth / 2;
        double halfHeight = shapeHeight / 2;

        double extraMargin = 42; // margine extra per non stare troppo vicino al bordo

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

    private static AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0);
        return rectangle;
    }

    private static AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height, null, 0.0, 0.0);
        return ellipse;
    }

    private static AbstractShape createText(Color strokeColor, Color fillColor, int borderWidth, double width, double height, String content) {
        AbstractShape textCreator = new TextCreator();
        textCreator.creationOfShape(strokeColor, fillColor, borderWidth, width, height, content, 1.0, 1.0);
        return textCreator;
    }
}
