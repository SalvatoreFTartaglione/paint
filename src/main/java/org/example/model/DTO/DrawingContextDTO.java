package org.example.model.DTO;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;


public class DrawingContextDTO {
    private Pane drawingPane;
    private Point2D startPoint;
    private double sceneX;
    private double sceneY;
    private ShapeOption shapeOption;
    private Color borderColor;
    private Color fillColor;
    private String textContent;
    private int fontSize;

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

    public double getSceneX() {
        return sceneX;
    }

    public void setSceneX(double sceneX) {
        this.sceneX = sceneX;
    }

    public double getSceneY() {
        return sceneY;
    }

    public void setSceneY(double sceneY) {
        this.sceneY = sceneY;
    }

    public ShapeOption getShapeOption() {
        return shapeOption;
    }

    public void setShapeOption(ShapeOption shapeOption) {
        this.shapeOption = shapeOption;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public Color getFillColor() {
        return fillColor;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    public String getText() {
        return textContent;
    }

    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    public void setText(String textContent){
        this.textContent = textContent;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }
}
