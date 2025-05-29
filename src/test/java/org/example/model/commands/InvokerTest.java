package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvasReceiver;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ShapeInterface;
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
        dto.setShapeOption(new ShapeOption("Rectangle", ""));

        invoker = new Invoker();
        addReceiver = new AddToCanvasReceiver(dto);
        shapeReceiver = new ShapeReceiver(dto);
    }

    @Test
    public void executeAndUndoMultipleCommandsStackBehavior() {
        // 1. Add shape
        CommandInterface addCmd = new AddShapeCommand(dto, addReceiver);
        invoker.executeCommand(addCmd);
        assertEquals(1, pane.getChildren().size());
        originalShape = (ShapeInterface) pane.getChildren().get(0).getUserData();

        // 2. Change Fill Color
        CommandInterface colorCmd = new ChangeFillColorCommand(originalShape, Color.BLUE, new ChangesOnShapeReceiver());
        invoker.executeCommand(colorCmd);
        assertEquals(Color.BLUE, originalShape.getFillColor());

        // 3. Resize shape
        PositionManagerReceiver pmr = new PositionManagerReceiver(originalShape);
        double oldWidth = originalShape.getWidth();
        double oldHeight = originalShape.getHeight();
        CommandInterface resizeCmd = new ResizeCommand(pmr, 200, 150, oldWidth, oldHeight);
        invoker.executeCommand(resizeCmd);
        assertEquals(200, originalShape.getWidth(), 0.01);
        assertEquals(150, originalShape.getHeight(), 0.01);

        // 4. Copy + Paste
        shapeReceiver.copy(originalShape);
        CommandInterface pasteCmd = new PasteCommand(shapeReceiver, dto);
        invoker.executeCommand(pasteCmd);
        assertEquals(2, pane.getChildren().size());

        // 5. Delete original shape
        CommandInterface deleteCmd = new DeleteCommand(dto, originalShape, shapeReceiver);
        invoker.executeCommand(deleteCmd);
        assertEquals(1, pane.getChildren().size());

        // UNDO SEQUENCE
        invoker.undo(); // Undo delete → shape back
        assertEquals(2, pane.getChildren().size());

        invoker.undo(); // Undo paste → shape removed
        assertEquals(1, pane.getChildren().size());

        invoker.undo(); // Undo resize → back to old size
        assertEquals(oldWidth, originalShape.getWidth(), 0.01);
        assertEquals(oldHeight, originalShape.getHeight(), 0.01);

        invoker.undo(); // Undo fill color → back to RED
        assertEquals(Color.RED, originalShape.getFillColor());

        invoker.undo(); // Undo add → shape removed
        assertEquals(0, pane.getChildren().size());
    }
}
