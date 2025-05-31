package org.example.model.shape;

import javafx.scene.paint.Color;

public class EllipseCreator extends AbstractShape{


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
    public ShapeInterface createShape(Color stroke, Color fillingColor, int strokeWidth, double width, double height, String content, double scaleX, double scaleY) {
        return new EllipseShape(stroke, fillingColor, strokeWidth, width, height);
    }

}
