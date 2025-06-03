package org.example.model.factory.concreteProductTest;

import javafx.scene.paint.Color;
import org.example.model.factory.concreteProduct.TextShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TextShapeTest {

    private TextShape textShape;

    @BeforeEach
    public void setup() {
        textShape = new TextShape("Hello", Color.BLACK, Color.RED, 100, 50, 16, 1.0, 1.0, 30.0);
        textShape.draw(200, 300);
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
        assertEquals(200, textShape.getX(), 0.01);
        assertEquals(300, textShape.getY(), 0.01);
    }

    @Test
    public void testMoveBy() {
        textShape.moveBy(20, 30);
        assertEquals(220, textShape.getX(), 0.01);
        assertEquals(330, textShape.getY(), 0.01);
    }

    @Test
    public void testClone() {
        TextShape clone = (TextShape) textShape.cloneShape();

        assertEquals(textShape.getText(), clone.getText());
        assertEquals(textShape.getStrokeColor(), clone.getStrokeColor());
        assertEquals(textShape.getFillColor(), clone.getFillColor());
        assertEquals(textShape.getCustomStrokeWidth(), clone.getCustomStrokeWidth(), 0.01);
        assertEquals(textShape.getX(), clone.getX(), 0.01);
        assertEquals(textShape.getY(), clone.getY(), 0.01);
        assertEquals(textShape.getAngle(), clone.getAngle(), 0.01);
    }

    @Test
    public void testStretch() {
        textShape.stretch(2.0, 3.0);
        assertEquals(2.0, textShape.getScaleX(), 0.01);
        assertEquals(3.0, textShape.getScaleY(), 0.01);
    }

    @Test
    public void testResetStretch() {
        textShape.resetStretch(1.5, 1.5);
        assertEquals(1.5, textShape.getScaleX(), 0.01);
        assertEquals(1.5, textShape.getScaleY(), 0.01);
    }

    @Test
    public void testMirrorAroundCenter() {
        double scaleX = textShape.getScaleX();
        double scaleY = textShape.getScaleY();

        textShape.mirrorHorizontallyAroundCenter();
        assertEquals(-scaleX, textShape.getScaleX(), 0.01);

        textShape.mirrorVerticallyAroundCenter();
        assertEquals(-scaleY, textShape.getScaleY(), 0.01);
    }

    @Test
    public void testAngleSetGet() {
        textShape.setAngle(75.0);
        assertEquals(75.0, textShape.getAngle(), 0.01);
    }

    @Test
    public void testToStringFormat() {
        String result = textShape.toString();
        assertTrue(result.startsWith("Text;"));
        assertTrue(result.contains("Hello"));
        assertTrue(result.contains(textShape.getStrokeColor().toString()));
        assertTrue(result.contains(textShape.getFillColor().toString()));
    }
}
