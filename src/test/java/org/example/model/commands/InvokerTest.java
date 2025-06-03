package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.concreteCommands.*;
import org.example.model.commands.invoker.Invoker;
import org.example.model.commands.receivers.AddToCanvasReceiver;
import org.example.model.commands.receivers.ChangesOnShapeReceiver;
import org.example.model.commands.receivers.PositionManagerReceiver;
import org.example.model.commands.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Full integration test for Invoker with multiple real commands.
 * Verifies execution, stack behavior, and undo for:
 * Add, Delete, Resize, ChangeFillColor, and Paste commands.
 */
public class InvokerTest {

    private DrawingContextDTO dto;
    private Pane pane;
    private Invoker invoker;
    private AddToCanvasReceiver addReceiver;
    private ShapeReceiver shapeReceiver;
    private ShapeInterface originalShape;

    @BeforeEach
    public void setUp() {
        pane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(pane);
        dto.setBorderColor(Color.BLACK);
        dto.setFillColor(Color.RED);
        dto.setSceneX(100);
        dto.setSceneY(100);
        dto.setShapeOption(new ShapeOption("Rectangle", "")); // Assicura che Rectangle sia supportato

        invoker = new Invoker();
        addReceiver = new AddToCanvasReceiver(dto);
        shapeReceiver = new ShapeReceiver(dto);
    }

    @Test
    public void executeAndUndoMultipleCommandsStackBehavior() {
        // 1. Add a shape to the pane
        CommandInterface addCmd = new AddShapeCommand(dto, addReceiver);
        invoker.execute(addCmd);
        assertEquals(1, pane.getChildren().size());
        originalShape = (ShapeInterface) pane.getChildren().get(0).getUserData();

        // 2. Change fill color of the shape
        CommandInterface colorCmd = new ChangeFillColorCommand(originalShape, Color.BLUE, new ChangesOnShapeReceiver());
        invoker.execute(colorCmd);
        assertEquals(Color.BLUE, originalShape.getFillColor());

        // 3. Resize the shape using a larger newSize to ensure visual change
        PositionManagerReceiver pmr = new PositionManagerReceiver(originalShape);
        double oldWidth = originalShape.getWidth();
        double oldHeight = originalShape.getHeight();
        double newSize = Math.max(oldWidth, oldHeight) + 30; // Ensure size changes
        CommandInterface resizeCmd = new ResizeCommand(pmr, newSize, oldWidth, oldHeight);
        invoker.execute(resizeCmd);
        assertNotEquals(oldWidth, originalShape.getWidth(), 0.01);
        assertNotEquals(oldHeight, originalShape.getHeight(), 0.01);

        // 4. Copy the shape and paste it into the canvas
        shapeReceiver.copy(originalShape);
        CommandInterface pasteCmd = new PasteCommand(shapeReceiver, dto);
        invoker.execute(pasteCmd);
        assertEquals(2, pane.getChildren().size());

        // 5. Delete the original shape from the canvas
        CommandInterface deleteCmd = new DeleteCommand(dto, originalShape, shapeReceiver);
        invoker.execute(deleteCmd);
        assertEquals(1, pane.getChildren().size());

        // UNDO STACK TESTING (reverse operations)

        invoker.undo(); // Undo delete: original shape restored
        assertEquals(2, pane.getChildren().size());

        invoker.undo(); // Undo paste: pasted shape removed
        assertEquals(1, pane.getChildren().size());

        invoker.undo(); // Undo resize: original dimensions restored
        assertEquals(oldWidth, originalShape.getWidth(), 0.01);
        assertEquals(oldHeight, originalShape.getHeight(), 0.01);

        invoker.undo(); // Undo fill color change: back to RED
        assertEquals(Color.RED, originalShape.getFillColor());

        invoker.undo(); // Undo add: pane should be empty
        assertEquals(0, pane.getChildren().size());
    }

}
