package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.shape.ShapeInterface;

public class ChangeFillColorCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;
    private ChangesOnShapeReceiver changesOnShapeReceiver;

    /**
     * Creates a command to change a shape's fill color.
     *
     * @param shape the shape to modify
     * @param newColor the new fill color
     */
    public ChangeFillColorCommand(ShapeInterface shape, Color newColor, ChangesOnShapeReceiver changesOnShapeReceiver) {
        this.shape = shape;
        this.newColor = newColor;
        this.changesOnShapeReceiver = changesOnShapeReceiver;
    }

    /**
     * Applies the new fill color and saves the old one for undo.
     */
    @Override
    public void executeCommand() {
        oldColor = shape.getFillColor();
        changesOnShapeReceiver.changeFillColor(shape,newColor);
    }

    /**
     * Restores the previous fill color.
     */
    @Override
    public void undoCommand() {
        shape.setFillColor(oldColor);
    }
}
