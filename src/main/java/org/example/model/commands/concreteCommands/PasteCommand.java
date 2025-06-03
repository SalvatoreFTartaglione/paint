package org.example.model.commands.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

public class PasteCommand implements CommandInterface {

    private final ShapeReceiver receiver;
    private final DrawingContextDTO drawingDTO;
    private ShapeInterface pastedShape;

    public PasteCommand(ShapeReceiver receiver, DrawingContextDTO drawingDTO) {
        this.receiver = receiver;
        this.drawingDTO = drawingDTO;
    }

    @Override
    public void executeCommand() {
            ShapeInterface shapeToPaste = receiver.getCopy();
            this.pastedShape = shapeToPaste;
            receiver.paste(shapeToPaste, drawingDTO);
    }

    public ShapeInterface getPastedShape() {
        return this.pastedShape;
    }

    @Override
    public void undoCommand() {
        if (pastedShape != null) {
            drawingDTO.getDrawingPane().getChildren().remove(pastedShape.getNode());
        }
    }
}
