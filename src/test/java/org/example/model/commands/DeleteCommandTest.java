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
 * Integration test for DeleteCommand using real implementations of ShapeReceiver, DrawingContextDTO, and ShapeInterface.
 *
 * This test verifies that:
 * 1. executeCommand() effectively removes the shape from the drawingPane.
 * 2. undoCommand() correctly restores the shape to its original position in the pane.
 */

public class DeleteCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeInterface shape;
    private ShapeReceiver receiver;
    private DeleteCommand command;

    @BeforeEach
    public void setUp() {
        // Prepara una figura e un DTO con pane
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);

        // Crea la shape
        shape = new RectangleShape(Color.BLACK, Color.ORANGE, 100, 50, 2);
        shape.draw(100, 100);

        // Aggiungila al Pane
        drawingPane.getChildren().add(shape.getNode());

        // Receiver reale
        receiver = new ShapeReceiver(dto);

        // Crea il comando
        command = new DeleteCommand(dto, shape, receiver);
    }

    @Test
    public void testExecuteCommandRemovesShapeFromPane() {
        // Pre-condizione: la figura è presente nel pane
        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be in Pane before deletion");

        command.executeCommand();

        // Dopo delete la figura deve essere rimossa
        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed from Pane after delete");
    }

    @Test
    public void testUndoCommandRestoresShapeToPane() {
        // Rimuovi la figura
        command.executeCommand();
        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed");

        // Undo
        command.undoCommand();

        // La figura deve tornare
        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be restored to Pane after undo");
    }

    @Test
    public void testShapeRestoredInSamePosition() {
        // Salva posizione originale
        int originalIndex = drawingPane.getChildren().indexOf(shape.getNode());

        command.executeCommand();
        command.undoCommand();

        int newIndex = drawingPane.getChildren().indexOf(shape.getNode());
        assertEquals(originalIndex, newIndex, "Shape should be restored in the same position");
    }
}
