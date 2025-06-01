package org.example.model.shapeCreators;

import javafx.scene.paint.Color;
import org.example.model.shape.AbstractShape;
import org.example.model.shape.ShapeInterface;
import org.example.model.shape.TextShape;

public class TextCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY) {
        return new TextShape(content, strokeColor, fillingColor, width, height, strokeWidth, scaleX, scaleY);
    }
}
