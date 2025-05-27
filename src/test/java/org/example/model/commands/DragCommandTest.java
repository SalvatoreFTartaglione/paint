package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This test verifies that:
 * 1. executeCommand() moves the shape by the specified deltas.
 * 2. undoCommand() resets the shape's position to the original coordinates.
 */
public class DragCommandTest {

    private ShapeInterface shape;
    private DraggingReceiver receiver;
    private DragCommand command;
    private Point2D initialPosition;

    @BeforeEach
    public void setUp() {
        shape = new RectangleShape(Color.BLACK, Color.YELLOW, 100, 60, 2);
        shape.draw(50, 80); // Initial position

        receiver = new DraggingReceiver() {
            @Override
            public void moveShape(ShapeInterface shape, double dx, double dy) {
                shape.moveBy(dx, dy);
            }

            @Override
            public void resetShapePosition(ShapeInterface shape, Point2D originalPosition) {
                shape.setX(originalPosition.getX());
                shape.setY(originalPosition.getY());
            }
        };

        initialPosition = new Point2D(shape.getX(), shape.getY());
        command = new DragCommand(shape, 20, 30, receiver);
    }

    @Test
    public void testExecuteCommandMovesShape() {
        command.executeCommand();

        assertEquals(initialPosition.getX() + 20, shape.getX(), 0.01);
        assertEquals(initialPosition.getY() + 30, shape.getY(), 0.01);
    }

    @Test
    public void testUndoCommandRestoresOriginalPosition() {
        command.executeCommand(); // move
        command.undoCommand();    // reset

        assertEquals(initialPosition.getX(), shape.getX(), 0.01);
        assertEquals(initialPosition.getY(), shape.getY(), 0.01);
    }
}
