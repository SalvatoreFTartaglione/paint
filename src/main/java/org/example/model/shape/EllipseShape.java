package org.example.model.shape;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;

public class EllipseShape extends Ellipse implements ShapeInterface {



    private Color strokeColor;
    private Color fillColor;
    private double coordinateX;
    private double coordinateY;
    private int strokeWidth;
    //private double angle;
    private double height;
    private double width;


    public EllipseShape(Color stroke, Color fill, /*double angle, int count,*/ double width, double height) {
        this.setSize(width, height);                    //default value
        this.strokeColor = stroke;
        this.fillColor = fill;
        //this.angle = angle;
        //this.strokeWidth = count;
    }

    @Override
    public void draw(double X, double Y) {
        //this.setStrokeWidth(this.getCount()); //default value
        this.setStroke(this.getStrokeColor());
        this.setFill(this.getFillColor());
        //this.setRotate(this.getAngle());
        this.setCenterX(X);
        this.setCenterY(Y);
        this.setX(X);
        this.setY(Y);
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
     * @return Color
     * Returns the fill color of the shape
     */
    @Override
    public Color getFillColor() {
        return this.fillColor;
    }


    /**
     * @param stroke
     * Sets the border color of the shape
     */
    @Override
    public void setStrokeColor(Color stroke) {
        this.strokeColor = stroke;
    }


    /**
     * @param fill
     * Sets the fill color of the shape
     */
    @Override
    public void setFillColor(Color fill) {
        this.fillColor = fill;

    }


    /**
     * @return double
     * Returns the coordinate along the abscissa axis
     */
    @Override
    public double getX() {
        return coordinateX;
    }


    /**
     * @return double
     * Returns the coordinate along the ordinate axis
     */
    @Override
    public double getY() {
        return coordinateY;
    }


    /**
     * @param X
     * Sets the coordinate along the abscissa axis
     */
    @Override
    public void setX(double X) {
        this.coordinateX = X;

    }


    /**
     * @param Y
     * Sets the coordinate along the ordinate axis
     */
    @Override
    public void setY(double Y) {
        this.coordinateY = Y;
    }

    /**
     * @return double
     * Returns the heigth of the shape
     */
    @Override
    public double getHeightShape() {
        return this.height;
    }


    /**
     * @return double
     * Returns the width of the shape
     */
    @Override
    public double getWidthShape() {
        return this.width;
    }


    /**
     * @param height
     * Sets the heigth of the shape
     */
    public void setHeight(double height){
        this.height = height;
    }

    /**
     * @param width
     * Sets the width of the shape
     */
    public void setWidth(double width){
        this.width = width;
    }

    public double getHeight(){
        return this.height;
    }

    public double getWidth(){
        return this.width;
    }

    /**
     * @param shape
     * @return ShapeInterface
     * Returns a copy of the shape passed as a parameter
     */
    /*
    @Override
    public ShapeInterface getAcopy(ShapeInterface shape) {
        EllipseShape newCopy = new EllipseShape(shape.getStrokeColor(), shape.getFillColor(), shape.getAngle(), shape.getCount(), shape.getWidthShape(), shape.getHeightShape());
        newCopy.draw(shape.getX(), shape.getY());
        newCopy.setSize(shape.getWidthShape(), shape.getHeightShape());
        newCopy.setCenterX(((EllipseShape)shape).getCenterX());
        newCopy.setCenterY(((EllipseShape)shape).getCenterY());
        return (ShapeInterface) newCopy;
    }
    */


    /**
     * @return double
     * Returns the rotation angle of the shape
     */
    /*@Override
    public double getAngle() {
        return this.angle;
    }*/


    /**
     * @param points
     */
    @Override
    public void draw(Double[] points) {
        // TODO
    }

    /**
     * @param width
     * @param height
     * Set the size of the shape
     */
    @Override
    public void setSize(double width, double height) {
        this.setRadiusY(height);
        this.setRadiusX(width);
        this.setHeight(height);
        this.setWidth(width);
    }


    /**
     * @param angle
     * Sets the rotation angle of the shape
     */
    /*@Override
    public void setAngle(double angle) {
        this.angle = angle;
    }
*/

    /**
     * @return String
     * Prints the properties of the shape
     */
    @Override
    public String toString() {
        return "Ellipse" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" +
                + this.getWidth() + ";" + this.getHeight() + ";" + this.getCenterX() + ";" + this.getCenterY() + "\n";
    }

}
