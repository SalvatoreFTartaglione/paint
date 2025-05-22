package org.example.model.DTO;

import javafx.geometry.Point2D;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import org.example.controller.ShapeOption;

public class DrawingContextDTO {
    private MouseEvent mouseEvent;
    private ContextMenu contextMenu;
    private ComboBox<ShapeOption> shapeComboBox;
    private Pane drawingPane;
    private Point2D startPoint;
    private ColorPicker borderColorPicker;
    private ColorPicker fillColorPicker;

    public ColorPicker getFillColorPicker() {
        return fillColorPicker;
    }

    public void setFillColorPicker(ColorPicker fillColorPicker) {
        this.fillColorPicker = fillColorPicker;
    }

    public MouseEvent getMouseEvent() {
        return mouseEvent;
    }

    public void setMouseEvent(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    public ContextMenu getContextMenu() {
        return contextMenu;
    }

    public void setContextMenu(ContextMenu contextMenu) {
        this.contextMenu = contextMenu;
    }

    public ComboBox<ShapeOption> getShapeComboBox() {
        return shapeComboBox;
    }

    public void setShapeComboBox(ComboBox<ShapeOption> shapeComboBox) {
        this.shapeComboBox = shapeComboBox;
    }

    public Pane getDrawingPane() {
        return drawingPane;
    }

    public void setDrawingPane(Pane drawingPane) {
        this.drawingPane = drawingPane;
    }

    public Point2D getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
    }

    public ColorPicker getBorderColorPicker() {
        return borderColorPicker;
    }

    public void setBorderColorPicker(ColorPicker borderColorPicker) {
        this.borderColorPicker = borderColorPicker;
    }
}
