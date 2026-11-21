package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Optional;

/**
 * A class representing*/
public class EllipseShape implements ShapeInterface {

    private Ellipse ellipseShape;

    /**
     * Constructs an EllipseShape with the specified stroke color, fill color, stroke width, width, height, and rotation angle.
     *
     * @param stroke the color of the stroke for the ellipse
     */
    public EllipseShape(Color stroke, Color fill, double strokeWidth, double width, double height, double angle) {
        this.ellipseShape = new Ellipse();
        ellipseShape.setStroke(stroke);
        ellipseShape.setFill(fill);
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
        ellipseShape.setStrokeWidth(strokeWidth);
        ellipseShape.setRotate(angle);
    }

    /**
     *
     */
    public void draw(double x, double y) {
        ellipseShape.setCenterX(x);
        ellipseShape.setCenterY(y);
    }

    /**
     * Determines if the shape supports a*/
    @Override
    public boolean canHaveFill() {
        return true;
    }

    /**
     * Draws the shape based on the provided array of points.
     * The specific implementation defines how the points are handled
     * to render the shape visually.
     *
     * @param points an array of Double values representing the coordinates
     *               or parameters required to draw the shape.
     */
    @Override
    public void draw(Double[] points) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Creates and returns a copy of this EllipseShape instance with the
     * same properties and state.
     *
     * @return a new instance of EllipseShape that is a copy of this shape
     */
    @Override
    public ShapeInterface cloneShape() {
        EllipseShape clone = new EllipseShape(
                this.getStrokeColor(),
                this.getFillColor(),
                this.getCustomStrokeWidth(),
                this.getWidth(),
                this.getHeight(),
                this.getAngle()
        );

        clone.setX(this.getX());
        clone.setY(this.getY());
        return clone;
    }

    /**
     * Moves the ellipse by the specified delta values along the x and y axes.
     *
     * @param deltaX the amount to move the ellipse along the x-axis
     * @param deltaY the amount to move the ellipse along the y-axis
     */
    @Override
    public void moveBy(double deltaX, double deltaY) {
        ellipseShape.setCenterX(ellipseShape.getCenterX() + deltaX);
        ellipseShape.setCenterY(ellipseShape.getCenterY() + deltaY);
    }

    /**
     * Determines if the ellipse can be moved by the specified delta values
     * along the x and y axes without exceeding the pane boundaries, considering
     * the zoom level and margin.
     *
     * @param deltaX the change in the x-coordinate
     * @param deltaY the change in the y-coordinate
     * @param paneWidth the width of the pane
     * @param paneHeight the height of the pane
     * @param zoom the current zoom level
     * @param margin the margin to be maintained from the pane edges
     * @return true if the ellipse can be moved within the specified constraints, false otherwise
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
     * Adjusts the vertical position of the ellipse to create a mirrored effect
     * across the centerline extending upwards from its current position.
     *
     * This method first calculates a point located above the current center
     * of the ellipse by subtracting the ellipse's height. It then determines
     * the distance between the current center and the calculated point. Using
     * this distance, it computes the new mirrored vertical coordinate and
     * updates the center position of the ellipse accordingly.
     */
    @Override
    public void mirrorUp() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointYUpFromCenterOfEllipse = new Point2D(centerOfEllipse.getX(), centerOfEllipse.getY() - (getHeight()));

        double distance = centerOfEllipse.getY() - pointYUpFromCenterOfEllipse.getY();
        double mirroredY = pointYUpFromCenterOfEllipse.getY() - distance;

        ellipseShape.setCenterY(mirroredY);
    }

    /**
     * Mirrors the vertical position of the ellipse downward, creating a symmetric effect below its current center point.
     *
     * The method calculates a point directly below the current center of the ellipse at a distance equal to its height.
     * Using this point, it computes the mirrored vertical position and updates the*/
    @Override
    public void mirrorDown() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointXDownFromCenterOfEllipse = new Point2D(centerOfEllipse.getX(), centerOfEllipse.getY() + (getHeight()));

        double distance = pointXDownFromCenterOfEllipse.getY() - centerOfEllipse.getY();
        double mirroredY = pointXDownFromCenterOfEllipse.getY() + distance;

        ellipseShape.setCenterY(mirroredY);
    }

    /**
     * Mirrors the position of the ellipse to the left of its current center point, creating a symmetric effect.
     *
     * This method calculates a point located to the left of the*/
    @Override
    public void mirrorLeft() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointLeft = new Point2D(centerOfEllipse.getX() - getWidth(), centerOfEllipse.getY());

        double distance = centerOfEllipse.getX() - pointLeft.getX();
        double mirroredX = pointLeft.getX() - distance;

        ellipseShape.setCenterX(mirroredX);
    }

    /**
     * Mirrors the position of the ellipse to the right of its current center point,
     * creating a symmetric effect.
     *
     * The method calculates a point located to the right of the ellipse's center
     * by adding the width of the ellipse to the center's x-coordinate.
     * It then determines the distance between this point and the center and uses
     * the distance to compute the new mirrored x-coordinate. Finally, it updates
     */
    @Override
    public void mirrorRight() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointRight = new Point2D(centerOfEllipse.getX() + getWidth(), centerOfEllipse.getY());

        double distance = pointRight.getX() - centerOfEllipse.getX();
        double mirroredX = pointRight.getX() + distance;

        ellipseShape.setCenterX(mirroredX);
    }

    /**
     * Sets the horizontal scale factor for the ellipse shape.
     *
     * @param scale*/
    @Override
    public void setScaleX(double scale) {
        ellipseShape.setScaleX(scale);
    }

    /**
     * Retrieves the horizontal scaling factor for the ellipse shape.
     * This factor indicates how much the shape is stretched or compressed
     * along the x-axis.
     *
     * @return the current horizontal scale factor of the ellipse
     */
    @Override
    public double getScaleX() {
        return ellipseShape.getScaleX();
    }

    /**
     * Sets the vertical scale factor for the ellipse shape.
     *
     * @param scale the factor by which the height of the ellipse shape is scaled
     */
    @Override
    public void setScaleY(double scale) {
        ellipseShape.setScaleY(scale);
    }

    /**
     * Retrieves the vertical scaling factor for the ellipse shape.
     * This factor indicates how much the shape is stretched or compressed
     * along the y-axis.
     *
     * @return the current vertical scale factor of the ellipse
     */
    @Override
    public double getScaleY() {
        return ellipseShape.getScaleY();
    }

    /**
     * Mirrors the horizontal scaling of the ellipse around its center.
     *
     * This method inverts the horizontal scale of the ellipse by*/
    @Override
    public void mirrorHorizontallyAroundCenter() {
        ellipseShape.setScaleX(ellipseShape.getScaleX() * -1);
    }

    /**
     * Mirrors the vertical scale of the ellipse around its center.
     *
     * This method inverts the vertical scaling factor of the ellipse shape
     * by multiplying its current vertical scale (scaleY) by -1. This operation
     * reverses the vertical orientation of the shape across its center
     * without affecting its position or other properties.
     */
    @Override
    public void mirrorVerticallyAroundCenter() {
        ellipseShape.setScaleY(ellipseShape.getScaleY() * -1);
    }

    /**
     * Simulates the mirroring of the ellipse's position based on the specified mirror
     * direction and returns the resulting coordinates as a {@link Point2D}.
     *
     * The method calculates a new position*/
    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double centerX = getX();
        double centerY = getY();
        double mirroredX = centerX;
        double mirroredY = centerY;

        switch (direction) {
            case UP:
                mirroredY = centerY - 2 * getHeight();
                break;

            case DOWN:
                mirroredY = centerY + 2 * getHeight();
                break;

            case LEFT:
                mirroredX = centerX - 2 * getWidth();
                break;

            case RIGHT:
                mirroredX = centerX + 2 * getWidth();
                break;

            case HORIZONTAL:
            case VERTICAL:
                return Optional.of(new Point2D(centerX, centerY));
        }

        return Optional.of(new Point2D(mirroredX, mirroredY));

    }


    /**
     * Retrieves the stroke color of the ellipse shape.
     *
     * @return the current stroke color as a {@link Color} object
     */
    @Override
    public Color getStrokeColor() {
       return (Color) ellipseShape.getStroke();
    }

    /**
     * Sets the stroke color for the ellipse shape.
     *
     * @param stroke the color to set as the stroke for the ellipse
     */
    @Override
    public void setStrokeColor(Color stroke) {
        ellipseShape.setStroke(stroke);
    }

    /**
     * Retrieves the fill color of the ellipse shape.
     *
     * @return the current fill color as a {@link Color} object
     */
    @Override
    public Color getFillColor() {
        return (Color) ellipseShape.getFill();
    }

    /**
     * Sets the fill color for the ellipse shape.
     *
     * @param fill the color to set as the fill for the ellipse
     */
    @Override
    public void setFillColor(Color fill){
        ellipseShape.setFill(fill);
    }

    /**
     * Retrieves the x-coordinate of the center of the ellipse.
     *
     * @return the x-coordinate of the ellipse's center as a double
     */
    @Override
    public double getX() {
        return ellipseShape.getCenterX();
    }

    /**
     * Updates the x-coordinate of the center of the ellipse shape.
     *
     * @param x the new x-coordinate to set for the ellipse's center
     */
    @Override
    public void setX(double x) {
       ellipseShape.setCenterX(x);
    }

    /**
     * Retrieves the y-coordinate of the center of the ellipse.
     *
     * @return the y-coordinate of the ellipse's center as a double
     */
    @Override
    public double getY() {
        return ellipseShape.getCenterY();
    }

    /**
     *
     */
    @Override
    public void setY(double y) {
        ellipseShape.setCenterY(y);
    }

    /**
     * Retrieves the horizontal radius (half the width) of the ellipse shape.
     *
     * @return the horizontal radius of the ellipse as a double
     */
    @Override
    public double getWidth() {
        return ellipseShape.getRadiusX();
    }

    /**
     * Retrieves the height of the ellipse shape, which corresponds to the vertical
     * radius of the ellipse.
     *
     * @return the vertical radius (height*/
    @Override
    public double getHeight() {
        return ellipseShape.getRadiusY();
    }

    /**
     * Sets the size of the ellipse by updating its horizontal and vertical radii.
     *
     * @param width the new horizontal radius (half the width) of the ellipse
     * @param height the new vertical radius (half the height) of the ellipse
     */
    @Override
    public void setSize(double width, double height){
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
    }

    /**
     * Resizes the ellipse while maintaining its aspect ratio. The new size is applied
     * based on the larger dimension of the current width or height.
     *
     * @param newSize the desired size to which the ellipse should be resized
     */
    @Override
    public void resize(double newSize) {
        // Mantieni l'aspect ratio dell'ellisse
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            setSize(getWidth() * scale, getHeight() * scale);
        } else {
            setSize(newSize, newSize);
        }
    }

    /**
     * Retrieves the stretch width of the ellipse shape.
     * The stretch width represents the horizontal scaling of the ellipse,
     * typically corresponding to its current width or scaled width.
     *
     * @return the stretch width of the ellipse as a double
     */
    @Override
    public double getStretchWidth() {
        return this.getWidth();
    }

    /**
     * Retrieves the stretch height of the ellipse shape.
     * The stretch height represents the vertical scaling of the ellipse,
     * typically corresponding to its current height or scaled height.
     *
     * @return the stretch height of the ellipse as a double
     */
    @Override
    public double getStretchHeight() {
        return this.getHeight();
    }

    /**
     * Adjusts the size of the ellipse by setting new values for its radii.
     *
     * @param width  the new horizontal radius of the ellipse
     * @param height the new vertical radius of the ellipse
     */
    @Override
    public void stretch(double width, double height){
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
    }

    /**
     * Resets the stretch dimensions of a component to the specified width and height.
     *
     * @param width  the new width of the component
     * @param height the new height of the component
     */
    @Override
    public void resetStretch(double width, double height) {
        this.setSize(width, height);
    }


    /**
     * Retrieves the*/
    @Override
    public double getCustomStrokeWidth() {
        return ellipseShape.getStrokeWidth();
    }

    /**
     *
     * @param strokeWidth the stroke width to set
     */
    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        ellipseShape.setStrokeWidth(strokeWidth);
    }

    /**
     * Retrieves an array of points represented as Double objects.
     *
     * @return an array of Double objects containing points, or null if no points are available.
     */
    @Override
    public Double[] getPoints(){
        return null;
    }

    /**
     * Retrieves the node associated with the current object.
     *
     * @return the Node representing the ellipse shape
     */
    @Override
    public Node getNode() {
        return this.ellipseShape;
    }

    /**
     * Calculates the effective size of an object by determining the greater dimension
     * between its width and height. Specifically useful for shapes like circles or ellipses
     * where the larger diameter determines the effective size.
     *
     * @return the greater value between the object's width and height as a double.
     */
    @Override
    public double getEffectiveSize() {
        // For circles/ellipses use the larger diameter
        return Math.max(getWidth(), getHeight());
    }

    /**
     * Generates and returns a formatted label for the slider based on the provided value.
     *
     * @param value the numeric value*/
    @Override
    public String getSliderLabel(double value) {
        return String.format("Diameter: %.1f", value);
    }

    /**
     * Retrieves the text content associated with this method.
     *
     * @return a string representing the text content, or null if no text is available
     */
    @Override
    public String getText() {
        return null;
    }

    /**
     * Sets the text content for the operation.
     *
     * @param content the text to be set
     * @throws UnsupportedOperationException if the operation is not supported
     */
    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Sets the rotation angle for the ellipse shape.
     *
     * @param angle the angle, in degrees, to rotate the ellipse shape
     */
    @Override
    public void setAngle(double angle) {
        ellipseShape.setRotate(angle);
    }

    /**
     * Retrieves the rotation angle of the ellipse shape.
     *
     * @return the rotation angle of the ellipse shape in degrees.
     */
    @Override
    public double getAngle() {
        return ellipseShape.getRotate();
    }

    /**
     * Returns a string representation of the Ellipse object, including its stroke, fill, stroke width,
     * radius values, center coordinates, and angle.
     *
     * @return a string containing the properties of the ellipse, separated by semicolons.
     */
    @Override
    public String toString() {
        return "Ellipse" + ";" + ellipseShape.getStroke() + ";" + ellipseShape.getFill() + ";" + ellipseShape.getStrokeWidth() + ";"
                + ellipseShape.getRadiusX() + ";" + ellipseShape.getRadiusY() + ";" + ellipseShape.getCenterX() + ";" +
                ellipseShape.getCenterY() + ";" + this.getAngle() + "\n";
    }
}
