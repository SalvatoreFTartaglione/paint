package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.shape.ShapeInterface;

public class ChangeFillColorCommand implements Command {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;

    public ChangeFillColorCommand(ShapeInterface shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        oldColor = shape.getFillColor();  // salva il colore precedente
        shape.setFillColor(newColor);     // imposta il nuovo colore
    }

    @Override
    public void undo() {
        shape.setFillColor(oldColor);     // ripristina il colore precedente
    }
}
