package org.example.model.shape;

import javafx.embed.swing.JFXPanel;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RectangleShapeTest {

    private RectangleShape rectangleShape;

    @BeforeEach
    void setUp() {
        // Inizializza JavaFX Toolkit
        new JFXPanel();
        rectangleShape = new RectangleShape(Color.RED, Color.BLUE, 3.0, 50.0, (int) 3.0);
    }

    @Test
    void testDrawWithCoordinates() {
        rectangleShape.draw(100.0, 200.0);
        Rectangle rect = (Rectangle) rectangleShape.getNode();

        assertEquals(100.0, rect.getX());
        assertEquals(200.0, rect.getY());
    }

    @Test
    void testDrawWithPointsArray() {
        Double[] points = {150.0, 250.0};

        assertThrows(UnsupportedOperationException.class, () -> rectangleShape.draw(points));
    }


    @Test
    void testGetStrokeColor() {
        assertEquals(Color.RED, rectangleShape.getStrokeColor());
    }

    @Test
    void testGetFillColor() {
        assertEquals(Color.BLUE, rectangleShape.getFillColor());
    }

    @Test
    void testSetStrokeColor() {
        rectangleShape.setStrokeColor(Color.GREEN);
        assertEquals(Color.GREEN, rectangleShape.getStrokeColor());
    }

    @Test
    void testSetFillColor() {
        rectangleShape.setFillColor(Color.YELLOW);
        assertEquals(Color.YELLOW, rectangleShape.getFillColor());
    }

    @Test
    void testSetXAndY() {
        rectangleShape.setX(60.0);
        rectangleShape.setY(70.0);
        assertEquals(60.0, rectangleShape.getX());
        assertEquals(70.0, rectangleShape.getY());
    }

    @Test
    void testSetSize() {
        rectangleShape.setSize(120.0, 90.0);
        assertEquals(120.0, rectangleShape.getWidth());
        assertEquals(90.0, rectangleShape.getHeight());
    }

    @Test
    void testGetCustomStrokeWidth() {
        assertEquals(3.0, rectangleShape.getCustomStrokeWidth());
    }

    @Test
    void testSetCustomStrokeWidth() {
        rectangleShape.setCustomStrokeWidth(5.0);
        assertEquals(5.0, rectangleShape.getCustomStrokeWidth());
    }

    @Test
    void testFindShapeAtInside() {
        rectangleShape.draw(10, 10);
        assertNotNull(rectangleShape.findShapeAt(10, 10));
    }

    @Test
    void testFindShapeAtOutside() {
        rectangleShape.draw(10, 10);
        assertNull(rectangleShape.findShapeAt(500, 500));
    }

    @Test
    void testGetNodeReturnsRectangle() {
        Node node = rectangleShape.getNode();
        assertNotNull(node);
        assertTrue(node instanceof Rectangle);
    }
}
