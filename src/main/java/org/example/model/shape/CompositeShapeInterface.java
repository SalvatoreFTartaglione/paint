package org.example.model.shape;

import java.util.List;

public interface CompositeShapeInterface extends ShapeInterface {
    void addChild(ShapeInterface child);
    void removeChild(ShapeInterface child);
    List<ShapeInterface> getChildren();
}