package org.example.model.receivers;

import javafx.geometry.Point2D;
import org.example.model.factory.product.ShapeInterface;

/**
 * The DraggingReceiver class is responsible for handling shape movement
 * and restoration of original position for shapes implementing the ShapeInterface.
 * It enables movement of a shape by specified deltas and resetting it to a predefined position.
 */
public class DraggingReceiver {

    /**
     * Constructs an instance of the DraggingReceiver class.
     * This class is responsible for handling the movement of shapes
     * that implement the ShapeInterface and restoring their original positions.
     * It serves as the primary mechanism to manipulate shape positioning
     * through specified coordinate deltas.
     */
    public DraggingReceiver(){
    }

    /**
     * Moves the specified shape by the given deltas in the X and Y directions.
     *
     * @param shape the shape to be moved; must implement the ShapeInterface
     * @param deltaX the amount to move the shape along the X-axis
     * @param deltaY the amount to move the shape along the Y*/
    public void moveShape(ShapeInterface shape, double deltaX, double deltaY){
        shape.moveBy(deltaX, deltaY);
    }

    /**
     * Resets the position of the given shape to its original position.
     *
     * @param shape the shape whose position is to be reset; must implement the ShapeInterface
     * @param originalPosition the original position to which the shape should be reset
     */
    public void resetShapePosition(ShapeInterface shape, Point2D originalPosition){
        Point2D currentPos = new Point2D(shape.getX(), shape.getY());
        double undoDeltaX = originalPosition.getX() - currentPos.getX();
        double undoDeltaY = originalPosition.getY() - currentPos.getY();
        shape.moveBy(undoDeltaX, undoDeltaY);
    }


}
