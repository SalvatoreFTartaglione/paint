package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvasReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for AddShapeCommand using real DTO, receiver, and shape.
 *
 * Verifies that:
 * 1. executeCommand() adds a shape to the drawingPane.
 * 2. undoCommand() removes the shape.
 * 3. extra undo calls are handled gracefully.
 */

public class AddShapeCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private AddToCanvasReceiver receiver;

    @BeforeEach
    public void setUp() {
        dto = new DrawingContextDTO();
        drawingPane = new Pane();
        dto.setDrawingPane(drawingPane);
        dto.setBorderColor(Color.BLACK);
        dto.setFillColor(Color.RED);
        dto.setSceneX(100);
        dto.setSceneY(100);
        dto.setShapeOption(new org.example.controller.ShapeOption("Rectangle", ""));

        receiver = new AddToCanvasReceiver(dto);
    }

    @Test
    public void testExecuteCommandAddShape() {
        AddShapeCommand cmd = new AddShapeCommand(dto, receiver);
        assertEquals(0, drawingPane.getChildren().size());

        cmd.executeCommand();

        assertEquals(1, drawingPane.getChildren().size());
        Node node = drawingPane.getChildren().get(0);
        assertNotNull(node.getUserData());
        assertTrue(node.getUserData() instanceof ShapeInterface);
    }

    @Test
    public void testUndoCommandRemoveShape() {
        AddShapeCommand cmd = new AddShapeCommand(dto, receiver);
        cmd.executeCommand();
        assertEquals(1, drawingPane.getChildren().size());

        cmd.undoCommand();
        assertEquals(0, drawingPane.getChildren().size());
    }

    @Test
    public void testUndoCommandDoesNothingIfPaneEmpty() {
        AddShapeCommand cmd = new AddShapeCommand(dto, receiver);
        cmd.executeCommand();
        cmd.undoCommand();  // Remove
        assertEquals(0, drawingPane.getChildren().size());

        // Chiamata extra a undo non deve causare crash
        cmd.undoCommand();
        assertEquals(0, drawingPane.getChildren().size());
    }
}
