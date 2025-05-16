package org.example.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public interface ShapeInterface {


    // Shape color management
    public void setStrokeColor(Color color);
    public Color getStrokeColor();

    // Height and Width
    public double getWidthShape();
    public double getHeightShape();
    public void setSize(double width, double height);

    //
    public double getX();
    public double getY();
    public void setX(double X);
    public void setY(double Y);

    // Managing selection and filling logic (where usable)
    public void setFillColor(Color color);
    public Color getFillColor();

    // Draws the shape
    public void draw(double X, double Y);
    public void draw(Double[] points);

    // Simplified serialization
    public String toString();
}