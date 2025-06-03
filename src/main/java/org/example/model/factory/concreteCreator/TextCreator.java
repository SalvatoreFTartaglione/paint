package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.factory.concreteProduct.TextShape;

/**
 * The TextCreator class is responsible for creating instances of TextShape.
 * This class extends AbstractShape and provides an implementation of the*/
public class TextCreator extends AbstractShape {

    /**
     * Creates a new instance of a shape with specified properties.
     *
     * @param strokeColor the color of the shape's stroke
     * @param fillingColor the color used to fill the shape
     * @param strokeWidth the width of the shape's stroke
     * @param width the width of the shape
     * @param height the height of the shape
     * @param content the textual content of the shape
     * @param scaleX the horizontal scale factor for the shape
     * @param scaleY the vertical scale factor for the shape
     * @param angle the angle of rotation for the shape, in degrees
     * @return a new instance of a shape with the specified properties
     */
    @Override
    public ShapeInterface createShape(Color strokeColor, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new TextShape(content, strokeColor, fillingColor, width, height, strokeWidth, scaleX, scaleY, angle);
    }
}
