package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvasReceiver;
import org.example.model.shape.ShapeInterface;
public class AddShapeCommand implements CommandInterface {

    private ShapeInterface shape;
    private DrawingContextDTO drawingDTO;
    private AddToCanvasReceiver addToCanvasReceiver;
    private boolean executed = false;

    public AddShapeCommand(DrawingContextDTO context, AddToCanvasReceiver addToCanvasReceiver) {
        this.drawingDTO = context;
        this.addToCanvasReceiver = addToCanvasReceiver;
    }

    public void executeCommand() {
        shape = addToCanvasReceiver.addShape();
        executed = (shape != null);
    }


    public void undoCommand() {
        if (executed && shape != null && shape.getNode() != null) {
            drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
        }
    }

    public ShapeInterface getShape() {
        return shape;
    }

    public boolean wasExecuted() {
        return executed;
    }



}
