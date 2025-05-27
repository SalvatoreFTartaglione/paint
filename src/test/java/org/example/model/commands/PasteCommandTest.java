package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for PasteCommand using real implementations of ShapeReceiver and DrawingContextDTO.
 *
 * This test verifies that:
 * 1. The shape copied into the receiver is correctly pasted into the drawingPane.
 * 2. The pasted shape is stored and can be removed by undoCommand().
 */
public class PasteCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeReceiver receiver;
    private ShapeInterface copiedShape;
    private PasteCommand command;

    @BeforeEach
    public void setUp() {
        // Prepare canvas and DTO
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);
        dto.setSceneX(100);
        dto.setSceneY(100);

        // Create and copy a shape
        copiedShape = new RectangleShape(Color.BLACK, Color.CORNFLOWERBLUE, 80, 40, 2);
        copiedShape.draw(50, 50);

        receiver = new ShapeReceiver(dto);
        receiver.copy(copiedShape); // simulate a prior CopyCommand

        command = new PasteCommand(receiver, dto);
    }

    @Test
    public void testExecuteCommandAddsPastedShapeToPane() {
        assertEquals(0, drawingPane.getChildren().size(), "Pane should be initially empty");

        command.executeCommand();

        ShapeInterface pasted = command.getPastedShape();

        assertNotNull(pasted, "Pasted shape should not be null");
        assertEquals(1, drawingPane.getChildren().size(), "Pane should contain one node after paste");
        assertTrue(drawingPane.getChildren().contains(pasted.getNode()), "Pasted shape node should be in the pane");
    }

    @Test
    public void testUndoCommandRemovesPastedShapeFromPane() {
        command.executeCommand();
        ShapeInterface pasted = command.getPastedShape();

        command.undoCommand();

        assertFalse(drawingPane.getChildren().contains(pasted.getNode()), "Pasted shape should be removed after undo");
    }
}
