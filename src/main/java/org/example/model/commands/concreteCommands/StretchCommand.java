package org.example.model.commands.concreteCommands;

import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;

public class StretchCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private final double newWidth;
    private final double newHeight;
    private final double oldWidth;
    private final double oldHeight;


    public StretchCommand(PositionManagerReceiver positionManagerReceiver, double newWidth, double newHeight, double oldWidth, double oldHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    @Override
    public void executeCommand() {
        positionManagerReceiver.stretch(this.newWidth, this.newHeight);
    }

    @Override
    public void undoCommand() {
        positionManagerReceiver.resetStretch(this.oldWidth, this.oldHeight);
    }
}