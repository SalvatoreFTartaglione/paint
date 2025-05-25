package org.example.model.shape;

import javafx.scene.paint.Color;

public class TextShapeCreator extends AbstractShape {
    private final String content;

    public TextShapeCreator(String content) {
        this.content = content;
    }

    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height) {
        return new TextShape(content, strokeColor, width);
    }
}
