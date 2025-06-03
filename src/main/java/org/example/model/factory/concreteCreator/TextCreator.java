package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.factory.concreteProduct.TextShape;

public class TextCreator extends AbstractShape {

    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new TextShape(content, strokeColor, fillingColor, width, height, strokeWidth, scaleX, scaleY, angle);
    }
}
