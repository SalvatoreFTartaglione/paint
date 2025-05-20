package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.shape.ShapeInterface;

public class AddShapeCommand implements Command {
    private final Pane canvas;
    private final ShapeInterface shape;

    /**
     * Creates a command to add the given shape to the specified canvas.
     *
     * @param canvas the drawing area
     * @param shape the shape to add
     */
    public AddShapeCommand(Pane canvas, ShapeInterface shape) {
        this.canvas = canvas;
        this.shape = shape;
    }

    /**
     * Executes the command by adding the shape's node to the canvas.
     * <p>
     * This method is called when the command is invoked, typically to apply
     * a user action such as drawing a shape on the canvas.
     * </p>
     */
    @Override
    public void execute() {
        Node node = shape.getNode();
        node.setUserData(shape);  // collega il node alla ShapeInterface
        if (!canvas.getChildren().contains(node)) {
            canvas.getChildren().add(node);
        }
    }




    /**
     * Undoes the command by removing the shape's node from the canvas.
     * <p>
     * This method is called to reverse the action performed by {@link #execute()},
     * supporting undo functionality in the application.
     * </p>
     */
    @Override
    public void undo() {
        canvas.getChildren().remove(shape.getNode());
    }
}
