package org.example.model.receivers;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.*;

public class AddToCanvas {

    public static void add(DrawingContextDTO context) {
        ShapeOption selectedShape = context.getShapeOption();

        switch (selectedShape.getName()) {
            case "Ellipse": {
                // coordinate locali del clic sul Canvas
                Point2D localCoords = context.getDrawingPane().sceneToLocal(context.getSceneX(), context.getSceneY());
                double x = localCoords.getX();
                double y = localCoords.getY();
                AbstractShape abstractEllipse = createEllipse(context.getBorderColor(), context.getFillColor(), 4, 80, 40);
                ShapeInterface shapeInterface = abstractEllipse.getShape();
                //controllo che la shape selezionata sia un'ellisse
                EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                ellipseShape.draw(x, y);
                // Add ellipse to Canvas
                ellipseShape.getNode().setUserData(shapeInterface);
                context.getDrawingPane().getChildren().add(ellipseShape.getNode());
                break;
            }
            case "Line": {
                Point2D localCoords = context.getDrawingPane().sceneToLocal(context.getSceneX(), context.getSceneY());
                double x = localCoords.getX();
                double y = localCoords.getY();
                if (context.getStartPoint() == null) {
                    // first click: save starting point
                    context.setStartPoint(localCoords);

                } else {
                    // second click: save end point
                    LineCreator lineCreator = new LineCreator();
                    lineCreator.creationOfShape(context.getBorderColor(), null, 5, 0, 0);
                    // Add line to container
                    ShapeInterface shapeInterface = lineCreator.getShape();
                    Double[] points = {x, y, context.getStartPoint().getX(), context.getStartPoint().getY()};
                    LineShape lineShape = (LineShape) shapeInterface;
                    lineShape.draw(points);
                    lineShape.getNode().setUserData(shapeInterface);
                    context.getDrawingPane().getChildren().add(lineShape.getNode());
                    context.setStartPoint(null);
                }
                break;
            }
            case "Rectangle": {
                // coordinate locali del clic sul Canvas
                Point2D localCoords = context.getDrawingPane().sceneToLocal(context.getSceneX(), context.getSceneY());
                double x = localCoords.getX();
                double y = localCoords.getY();
                AbstractShape abstractRectangle = createRectangle(context.getBorderColor(), context.getFillColor(), 4, 80, 40);
                ShapeInterface shapeInterface = abstractRectangle.getShape();
                RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                rectangleShape.draw(x, y);
                rectangleShape.getNode().setUserData(shapeInterface);
                context.getDrawingPane().getChildren().add(rectangleShape.getNode());
                break;
            }
        }
    }

    private static AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return rectangle;
    }

    private static AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return ellipse;
    }
}
