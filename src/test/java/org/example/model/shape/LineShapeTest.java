package org.example.model.shape;

import javafx.embed.swing.JFXPanel;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LineShapeTest {

    private LineShape lineShape;

    @BeforeEach
    void setUp() {
        // Inizializza JavaFX (necessario per test JavaFX)
        new JFXPanel();  // evita errori headless
        lineShape = new LineShape(Color.BLACK, 2.0);
    }

    @Test
    void testDrawWithPoints() {
        Double[] points = {10.0, 20.0, 30.0, 40.0};
        lineShape.draw(points);

        Line line = (Line) lineShape.getNode();

        assertEquals(10.0, line.getStartX());
        assertEquals(20.0, line.getStartY());
        assertEquals(30.0, line.getEndX());
        assertEquals(40.0, line.getEndY());
    }

    @Test
    void testGetStrokeColor() {
        assertEquals(Color.BLACK, lineShape.getStrokeColor());
    }

    @Test
    void testSetStrokeColor() {
        lineShape.setStrokeColor(Color.BLUE);
        assertEquals(Color.BLUE, lineShape.getStrokeColor());
    }

    @Test
    void testGetXAndY() {
        lineShape.draw(new Double[]{5.0, 6.0, 15.0, 16.0});
        assertEquals(5.0, lineShape.getX());
        assertEquals(6.0, lineShape.getY());
    }

    @Test
    void testSetXAndY() {
        lineShape.setX(7.0);
        lineShape.setY(8.0);
        assertEquals(7.0, lineShape.getX());
        assertEquals(8.0, lineShape.getY());
    }

    @Test
    void testSetFillColorUnsupported() {
        assertThrows(UnsupportedOperationException.class, () -> lineShape.setFillColor(Color.RED));
    }

    @Test
    void testFindShapeAt() {
        lineShape.draw(new Double[]{0.0, 0.0, 100.0, 100.0});
        assertNotNull(lineShape.findShapeAt(50, 50));
        assertNull(lineShape.findShapeAt(300, 300));
    }

    @Test
    void testGetCustomStrokeWidth() {
        assertEquals(2.0, lineShape.getCustomStrokeWidth());
    }

    @Test
    void testGetNodeReturnsLine() {
        assertTrue(lineShape.getNode() instanceof Line);
    }

}
