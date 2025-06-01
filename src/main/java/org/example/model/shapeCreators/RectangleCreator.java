package org.example.model.shapeCreators;

import javafx.scene.paint.Color;
import org.example.model.shape.AbstractShape;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;

public class RectangleCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new RectangleShape(stroke, fill, width, height, strokeWidth, angle);
    }



}