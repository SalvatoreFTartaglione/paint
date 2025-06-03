package org.example.model.factory.concreteCreatorTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.LineCreator;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineCreatorTest {

    @Test
    void createLineShape_withValidParams_shouldReturnNotNull() {
        LineCreator creator = new LineCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, null, 1, 100, 0, null, 1, 1, 0);
        assertNotNull(shape);
    }

    @Test
    void createLineShape_checkType() {
        LineCreator creator = new LineCreator();
        ShapeInterface shape = creator.createShape(Color.GRAY, null, 2, 80, 0, null, 1, 1, 15);
        assertEquals("LineShape", shape.getClass().getSimpleName());
    }
}
