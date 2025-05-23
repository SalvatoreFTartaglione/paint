package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.shape.ShapeInterface;

public class ChangeStrokeColorCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;
    private ChangesOnShapeReceiver changesOnShapeReceiver;

    /**
     * Creates a command to change a shape's stroke color.
     *
     * @param shape                  the shape to modify
     * @param newColor               the new stroke color
     * @param changesOnShapeReceiver
     */
    public ChangeStrokeColorCommand(ShapeInterface shape, Color newColor, ChangesOnShapeReceiver changesOnShapeReceiver) {
        this.shape = shape;
        this.newColor = newColor;
        this.changesOnShapeReceiver = changesOnShapeReceiver;
    }

    /**
     * Applies the new stroke color and saves the old one for undo.
     */
    @Override
    public void executeCommand() {
        oldColor = shape.getStrokeColor();
        changesOnShapeReceiver.changeStrokeColor(shape, newColor);
    }

    /**
     * Restores the previous stroke color.
     */
    @Override
    public void undoCommand() {
        shape.setStrokeColor(oldColor);
    }

}

