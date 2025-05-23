package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.example.model.receivers.IndexingManager;

public class BringForwardCommand implements CommandInterface{

    private IndexingManager indexingManger;
    private final Node node;
    private final Pane drawingPane;
    private int oldIndex;

    public BringForwardCommand(Node node, Pane drawingPane){
        this.node = node;
        indexingManger = new IndexingManager(node);
        this.drawingPane = drawingPane;
    }

    @Override
    public void executeCommand() {
        oldIndex = drawingPane.getChildren().indexOf(node);
        indexingManger.toBack();

    }

    @Override
    public void undoCommand(){
        drawingPane.getChildren().remove(node);                  // remove from actual position
        drawingPane.getChildren().add(oldIndex, node);           // reset to original position
    }
}
