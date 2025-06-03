package org.example.model.commands.receivers;

import javafx.geometry.Point2D;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.List;
import java.util.Optional;

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

    public void resize(double newSize){
        /*if (shape.isGroup()) {
            oldChildSizes = shape.getChildren().stream().map(ShapeInterface::getEffectiveSize).collect(Collectors.toList());
        }*/
        shape.resize(newSize);
    }

    public void stretch(double newWidth, double newHeight){
        shape.stretch(newWidth, newHeight);
    }

    public void resetResize(double oldWidth, double oldHeight){
        shape.setSize(oldWidth, oldHeight);
    }

    public void resetStretch(double oldWidth, double oldHeight){
        shape.resetStretch(oldWidth, oldHeight);
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

    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {return shape.simulateMirror(direction);}

    public void setAngle(double angle){
        shape.getNode().setRotate(angle);
    }

}
