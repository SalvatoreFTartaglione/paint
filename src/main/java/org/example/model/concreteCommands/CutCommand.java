package org.example.model.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * Represents a concrete implementation of the Command design pattern for cutting a shape.
 * Encapsulates the action of cutting a shape from a drawing context and allows the operation
 * to be executed and undone.
 */
public class CutCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;
    private final DrawingContextDTO drawingContextDTO;

    public CutCommand(ShapeInterface shape, ShapeReceiver receiver, DrawingContextDTO drawingContextDTO) {
        this.shape = shape;
        this.receiver = receiver;
        this.drawingContextDTO = drawingContextDTO;
    }

    /**
     * Executes the cut command by delegating the action to the receiver.
     * This method removes the specified shape from the drawing context
     * and stores it as a clone within the receiver for later usage (e.g., paste).
     * The command adheres to the Command*/
    @Override
    public void executeCommand() {
        receiver.cut(shape, drawingContextDTO);
    }

    /**
     * Reverts the changes made by the execution of the cut command.
     * This method restores the previously deleted shape back to the drawing context
     * and clears any intermediate state maintained within the receiver related to the cut operation.
     *
     * The undo operation is performed in two steps:
     * - The receiver's `undoCopy` method is invoked to clear any copied shape and reset the cut state.
     * - The receiver's `undoDelete` method is called with the drawing context to restore the last deleted shape
     *   and its position to the drawing pane.
     */
    @Override
    public void undoCommand() {
        receiver.undoCopy();
        receiver.undoDelete(drawingContextDTO);
    }
}

