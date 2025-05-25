package org.example.model.shape;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.model.commands.UndoManager;

public class RectangleShape implements ShapeInterface {

    private Rectangle rectangleShape;

    public RectangleShape(Color stroke, Color fill, double width, double height, double strokeWidth) {
        rectangleShape = new Rectangle();
        rectangleShape.setStroke(stroke);
        rectangleShape.setFill(fill);
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
        rectangleShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public void draw(double x, double y) {
        rectangleShape.setX(x);
        rectangleShape.setY(y);
    }

    @Override
    public void draw(Double[] points) {
        throw new UnsupportedOperationException("The following feature is not supported");
    }

    @Override
    public ShapeInterface cloneShape() {
        RectangleShape clone = new RectangleShape(
                this.getStrokeColor(),
                this.getFillColor(),
                this.getWidth(),
                this.getHeight(),
                this.getCustomStrokeWidth()
        );

        clone.setX(this.getX());
        clone.setY(this.getY());
        return clone;
    }


    @Override
    public void moveBy(double deltaX, double deltaY) {
        rectangleShape.setX(rectangleShape.getX() + deltaX);
        rectangleShape.setY(rectangleShape.getY() + deltaY);
    }

    @Override
    public Color getStrokeColor() {
        return (Color) rectangleShape.getStroke();
    }

    @Override
    public void setStrokeColor(Color stroke) {
        rectangleShape.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color) rectangleShape.getFill();
    }

    @Override
    public void setFillColor(Color fill){
        rectangleShape.setFill(fill);
    }

    @Override
    public double getX() {
        return rectangleShape.getX();
    }

    @Override
    public void setX(double x) {
        rectangleShape.setX(x);
    }

    @Override
    public double getY() {
        return rectangleShape.getY();
    }

    @Override
    public void setY(double y) {
        rectangleShape.setY(y);
    }

    @Override
    public double getWidth() {
        return rectangleShape.getWidth();
    }

    @Override
    public double getHeight() {
        return rectangleShape.getHeight();
    }

    @Override
    public void setSize(double width, double height){
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
    }

    @Override
    public double getCustomStrokeWidth() {
        return rectangleShape.getStrokeWidth();
    }

    @Override
    public Double[] getPoints(){
        return null;
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        rectangleShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public Node getNode() {
        return rectangleShape;
    }

    @Override
    public double getEffectiveSize() {
        // Per i rettangoli, usa la dimensione maggiore
        return Math.max(getWidth(), getHeight());
    }

    @Override
    public void scaleToSize(double newSize) {
        // Per i rettangoli, mantieni l'aspect ratio
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            double newWidth = getWidth() * scale;
            double newHeight = getHeight() * scale;
            setSize(newWidth, newHeight);
        } else {
            // Se degenerato, crea un quadrato
            setSize(newSize, newSize);
        }
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("Dimension: %.1f", value);
    }

    @Override
    public String toString() {
        return "Rectangle" + ";" + rectangleShape.getStroke() + ";" + rectangleShape.getFill() + ";"
                + rectangleShape.getStrokeWidth() + ";" + rectangleShape.getWidth() + ";"
                + rectangleShape.getHeight() + ";" + rectangleShape.getX() + ";" + rectangleShape.getY() + "\n";
    }


}
