package org.example.model.commands.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.AddToCanvasReceiver;
import org.example.model.factory.product.ShapeInterface;

public class AddShapeCommand implements CommandInterface {

    private ShapeInterface shape;
    private DrawingContextDTO drawingDTO;
    private AddToCanvasReceiver addToCanvasReceiver;
    private boolean executed = false;

    public AddShapeCommand(DrawingContextDTO context, AddToCanvasReceiver addToCanvasReceiver) {
        this.drawingDTO = context;
        this.addToCanvasReceiver = addToCanvasReceiver;
    }

    @Override
    public void executeCommand() {
        shape = addToCanvasReceiver.addShape();
        executed = (shape != null);
    }


    public void undoCommand() {
        if (executed && shape != null && shape.getNode() != null) {
            drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
        }
    }

    @Override
    public boolean isUndoable() {
        return wasExecuted();
    }

    public ShapeInterface getShape() {
        return shape;
    }

    public boolean wasExecuted() {
        return executed;
    }



}
