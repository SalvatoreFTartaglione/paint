package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvasReceiver;

public class AddShapeCommand implements CommandInterface {


    private DrawingContextDTO drawingDTO;
    private AddToCanvasReceiver addToCanvasReceiver;

    public AddShapeCommand(DrawingContextDTO context, AddToCanvasReceiver addToCanvasReceiver) {
        this.drawingDTO = context;
        this.addToCanvasReceiver = addToCanvasReceiver;
    }

    public void executeCommand() {
        addToCanvasReceiver.add();
    }

    public void undoCommand() {
        int indexOfLastShape = drawingDTO.getDrawingPane().getChildren().size()-1;
        if (!drawingDTO.getDrawingPane().getChildren().isEmpty()) {
            drawingDTO.getDrawingPane().getChildren().remove(indexOfLastShape);
        }
    }


}
