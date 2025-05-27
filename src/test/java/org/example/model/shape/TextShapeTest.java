package org.example.model.shape;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextShapeTest {

    private TextShape textShape;

    @BeforeEach
    public void setup() {
        textShape = new TextShape("Hello", Color.BLACK, Color.RED, 100, 50, 16);
    }

    @Test
    public void testTextContentIsSetCorrectly() {
        assertEquals("Hello", textShape.getText());
    }

    @Test
    public void testStrokeColor() {
        assertEquals(Color.BLACK, textShape.getStrokeColor());

        textShape.setStrokeColor(Color.BLUE);
        assertEquals(Color.BLUE, textShape.getStrokeColor());
    }

    @Test
    public void testFillColor() {
        assertEquals(Color.RED, textShape.getFillColor());

        textShape.setFillColor(Color.GREEN);
        assertEquals(Color.GREEN, textShape.getFillColor());
    }

    @Test
    public void testFontSize() {
        assertEquals(16, textShape.getCustomStrokeWidth(), 0.01);

        textShape.setCustomStrokeWidth(24);
        assertEquals(24, textShape.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testPositioning() {
        textShape.draw(200, 300);
        assertEquals(200, textShape.getX(), 0.01);
        assertEquals(300, textShape.getY(), 0.01);
    }

    @Test
    public void testMoveBy() {
        textShape.draw(50, 50);
        textShape.moveBy(20, 30);

        assertEquals(70, textShape.getX(), 0.01);
        assertEquals(80, textShape.getY(), 0.01);
    }

    @Test
    public void testClone() {
        textShape.draw(100, 100);
        TextShape clone = (TextShape) textShape.cloneShape();

        assertEquals(textShape.getText(), clone.getText());
        assertEquals(textShape.getStrokeColor(), clone.getStrokeColor());
        assertEquals(textShape.getFillColor(), clone.getFillColor());
        assertEquals(textShape.getCustomStrokeWidth(), clone.getCustomStrokeWidth(), 0.01);
        assertEquals(textShape.getX(), clone.getX(), 0.01);
        assertEquals(textShape.getY(), clone.getY(), 0.01);
    }

//    @Test
//    public void testSetSizeScalesText() {
//        double originalWidth = textShape.getWidth();
//        double originalHeight = textShape.getHeight();
//
//        textShape.setSize(originalWidth * 2, originalHeight * 2);
//
//        assertTrue(textShape.getWidth() > originalWidth);
//        assertTrue(textShape.getHeight() > originalHeight);
//    }
}
