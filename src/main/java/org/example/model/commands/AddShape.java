package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.*;

public class AddShape implements CommandInterface {

    private DrawingContextDTO dto;

    public AddShape(DrawingContextDTO context) {
        dto = context;
    }

    public void executeCommand() {
        if (dto.getMouseEvent().getButton() == MouseButton.PRIMARY) {
            ShapeOption selectedShape = dto.getShapeComboBox().getValue();
            if (selectedShape != null) {
                switch (selectedShape.getName()) {
                    case "Ellipse": {
                        // coordinate locali del clic sul Canvas
                        Point2D localCoords = dto.getDrawingPane().sceneToLocal(dto.getMouseEvent().getSceneX(), dto.getMouseEvent().getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        AbstractShape abstractEllipse = createEllipse(dto.getBorderColorPicker().getValue(), dto.getFillColorPicker().getValue(), 4, 80, 40);
                        ShapeInterface shapeInterface = abstractEllipse.getShape();
                        //controllo che la shape selezionata sia un'ellisse
                        EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                        ellipseShape.draw(x, y);
                        // Add ellipse to Canvas
                        ellipseShape.getNode().setUserData(shapeInterface);
                        dto.getDrawingPane().getChildren().add(ellipseShape.getNode());
                        break;
                    }
                    case "Line": {
                        Point2D localCoords = dto.getDrawingPane().sceneToLocal(dto.getMouseEvent().getSceneX(), dto.getMouseEvent().getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        if (dto.getStartPoint() == null) {
                            // first click: save starting point
                            dto.setStartPoint(localCoords);

                        } else {
                            // second click: save end point
                            LineCreator lineCreator = new LineCreator();
                            lineCreator.creationOfShape(dto.getBorderColorPicker().getValue(), null, 5, 0, 0);
                            // Add line to container
                            ShapeInterface shapeInterface = lineCreator.getShape();
                            Double[] points = {x, y, dto.getStartPoint().getX(), dto.getStartPoint().getY()};
                            LineShape lineShape = (LineShape) shapeInterface;
                            lineShape.draw(points);
                            lineShape.getNode().setUserData(shapeInterface);
                            dto.getDrawingPane().getChildren().add(lineShape.getNode());
                            dto.setStartPoint(null);
                        }
                        break;
                    }
                    case "Rectangle": {
                        // coordinate locali del clic sul Canvas
                        Point2D localCoords = dto.getDrawingPane().sceneToLocal(dto.getMouseEvent().getSceneX(), dto.getMouseEvent().getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        AbstractShape abstractRectangle = createRectangle(dto.getBorderColorPicker().getValue(), dto.getFillColorPicker().getValue(), 4, 80, 40);
                        ShapeInterface shapeInterface = abstractRectangle.getShape();
                        RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                        rectangleShape.draw(x, y);
                        rectangleShape.getNode().setUserData(shapeInterface);
                        dto.getDrawingPane().getChildren().add(rectangleShape.getNode());
                        break;
                    }
                }
            }
        }

    }

    public void undoCommand() {
    }

    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return rectangle;
    }

    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return ellipse;
    }
}
