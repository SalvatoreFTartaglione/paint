package org.example.model.shape;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LineShapeTest {

    private LineShape line;

    @BeforeEach
    public void setUp() {
        line = new LineShape(Color.BLACK, 2.0, 0.0);
        Double[] points = {50.0, 100.0, 150.0, 200.0}; // X1, Y1, X2, Y2
        line.draw(points);
    }

    @Test
    public void testInitialPoints() {
        Double[] pts = line.getPoints();
        assertNotNull(pts);
        assertEquals(4, pts.length);
        assertEquals(50.0, pts[0], 0.01);
        assertEquals(100.0, pts[1], 0.01);
        assertEquals(150.0, pts[2], 0.01);
        assertEquals(200.0, pts[3], 0.01);
    }

    @Test
    public void testStrokeColor() {
        assertEquals(Color.BLACK, line.getStrokeColor());
    }

    @Test
    public void testStrokeWidth() {
        assertEquals(2.0, line.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testMoveBy() {
        Double[] original = line.getPoints();
        line.moveBy(10, 20);
        Double[] moved = line.getPoints();

        assertEquals(original[0] + 10, moved[0], 0.01);
        assertEquals(original[1] + 20, moved[1], 0.01);
        assertEquals(original[2] + 10, moved[2], 0.01);
        assertEquals(original[3] + 20, moved[3], 0.01);
    }

    @Test
    public void testCloneShape() {
        LineShape clone = (LineShape) line.cloneShape();
        Double[] original = line.getPoints();
        Double[] copied = clone.getPoints();

        assertArrayEquals(original, copied);
        assertEquals(line.getStrokeColor(), clone.getStrokeColor());
        assertEquals(line.getCustomStrokeWidth(), clone.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testToStringFormat() {
        String output = line.toString();
        assertTrue(output.startsWith("Line;"));
        assertTrue(output.contains(line.getStrokeColor().toString()));
    }

    @Test
    public void testResizePreservesOrientation() {
        double oldLength = Math.hypot(line.getWidth(), line.getHeight());
        line.resize(oldLength * 2);
        double newLength = Math.hypot(line.getWidth(), line.getHeight());
        assertEquals(oldLength * 2, newLength, 0.1);
    }

    @Test
    public void testStretch() {
        line.stretch(120.0, 5.0);
        double length = Math.hypot(line.getWidth(), line.getHeight());
        assertEquals(120.0, length, 1.0);
        assertEquals(5.0, line.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testResetStretch() {
        line.resetStretch(100.0, 3.0);
        double length = Math.hypot(line.getWidth(), line.getHeight());
        assertEquals(100.0, length, 1.0);
        assertEquals(3.0, line.getCustomStrokeWidth(), 0.01);
    }

    @Test
    public void testMirrorHorizontallyAroundCenter() {
        double centerY = (line.getPoints()[1] + line.getPoints()[3]) / 2;
        line.mirrorHorizontallyAroundCenter();
        Double[] mirrored = line.getPoints();
        double newCenterY = (mirrored[1] + mirrored[3]) / 2;
        assertEquals(centerY, newCenterY, 0.01);
    }

    @Test
    public void testMirrorVerticallyAroundCenter() {
        double centerX = (line.getPoints()[0] + line.getPoints()[2]) / 2;
        line.mirrorVerticallyAroundCenter();
        Double[] mirrored = line.getPoints();
        double newCenterX = (mirrored[0] + mirrored[2]) / 2;
        assertEquals(centerX, newCenterX, 0.01);
    }

    @Test
    public void testAngleSetGet() {
        line.setAngle(45.0);
        assertEquals(45.0, line.getAngle(), 0.01);
    }
}
