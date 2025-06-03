package org.example.model.factory.concreteCreatorTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.RectangleCreator;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleCreatorTest {

    @Test
    void createRectangleShape_shouldReturnNonNull() {
        RectangleCreator creator = new RectangleCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, Color.GREEN, 3, 200, 100, null, 1, 1, 0);
        assertNotNull(shape);
    }

    @Test
    void createRectangleShape_checkType() {
        RectangleCreator creator = new RectangleCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, null, 1, 120, 60, null, 1, 1, 90);
        assertEquals("RectangleShape", shape.getClass().getSimpleName());
    }
}
