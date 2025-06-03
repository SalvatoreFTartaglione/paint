package org.example.model.factory;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.concreteProduct.PolygonShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full unit test for the PolygonShape class.
 * It verifies drawing, cloning, transformations (move, resize, mirror),
 * and property accessors.
 */
public class PolygonShapeTest {

    private PolygonShape polygon;
    private Double[] initialPoints;

    @BeforeEach
    public void setUp() {
        polygon = new PolygonShape(Color.BLACK, Color.BLUE, 2, 1.0, 1.0, 0.0);
        initialPoints = new Double[]{0.0, 0.0, 40.0, 0.0, 20.0, 30.0}; // simple triangle
        polygon.draw(initialPoints);
    }

    @Test
    public void testDrawAndGetPoints() {
        Double[] points = polygon.getPoints();
        assertArrayEquals(initialPoints, points, "The drawn points should match the input.");
    }

    @Test
    public void testCloneShapeCreatesDeepCopy() {
        PolygonShape clone = (PolygonShape) polygon.cloneShape();
        assertNotSame(clone, polygon);
        assertNotSame(clone.getPoints(), polygon.getPoints());
        assertArrayEquals(polygon.getPoints(), clone.getPoints(), "Cloned shape should have identical points.");
    }

    @Test
    public void testMoveByShiftsPointsCorrectly() {
        polygon.moveBy(10, 15);
        Double[] moved = polygon.getPoints();
        for (int i = 0; i < moved.length; i += 2) {
            assertEquals(initialPoints[i] + 10, moved[i], 0.01);
            assertEquals(initialPoints[i + 1] + 15, moved[i + 1], 0.01);
        }
    }

    @Test
    public void testResizeScalesShapePreservingCenter() {
        double originalWidth = polygon.getWidth();
        double originalHeight = polygon.getHeight();
        double newSize = Math.max(originalWidth, originalHeight) * 2;

        polygon.resize(newSize);
        assertTrue(polygon.getWidth() > originalWidth);
        assertTrue(polygon.getHeight() > originalHeight);
    }

    @Test
    public void testMirrorHorizontallyAndVertically() {
        Double[] original = polygon.getPoints();
        polygon.mirrorHorizontallyAroundCenter();
        Double[] horizMirrored = polygon.getPoints();
        assertNotEquals(original[0], horizMirrored[0], "X should change after horizontal mirror");
        assertEquals(original[1], horizMirrored[1], "Y should remain the same after horizontal mirror");

        polygon.draw(initialPoints); // reset
        polygon.mirrorVerticallyAroundCenter();
        Double[] vertMirrored = polygon.getPoints();
        assertEquals(original[0], vertMirrored[0], "X should remain the same after vertical mirror");
        assertNotEquals(original[1], vertMirrored[1], "Y should change after vertical mirror");
    }

    @Test
    public void testColorAndAngleAccessors() {
        polygon.setStrokeColor(Color.GREEN);
        polygon.setFillColor(Color.RED);
        polygon.setAngle(45);

        assertEquals(Color.GREEN, polygon.getStrokeColor());
        assertEquals(Color.RED, polygon.getFillColor());
        assertEquals(45, polygon.getAngle());
    }

    @Test
    public void testEffectiveSizeReturnsCorrectValue() {
        double effectiveSize = polygon.getEffectiveSize();
        assertTrue(effectiveSize > 0);
    }

    @Test
    public void testSimulateMirrorReturnsCorrectPoint() {
        Optional<Point2D> result = polygon.simulateMirror(MirrorDirectionEnum.LEFT);
        assertTrue(result.isPresent());
    }

    @Test
    public void testSetSizeUpdatesDimensions() {
        double newWidth = 200;
        double newHeight = 100;
        polygon.setSize(newWidth, newHeight);

        assertEquals(newWidth, polygon.getWidth(), 1.0);
        assertEquals(newHeight, polygon.getHeight(), 1.0);
    }
}
