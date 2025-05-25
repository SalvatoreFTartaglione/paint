package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvas;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.LineShape;
import org.example.model.shape.ShapeInterface;

import javax.sound.sampled.Line;

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

    @Override
    public void undoCommand() {
        if (pastedShape != null) {
            drawingDTO.getDrawingPane().getChildren().remove(pastedShape.getNode());
        }
    }

    public ShapeInterface getPastedShape() {
        return this.pastedShape;
    }
}
