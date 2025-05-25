/*package org.example.model.shape;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextShape implements ShapeInterface {

    private final Text textNode;
    private double strokeWidth = 1.0;

    private Double[] points = new Double[2]; // X, Y

    public TextShape(String content, Color strokeColor, double fontSize) {
        this.textNode = new Text(content);
        this.textNode.setFill(strokeColor);
        this.textNode.setFont(Font.font(fontSize));
    }

    @Override
    public void draw(double X, double Y) {
        textNode.setX(X);
        textNode.setY(Y);
        points[0] = X;
        points[1] = Y;
    }

    @Override
    public void draw(Double[] points) {
        if (points.length >= 2) {
            draw(points[0], points[1]);
        }
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    public ShapeInterface cloneShape() {
        TextShape clone = new TextShape(getText(), getStrokeColor(), getFontSize());
        clone.draw(getX(), getY());
        return clone;
    }

    @Override
    public Color getStrokeColor() {
        return (Color) textNode.getFill();
    }

    @Override
    public void setStrokeColor(Color stroke) {
        textNode.setFill(stroke);
    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void setFillColor(Color fill) {
        throw new UnsupportedOperationException("TextShape non supporta il riempimento");
    }

    @Override
    public double getWidth() {
        return textNode.getLayoutBounds().getWidth();
    }

    @Override
    public double getHeight() {
        return textNode.getLayoutBounds().getHeight();
    }

    @Override
    public void setSize(double width, double height) {
        textNode.setFont(Font.font(width)); // interpreta width come fontSize
    }

    @Override
    public double getX() {
        return textNode.getX();
    }

    @Override
    public void setX(double x) {
        textNode.setX(x);
        points[0] = x;
    }

    @Override
    public double getY() {
        return textNode.getY();
    }

    @Override
    public void setY(double y) {
        textNode.setY(y);
        points[1] = y;
    }

    @Override
    public Double[] getPoints() {
        return points;
    }

    @Override
    public double getCustomStrokeWidth() {
        return strokeWidth;
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    @Override
    public Node getNode() {
        return textNode;
    }

    @Override
    public String toString() {
        return "Text;" + getText() + ";" + getStrokeColor() + ";" + getX() + ";" + getY() + ";" + getFontSize() + "\n";
    }

    public String getText() {
        return textNode.getText();
    }

    public void setText(String content) {
        textNode.setText(content);
    }

    public double getFontSize() {
        return textNode.getFont().getSize();
    }
}*/
