package org.example.model.shape;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class EllipseShapeTest {

    private EllipseShape ellipse;

    @BeforeEach
    public void setUp() {
        ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0);
        ellipse.draw(100.0, 200.0); // posiziona al centro
    }

    @Test
    public void testInitialProperties() {
        assertEquals(Color.BLACK, ellipse.getStrokeColor());
        assertEquals(Color.BLUE, ellipse.getFillColor());
        assertEquals(2.0, ellipse.getCustomStrokeWidth(), 0.01);
        assertEquals(50.0, ellipse.getWidth(), 0.01);
        assertEquals(30.0, ellipse.getHeight(), 0.01);
        assertEquals(100.0, ellipse.getX(), 0.01);
        assertEquals(200.0, ellipse.getY(), 0.01);
    }

    @Test
    public void testSetColors() {
        ellipse.setStrokeColor(Color.RED);
        ellipse.setFillColor(Color.YELLOW);

        assertEquals(Color.RED, ellipse.getStrokeColor());
        assertEquals(Color.YELLOW, ellipse.getFillColor());
    }

    @Test
    public void testMoveBy() {
        ellipse.moveBy(10, -20);
        assertEquals(110.0, ellipse.getX(), 0.01);
        assertEquals(180.0, ellipse.getY(), 0.01);
    }

    @Test
    public void testSetSize() {
        ellipse.setSize(70.0, 40.0);
        assertEquals(70.0, ellipse.getWidth(), 0.01);
        assertEquals(40.0, ellipse.getHeight(), 0.01);
    }

    @Test
    public void testCustomStrokeWidth() {
        ellipse.setCustomStrokeWidth(5.0);
        assertEquals(5.0, ellipse.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testCloneShape() {
        ellipse.setX(120.0);
        ellipse.setY(150.0);
        ellipse.setSize(60.0, 45.0);

        EllipseShape clone = (EllipseShape) ellipse.cloneShape();

        assertEquals(ellipse.getStrokeColor(), clone.getStrokeColor());
        assertEquals(ellipse.getFillColor(), clone.getFillColor());
        assertEquals(ellipse.getCustomStrokeWidth(), clone.getCustomStrokeWidth(), 0.01);
        assertEquals(ellipse.getWidth(), clone.getWidth(), 0.01);
        assertEquals(ellipse.getHeight(), clone.getHeight(), 0.01);
        // Nota: cloneShape non copia posizione
    }

    @Test
    public void testScaleToSize() {
        double oldMax = Math.max(ellipse.getWidth(), ellipse.getHeight());
        ellipse.scaleToSize(100);
        double newMax = Math.max(ellipse.getWidth(), ellipse.getHeight());
        assertEquals(100, newMax, 0.1);
    }

    @Test
    public void testToStringFormat() {
        String result = ellipse.toString();
        assertTrue(result.startsWith("Ellipse;"));
        assertTrue(result.contains(ellipse.getStrokeColor().toString()));
        assertTrue(result.contains(ellipse.getFillColor().toString()));
    }

}
