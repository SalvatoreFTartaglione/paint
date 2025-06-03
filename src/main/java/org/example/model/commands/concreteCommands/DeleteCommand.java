package org.example.model.commands.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

public class DeleteCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;
    private DrawingContextDTO drawingDTO;

    public DeleteCommand(DrawingContextDTO drawingDTO, ShapeInterface shape, ShapeReceiver receiver) {
        this.shape = shape;
        this.receiver = receiver;
        this.drawingDTO = drawingDTO;
    }

    @Override
    public void executeCommand() {
        receiver.delete(drawingDTO, shape);
    }

    @Override
    public void undoCommand() {
        receiver.undoDelete(drawingDTO);
    }

}
