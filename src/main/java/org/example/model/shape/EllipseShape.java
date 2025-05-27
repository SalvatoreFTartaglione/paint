package org.example.model.shape;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;


public class EllipseShape implements ShapeInterface {

    private Ellipse ellipseShape;

    public EllipseShape(Color stroke, Color fill, double strokeWidth, double width, double height) {
        this.ellipseShape = new Ellipse();
        ellipseShape.setStroke(stroke);
        ellipseShape.setFill(fill);
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
        ellipseShape.setStrokeWidth(strokeWidth);
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
    public ShapeInterface cloneShape() {
        return new EllipseShape(this.getStrokeColor(), this.getFillColor(), this.getCustomStrokeWidth(), this.getWidth(), this.getHeight());
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {
        ellipseShape.setCenterX(ellipseShape.getCenterX() + deltaX);
        ellipseShape.setCenterY(ellipseShape.getCenterY() + deltaY);
    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double minX = (getWidth() / 2 + margin) / zoom;
        double maxX = (paneWidth - getWidth() / 2 - margin) / zoom;
        double minY = (getHeight() / 2 + margin) / zoom;
        double maxY = (paneHeight - getHeight() / 2 - margin) / zoom;

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }


    @Override
    public Color getStrokeColor() {
       return (Color) ellipseShape.getStroke();
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
    public Double[] getPoints(){
        return null;
    }

    @Override
    public Node getNode() {
        return this.ellipseShape;
    }

    @Override
    public double getEffectiveSize() {
        // Per i cerchi/ellissi, usa il diametro maggiore
        return Math.max(getWidth(), getHeight());
    }

    @Override
    public void scaleToSize(double newSize) {
        // Mantieni l'aspect ratio dell'ellisse
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            setSize(getWidth() * scale, getHeight() * scale);
        } else {
            setSize(newSize, newSize);
        }
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("Diameter: %.1f", value);
    }

    @Override
    public String getText() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public String toString() {
        return "Ellipse" + ";" + ellipseShape.getStroke() + ";" + ellipseShape.getFill() + ";" + ellipseShape.getStrokeWidth() + ";"
                + ellipseShape.getRadiusX() + ";" + ellipseShape.getRadiusY() + ";" + ellipseShape.getCenterX() + ";" + ellipseShape.getCenterY() + "\n";
    }
}
