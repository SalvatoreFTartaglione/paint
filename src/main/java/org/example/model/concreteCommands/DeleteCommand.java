package org.example.model.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * Represents a command to delete a shape from a drawing context.
 * This class implements the Command design pattern, encapsulating the
 * information and actions required to delete a shape and undo the deletion.
 */
public class DeleteCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;
    private DrawingContextDTO drawingDTO;

    public DeleteCommand(DrawingContextDTO drawingDTO, ShapeInterface shape, ShapeReceiver receiver) {
        this.shape = shape;
        this.receiver = receiver;
        this.drawingDTO = drawingDTO;
    }

    /**
     * Executes the delete command by delegating the deletion operation to the receiver.
     * This method removes a specified shape from the drawing context and ensures that
     * the deleted shape and its position are stored for potential restoration via undo.
     *
     * The deletion process involves:
     * - Removing the shape from the drawing pane within the provided drawing context.
     * - Storing the deleted shape and its position in the receiver's stack to maintain
     *   undo capability.
     */
    @Override
    public void executeCommand() {
        receiver.delete(drawingDTO, shape);
    }

    /**
     * Reverts the actions performed by the execution of the delete command. This method
     * restores the most recently deleted shape back to the drawing context using the
     * receiver's undo mechanisms. The shape and its position, which were stored during
     * the deletion process, are retrieved and re-added to the drawing pane.
     *
     * The undo operation ensures the restoration of:
     * - The deleted shape to its original position.
     * - The drawing state prior to the execution of the delete command.
     *
     * Delegates the undo logic to the receiver's `undoDelete` method, which manages
     * the restoration process.
     */
    @Override
    public void undoCommand() {
        receiver.undoDelete(drawingDTO);
    }

}
