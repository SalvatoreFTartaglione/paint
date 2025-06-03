package org.example.model.commands.concreteCommands;

import javafx.geometry.Point2D;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;
import org.example.model.enums.MirrorDirectionEnum;

import java.util.Optional;

public class MirroringCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private MirrorDirectionEnum mirrorDirection;
    private DrawingContextDTO contextDTO;

    public MirroringCommand(PositionManagerReceiver positionManagerReceiver, MirrorDirectionEnum mirrorDirection, DrawingContextDTO contextDTO) {
        this.positionManagerReceiver = positionManagerReceiver;
        this.mirrorDirection = mirrorDirection;
        this.contextDTO = contextDTO;
    }

    @Override
    public void executeCommand() {
        switch (mirrorDirection) {
            case UP: positionManagerReceiver.mirrorUp(); break;
            case DOWN: positionManagerReceiver.mirrorDown(); break;
            case LEFT: positionManagerReceiver.mirrorLeft(); break;
            case RIGHT: positionManagerReceiver.mirrorRight(); break;
            case VERTICAL: positionManagerReceiver.mirrorVertical(); break;
            case HORIZONTAL: positionManagerReceiver.mirrorHorizontal(); break;
        }

    }

    @Override
    public void undoCommand() {
        switch (mirrorDirection) {
            case UP: positionManagerReceiver.mirrorDown(); break;
            case DOWN: positionManagerReceiver.mirrorUp(); break;
            case LEFT: positionManagerReceiver.mirrorRight(); break;
            case RIGHT: positionManagerReceiver.mirrorLeft(); break;
            case VERTICAL: positionManagerReceiver.mirrorVertical(); break;
            case HORIZONTAL: positionManagerReceiver.mirrorHorizontal(); break;
        }
    }

    @Override
    public boolean isExecutable() {
        Optional<Point2D> simulated = positionManagerReceiver.simulateMirror(mirrorDirection);
        if (simulated.isEmpty()) return true;

        double x = simulated.get().getX();
        double y = simulated.get().getY();

        final double tolerance = -0.5;

        return x >= tolerance && y >= tolerance;
    }

}
