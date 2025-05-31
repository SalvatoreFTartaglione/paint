package org.example.model.commands;

import org.example.model.receivers.PositionManagerReceiver;

public class ResizeCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private final double newWidth;
    private final double newHeight;
    private final double oldWidth;
    private final double oldHeight;


    public ResizeCommand(PositionManagerReceiver positionManagerReceiver, double newWidth, double newHeight, double oldWidth, double oldHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.positionManagerReceiver = positionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    @Override
    public void executeCommand() {
        positionManagerReceiver.resize(newWidth, newHeight);
    }

    @Override
    public void undoCommand() {
        positionManagerReceiver.resize(this.oldWidth, this.oldHeight);
    }
}