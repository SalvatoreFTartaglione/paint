

package org.example.model.shape;

import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;


public class LineShape extends Line implements ShapeInterface{

    private Color strokeColor;
    private double startX, startY, endX, endY;
    private Double[]    points;
    private int strokeWidth;
    // private double angle;

    /**
     * Creates a new {@code LineShape} with the specified stroke color and stroke width.
     *
     * @param strokeColor the border color
     * @param strokeWidth the line thickness
     */
    public LineShape(Color strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;
    }

    /**
     * Draws the line using the previously stored relative offset and updates the visual position.
     *
     * @param X the new start X coordinate
     * @param Y the new start Y coordinate
     */
    @Override
    public void draw(double X, double Y) {
        startX = this.points[0];
        startY = this.points[1];
        endX = this.points[2];
        endY = this.points[3];
        this.points[0] = X;
        this.points[1] = Y;
        this.points[2] = X + (endX - startX);
        this.points[3] = Y + (endY - startY);
        this.setStartX(this.points[0]);
        this.setStartY(this.points[1]);
        this.setEndX(this.points[2]);
        this.setEndY(this.points[3]);
        this.setStroke(this.getStrokeColor());
        this.setStrokeWidth(this.getCustomStrokeWidth());
    }

    /**
     * @param points
     * It assigns to the selected shape (in this case the line) the properties obtained thanks to the "set" methods
     * and the coordinates passed as a parameter
     */
    @Override
    public void draw(Double[] points) {
        this.setStroke(this.getStrokeColor());
        this.setStartX(points[0]);
        this.setStartY(points[1]);
        this.setEndX(points[2]);
        this.setEndY(points[3]);
        this.setStrokeWidth(this.getCustomStrokeWidth());
        this.setPoints(points);
    }

    /**
     * @return Color
     * Returns the border color of the shape
     */
    @Override
    public Color getStrokeColor() {
        return this.strokeColor;
    }

    /**
     * @param color
     * Sets the border color of the shape
     */
    @Override
    public void setStrokeColor(Color color) {
        this.strokeColor = color;
        this.setStroke(color); // aggiorna visivamente
    }

    /**
     * @return double
     * Returns the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public double getX() {
        return this.startX;
    }

    /**
     * @return double
     * Returns the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public double getY() {
        return this.startY;
    }

    /**
     * @param point
     * Sets the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public void setX(double point) {
        this.startX = point;

    }

    /**
     * @param point
     * Sets the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public void setY(double point) {
        this.startY = point;

    }

    /**
     * @param point
     * Sets the coordinate along the abscissa axis of the end point of the line
     */
    public void endX(double point) {
        this.endX = point;
    }

    /**
     * @param point
     * Sets the coordinate along the ordinate axis of the end point of the line
     */
    public void endY(double point) {
        this.endY = point;
    }

    /**
     * @return double
     * Returns the heigth of the shape
     */
    @Override
    public double getHeight() {
        return this.getEndY();
    }

    /**
     * @return double
     * Returns the width of the shape
     */
    @Override
    public double getWidth() {
        return this.getEndX();
    }

    /**
     * @param X
     * @param Y
     * Set the size of the shape
     */
    @Override
    public void setSize(double X, double Y) {
        this.setEndX(X);
        this.setEndY(Y);
    }

    /**
     * @param points
     * Sets the coordinate of the line and inserts them in an array of double
     */
    public void setPoints(Double[] points){
        this.points = points;
    }

    /**
     * @return the custom stroke width of the line
     */
    @Override
    public int getCustomStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @return always null (lines are not fillable)
     */
    @Override
    public Color getFillColor() {
        return null;
    }


    @Override
    public Node getNode() {
        return this;
    }

    /**
     * Sets the stroke width of the line.
     *
     * @param strokeWidth the new stroke width
     */
    @Override
    public void setStrokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
    }

    /**
     * @return String
     * Prints the propeties of the shape
     */
    @Override
    public String toString() {
        return "Line" + ";" + this.getStrokeColor() + ";" + this.getEndX() + ";" + this.getEndY() + ";"
                + this.getStartX() + ";" + this.getStartY() + "\n";
    }

    @Override
    public void setFillColor(Color fill) {

    }
    /*@Override
    public void setFillColor(Color fill) {
        this.fillColor = fill;
        this.setFill(fill);  // ✅ aggiorna graficamente la figura
    }*/



}
