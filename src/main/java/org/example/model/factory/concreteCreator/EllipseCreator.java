package org.example.model.factory.concreteCreator;

import javafx.scene.paint.Color;
import org.example.model.factory.creator.AbstractShape;
import org.example.model.factory.concreteProduct.EllipseShape;
import org.example.model.factory.product.ShapeInterface;

/**
 * Implementation of the {@code AbstractShape} abstract class for creating ellipse shapes.
 * This class specifically focuses on generating instances of {@code EllipseShape},
 * a type of geometrical shape with defined horizontal and vertical radii.
 */
public class EllipseCreator extends AbstractShape {


    /**
     * Creates an {@code EllipseShape} with the specified properties.
     *
     * @param stroke the border color of the ellipse
     * @param fillingColor the fill color of the ellipse (can be {@code null})
     * @param strokeWidth the width of the border
     * @param width the horizontal radius of the ellipse
     * @param height the vertical radius of the ellipse
     * @return a new instance of {@code EllipseShape}
     */
    @Override
    public ShapeInterface createShape(Color stroke, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY, double angle) {
        return new EllipseShape(stroke, fillingColor, strokeWidth, width, height, angle);
    }

}
