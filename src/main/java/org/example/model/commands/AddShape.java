package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.shape.*;

public class AddShape implements CommandInterface {

    private MouseEvent mouseEvent;
    private ContextMenu contextMenu;
    private ComboBox<ShapeOption> shapeComboBox;
    private Pane drawingPane;
    private Point2D startPoint;
    private ColorPicker borderColorPicker;
    private ColorPicker fillColorPicker;

    public AddShape(MouseEvent mouseEvent, ContextMenu contextMenu, ComboBox<ShapeOption> shapeComboBox, Pane drawingPane, Point2D startPoint, ColorPicker borderColorPicker, ColorPicker fillColorPicker) {
        this.mouseEvent = mouseEvent;
        this.contextMenu = contextMenu;
        this.shapeComboBox = shapeComboBox;
        this.drawingPane = drawingPane;
        this.startPoint = startPoint;
        this.borderColorPicker = borderColorPicker;
        this.fillColorPicker = fillColorPicker;
    }

    public void executeCommand() {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ShapeOption selectedShape = shapeComboBox.getValue();
            if (!contextMenu.isShowing()) {
                if (selectedShape != null) {
                    switch (selectedShape.getName()) {
                        case "Ellipse": {
                            // coordinate locali del clic sul Canvas
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            AbstractShape abstractEllipse = createEllipse(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                            ShapeInterface shapeInterface = abstractEllipse.getShape();
                            //controllo che la shape selezionata sia un'ellisse
                            EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                            ellipseShape.draw(x, y);
                            // Add ellipse to Canvas
                            drawingPane.getChildren().add(ellipseShape.getNode());
                            break;
                        }
                        case "Line": {
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            if (startPoint == null) {
                                // first click: save starting point
                                startPoint = new Point2D(x, y);
                            } else {
                                // second click: save end point
                                LineCreator lineCreator = new LineCreator();
                                lineCreator.creationOfShape(borderColorPicker.getValue(), null, 5, 0, 0);
                                // Add line to container
                                ShapeInterface shapeInterface = lineCreator.getShape();
                                Double[] points = {x, y, startPoint.getX(), startPoint.getY()};
                                LineShape lineShape = (LineShape) shapeInterface;
                                lineShape.draw(points);
                                drawingPane.getChildren().add(lineShape.getNode());
                            }
                            break;
                        }
                        case "Rectangle": {
                            // coordinate locali del clic sul Canvas
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            AbstractShape abstractRectangle = createRectangle(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                            ShapeInterface shapeInterface = abstractRectangle.getShape();
                            RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                            rectangleShape.draw(x, y);
                            drawingPane.getChildren().add(rectangleShape.getNode());
                            break;
                        }
                    }
                }
            }/*else {
                contextMenu.hide();
            }*/
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
