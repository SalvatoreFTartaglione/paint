package org.example.model.concreteCommands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.PositionManagerReceiver;

/**
 * Represents a concrete implementation of the Command design pattern that sends
 * a JavaFX node backward in the z-order within its parent container. This command
 * is reversible and allows the node's original position to be restored.
 *
 * The command interacts with a {@link PositionManagerReceiver} to encapsulate node
 * management operations and provides functionality to execute and undo the action.
 */
public class SendBackwardCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private Node node;
    private Pane drawingPane;
    private int oldIndex;

    /**
     * Constructs a new {@code SendBackwardCommand} with the specified {@code PositionManagerReceiver}.
     * Initializes the command with the node derived*/
    public SendBackwardCommand(PositionManagerReceiver positionManagerReceiver){
       this.node = positionManagerReceiver.getShape().getNode();
       this.positionManagerReceiver = positionManagerReceiver;
    }

    /**
     * Executes the SendBackward command, which*/
    @Override
    public void executeCommand() {
        // Get container of the node
        if (node.getParent() instanceof Pane) {
            drawingPane = (Pane) node.getParent();
            oldIndex = drawingPane.getChildren().indexOf(node);
            // Send node backward
            positionManagerReceiver.toBack();
        } else {
            throw new IllegalStateException("Node does not have a Pane parent");
        }
    }

    /**
     * Reverts the changes made by the execution of the send-backward command.
     * This method restores the node to its original position in the node list
     * of its parent container (Pane), effectively undoing the reordering action.
     *
     * The undo operation is performed only if the parent container (drawingPane)
     * is not null. The following steps are executed:
     * - The node is removed from its current position in the child list of the pane.
     * - The node is re-added to its original index, restoring its initial position.
     *
     * This method ensures that the node's position within the z-order of the
     * parent container is reverted to the state prior to the execution of
     * the send-backward command.
     */
    @Override
    public void undoCommand(){
        if (drawingPane != null) {
            drawingPane.getChildren().remove(node);             // remove from actual position
            drawingPane.getChildren().add(oldIndex, node);     // reset the original position
        }
    }

}
