package org.example.model.receivers;

import org.example.model.shape.ShapeInterface;

public class PositionManagerReceiver {

    private ShapeInterface shape;

    public PositionManagerReceiver(ShapeInterface shape) {
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

    public void mirrorUp(){
        shape.mirrorUp();
    }

    public void mirrorDown(){
        shape.mirrorDown();
    }

    public void mirrorLeft(){
        shape.mirrorLeft();
    }

    public void mirrorRight(){
        shape.mirrorRight();
    }
    public void mirrorHorizontal(){
        shape.mirrorHorizontallyAroundCenter();
    }
    public void mirrorVertical(){
        shape.mirrorVerticallyAroundCenter();
    }
}
