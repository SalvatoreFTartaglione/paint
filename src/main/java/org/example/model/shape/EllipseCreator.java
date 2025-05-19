package org.example.model.shape;

import javafx.scene.paint.Color;

public class EllipseCreator extends AbstractShape{


    /**
     * @param stroke
     * @param fillingColor
     * @return ShapeInterface
     * Create a new shape of type ellipse by setting all the properties passed as a parameter
     */
    @Override
    public ShapeInterface createShape(Color stroke, Color fillingColor, int strokeWidth, double width, double height) {
        return new EllipseShape(stroke, fillingColor, strokeWidth, width, height);
    }

}
