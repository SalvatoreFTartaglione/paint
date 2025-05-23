package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvas;

public class AddShape implements CommandInterface {


    private DrawingContextDTO drawingDTO;
    private AddToCanvas addToCanvas;

    public AddShape(DrawingContextDTO context, AddToCanvas addToCanvas) {
        this.drawingDTO = context;
        this.addToCanvas = addToCanvas;
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
