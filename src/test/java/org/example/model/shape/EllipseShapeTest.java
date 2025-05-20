package org.example.model.shape;

import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EllipseShapeTest {

    private EllipseShape ellipseShape;

    @BeforeEach
    void setUp() {
        ellipseShape = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0);
    }

    @Test
    void testDrawCoordinates() {
        ellipseShape.draw(100.0, 200.0);
        Ellipse ellipse = (Ellipse) ellipseShape.getNode();

        assertEquals(100.0, ellipse.getCenterX());
        assertEquals(200.0, ellipse.getCenterY());
    }

    @Test
    void testDrawWithPointsArrayUnsupported() {
        Double[] points = {100.0, 200.0};
        assertThrows(UnsupportedOperationException.class, () -> ellipseShape.draw(points));
    }

    @Test
    void testGetAndSetStrokeColor() {
        ellipseShape.setStrokeColor(Color.RED);
        assertEquals(Color.RED, ellipseShape.getStrokeColor());
    }

    @Test
    void testGetAndSetFillColor() {
        ellipseShape.setFillColor(Color.GREEN);
        assertEquals(Color.GREEN, ellipseShape.getFillColor());
    }

    @Test
    void testGetAndSetSize() {
        ellipseShape.setSize(80.0, 40.0);
        assertEquals(80.0, ellipseShape.getWidth());
        assertEquals(40.0, ellipseShape.getHeight());
    }

    @Test
    void testGetAndSetCustomStrokeWidth() {
        ellipseShape.setCustomStrokeWidth(5.0);
        assertEquals(5.0, ellipseShape.getCustomStrokeWidth());
    }

    @Test
    void testGetAndSetPosition() {
        ellipseShape.setX(120.0);
        ellipseShape.setY(140.0);
        assertEquals(120.0, ellipseShape.getX());
        assertEquals(140.0, ellipseShape.getY());
    }

    @Test
    void testToStringFormat() {
        ellipseShape.setX(10.0);
        ellipseShape.setY(20.0);
        String result = ellipseShape.toString();
        assertTrue(result.startsWith("Ellipse"));
        assertTrue(result.contains(";"));
    }
}
