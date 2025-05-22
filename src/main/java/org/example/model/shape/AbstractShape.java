package org.example.model.shape;

import javafx.scene.paint.Color;

public abstract class AbstractShape {

    // stores a 'shape' (rectangle, ellipse, line) object created by a subclass
    private ShapeInterface shape;


    // Abstract method to create a shape that will be implemented in  the subclasses (LineCreator, RectangleCreator, EllipseCreator)
    /**
     * Creates a shape with the given properties.
     * <p>
     * This method must be implemented by subclasses to instantiate
     * specific shapes (e.g., rectangle, ellipse, line).
     *
     * @param strokeColor the border color of the shape
     * @param fillingColor the fill color (nullable for open shapes)
     * @param strokeColorWidth the stroke width
     * @param width the shape width
     * @param height the shape height
     * @return the created shape
     */
    public abstract ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeColorWidth, double width, double height);

    /**
     * Creates and stores a shape using the subclass-defined {@code createShape} method.
     *
     * @param strokeColor the border color
     * @param fillingColor the fill color (nullable)
     * @param strokeColorWidth the stroke width
     * @param width the width of the shape
     * @param height the height of the shape
     */
    // Concrete method that creates a shape
    public void creationOfShape(Color strokeColor, Color fillingColor, int strokeColorWidth, double width, double height) {
        shape = this.createShape(strokeColor, fillingColor, strokeColorWidth, width, height);
    }

    public ShapeInterface getShape() {
        return shape;
    }

}