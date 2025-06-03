package org.example.model.commands.concreteCommands;

import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

public class CopyCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;

    public CopyCommand(ShapeInterface shape, ShapeReceiver receiver) {
        this.shape = shape;
        this.receiver = receiver;
    }

    @Override
    public void executeCommand() {
        receiver.copy(shape);
    }

    @Override
    public void undoCommand() {
        receiver.undoCopy();
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
