package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

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
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public boolean canHaveFill() {
        return true;
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
    public void mirrorUp() {
        double centerY = getY() + getHeight() / 2;
        double topY = getY();
        double distance = centerY - topY;
        double mirroredCenterY = topY - distance;
        double newY = mirroredCenterY - getHeight() / 2;

        rectangleShape.setY(newY);
    }

    @Override
    public void mirrorDown() {
        double centerY = getY() + getHeight() / 2;
        double bottomY = getY() + getHeight();
        double distance = bottomY - centerY;
        double mirroredCenterY = bottomY + distance;
        double newY = mirroredCenterY - getHeight() / 2;

        rectangleShape.setY(newY);
    }

    @Override
    public void mirrorLeft() {
        double centerX = getX() + getWidth() / 2;
        double leftX = getX();
        double distance = centerX - leftX;
        double mirroredCenterX = leftX - distance;
        double newX = mirroredCenterX - getWidth() / 2;

        rectangleShape.setX(newX);
    }

    @Override
    public void mirrorRight() {
        double centerX = getX() + getWidth() / 2;
        double rightX = getX() + getWidth();
        double distance = rightX - centerX;
        double mirroredCenterX = rightX + distance;
        double newX = mirroredCenterX - getWidth() / 2;

        rectangleShape.setX(newX);
    }

    @Override
    public void setTextScaleX(double scale) {
        rectangleShape.setScaleX(scale);
    }

    @Override
    public double getTextScaleX() {
        return rectangleShape.getScaleX();
    }

    @Override
    public void setTextScaleY(double scale) {
        rectangleShape.setScaleY(scale);
    }

    @Override
    public double getTextScaleY() {
        return rectangleShape.getScaleY();
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {
        rectangleShape.setScaleX(rectangleShape.getScaleX() * -1);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        rectangleShape.setScaleY(rectangleShape.getScaleY() * -1);
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
    public double getStretchWidth() {
        return this.getWidth();
    }

    @Override
    public double getStretchHeight() {
        return this.getHeight();
    }

    @Override
    public void stretch(double width, double height){
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
    }

    @Override
    public void resetStretch(double width, double height) {
        this.setSize(width, height);
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
    public void resize(double newSize) {
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
    public String getText() {
        return null;
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public String toString() {
        return "Rectangle" + ";" + rectangleShape.getStroke() + ";" + rectangleShape.getFill() + ";"
                + rectangleShape.getStrokeWidth() + ";" + rectangleShape.getWidth() + ";"
                + rectangleShape.getHeight() + ";" + rectangleShape.getX() + ";" + rectangleShape.getY() + "\n";
    }


}
