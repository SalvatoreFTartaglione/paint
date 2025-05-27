package org.example.model.commands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ShapeInterface;

public class CutCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final ShapeReceiver receiver;
    private final DrawingContextDTO drawingContextDTO;

    public CutCommand(ShapeInterface shape, ShapeReceiver receiver, DrawingContextDTO drawingContextDTO) {
        this.shape = shape;
        this.receiver = receiver;
        this.drawingContextDTO = drawingContextDTO;
    }

    @Override
    public void executeCommand() {
        receiver.cut(shape, drawingContextDTO);
    }

    @Override
    public void undoCommand() {
        receiver.undoCopy();
        receiver.undoDelete(drawingContextDTO);
    }
}

