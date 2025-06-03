package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * A command that encapsulates the action of copying a shape within a receiver.
 * Implements the Command design pattern to decouple the action of copying from
 * its execution logic.
 */
public class CopyCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;

    public CopyCommand(ShapeInterface shape, ShapeReceiver receiver) {
        this.shape = shape;
        this.receiver = receiver;
    }

    /**
     * Executes the copy command on the provided shape by delegating the action to the receiver.
     * This method encapsulates the logic of copying a shape and ensures that the command
     * adheres to the Command design pattern.
     */
    @Override
    public void executeCommand() {
        receiver.copy(shape);
    }

    /**
     * Reverts the action of the copy command by delegating to the receiver's `undoCopy` method.
     * This method ensures that any previously copied shape within the receiver is cleared,
     * effectively undoing the copy operation.
     */
    @Override
    public void undoCommand() {
        receiver.undoCopy();
    }

    /**
     * Determines whether the command is undoable.
     * This method provides information about whether the action
     * performed by the concrete command can be reversed.
     *
     * @return false as this command is not undoable.
     */
    @Override
    public boolean isUndoable() {
        return false;
    }
}
