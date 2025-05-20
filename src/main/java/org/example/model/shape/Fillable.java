package org.example.model.shape;

import javafx.scene.paint.Color;

public interface Fillable extends ShapeInterface{

    void setFillColor(Color fill);
    Color getFillColor();
}
