package org.example.model.commands.concreteCommands;

import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;

public class ResizeCommand implements CommandInterface {

    private PositionManagerReceiver poistionManagerReceiver;
    private final double newSize;
    private final double oldWidth;
    private final double oldHeight;


    public ResizeCommand(PositionManagerReceiver poistionManagerReceiver, double newSize, double oldWidth, double oldHeight) {
        this.newSize = newSize;
        this.poistionManagerReceiver = poistionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    @Override
    public void executeCommand() {
        poistionManagerReceiver.resize(newSize);
    }

    @Override
    public void undoCommand() {
        poistionManagerReceiver.resetResize(this.oldWidth, this.oldHeight);
    }
}