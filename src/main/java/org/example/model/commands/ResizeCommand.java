package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.PoistionManagerReceiver;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ShapeInterface;

public class ResizeCommand implements CommandInterface {

    private PoistionManagerReceiver poistionManagerReceiver;
    private final double newWidth;
    private final double newHeight;
    private final double oldWidth;
    private final double oldHeight;


    public ResizeCommand(PoistionManagerReceiver poistionManagerReceiver, double newWidth, double newHeight, double oldWidth, double oldHeight) {
        this.newWidth = newWidth;
        this.newHeight = newHeight;
        this.poistionManagerReceiver = poistionManagerReceiver;
        this.oldWidth = oldWidth;
        this.oldHeight = oldHeight;
    }

    @Override
    public void executeCommand() {
        poistionManagerReceiver.resize(newWidth, newHeight);
    }

    @Override
    public void undoCommand() {
        poistionManagerReceiver.resize(this.oldWidth, this.oldHeight);
    }
}