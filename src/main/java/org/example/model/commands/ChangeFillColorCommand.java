package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.shape.ShapeInterface;

public class ChangeFillColorCommand implements Command {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;

    /**
     * Creates a command to change a shape's fill color.
     *
     * @param shape the shape to modify
     * @param newColor the new fill color
     */
    public ChangeFillColorCommand(ShapeInterface shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor;
    }

    /**
     * Applies the new fill color and saves the old one for undo.
     */
    @Override
    public void execute() {
        oldColor = shape.getFillColor();  // salva il colore precedente
        shape.setFillColor(newColor);     // imposta il nuovo colore
    }

    /**
     * Restores the previous fill color.
     */
    @Override
    public void undo() {
        shape.setFillColor(oldColor);     // ripristina il colore precedente
    }
}
