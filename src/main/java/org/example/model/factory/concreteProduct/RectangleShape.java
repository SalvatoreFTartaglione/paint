package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Optional;

/**
 * Represents a rectangle shape that implements the ShapeInterface.
 * This class provides methods to define, manipulate, and render a rectangle
 * within a graphical application. It supports operations such as resizing,
 * mirroring, moving, and scaling.
 */
public class RectangleShape implements ShapeInterface {

    private Rectangle rectangleShape;

    /**
     * Creates a new instance of a rectangle shape with the specified attributes.
     *
     * @param*/
    public RectangleShape(Color stroke, Color fill, double width, double height, double strokeWidth, double angle) {
        rectangleShape = new Rectangle();
        rectangleShape.setStroke(stroke);
        rectangleShape.setFill(fill);
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
        rectangleShape.setStrokeWidth(strokeWidth);
        rectangleShape.setRotate(angle);
    }

    /**
     * Draws the rectangle shape at the specified coordinates.
     *
     * @param x the x-coordinate where the rectangle should be drawn
     * @param y the y-coordinate where the rectangle should be drawn
     */
    @Override
    public void draw(double x, double y) {
        rectangleShape.setX(x);
        rectangleShape.setY(y);
    }

    /**
     * Draws the rectangle shape using the specified array of points.
     *
     * @param points an array of Double values representing the points used to draw the shape
     *               where the interpretation of the points depends on the implementation.
     */
    @Override
    public void draw(Double[] points) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Determines whether the rectangle shape can have a fill color.
     *
     * @return true if the*/
    @Override
    public boolean canHaveFill() {
        return true;
    }

    /**
     * Creates and returns a copy of the current rectangle shape instance.
     * The clone will have the same attributes such as stroke color, fill color, width,
     * height, custom stroke width, angle, and coordinates (x, y) as the original shape.
     *
     * @return a new ShapeInterface object that is a clone of the current rectangle shape
     */
    @Override
    public ShapeInterface cloneShape() {
        RectangleShape clone = new RectangleShape(
                this.getStrokeColor(),
                this.getFillColor(),
                this.getWidth(),
                this.getHeight(),
                this.getCustomStrokeWidth(),
                this.getAngle()
        );

        clone.setX(this.getX());
        clone.setY(this.getY());
        return clone;
    }

    /**
     * Moves the rectangle shape by a given horizontal and vertical distance.
     *
     * @param deltaX the horizontal distance by which to move the rectangle
     * @param deltaY the vertical distance by which to move the rectangle
     */
    @Override
    public void moveBy(double deltaX, double deltaY) {
        rectangleShape.setX(rectangleShape.getX() + deltaX);
        rectangleShape.setY(rectangleShape.getY() + deltaY);
    }

    /**
     * Determines whether the rectangle shape can move by the specified horizontal
     * and vertical distances (`deltaX` and `deltaY`) while remaining within the
     * bounds of a pane with the given dimensions, zoom level, and margin.
     *
     * @param deltaX the horizontal distance by which the rectangle may move
     * @param deltaY the vertical distance by which the rectangle may move
     * @param paneWidth the width of the pane within which the rectangle must stay
     * @param paneHeight the height of the pane within which the rectangle must stay
     * @param zoom the current zoom level affecting the dimensions
     * @param margin the margin inside the pane that the rectangle must respect
     * @return true if the rectangle can move by the given distances while remaining
     *         within the pane bounds, false otherwise
     */
    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double minX = (getWidth() / 2 + margin) / zoom;
        double maxX = (paneWidth - getWidth() / 2 - margin) / zoom;
        double minY = (getHeight() / 2 + margin) / zoom;
        double maxY = (paneHeight - getHeight() / 2 - margin) / zoom;

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }

    /**
     * Mirrors the rectangle shape vertically upwards by computing a mirrored position
     * based on the top edge of the shape. The method calculates the distance
     * from the center of the shape to the top edge, then applies this distance
     **/
    @Override
    public void mirrorUp() {
        double centerY = getY() + getHeight() / 2;
        double topY = getY();
        double distance = centerY - topY;
        double mirroredCenterY = topY - distance;
        double newY = mirroredCenterY - getHeight() / 2;

        rectangleShape.setY(newY);
    }

    /**
     * Mirrors the rectangle shape vertically downward by computing a mirrored position
     * based on the bottom edge of the shape. This method calculates the distance
     * from the center of the shape to the bottom edge and applies this distance
     * to determine the new vertical position such that the shape appears mirrored
     * below its original position.
     */
    @Override
    public void mirrorDown() {
        double centerY = getY() + getHeight() / 2;
        double bottomY = getY() + getHeight();
        double distance = bottomY - centerY;
        double mirroredCenterY = bottomY + distance;
        double newY = mirroredCenterY - getHeight() / 2;

        rectangleShape.setY(newY);
    }

    /**
     * Mirrors the rectangle shape horizontally to the*/
    @Override
    public void mirrorLeft() {
        double centerX = getX() + getWidth() / 2;
        double leftX = getX();
        double distance = centerX - leftX;
        double mirroredCenterX = leftX - distance;
        double newX = mirroredCenterX - getWidth() / 2;

        rectangleShape.setX(newX);
    }

    /**
     * Mirrors the*/
    @Override
    public void mirrorRight() {
        double centerX = getX() + getWidth() / 2;
        double rightX = getX() + getWidth();
        double distance = rightX - centerX;
        double mirroredCenterX = rightX + distance;
        double newX = mirroredCenterX - getWidth() / 2;

        rectangleShape.setX(newX);
    }

    /**
     * Sets the scale factor along the X-axis for the rectangle shape.
     *
     * @param scale the scale factor to be applied on the X-axis
     */
    @Override
    public void setScaleX(double scale) {
        rectangleShape.setScaleX(scale);
    }

    /**
     * Retrieves the horizontal scale factor for the rectangle shape.
     *
     * @return the scale factor along the X-axis.
     */
    @Override
    public double getScaleX() {
        return rectangleShape.getScaleX();
    }

    /**
     * Sets the vertical scale factor for the rectangle shape.
     *
     * @param scale the scale factor along the Y-axis, which determines the vertical
     *              stretching or shrinking of the rectangle shape
     */
    @Override
    public void setScaleY(double scale) {
        rectangleShape.setScaleY(scale);
    }

    /**
     * Retrieves the vertical scale factor for the rectangle shape.
     *
     */
    @Override
    public double getScaleY() {
        return rectangleShape.getScaleY();
    }

    /**
     * Mirrors the rectangle shape horizontally around its center.
     * This operation inverts the horizontal scale of the rectangle,
     * effectively flipping it along its vertical axis.
     *
     * The method achieves this by adjusting the scale factor on
     * the X-axis to the negative of its current value.
     */
    @Override
    public void mirrorHorizontallyAroundCenter() {
        rectangleShape.setScaleX(rectangleShape.getScaleX() * -1);
    }

    /**
     * Mirrors the rectangle shape vertically around its center.
     * This operation inverts the vertical scale of the rectangle,
     * effectively flipping it along its horizontal*/
    @Override
    public void mirrorVerticallyAroundCenter() {
        rectangleShape.setScaleY(rectangleShape.getScaleY() * -1);
    }

    /**
     * Simulates the mirroring of the rectangle shape in the specified direction and calculates the new position.
     * The method does not change the actual position of the shape but returns an {@code Optional<Point2D>}
     * representing the new top-left corner after mirroring.
     *
     * @param direction the direction of the mirroring operation. Valid options are:
     *                  {@code UP}, {@code DOWN}, {@code LEFT}, {@code RIGHT}, {@code VERTICAL}, and {@code HORIZONTAL}.
     * @return an optional containing the new top-left {@code Point2D} of the rectangle shape after the mirroring operation.
     *         For {@code VERTICAL} and {@code HORIZONTAL} directions,*/
    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double x = getX();
        double y = getY();
        double width = getWidth();
        double height = getHeight();

        double centerX = x + width / 2;
        double centerY = y + height / 2;

        double mirroredCenterX = centerX;
        double mirroredCenterY = centerY;

        switch (direction) {
            case UP: {
                double topY = y;
                double distance = centerY - topY;
                mirroredCenterY = topY - distance;
                break;
            }

            case DOWN: {
                double bottomY = y + height;
                double distance = bottomY - centerY;
                mirroredCenterY = bottomY + distance;
                break;
            }

            case LEFT: {
                double leftX = x;
                double distance = centerX - leftX;
                mirroredCenterX = leftX - distance;
                break;
            }

            case RIGHT: {
                double rightX = x + width;
                double distance = rightX - centerX;
                mirroredCenterX = rightX + distance;
                break;
            }

            case HORIZONTAL:
            case VERTICAL:
                return Optional.of(new Point2D(centerX, centerY));
        }

        // Calcolo nuovo angolo superiore sinistro
        double newX = mirroredCenterX - width / 2;
        double newY = mirroredCenterY - height / 2;

        return Optional.of(new Point2D(newX, newY));
    }


    /**
     * Retrieves the stroke color of the rectangle shape.
     *
     * @return the current*/
    @Override
    public Color getStrokeColor() {
        return (Color) rectangleShape.getStroke();
    }

    /**
     * Sets the stroke color for the rectangle shape.
     *
     * @param stroke the new stroke color to be applied to the rectangle shape
     */
    @Override
    public void setStrokeColor(Color stroke) {
        rectangleShape.setStroke(stroke);
    }

    /**
     * Retrieves the fill color of the rectangle shape.
     *
     * @return the current fill color*/
    @Override
    public Color getFillColor() {
        return (Color) rectangleShape.getFill();
    }

    /**
     * Sets the fill color of the rectangle shape.
     *
     * @param fill the new fill color to be applied to the rectangle shape
     */
    @Override
    public void setFillColor(Color fill){
        rectangleShape.setFill(fill);
    }

    /**
     * Retrieves the x-coordinate of the rectangle shape.
     *
     * @return the x-coordinate of the rectangle shape.
     */
    @Override
    public double getX() {
        return rectangleShape.getX();
    }

    /**
     * Sets the x-coordinate of the rectangle shape.
     *
     * @param x the new x-coordinate to be set for the rectangle shape
     */
    @Override
    public void setX(double x) {
        rectangleShape.setX(x);
    }

    /**
     * Retrieves the y-coordinate of the rectangle shape.
     *
     * @return the y-coordinate*/
    @Override
    public double getY() {
        return rectangleShape.getY();
    }

    /**
     * Sets the y-coordinate*/
    @Override
    public void setY(double y) {
        rectangleShape.setY(y);
    }

    /**
     **/
    @Override
    public double getWidth() {
        return rectangleShape.getWidth();
    }

    /**
     * Retrieves the height of the rectangle shape.
     *
     * @return the height of the*/
    @Override
    public double getHeight() {
        return rectangleShape.getHeight();
    }

    /**
     * Sets the size of the rectangle shape by updating its width and height.
     *
     * @param width  the new width of the rectangle
     * @param height the new height of the rectangle
     */
    @Override
    public void setSize(double width, double height){
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
    }

    /**
     * Retrieves the stretched width of the rectangle shape.
     *
     * @return the current width of the rectangle shape as calculated with stretching applied.
     */
    @Override
    public double getStretchWidth() {
        return this.getWidth();
    }

    /**
     * Retrieves the current height of the rectangle shape with any stretch modifications applied.
     *
     * @return the stretched height of the rectangle shape.
     */
    @Override
    public double getStretchHeight() {
        return this.getHeight();
    }

    /**
     * Adjusts the dimensions of the rectangle shape by setting its width and height.
     *
     * @param width  the new width to be applied to the rectangle shape
     * @param height the new height to be applied to the rectangle shape
     */
    @Override
    public void stretch(double width, double height){
        rectangleShape.setWidth(width);
        rectangleShape.setHeight(height);
    }

    /**
     * Resets the stretch of the rectangle shape by updating its width and height.
     *
     * @param width  the new width to reset the rectangle shape to
     * @param height the new height to reset the rectangle shape to
     */
    @Override
    public void resetStretch(double width, double height) {
        this.setSize(width, height);
    }


    /**
     */
    @Override
    public double getCustomStrokeWidth() {
        return rectangleShape.getStrokeWidth();
    }

    /**
     * Retrieves an array of points representing the corners or key geometry of the rectangle shape.
     *
     * @return an array of {@code Double} values where each pair represents the x and y coordinates of a point
     */
    @Override
    public Double[] getPoints(){
        return null;
    }

    /**
     * Sets the custom stroke width for the rectangle shape.
     *
     * @param strokeWidth the new stroke width to be applied to the rectangle shape
     */
    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        rectangleShape.setStrokeWidth(strokeWidth);
    }

    /**
     * Retrieves the node associated with this object.
     *
     * @return the Node instance, which in*/
    @Override
    public Node getNode() {
        return rectangleShape;
    }

    /**
     * Calculates and returns the effective size of an object.
     * For rectangles, it uses the greater dimension between the width and the height.
     *
     * @return the effective size of the object as a double, determined by the larger of the two dimensions.
     */
    @Override
    public double getEffectiveSize() {
        // Per i rettangoli, usa la dimensione maggiore
        return Math.max(getWidth(), getHeight());
    }

    /**
     * Resizes the rectangle while maintaining its aspect ratio.
     * If the current rectangle is degenerate (width and height are zero),
     * it becomes a square with the specified size.
     *
     * @param newSize the desired new size of the rectangle's longest dimension
     */
    @Override
    public void resize(double newSize) {
        // Per i rettangoli, mantieni l'aspect ratio
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            double newWidth = getWidth() * scale;
            double newHeight = getHeight() * scale;
            setSize(newWidth, newHeight);
        } else {
            // Se degenerato, crea un quadrato
            setSize(newSize, newSize);
        }
    }

    /**
     * Generates a formatted label for a slider based on the provided value.
     *
     * @param value the current value of the slider
     * @return a formatted string representing the slider label with the specified value
     */
    @Override
    public String getSliderLabel(double value) {
        return String.format("Dimension: %.1f", value);
    }

    /**
     * Retrieves the text representation for the current object or context.
     *
     * @return a string containing the text, or null if no text is available
     */
    @Override
    public String getText() {
        return null;
    }

    /**
     * Sets the text content for this instance. This method is not supported and will throw an UnsupportedOperationException.
     *
     * @param content the text content to set
     */
    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Sets the angle of rotation for the rectangle shape.
     *
     * @param angle the angle in degrees to rotate the rectangle shape
     */
    @Override
    public void setAngle(double angle) {
        rectangleShape.setRotate(angle);
    }

    /**
     * Retrieves the current angle of rotation for the rectangle shape.
     *
     * @return the angle of rotation in degrees as a double.
     */
    @Override
    public double getAngle() {
        return rectangleShape.getRotate();
    }

    /**
     * Returns a string representation of the Rectangle object. The string includes
     * the stroke, fill, stroke width, width, height, x and y position of the rectangle
     * along with its angle, formatted in a specific structure.
     *
     * @return a string representation of the rectangle object with its properties.
     */
    @Override
    public String toString() {
        return "Rectangle" + ";" + rectangleShape.getStroke() + ";" + rectangleShape.getFill() + ";"
                + rectangleShape.getStrokeWidth() + ";" + rectangleShape.getWidth() + ";"
                + rectangleShape.getHeight() + ";" + rectangleShape.getX() + ";" + rectangleShape.getY()  + ";"
                + this.getAngle() + '\n';
    }


}
