package org.example.model.commands.receivers;

import javafx.geometry.Point2D;
import org.example.model.factory.product.ShapeInterface;

public class DraggingReceiver {

    public DraggingReceiver(){
    }

    public void moveShape(ShapeInterface shape, double deltaX, double deltaY){
        shape.moveBy(deltaX, deltaY);
    }

    public void resetShapePosition(ShapeInterface shape, Point2D originalPosition){
        Point2D currentPos = new Point2D(shape.getX(), shape.getY());
        double undoDeltaX = originalPosition.getX() - currentPos.getX();
        double undoDeltaY = originalPosition.getY() - currentPos.getY();
        shape.moveBy(undoDeltaX, undoDeltaY);
    }


}
