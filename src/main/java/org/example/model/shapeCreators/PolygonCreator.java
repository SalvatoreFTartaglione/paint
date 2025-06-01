package org.example.model.shapeCreators;

import javafx.scene.paint.Color;
import org.example.model.shape.AbstractShape;
import org.example.model.shape.PolygonShape;
import org.example.model.shape.ShapeInterface;

public class PolygonCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new PolygonShape(stroke, fill, strokeWidth, scaleX, scaleY, angle);
    }

}
