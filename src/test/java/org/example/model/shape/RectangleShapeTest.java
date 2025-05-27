package org.example.model.shape;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class RectangleShapeTest {

    private RectangleShape rectangle;

    @BeforeEach
    public void setUp() {
        rectangle = new RectangleShape(Color.BLACK, Color.ORANGE, 100, 60, 3);
        rectangle.draw(150, 200);
    }

    @Test
    public void testInitialProperties() {
        assertEquals(Color.BLACK, rectangle.getStrokeColor());
        assertEquals(Color.ORANGE, rectangle.getFillColor());
        assertEquals(100.0, rectangle.getWidth(), 0.01);
        assertEquals(60.0, rectangle.getHeight(), 0.01);
        assertEquals(3.0, rectangle.getCustomStrokeWidth(), 0.01);
        assertEquals(150.0, rectangle.getX(), 0.01);
        assertEquals(200.0, rectangle.getY(), 0.01);
    }

    @Test
    public void testSetColors() {
        rectangle.setStrokeColor(Color.RED);
        rectangle.setFillColor(Color.YELLOW);
        assertEquals(Color.RED, rectangle.getStrokeColor());
        assertEquals(Color.YELLOW, rectangle.getFillColor());
    }

    @Test
    public void testMoveBy() {
        rectangle.moveBy(10, -20);
        assertEquals(160.0, rectangle.getX(), 0.01);
        assertEquals(180.0, rectangle.getY(), 0.01);
    }

    @Test
    public void testSetSize() {
        rectangle.setSize(200, 150);
        assertEquals(200.0, rectangle.getWidth(), 0.01);
        assertEquals(150.0, rectangle.getHeight(), 0.01);
    }

    @Test
    public void testCustomStrokeWidth() {
        rectangle.setCustomStrokeWidth(6.0);
        assertEquals(6.0, rectangle.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testCloneShape() {
        RectangleShape clone = (RectangleShape) rectangle.cloneShape();

        assertEquals(rectangle.getStrokeColor(), clone.getStrokeColor());
        assertEquals(rectangle.getFillColor(), clone.getFillColor());
        assertEquals(rectangle.getCustomStrokeWidth(), clone.getCustomStrokeWidth(), 0.01);
        assertEquals(rectangle.getWidth(), clone.getWidth(), 0.01);
        assertEquals(rectangle.getHeight(), clone.getHeight(), 0.01);
        assertEquals(rectangle.getX(), clone.getX(), 0.01);
        assertEquals(rectangle.getY(), clone.getY(), 0.01);
    }

    @Test
    public void testScaleToSize() {
        double originalMax = Math.max(rectangle.getWidth(), rectangle.getHeight());
        rectangle.scaleToSize(200);
        double newMax = Math.max(rectangle.getWidth(), rectangle.getHeight());
        assertEquals(200, newMax, 0.5);
    }

    @Test
    public void testSliderLabel() {
        String label = rectangle.getSliderLabel(123.456);
        assertEquals("Dimension: 123.5", label);
    }

    @Test
    public void testToStringFormat() {
        String str = rectangle.toString();
        assertTrue(str.startsWith("Rectangle;"));
        assertTrue(str.contains(rectangle.getStrokeColor().toString()));
        assertTrue(str.contains(rectangle.getFillColor().toString()));
    }

}
