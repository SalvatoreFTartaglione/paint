package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.concreteProduct.PolygonShape;
import org.example.model.factory.product.ShapeInterface;

public class PolygonCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new PolygonShape(stroke, fill, strokeWidth, scaleX, scaleY, angle);
    }

}
