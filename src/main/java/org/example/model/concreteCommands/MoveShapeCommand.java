package org.example.model.concreteCommands;

import javafx.geometry.Point2D;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * Implements a command to move a shape by a specified offset in the*/
public class MoveShapeCommand implements CommandInterface {

    private final ShapeInterface shape;
    private final double deltaX;
    private final double deltaY;
    private final Point2D originalPosition;
    private final DraggingReceiver draggingReceiver;


    /**
     * Constructs a command to move a shape by specified x and y offsets.
     *
     * @param shape the*/
    public MoveShapeCommand(ShapeInterface shape, double deltaX, double deltaY, DraggingReceiver draggingReceiver) {
        this.shape = shape;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.originalPosition = new Point2D(shape.getX(), shape.getY());
        this.draggingReceiver = draggingReceiver;
    }

    /**
     * Executes the command to move a shape by the specified x and y offsets.
     * This method delegates the movement operation to the dragging receiver,
     * which is responsible for handling the actual behavior of moving the shape.
     *
     * The command encapsulates the logic of interaction with the receiver
     * to perform the move operation, adhering to the Command design pattern.
     */
    @Override
    public void executeCommand() {
        draggingReceiver.moveShape(shape, deltaX, deltaY);
    }

    /**
     * Reverts the move operation executed by this command, restoring the shape to its original position.
     * This method utilizes the associated dragging receiver to reset the shape's position
     * based on the stored original coordinates.
     */
    @Override
    public void undoCommand() {
        draggingReceiver.resetShapePosition(shape, originalPosition);
    }

}