package org.example.model.commands.concreteCommands;

import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;

public class RotateCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private double oldAngle;
    private double newAngle;

    public RotateCommand(PositionManagerReceiver positionManagerReceiver, double oldAngle, double newAngle) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldAngle = oldAngle;
        this.newAngle = newAngle;

    }

    @Override
    public void executeCommand() {
        positionManagerReceiver.setAngle(newAngle);
    }

    @Override
    public void undoCommand() {
        positionManagerReceiver.setAngle(oldAngle);
    }

}
