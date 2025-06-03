package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.PositionManagerReceiver;

/**
 * Represents a concrete implementation of the Command design pattern for stretching a shape.
 * Encapsulates the action of resizing a shape to new dimensions and provides the ability
 * to execute and undo this operation.
 */
public class StretchCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private final double newWidth;
    private final double newHeight;
    private final double oldWidth;
    private final double oldHeight;


    /**
     * Constructs a StretchCommand that resizes a shape to new dimensions while
     * enabling the operation to be undone by resetting it back to the original dimensions.
     *
     * @param positionManagerReceiver the receiver responsible for handling the
     *                                stretching and resetting actions of the shape
     * @param newWidth                the new width to which the shape will be stretched
     * @param newHeight               the new height to which the shape will be stretched
     * @param oldWidth                the initial width of the shape before stretching
     * @param oldHeight               the initial height of the shape before stretching
     */
    public StretchCommand(PositionManagerReceiver positionManagerReceiver, double newWidth, double newHeight, double oldWidth, double oldHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    /**
     * Executes the stretch command by invoking the stretch operation on the receiver.
     * This method adjusts the dimensions of the associated shape to the new width
     * and height values specified during the command's construction.
     *
     * The execution is delegated to the {@code positionManagerReceiver} to perform
     * the actual resizing operation, ensuring encapsulation of the stretch logic.
     */
    @Override
    public void executeCommand() {
        positionManagerReceiver.stretch(this.newWidth, this.newHeight);
    }

    /**
     * Reverts the previously executed stretch operation by resetting the dimensions
     * of the shape to its original width and height.
     *
     * This method invokes the {@code resetStretch} method on the {@code positionManagerReceiver}
     * with the stored original dimensions (oldWidth and oldHeight), effectively restoring
     * the shape to its state prior to the stretch command's execution.
     *
     * The undo functionality is a key component of the Command design pattern, allowing
     * for the reversal of executed operations to maintain application state consistency.
     */
    @Override
    public void undoCommand() {
        positionManagerReceiver.resetStretch(this.oldWidth, this.oldHeight);
    }
}