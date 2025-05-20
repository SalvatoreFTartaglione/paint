package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.shape.ShapeInterface;

public class ChangeStrokeColorCommand implements Command {
    private final ShapeInterface shape;
    private final Color newColor;
    private Color oldColor;

    public ChangeStrokeColorCommand(ShapeInterface shape, Color newColor) {
        this.shape = shape;
        this.newColor = newColor;
    }

    @Override
    public void execute() {
        oldColor = shape.getStrokeColor();
        shape.setStrokeColor(newColor);
    }

    @Override
    public void undo() {
        shape.setStrokeColor(oldColor);
    }
}
