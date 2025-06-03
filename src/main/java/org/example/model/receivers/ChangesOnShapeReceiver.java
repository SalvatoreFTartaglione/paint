package org.example.model.receivers;

import javafx.scene.paint.Color;
import org.example.model.factory.product.ShapeInterface;

/**
 * The ChangesOnShapeReceiver class provides functionalities
 * to modify attributes of shapes, such as their stroke and fill colors.
 */
public class ChangesOnShapeReceiver {

    /**
     * Default constructor for the ChangesOnShapeReceiver class.
     * This class provides functionality for changing attributes of shapes, such as stroke color
     **/
    public ChangesOnShapeReceiver() {
    }

    /**
     * Changes the stroke color of the specified shape.
     *
     * @param shape the shape whose stroke color is to be changed
     * @param newColor the new color to set as the stroke color
     */
    public void changeStrokeColor(ShapeInterface shape, Color newColor) {
        shape.setStrokeColor(newColor);
    }

    /**
     * Changes the fill color of the specified shape.
     *
     * @param shape the shape whose fill color is to be changed
     * @param newColor the new color to set as the fill color
     */
    public void changeFillColor(ShapeInterface shape, Color newColor) {
        shape.setFillColor(newColor);
    }
}
