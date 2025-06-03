package org.example.model.receivers;

import javafx.geometry.Point2D;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.List;
import java.util.Optional;

/**
 * The PositionManagerReceiver class is responsible for managing transformations
 * and position-related operations on a ShapeInterface instance. It provides
 * methods for manipulating the shape's visual properties such as resizing,
 * stretching, mirroring, and rotating. This class acts as a receiver in case
 * of using a command-like design pattern and delegates operations to the
 * underlying ShapeInterface.
 */
public class PositionManagerReceiver {

    private ShapeInterface shape;

    /**
     * Constructs a new PositionManagerReceiver with the specified ShapeInterface instance.
     * This*/
    public PositionManagerReceiver(ShapeInterface shape) {
        this.shape = shape;
    }

    /**
     * Retrieves the current ShapeInterface instance associated with this object.
     * This shape can be manipulated or queried to perform operations such as resizing,
     * mirroring, or rotating.
     *
     * @return the current ShapeInterface instance managed by this object
     */
    public ShapeInterface getShape() {
        return shape;
    }

    /**
     * Sets the current ShapeInterface instance to the specified shape.
     * This method updates the internal reference of this class to manage
     * and perform operations on the new shape.
     *
     * @param shape the ShapeInterface instance to be assigned and managed
     */
    public void setShape(ShapeInterface shape) {
        this.shape = shape;
    }

    /**
     * Moves the visual representation of the shape to the back of the drawing order
     * in its container. This affects the stacking order of the graphical elements,
     * ensuring that this shape is rendered behind other overlapping elements.
     */
    public void toBack(){
        shape.getNode().toBack();
    }

    /**
     * Brings the visual representation of the associated shape to the front of the
     * drawing order in its container. This operation ensures that the shape is rendered
     * above any other overlapping elements, affecting the display stack order in the UI.
     */
    public void toFront(){
        shape.getNode().toFront();
    }

    public void resize(double newSize){
        /*if (shape.isGroup()) {
            oldChildSizes = shape.getChildren().stream().map(ShapeInterface::getEffectiveSize).collect(Collectors.toList());
        }*/
        shape.resize(newSize);
    }

    /**
     * Stretches the associated shape to the specified dimensions by modifying
     * its width and height. This method delegates the stretch operation to the
     * shape's internal implementation.
     *
     * @param newWidth  the new width of the shape after stretching
     * @param newHeight the new height of the shape after stretching
     */
    public void stretch(double newWidth, double newHeight){
        shape.stretch(newWidth, newHeight);
    }

    /**
     * Resets the size of the associated shape to its previous dimensions.
     * This method adjusts the width and height of the shape using the provided
     * parameters, effectively reverting any prior resizing operations.
     *
     * @param oldWidth  the original width to reset the shape to
     * @param oldHeight the original height to reset the shape to
     */
    public void resetResize(double oldWidth, double oldHeight){
        shape.setSize(oldWidth, oldHeight);
    }

    /**
     * Reverts any stretching applied to the associated shape by resetting its dimensions
     * to the specified original width and height. This operation effectively restores
     * the initial size of the shape prior to any stretch transformations.
     *
     * @param oldWidth  the original width to reset the shape to
     * @param oldHeight the original height to reset the shape to
     */
    public void resetStretch(double oldWidth, double oldHeight){
        shape.resetStretch(oldWidth, oldHeight);
    }

    /**
     * Mirrors the associated shape vertically upwards.
     *
     * This method invokes the `mirrorUp` operation on the managed `ShapeInterface` instance,
     * altering its graphical representation by reflecting it along a horizontal axis.
     * The operation maintains the general alignment of the shape but flips its content
     * vertically about the defined axis.
     */
    public void mirrorUp(){
        shape.mirrorUp();
    }

    /**
     * Mirrors the associated shape vertically downwards.
     *
     * This method delegates the mirroring operation to the `mirrorDown` function
     * of the underlying `ShapeInterface` instance. The operation alters the
     * graphical representation of the shape by flipping it vertically along a
     * horizontal*/
    public void mirrorDown(){
        shape.mirrorDown();
    }

    /**
     * Mirrors the associated shape horizontally to the left.
     *
     * This method delegates the operation to the `mirrorLeft` function
     * of the managed `ShapeInterface` instance. It alters the graphical
     * representation of the shape by reflecting it along a vertical axis,
     * effectively flipping the content to its left-hand side.
     */
    public void mirrorLeft(){
        shape.mirrorLeft();
    }

    /**
     * Mirrors the associated shape horizontally to the right.
     *
     * This method invokes the `mirrorRight` operation on the managed `ShapeInterface` instance,
     * flipping its graphical representation along a vertical axis. The operation effectively
     * mirrors the shape's content to its right-hand side while maintaining its alignment.
     */
    public void mirrorRight(){
        shape.mirrorRight();
    }

    /**
     * Mirrors the associated shape horizontally around its center.
     *
     * This method invokes the `mirrorHorizontallyAroundCenter` function of the
     * underlying `ShapeInterface` instance. The operation reflects the shape's
     * graphical representation horizontally along its center axis, effectively
     * inverting its content from left to right.
     */
    public void mirrorHorizontal(){
        shape.mirrorHorizontallyAroundCenter();
    }

    /**
     * Mirrors the associated shape vertically around its center axis.
     *
     * This method invokes the `mirrorVerticallyAroundCenter` function on the
     * managed `ShapeInterface` instance. The operation reflects the shape's
     * graphical representation along a vertical axis passing through its center,
     * effectively flipping the shape's content while preserving its alignment
     * relative to the center line.
     */
    public void mirrorVertical(){
        shape.mirrorVerticallyAroundCenter();
    }

    /**
     * Simulates the mirror operation for the associated shape*/
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {return shape.simulateMirror(direction);}

    /**
     * Sets the rotation angle for the associated shape.
     * This method modifies the orientation of the shape by applying
     * the specified rotation angle in degrees.
     *
     * @param angle the rotation angle, in degrees, to be applied to the shape
     */
    public void setAngle(double angle){
        shape.getNode().setRotate(angle);
    }

}
