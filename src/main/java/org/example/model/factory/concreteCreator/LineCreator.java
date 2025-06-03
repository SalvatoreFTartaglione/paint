package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.concreteProduct.LineShape;
import org.example.model.factory.product.ShapeInterface;

public class LineCreator extends AbstractShape {


    /**
     * @param strokeColor
     * @param fillingColor
     * @return ShapeInterface
     * Create a new shape of type line by setting all the properties passed as a parameter
     */
    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new LineShape(strokeColor, strokeWidth, angle);
    }

}