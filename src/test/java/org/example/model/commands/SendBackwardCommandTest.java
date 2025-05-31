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
 * Integration test for SendBackwardCommand using real Pane, ShapeInterface, and PositionManagerReceiver.
 *
 * This test verifies that:
 * 1. executeCommand() sends the node to the back of the pane.
 * 2. undoCommand() restores the node to its original position.
 */
public class SendBackwardCommandTest {

    private Pane drawingPane;
    private ShapeInterface shapeFront;
    private ShapeInterface shapeBack;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();

        // Create two overlapping shapes
        shapeBack = new RectangleShape(Color.BLACK, Color.ORANGE, 100, 60, 2);
        shapeFront = new RectangleShape(Color.BLACK, Color.LIGHTBLUE, 100, 60, 2);

        shapeBack.draw(100, 100);
        shapeFront.draw(100, 100);

        // Add them in order: back first, then front
        drawingPane.getChildren().addAll(shapeBack.getNode(), shapeFront.getNode());
    }

    @Test
    public void testExecuteCommandSendsShapeToBack() {
        // Pre-condition: shapeFront is on top
        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(1));

        PositionManagerReceiver receiver = new PositionManagerReceiver(shapeFront);
        SendBackwardCommand command = new SendBackwardCommand(receiver);

        command.executeCommand();

        // After sending backward, shapeFront should be at index 0
        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(1));
    }

    @Test
    public void testUndoCommandRestoresOriginalOrder() {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shapeFront);
        SendBackwardCommand command = new SendBackwardCommand(receiver);

        command.executeCommand();
        command.undoCommand();

        // Should return to original order
        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(1));
    }

    @Test
    public void testExecuteCommandThrowsIfNodeHasNoPaneParent() {
        // Create shape not added to any Pane
        ShapeInterface orphan = new RectangleShape(Color.BLACK, Color.GRAY, 80, 40, 2);
        PositionManagerReceiver receiver = new PositionManagerReceiver(orphan);
        SendBackwardCommand command = new SendBackwardCommand(receiver);

        Exception exception = assertThrows(IllegalStateException.class, command::executeCommand);
        assertTrue(exception.getMessage().contains("Node does not have a Pane parent"));
    }
}
