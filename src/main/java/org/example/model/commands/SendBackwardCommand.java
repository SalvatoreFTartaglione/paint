package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.PoistionManagerReceiver;
import org.example.model.shape.ShapeInterface;

public class SendBackwardCommand implements CommandInterface{

    private PoistionManagerReceiver poistionManagerReceiver;
    private Node node;
    private Pane drawingPane;
    private int oldIndex;

    public SendBackwardCommand(PoistionManagerReceiver poistionManagerReceiver){
       this.node = poistionManagerReceiver.getShape().getNode();
       this.poistionManagerReceiver = poistionManagerReceiver;
    }

    @Override
    public void executeCommand() {
        // Get container of the node
        if (node.getParent() instanceof Pane) {
            drawingPane = (Pane) node.getParent();
            oldIndex = drawingPane.getChildren().indexOf(node);
            // Send node backward
            poistionManagerReceiver.toBack();
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
