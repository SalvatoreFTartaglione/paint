package org.example.model.commands.concreteCommands;

import javafx.geometry.Point2D;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.DraggingReceiver;
import org.example.model.factory.product.ShapeInterface;

public class MoveShapeCommand implements CommandInterface {

    private final ShapeInterface shape;
    private final double deltaX;
    private final double deltaY;
    private final Point2D originalPosition;
    private final DraggingReceiver draggingReceiver;


    public MoveShapeCommand(ShapeInterface shape, double deltaX, double deltaY, DraggingReceiver draggingReceiver) {
        this.shape = shape;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.originalPosition = new Point2D(shape.getX(), shape.getY());
        this.draggingReceiver = draggingReceiver;
    }

    @Override
    public void executeCommand() {
        draggingReceiver.moveShape(shape, deltaX, deltaY);
    }

    @Override
    public void undoCommand() {
        draggingReceiver.resetShapePosition(shape, originalPosition);
    }

}