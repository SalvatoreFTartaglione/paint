package org.example.model.shape;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.model.commands.UndoManager;

public class RectangleShape implements ShapeInterface {

    private Rectangle rectangleShape;
    private ContextMenuCreator contextMenuCreator;

    public RectangleShape(Color stroke, Color fill, double width, double height, int strokeWidth) {
        rectangleShape = new Rectangle();
        rectangleShape.setStroke(stroke);
        rectangleShape.setFill(fill);
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
        rectangleShape.setStrokeWidth(strokeWidth);
        contextMenuCreator = new ContextMenuCreator();
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
    public Color getStrokeColor() {
        return (Color) rectangleShape.getStroke();
    }

    @Override
    public Node findShapeAt(double x, double y) {
        Bounds bounds = rectangleShape.getBoundsInParent();
        double tolerance = 5.0;

        if (x >= bounds.getMinX() - tolerance && x <= bounds.getMaxX() + tolerance &&
                y >= bounds.getMinY() - tolerance && y <= bounds.getMaxY() + tolerance) {
            return rectangleShape;
        }

        return null;
    }

    @Override
    public ContextMenuCreator.ContextMenuWithPickers createContextMenu(Color fillColor, Color strokeColor) {
        return contextMenuCreator.create(fillColor, strokeColor);
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
    public void setCustomStrokeWidth(double strokeWidth) {
        rectangleShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public Node getNode() {
        return rectangleShape;
    }

    @Override
    public String toString() {
        return "Rectangle" + ";" + rectangleShape.getStroke() + ";" + rectangleShape.getFill() + ";"
                + rectangleShape.getStrokeWidth() + ";" + rectangleShape.getWidth() + ";"
                + rectangleShape.getHeight() + ";" + rectangleShape.getX() + ";" + rectangleShape.getY() + "\n";
    }


}
