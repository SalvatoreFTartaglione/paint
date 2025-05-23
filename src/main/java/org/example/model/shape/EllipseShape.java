package org.example.model.shape;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


public class EllipseShape implements ShapeInterface {

    private Ellipse ellipseShape;
    private ContextMenuCreator contextMenuCreator;

    public EllipseShape(Color stroke, Color fill, double strokeWidth, double width, double height) {
        this.ellipseShape = new Ellipse();
        ellipseShape.setStroke(stroke);
        ellipseShape.setFill(fill);
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
        ellipseShape.setStrokeWidth(strokeWidth);
        contextMenuCreator = new ContextMenuCreator();
    }

    public void draw(double x, double y) {
        ellipseShape.setCenterX(x);
        ellipseShape.setCenterY(y);
    }

    @Override
    public void draw(Double[] points) {
        throw new UnsupportedOperationException("The following feature is not supported");
    }

    @Override
    public Color getStrokeColor() {
       return (Color) ellipseShape.getStroke();
    }

    @Override
    public Node findShapeAt(double x, double y) {
        Bounds bounds = ellipseShape.getBoundsInParent();
        double tolerance = 5.0;

        if (x >= bounds.getMinX() - tolerance && x <= bounds.getMaxX() + tolerance &&
                y >= bounds.getMinY() - tolerance && y <= bounds.getMaxY() + tolerance) {
            return ellipseShape;
        }

        return null;
    }

    @Override
    public ContextMenuCreator.ContextMenuWithPickers createContextMenu(Color fillColor, Color strokeColor) {
        return contextMenuCreator.create(fillColor, strokeColor);
    }

    @Override
    public void setStrokeColor(Color stroke) {
        ellipseShape.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color) ellipseShape.getFill();
    }

    @Override
    public void setFillColor(Color fill){
        ellipseShape.setFill(fill);
    }

    @Override
    public double getX() {
        return ellipseShape.getCenterX();
    }

    @Override
    public void setX(double x) {
       ellipseShape.setCenterX(x);
    }

    @Override
    public double getY() {
        return ellipseShape.getCenterY();
    }

    @Override
    public void setY(double y) {
        ellipseShape.setCenterY(y);
    }

    @Override
    public double getWidth() {
        return ellipseShape.getRadiusX();
    }

    @Override
    public double getHeight() {
        return ellipseShape.getRadiusY();
    }

    @Override
    public void setSize(double width, double height){
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
    }

    @Override
    public double getCustomStrokeWidth() {
        return ellipseShape.getStrokeWidth();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        ellipseShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public Node getNode() {
        return this.ellipseShape;
    }

    @Override
    public String toString() {
        return "Ellipse" + ";" + ellipseShape.getStroke() + ";" + ellipseShape.getFill() + ";" + ellipseShape.getStrokeWidth() + ";"
                + ellipseShape.getRadiusX() + ";" + ellipseShape.getRadiusY() + ";" + ellipseShape.getCenterX() + ";" + ellipseShape.getCenterY() + "\n";
    }
}
