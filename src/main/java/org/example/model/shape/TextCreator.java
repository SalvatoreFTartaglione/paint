package org.example.model.shape;

import javafx.scene.paint.Color;

public class TextCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content) {
        return new TextShape(content, strokeColor, fillingColor, width, height, strokeWidth);
    }
}
