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
 * Integration test for CutCommand using real implementations of ShapeReceiver, DrawingContextDTO, and ShapeInterface.
 *
 * This test verifies that:
 * 1. The shape is removed from the drawingPane when executeCommand() is called.
 * 2. The shape is restored correctly to the drawingPane when undoCommand() is called.
 */
public class CutCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeInterface shape;
    private ShapeReceiver receiver;
    private CutCommand command;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);

        shape = new RectangleShape(Color.BLACK, Color.LIGHTBLUE, 80, 50, 2);
        shape.draw(100, 100);

        drawingPane.getChildren().add(shape.getNode());

        receiver = new ShapeReceiver(dto);
        command = new CutCommand(shape, receiver, dto);
    }

    @Test
    public void testExecuteCommandRemovesShapeAndKeepsCopy() {
        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be in the pane before cut");

        command.executeCommand();

        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed from pane after cut");
        assertTrue(receiver.hasShape(), "Receiver should hold a copied shape");
    }

    @Test
    public void testUndoCommandRestoresShape() {
        command.executeCommand();
        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed after cut");

        command.undoCommand();

        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be restored to pane after undo");
    }
}
