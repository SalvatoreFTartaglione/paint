package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for BringForwardCommand using real shapes, receiver, and pane.
 *
 * This test verifies that:
 * 1. executeCommand() brings a shape to the front of the pane.
 * 2. undoCommand() restores the original Z-order.
 * 3. An IllegalStateException is thrown if the node is not in a Pane.
 */

public class BringForwardCommandTest {

    private Pane drawingPane;
    private ShapeInterface shape1;
    private ShapeInterface shape2;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();

        // Due rettangoli sovrapposti, shape1 è sotto, shape2 sopra
        shape1 = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2);
        shape2 = new RectangleShape(Color.BLACK, Color.BLUE, 100, 60, 2);

        shape1.draw(100, 100);
        shape2.draw(100, 100);

        drawingPane.getChildren().addAll(shape1.getNode(), shape2.getNode());
    }

    @Test
    public void testExecuteCommandBringsShapeToFront() {
        assertEquals(shape1.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shape2.getNode(), drawingPane.getChildren().get(1));

        // shape1 in basso, portiamola sopra
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape1);
        BringForwardCommand cmd = new BringForwardCommand(receiver);

        cmd.executeCommand();

        assertEquals(shape2.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shape1.getNode(), drawingPane.getChildren().get(1));
    }

    @Test
    public void testUndoCommandRestoresOriginalPosition() {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape1);
        BringForwardCommand cmd = new BringForwardCommand(receiver);

        cmd.executeCommand(); // porta in alto
        cmd.undoCommand();    // torna nella posizione originale

        assertEquals(shape1.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shape2.getNode(), drawingPane.getChildren().get(1));
    }

    @Test
    public void testExecuteThrowsIfNodeHasNoParent() {
        // Crea una shape non ancora aggiunta a un Pane
        ShapeInterface unboundShape = new RectangleShape(Color.BLACK, Color.GREEN, 100, 60, 2);
        PositionManagerReceiver receiver = new PositionManagerReceiver(unboundShape);
        BringForwardCommand cmd = new BringForwardCommand(receiver);

        Exception exception = assertThrows(IllegalStateException.class, cmd::executeCommand);
        assertTrue(exception.getMessage().contains("Node does not have a Pane parent"));
    }
}
