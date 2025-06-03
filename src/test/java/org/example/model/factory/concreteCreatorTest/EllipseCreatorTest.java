package org.example.model.factory.concreteCreatorTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.EllipseCreator;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EllipseCreatorTest {

    @Test
    void createEllipseShape_withValidParameters_shouldNotBeNull() {
        EllipseCreator creator = new EllipseCreator();
        ShapeInterface shape = creator.createShape(Color.BLACK, Color.RED, 2, 100, 50, null, 1, 1, 0);
        assertNotNull(shape);
    }

    @Test
    void createEllipseShape_checkInstanceAndType() {
        EllipseCreator creator = new EllipseCreator();
        ShapeInterface shape = creator.createShape(Color.BLUE, null, 1, 60, 30, null, 1, 1, 45);
        assertEquals("EllipseShape", shape.getClass().getSimpleName());
    }
}
