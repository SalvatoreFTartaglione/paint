package org.example.model.commands.receivers;

import javafx.scene.paint.Color;
import org.example.model.factory.product.ShapeInterface;

public class ChangesOnShapeReceiver {

    public ChangesOnShapeReceiver() {
    }

    public void changeStrokeColor(ShapeInterface shape, Color newColor) {
        shape.setStrokeColor(newColor);
    }

    public void changeFillColor(ShapeInterface shape, Color newColor) {
        shape.setFillColor(newColor);
    }
}
