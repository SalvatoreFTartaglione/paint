package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.shape.ShapeInterface;

public class ChangeStrokeColorCommand implements CommandInterface {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;

    /**
     * Creates a command to change a shape's stroke color.
     *
     * @param shape the shape to modify
     * @param newColor the new stroke color
     */
    public ChangeStrokeColorCommand(ShapeInterface shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor;
    }

    /**
     * Applies the new stroke color and saves the old one for undo.
     */
    @Override
    public void executeCommand() {
        oldColor = shape.getStrokeColor();
        shape.setStrokeColor(newColor);
    }

    /**
     * Restores the previous stroke color.
     */
    @Override
    public void undoCommand() {
        shape.setStrokeColor(oldColor);
    }

}

