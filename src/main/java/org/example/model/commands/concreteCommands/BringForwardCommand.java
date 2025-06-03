package org.example.model.commands.concreteCommands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;

/**
 * Command to bring a shape's graphical node to the front of its parent {@link javafx.scene.layout.Pane}.
 * <p>
 * Implements the Command pattern, allowing execution and undo of the z-order change.
 * This command interacts with a {@link PositionManagerReceiver} to manipulate the shape's position in the UI.
 */
public class BringForwardCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private Pane drawingPane;
    private int oldIndex;
    private Node node;

    /**
     * Constructs a new {@code BringForwardCommand} using the specified position manager.
     *
     * @param positionManagerReceiver the receiver that manages position-related operations for the shape
     */
    public BringForwardCommand(PositionManagerReceiver positionManagerReceiver){
        this.node = positionManagerReceiver.getShape().getNode();
        this.positionManagerReceiver = positionManagerReceiver;
    }

    /**
     * Executes the command by moving the shape's node to the front of its parent container.
     * Stores the old index for undo purposes.
     *
     * @throws IllegalStateException if the node's parent is not a {@link Pane}
     */
    @Override
    public void executeCommand() {
        // Get container of the node
        if (node.getParent() instanceof Pane) {
            drawingPane = (Pane) node.getParent();
            oldIndex = drawingPane.getChildren().indexOf(node);
            // Send node backward
            positionManagerReceiver.toFront();
        } else {
            throw new IllegalStateException("Node does not have a Pane parent");
        }
    }

    /**
     * Undoes the z-order change by restoring the node to its original index in the parent pane.
     */
    @Override
    public void undoCommand(){
        if (drawingPane != null) {
            drawingPane.getChildren().remove(node);             // remove from actual position
            drawingPane.getChildren().add(oldIndex, node);     // reset the original position
        }
    }
}
