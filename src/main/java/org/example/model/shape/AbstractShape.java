package org.example.model.shape;
import javafx.scene.paint.Color;

public abstract class AbstractShape {

    // stores a 'shape' (rectangle, ellipse, line) object created by a subclass
    private ShapeInterface shape;

    // Abstract method to create a shape that will be implemented in  the subclasses (LineCreator, RectangleCreator, EllipseCreator)
    public abstract ShapeInterface createShape(Color stroke, Color fill, /*double angle, String text, int count,*/ double width, double height);

    /**
     * @param stroke
     * @param fill
     * @param angle
     * @param text
     * @param count
     * Create a generic Shape by setting all the properties passed as a parameter
     */
    // Concrete method that creates a shape
    public void creationOfShape(Color stroke, Color fill, double angle, String text, int count, double width, double height) {
        shape = this.createShape(stroke, fill, /*angle, text, count,*/ width, height);
        shape.setStrokeColor(stroke);
        shape.setFillColor(fill);
        /*shape.setAngle(angle);
        shape.setString(text);
        shape.setCount(count);*/
        shape.setSize(width, height);
    }

    /**
     * @return ShapeInterface
     * Return an object of type shape
     */
    // Returns the created shape
    public ShapeInterface getShape(){
        return this.shape;
    }




}