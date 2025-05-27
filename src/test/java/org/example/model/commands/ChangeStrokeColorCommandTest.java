package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ChangeStrokeColorCommand.
 *
 * Verifies that:
 * 1. executeCommand() updates the stroke color of the shape.
 * 2. undoCommand() restores the previous stroke color.
 */

public class ChangeStrokeColorCommandTest {

    private ShapeInterface shape;
    private ChangesOnShapeReceiver receiver;

    @BeforeEach
    public void setUp() {
        shape = new RectangleShape(Color.BLACK, Color.LIGHTGRAY, 100, 60, 2); // stroke: BLACK, fill: LIGHTGRAY
        receiver = new ChangesOnShapeReceiver(); // può essere una classe vuota se non fa logica extra
    }

    @Test
    public void testExecuteCommandChangesStrokeColor() {
        assertEquals(Color.BLACK, shape.getStrokeColor(), "Initial stroke color should be BLACK");

        CommandInterface cmd = new ChangeStrokeColorCommand(shape, Color.DARKGREEN, receiver);
        cmd.executeCommand();

        assertEquals(Color.DARKGREEN, shape.getStrokeColor(), "Stroke color should be DARKGREEN after execution");
    }

    @Test
    public void testUndoCommandRestoresOldStrokeColor() {
        CommandInterface cmd = new ChangeStrokeColorCommand(shape, Color.DARKBLUE, receiver);
        cmd.executeCommand();
        assertEquals(Color.DARKBLUE, shape.getStrokeColor(), "Stroke color should be DARKBLUE after execution");

        cmd.undoCommand();
        assertEquals(Color.BLACK, shape.getStrokeColor(), "Stroke color should revert to BLACK after undo");
    }
}
