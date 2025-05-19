package org.example.model.shape;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public abstract class AbstractShape {

    // stores a 'shape' (rectangle, ellipse, line) object created by a subclass
    private ShapeInterface shape;


    // Abstract method to create a shape that will be implemented in  the subclasses (LineCreator, RectangleCreator, EllipseCreator)
    public abstract ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeColorWidth, double width, double height);

    /**
     * @param strokeColor
     * @param fillingColor
     * @param strokeColorWidth
     * Create a generic Shape by setting all the properties passed as a parameter
     */
    // Concrete method that creates a shape
    public void creationOfShape(Color stroke, Color fill, /*double angle, String text,*/ int strokeWidth, double width, double height) {
        shape = this.createShape(stroke, fill, /*angle, text,*/ strokeWidth, width, height);
        /*shape.setStrokeColor(stroke);
        shape.setFillColor(fill);
        shape.setStrokeWidth(strokeWidth);
        shape.setSize(width, height);*/
    }

    /**
     * @return ShapeInterface
     * Return an object of type shape
     */
    public ShapeInterface getShape(){
        return this.shape;
    }

}