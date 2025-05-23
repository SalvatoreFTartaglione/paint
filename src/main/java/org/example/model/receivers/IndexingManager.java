package org.example.model.receivers;

import javafx.scene.Node;

public class IndexingManager {

    private Node node;

    public IndexingManager(Node node) {
        this.node = node;
    }

    public void toBack(){
        this.node.toBack();
    }

    public void toFront(){
        this.node.toFront();
    }
}
