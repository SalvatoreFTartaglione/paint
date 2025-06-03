package org.example.model.concreteCommands;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.product.ShapeInterface;

/**
 * Represents a command for pasting shapes onto a drawing context in the application.
 * This command is part of the Command design pattern and allows a user to paste
 * a copied shape onto a specified location in the drawing context and also undo
 * the paste operation if required.
 */
public class PasteCommand implements CommandInterface {

    private final ShapeReceiver receiver;
    private final DrawingContextDTO drawingDTO;
    private ShapeInterface pastedShape;

    /**
     * Constructs a PasteCommand which is responsible for pasting a copied shape
     * onto a specified location in the drawing context.
     *
     * @param receiver the ShapeReceiver responsible for managing shapes and their operations like copy, cut, and paste
     * @param drawingDTO the DrawingContextDTO representing the drawing context, containing details such as the
     *                   drawing pane, scene coordinates, and other context-specific metadata
     */
    public PasteCommand(ShapeReceiver receiver, DrawingContextDTO drawingDTO) {
        this.receiver = receiver;
        this.drawingDTO = drawingDTO;
    }

    /**
     * Executes the paste command, retrieving a copied shape from the receiver and
     * pasting it onto the drawing context represented by the drawingDTO.
     * This method makes use of the Command design pattern, ensuring encapsulation
     * of the*/
    @Override
    public void executeCommand() {
            ShapeInterface shapeToPaste = receiver.getCopy();
            this.pastedShape = shapeToPaste;
            receiver.paste(shapeToPaste, drawingDTO);
    }

    public ShapeInterface getPastedShape() {
        return this.pastedShape;
    }

    /**
     * Undoes the paste operation by removing the pasted shape from the drawing pane.
     * This method checks if a shape had been previously pasted and, if so,
     * removes its associated visual node from the drawing context's pane.
     * It ensures that the changes made by the paste operation can be reverted,
     * aligning with the Command design pattern's undo functionality.
     */
    @Override
    public void undoCommand() {
        if (pastedShape != null) {
            drawingDTO.getDrawingPane().getChildren().remove(pastedShape.getNode());
        }
    }
}
