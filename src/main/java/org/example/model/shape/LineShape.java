package org.example.model.shape;

import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.example.model.commands.UndoManager;


public class LineShape implements ShapeInterface{

    private Line lineShape;
    private Double[] points;


    public LineShape(Color stroke, double strokeWidth) {
        lineShape = new Line();
        lineShape.setStroke(stroke);
        lineShape.setStrokeWidth(strokeWidth);
    }


    @Override
    public void draw(double X, double Y) {
        lineShape.setStartX(this.points[0]);
        lineShape.setStartY(this.points[1]);
        lineShape.setEndX(this.points[2]);
        lineShape.setEndY(this.points[3]);

        this.points[0] = X;
        this.points[1] = Y;
        this.points[2] = X + (lineShape.getEndX() - lineShape.getStartX());
        this.points[3] = Y + (lineShape.getEndY() - lineShape.getStartY());

        lineShape.setStartX(this.points[0]);
        lineShape.setStartY(this.points[1]);
        lineShape.setEndX(this.points[2]);
        lineShape.setEndY(this.points[3]);
    }


    /**
     * @param points
     * It assigns to the selected shape (in this case the line) the properties obtained thanks to the "set" methods
     * and the coordinates passed as a parameter
     */
    @Override
    public void draw(Double[] points) {
        lineShape.setStartX(points[0]);
        lineShape.setStartY(points[1]);
        lineShape.setEndX(points[2]);
        lineShape.setEndY(points[3]);
        this.setPoints(points);
    }

    @Override
    public Double[] getPoints(){
        return this.points;
    }

    public void moveBy(double deltaX, double deltaY) {
        // Moves both points of the line
        lineShape.setStartX(lineShape.getStartX() + deltaX);
        lineShape.setStartY(lineShape.getStartY() + deltaY);
        lineShape.setEndX(lineShape.getEndX() + deltaX);
        lineShape.setEndY(lineShape.getEndY() + deltaY);
    }

    @Override
    public ShapeInterface cloneShape() {
        LineShape cloned = new LineShape(this.getStrokeColor(), this.getCustomStrokeWidth());

        if (this.points != null && this.points.length == 4) {
            // Copia profonda dell'array dei punti
            Double[] copiedPoints = new Double[] {
                    this.points[0], this.points[1],
                    this.points[2], this.points[3]
            };

            cloned.draw(copiedPoints);  // inizializza il nodo grafico e setta anche cloned.points
        }

        return cloned;
    }

    @Override
    public Color getStrokeColor() {
        return (Color) lineShape.getStroke();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {

    }

    @Override
    public void setStrokeColor(Color stroke) {
        lineShape.setStroke(stroke);
    }

    /**
     * @return double
     * Returns the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public double getX() {
        return lineShape.getStartX();
    }

    /**
     * @param point
     * Sets the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public void setX(double point) {
        lineShape.setStartX(point);

    }

    /**
     * @return double
     * Returns the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public double getY() {
        return lineShape.getStartY();
    }

    /**
     * @param point
     * Sets the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public void setY(double point) {
        lineShape.setStartY(point);

    }

    @Override
    public double getWidth() {
        return lineShape.getEndX() - lineShape.getStartX();
    }

    @Override
    public double getHeight() {
        return lineShape.getEndY() - lineShape.getStartY();
    }
    /**
     * @param x
     * @param y
     * Set the size of the shape
     */
    @Override
    public void setSize(double x, double y) {
        lineShape.setStartX(x);
        lineShape.setStartY(y);
    }

    @Override
    public double getCustomStrokeWidth() {
        return lineShape.getStrokeWidth();
    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void setFillColor(Color fill) {
        throw new UnsupportedOperationException("The following feature is not supported");
    }

    /**
     * @return String
     * Prints the propeties of the shape
     */
    @Override
    public String toString() {
        return "Line" + ";" + lineShape.getStroke() + ";" + lineShape.getEndX() + ";" + lineShape.getEndY() + ";"
                + lineShape.getStartX() + ";" + lineShape.getStartY() + ";" + lineShape.getStrokeWidth()+ "\n";
    }

    @Override
    public Node getNode() {
        return this.lineShape;
    }

    /**
     * @param points
     * Sets the coordinate of the line and inserts them in an array of double
     */
    private void setPoints(Double[] points){
        this.points = points;
    }



}
