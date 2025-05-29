package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.PositionManagerReceiver;

public class SendBackwardCommand implements CommandInterface{

    private PositionManagerReceiver positionManagerReceiver;
    private Node node;
    private Pane drawingPane;
    private int oldIndex;

    public SendBackwardCommand(PositionManagerReceiver positionManagerReceiver){
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
            positionManagerReceiver.toBack();
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
