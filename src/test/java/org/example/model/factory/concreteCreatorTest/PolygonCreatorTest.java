package org.example.model.factory.concreteCreatorTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.PolygonCreator;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PolygonCreatorTest {

    @Test
    void createPolygonShape_shouldReturnValidObject() {
        PolygonCreator creator = new PolygonCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, Color.YELLOW, 2, 0, 0, null, 1, 1, 0);
        assertNotNull(shape);
    }

    @Test
    void createPolygonShape_checkClassName() {
        PolygonCreator creator = new PolygonCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, Color.YELLOW, 2, 0, 0, null, 1, 1, 30);
        assertEquals("PolygonShape", shape.getClass().getSimpleName());
    }
}
