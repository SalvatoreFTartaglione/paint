package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ShapeInterface;

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