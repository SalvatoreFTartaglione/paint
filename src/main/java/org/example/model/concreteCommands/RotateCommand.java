package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.PositionManagerReceiver;

/**
 * A command that encapsulates the action of rotating a shape to a specified angle.
 * Implements the Command design pattern to decouple the rotation operation from its execution logic.
 * The rotation can also be undone, reverting to the previous angle.
 */
public class RotateCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private double oldAngle;
    private double newAngle;

    /**
     * Constructs a Rotate*/
    public RotateCommand(PositionManagerReceiver positionManagerReceiver, double oldAngle, double newAngle) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldAngle = oldAngle;
        this.newAngle = newAngle;

    }

    /**
     * Executes the rotation command by updating the angle of the shape managed
     * by the receiver. This method sets the new angle for the shape, as specified
     * during the creation of the command.
     *
     * This implementation adheres to the Command design pattern, encapsulating
     * the logic for rotating a shape and delegating the actual rotation operation
     * to the receiver.
     */
    @Override
    public void executeCommand() {
        positionManagerReceiver.setAngle(newAngle);
    }

    /**
     * Reverts the action performed by the rotation command, restoring the shape's
     * angle to its previous value. This operation is part of the undo functionality
     * encapsulated within the RotateCommand class, adhering to the Command design pattern.
     *
     * This method delegates the operation to the receiver by instructing it to reset
     * the angle of the shape to the value stored prior to the execution of the rotation.
     * The receiver is responsible for applying this change to the shape's state.
     */
    @Override
    public void undoCommand() {
        positionManagerReceiver.setAngle(oldAngle);
    }

}
