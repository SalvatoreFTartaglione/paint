package org.example.model.shape;

import javafx.scene.paint.Color;

public interface ShapeInterface {


    // Shape color management
    public void setStrokeColor(Color color);
    public Color getStrokeColor();
    void setStrokeWidth(int strokeWidth);
    int getCustomStrokeWidth();

    // Height and Width
    public double getWidth();
    public double getHeight();
    public void setSize(double width, double height);

    // Drawing points for the shape
    public double getX();
    public double getY();
    public void setX(double X);
    public void setY(double Y);

    // Draws the shape
    public void draw(double X, double Y);
    public void draw(Double[] points);

    // Simplified serialization
    public String toString();

    void setFillColor(Color fill);
}