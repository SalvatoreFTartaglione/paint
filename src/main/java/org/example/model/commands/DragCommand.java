package org.example.model.commands;

import javafx.geometry.Point2D;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.shape.ShapeInterface;

public class DragCommand implements CommandInterface {

    private final ShapeInterface shape;
    private final double deltaX;
    private final double deltaY;
    private final Point2D originalPosition;
    private final DraggingReceiver draggingReceiver;


    public DragCommand(ShapeInterface shape, double deltaX, double deltaY, DraggingReceiver draggingReceiver) {
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