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
 * Integration test for CopyCommand using real implementations of ShapeReceiver, DrawingContextDTO, and ShapeInterface.
 *
 * This test verifies that:
 * 1. The original shape is correctly copied.
 * 2. The copied shape is added to the drawingPane.
 * 3. The undo operation removes only the copied shape from the drawingPane.
 */

public class CopyCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeInterface originalShape;
    private ShapeReceiver receiver;
    private CopyCommand command;

    @BeforeEach
    public void setUp() {
        // Inizializza il Pane e il DTO
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);

        // Crea la forma originale
        originalShape = new RectangleShape(Color.BLACK, Color.BLUE, 80, 40, 2);
        originalShape.draw(50, 50);

        // Aggiungi la forma originale al Pane
        drawingPane.getChildren().add(originalShape.getNode());

        // Inizializza il receiver
        receiver = new ShapeReceiver(dto);

        // Crea il comando di copia
        command = new CopyCommand(originalShape, receiver);
    }

    @Test
    public void testExecuteCommandAddsCopyToPane() {
        // Esegui il comando di copia
        command.executeCommand();

        // Verifica che ci siano due nodi nel Pane
        assertEquals(2, drawingPane.getChildren().size(), "Il Pane dovrebbe contenere due nodi dopo la copia.");

        // Verifica che la copia sia presente nel Pane
        boolean copyFound = drawingPane.getChildren().stream()
                .anyMatch(node -> node != originalShape.getNode());
        assertTrue(copyFound, "La copia dovrebbe essere presente nel Pane.");
    }

    @Test
    public void testUndoCommandRemovesCopyFromPane() {
        // Esegui il comando di copia
        command.executeCommand();

        // Esegui l'undo del comando
        command.undoCommand();

        // Verifica che ci sia solo un nodo nel Pane (la forma originale)
        assertEquals(1, drawingPane.getChildren().size(), "Il Pane dovrebbe contenere solo la forma originale dopo l'undo.");

        // Verifica che la forma originale sia ancora presente
        assertTrue(drawingPane.getChildren().contains(originalShape.getNode()), "La forma originale dovrebbe essere ancora presente nel Pane.");
    }
}
