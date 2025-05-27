package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for ChangeFillColorCommand.
 *
 * Verifies that:
 * 1. executeCommand() changes the fill color of a shape.
 * 2. undoCommand() restores the original fill color.
 */

public class ChangeFillColorCommandTest {

    private ShapeInterface shape;
    private ChangesOnShapeReceiver receiver;

    @BeforeEach
    public void setUp() {
        shape = new RectangleShape(Color.BLACK, Color.BLUE, 100, 60, 2);
        receiver = new ChangesOnShapeReceiver(); // questo deve essere stateless, va bene anche finto
    }

    @Test
    public void testExecuteCommandChangesFillColor() {
        assertEquals(Color.BLUE, shape.getFillColor(), "Initial fill color should be BLUE");

        CommandInterface cmd = new ChangeFillColorCommand(shape, Color.RED, receiver);
        cmd.executeCommand();

        assertEquals(Color.RED, shape.getFillColor(), "Fill color should be RED after execution");
    }

    @Test
    public void testUndoCommandRestoresOldFillColor() {
        CommandInterface cmd = new ChangeFillColorCommand(shape, Color.YELLOW, receiver);
        cmd.executeCommand();
        assertEquals(Color.YELLOW, shape.getFillColor(), "Fill color should be YELLOW after execution");

        cmd.undoCommand();
        assertEquals(Color.BLUE, shape.getFillColor(), "Fill color should revert to BLUE after undo");
    }
}
