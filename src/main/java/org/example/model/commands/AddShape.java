package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvas;

public class AddShape implements CommandInterface {

    private final AddToCanvas addToCanvas;
    private DrawingContextDTO drawingDTO;

    public AddShape(DrawingContextDTO context) {
        this.drawingDTO = context;
        addToCanvas = new AddToCanvas(context);
    }

    public void executeCommand() {
        addToCanvas.add();
    }

    public void undoCommand() {
        int indexOfLastShape = drawingDTO.getDrawingPane().getChildren().size()-1;
        if (!drawingDTO.getDrawingPane().getChildren().isEmpty()) {
            drawingDTO.getDrawingPane().getChildren().remove(indexOfLastShape);
        }
    }


}
