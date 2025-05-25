package org.example.model.commands;

import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ShapeInterface;

public class CutCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;

    public CutCommand(ShapeInterface shape, ShapeReceiver receiver) {
        this.shape = shape;
        this.receiver = receiver;
    }

    @Override
    public void executeCommand() {
        receiver.cut(shape);
    }

    @Override
    public void undoCommand() {
        receiver.clear();
    }
}

