package org.example.model.shape;

import javafx.scene.paint.Color;

public class RectangleCreator extends AbstractShape {


    /**
     * @param stroke
     * @param fill
     * @return ShapeInterface
     * Create a new shape of type rectangle by setting all the properties passed as a parameter
     */
    @Override
    public ShapeInterface createShape(Color stroke, Color fill, /*double angle, String text,*/ int strokeWidth, double width, double height) {
        return new RectangleShape(stroke, fill, /*angle, count,*/ width, height);
    }




}