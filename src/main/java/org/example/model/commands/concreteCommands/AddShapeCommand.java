package org.example.model.commands.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.AddToCanvasReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * Command for adding a shape to the drawing canvas.
 * <p>
 * Implements the Command pattern to allow execution and undoing of shape additions.
 * Uses a {@link DrawingContextDTO} to provide context and an {@link AddToCanvasReceiver}
 * to perform the actual addition of the shape.
 */
public class AddShapeCommand implements CommandInterface {

    private ShapeInterface shape;
    private DrawingContextDTO drawingDTO;
    private AddToCanvasReceiver addToCanvasReceiver;
    private boolean executed = false;

    /**
     * Constructs a new {@code AddShapeCommand} with the given drawing context and receiver.
     *
     * @param context              the drawing context containing pane, color, shape, and position data
     * @param addToCanvasReceiver the receiver responsible for performing the actual shape addition
     */
    public AddShapeCommand(DrawingContextDTO context, AddToCanvasReceiver addToCanvasReceiver) {
        this.drawingDTO = context;
        this.addToCanvasReceiver = addToCanvasReceiver;
    }

    /**
     * Executes the command by adding the shape to the canvas.
     * Sets the internal {@code executed} flag to true if the shape was added successfully.
     */
    @Override
    public void executeCommand() {
        shape = addToCanvasReceiver.addShape();
        executed = (shape != null);
    }

    /**
     * Undoes the shape addition by removing the shape's node from the drawing pane.
     * This operation is only performed if the shape was successfully executed and exists.
     */
    public void undoCommand() {
        if (executed && shape != null && shape.getNode() != null) {
            drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
        }
    }

    /**
     * Indicates whether this command is undoable.
     *
     * @return true if the shape was added and the command was executed; false otherwise
     */
    @Override
    public boolean isUndoable() {
        return wasExecuted();
    }

    /**
     * Returns the shape that was added during execution.
     *
     * @return the added {@link ShapeInterface}, or null if not executed
     */
    public ShapeInterface getShape() {
        return shape;
    }

    /**
     * Indicates whether the command was successfully executed.
     *
     * @return true if the shape was added; false otherwise
     */
    public boolean wasExecuted() {
        return executed;
    }



}
