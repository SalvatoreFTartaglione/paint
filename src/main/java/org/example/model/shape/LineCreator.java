package org.example.model.shape;

import javafx.scene.paint.Color;

public class LineCreator extends AbstractShape{


    /**
     * @param strokeColor
     * @param fillingColor
     * @return ShapeInterface
     * Create a new shape of type line by setting all the properties passed as a parameter
     */
    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content) {
        return new LineShape(strokeColor, strokeWidth);
    }

}