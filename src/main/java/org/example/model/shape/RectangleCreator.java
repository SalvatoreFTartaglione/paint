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
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY) {
        return new RectangleShape(stroke, fill, width, height, strokeWidth);
    }






}