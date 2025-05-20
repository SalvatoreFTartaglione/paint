package org.example.model.commands;

import javafx.scene.layout.Pane;
import org.example.model.shape.ShapeInterface;

public class AddShapeCommand implements Command {
    private final Pane canvas;
    private final ShapeInterface shape;

    public AddShapeCommand(Pane canvas, ShapeInterface shape) {
        this.canvas = canvas;
        this.shape = shape;
    }

    @Override
    public void execute() {
        canvas.getChildren().add(shape.getNode());
    }

    @Override
    public void undo() {
        canvas.getChildren().remove(shape.getNode());
    }
}
