package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.PositionManagerReceiver;

/**
 * A concrete implementation of the Command design pattern responsible for resizing shapes.
 * Encapsulates the resize action performed by a {@link PositionManagerReceiver} and provides
 **/
public class ResizeCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private final double newSize;
    private final double oldWidth;
    private final double oldHeight;


    /**
     * Constructs a ResizeCommand object, which encapsulates the resizing operation for a shape.
     * This command utilizes a PositionManagerReceiver to perform the resizing actions and
     * tracks the necessary data for undoing the resize operation.
     *
     * @param positionManagerReceiver the receiver responsible for managing the position
     *                                and resizing operations for shapes.
     * @param newSize the new size to which the shape needs to be resized.
     * @param oldWidth the original width of the shape before resizing.
     * @param oldHeight the original height of the shape before resizing.
     */
    public ResizeCommand(PositionManagerReceiver positionManagerReceiver, double newSize, double oldWidth, double oldHeight) {
        this.newSize = newSize;
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    /**
     * Executes the resize command by delegating the resizing action to the {@*/
    @Override
    public void executeCommand() {
        positionManagerReceiver.resize(newSize);
    }

    /**
     * Reverts the resizing operation performed by the executeCommand method
     * and restores the shape's dimensions to their original state.
     *
     * This method delegates the undo action to the associated
     * PositionManagerReceiver, which resets the shape's size
     * to the stored original width and height.
     */
    @Override
    public void undoCommand() {
        positionManagerReceiver.resetResize(this.oldWidth, this.oldHeight);
    }
}