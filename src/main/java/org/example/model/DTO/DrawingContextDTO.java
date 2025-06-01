package org.example.model.DTO;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.example.controller.ShapeOption;
import org.example.model.shape.ShapeInterface;

import java.util.ArrayList;
import java.util.List;


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
    private double selStartX, selStartY, selEndX, selEndY; //Per il rettangolo della selezione multipla
    private ScrollPane scrollPane;
    private Group mainCanvasContainer;
    private List<Point2D> polygonPoints = new ArrayList<>();
    private static final double TOLERANCE = 7.0;
    private ShapeInterface shape;                                           // utils for favorite shapes

    public Group getMainCanvasContainer() {
        return mainCanvasContainer;
    }

    public void setMainCanvasContainer(Group mainCanvasContainer) {
        this.mainCanvasContainer = mainCanvasContainer;
    }

    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
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

    //Metodi del rettangolo della selezione multipla
    public double getSelStartX() { return selStartX; }
    public void setSelStartX(double selStartX) { this.selStartX = selStartX; }
    public double getSelStartY() { return selStartY; }
    public void setSelStartY(double selStartY) { this.selStartY = selStartY; }
    public double getSelEndX() { return selEndX; }
    public void setSelEndX(double selEndX) { this.selEndX = selEndX; }
    public double getSelEndY() { return selEndY; }
    public void setSelEndY(double selEndY) { this.selEndY = selEndY; }

    public ShapeInterface getShape() {
        return shape;
    }

    public void setShape(ShapeInterface shape) {
        this.shape = shape;
    }

    // Polygon methods
    public List<Point2D> getPolygonPoints() {
        return polygonPoints;
    }

    public void setPolygonPoint(Point2D point) {
        polygonPoints.add(point);
    }

    /*
    Checks if the user has added more than two points and if the starting point is the same as the
    ending point
     */
    public boolean canCreatePolygon() {
        Point2D firstPoint = polygonPoints.get(0);
        Point2D lastPoint = polygonPoints.get(polygonPoints.size() - 1);
        // Check if points are approximately equal within tolerance
        boolean isApproxEqual = Math.abs(firstPoint.getX() - lastPoint.getX()) <= TOLERANCE &&
                Math.abs(firstPoint.getY() - lastPoint.getY()) <= TOLERANCE;

        return polygonPoints != null &&  polygonPoints.size() >= 4 && isApproxEqual;
    }

    public void clearPolygonPoints() { polygonPoints.clear(); }
}
