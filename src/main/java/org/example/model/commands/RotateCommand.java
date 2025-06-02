package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.PositionManagerReceiver;

import java.util.Optional;

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
