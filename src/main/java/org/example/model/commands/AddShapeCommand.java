package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvasReceiver;
import org.example.model.shape.ShapeInterface;

public class AddShapeCommand implements CommandInterface {

    private ShapeInterface shape;
    private DrawingContextDTO drawingDTO;
    private AddToCanvasReceiver addToCanvasReceiver;

    public AddShapeCommand(DrawingContextDTO context, AddToCanvasReceiver addToCanvasReceiver) {
        this.drawingDTO = context;
        this.addToCanvasReceiver = addToCanvasReceiver;
    }

    public void executeCommand() {
        shape = addToCanvasReceiver.addShape();
    }


    public void undoCommand() {
        if (!drawingDTO.getDrawingPane().getChildren().isEmpty()) {
            drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
        }
    }



}
