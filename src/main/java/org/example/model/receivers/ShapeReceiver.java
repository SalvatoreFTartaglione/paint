package org.example.model.receivers;

import javafx.scene.shape.Shape;
import org.example.model.shape.ShapeInterface;

public class ShapeReceiver {
    private ShapeInterface shape;

    public void copy(ShapeInterface shape) {
        this.shape = shape.cloneShape();
    }

    public ShapeInterface getCopy() {
        return shape == null ? null : shape.cloneShape();
    }

    public void clear() {
        shape = null;
    }

    public boolean hasShape() {
        return shape != null;
    }
}
