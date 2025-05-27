package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.PoistionManagerReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for ResizeCommand using real implementations of ShapeInterface and PoistionManagerReceiver.
 *
 * This test verifies that:
 * 1. The shape is resized correctly when executeCommand() is called.
 * 2. The original size is restored when undoCommand() is called.
 */
public class ResizeCommandTest {

    private ShapeInterface shape;
    private PoistionManagerReceiver receiver;
    private ResizeCommand command;
    private double originalWidth;
    private double originalHeight;
    private double newWidth = 200;
    private double newHeight = 150;

    @BeforeEach
    public void setUp() {
        shape = new RectangleShape(Color.BLACK, Color.GREEN, 100, 60, 2);
        shape.draw(100, 100);

        receiver = new PoistionManagerReceiver(shape);
        originalWidth = shape.getWidth();
        originalHeight = shape.getHeight();

        command = new ResizeCommand(receiver, newWidth, newHeight, originalWidth, originalHeight);
    }

    @Test
    public void testExecuteCommandChangesSize() {
        command.executeCommand();

        assertEquals(newWidth, shape.getWidth(), 0.01);
        assertEquals(newHeight, shape.getHeight(), 0.01);
    }

    @Test
    public void testUndoCommandRestoresOriginalSize() {
        command.executeCommand();
        command.undoCommand();

        assertEquals(originalWidth, shape.getWidth(), 0.01);
        assertEquals(originalHeight, shape.getHeight(), 0.01);
    }
}
