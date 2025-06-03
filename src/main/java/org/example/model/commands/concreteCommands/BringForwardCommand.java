package org.example.model.commands.concreteCommands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.PositionManagerReceiver;

public class BringForwardCommand implements CommandInterface {

    private PositionManagerReceiver positionManagerReceiver;
    private Pane drawingPane;
    private int oldIndex;
    private Node node;

    public BringForwardCommand(PositionManagerReceiver positionManagerReceiver){
        this.node = positionManagerReceiver.getShape().getNode();
        this.positionManagerReceiver = positionManagerReceiver;
    }

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

    @Override
    public void undoCommand(){
        if (drawingPane != null) {
            drawingPane.getChildren().remove(node);             // remove from actual position
            drawingPane.getChildren().add(oldIndex, node);     // reset the original position
        }
    }
}
