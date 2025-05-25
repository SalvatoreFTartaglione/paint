package org.example.model.receivers;

import javafx.scene.Node;
import org.example.model.shape.ShapeInterface;

public class PoistionManagerReceiver {

    private ShapeInterface shape;

    public PoistionManagerReceiver(ShapeInterface shape) {
        this.shape = shape;
    }

    public ShapeInterface getShape() {
        return shape;
    }

    public void setShape(ShapeInterface shape) {
        this.shape = shape;
    }

    public void toBack(){
        shape.getNode().toBack();
    }

    public void toFront(){
        shape.getNode().toFront();
    }

    public void resize(double width, double height){
        shape.setSize(width, height);
    }
}
