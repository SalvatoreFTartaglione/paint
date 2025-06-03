package org.example.model.factory.concreteCreatorTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.TextCreator;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TextCreatorTest {

    @Test
    void createTextShape_shouldReturnShapeWithContent() {
        TextCreator creator = new TextCreator();
        String content = "Hello";
        ShapeInterface shape = creator.createShape(Color.BLACK, Color.WHITE, 1, 100, 50, content, 1, 1, 0);
        assertNotNull(shape);
    }

    @Test
    void createTextShape_checkType() {
        TextCreator creator = new TextCreator();
        ShapeInterface shape = creator.createShape(Color.BLUE, Color.TRANSPARENT, 1, 80, 40, "Sample", 1, 1, 0);
        assertEquals("TextShape", shape.getClass().getSimpleName());
    }
}
