package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.example.model.factory.product.ShapeInterface;

/**
 * RectangleCreator is a concrete implementation of AbstractShape.
 * It is responsible for creating rectangle shapes with specific properties.
 * This class overrides the createShape method defined in the AbstractShape
 * to provide specific behavior for rectangle*/
public class RectangleCreator extends AbstractShape {

    /**
     * Creates a rectangle shape with the specified properties.
     *
     * @param stroke the color to be used for the stroke of the shape
     * @param fill the color to be used to fill the shape
     * @param strokeWidth the width of the stroke
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param content the content or label associated with the shape
     * @param scaleX the horizontal scaling factor for the rectangle
     * @param scaleY the vertical scaling factor for the rectangle
     * @param angle the rotation angle for the rectangle in degrees
     * @return a RectangleShape object initialized with the specified properties
     */
    @Override
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new RectangleShape(stroke, fill, width, height, strokeWidth, angle);
    }



}