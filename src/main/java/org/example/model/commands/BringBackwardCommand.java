package org.example.model.commands;

import javafx.scene.Node;
import org.example.model.receivers.IndexingManager;

public class BringBackwardCommand implements CommandInterface{

    private Node node;

    public BringBackwardCommand(Node node) {
        this.node = node;
    }

    public void executeCommand(){
        IndexingManager indexingManager = new IndexingManager(node);
        indexingManager.toBack();
    }

    public void undoCommand(){

    }

}
