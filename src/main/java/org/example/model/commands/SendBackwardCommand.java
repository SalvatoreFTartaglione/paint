package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.IndexingManager;

public class SendBackwardCommand implements CommandInterface{

    private IndexingManager indexingManager ;
    private final Node node;
    private Pane drawingPane;
    private int oldIndex;

    public SendBackwardCommand(Node node, Pane drawingPane){
        this.node = node;
        indexingManager  = new IndexingManager(node);
    }

    @Override
    public void executeCommand() {
        // Get container of the node
        if (node.getParent() instanceof Pane) {
            drawingPane = (Pane) node.getParent();
            oldIndex = drawingPane.getChildren().indexOf(node);
            // Send node backward
            indexingManager.toBack();
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
