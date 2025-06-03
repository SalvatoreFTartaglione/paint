package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.concreteProduct.PolygonShape;
import org.example.model.factory.product.ShapeInterface;

/**
 * PolygonCreator is a concrete subclass of AbstractShape responsible for creating polygon shapes.
 * This class implements the abstract*/
public class PolygonCreator extends AbstractShape {

    /**
     * Creates a new polygon shape with the specified parameters.
     *
     * @param stroke the stroke color of the shape
     * @param fill the fill color of the shape
     * @param strokeWidth the width of the stroke
     * @param width the width of the shape
     * @param height the height of the shape
     * @param content the textual content or label for the shape
     * @param scaleX the horizontal scaling factor
     * @param scaleY the vertical scaling factor
     * @param angle the rotation angle of the shape in degrees
     * @return a new instance of a polygon shape with the specified attributes
     */
    @Override
    public ShapeInterface createShape(Color stroke, Color fill, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new PolygonShape(stroke, fill, strokeWidth, scaleX, scaleY, angle);
    }

}
