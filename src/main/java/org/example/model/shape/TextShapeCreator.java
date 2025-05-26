package org.example.model.shape;

import javafx.scene.paint.Color;

public class TextShapeCreator extends AbstractShape {
    private final String content;
    private int fontSize;

    public TextShapeCreator(String content, int fontSize) {
        this.content = content;
        this.fontSize = fontSize;
    }

    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height) {
        return new TextShape(content, strokeColor, fontSize);
    }
}
