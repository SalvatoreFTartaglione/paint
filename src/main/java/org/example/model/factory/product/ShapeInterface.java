package org.example.model.factory.product;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.example.model.enums.MirrorDirectionEnum;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Interface representing a generic drawable shape with support for
 * transformation, color management, serialization, text handling,
 * mirroring, and persistence.
 */
public interface ShapeInterface {

    // ========= Clone =========

    /**
     * Creates and returns a deep copy of the shape.
     *
     * @return a new ShapeInterface that is a clone of the current shape
     */
    ShapeInterface cloneShape();

    // ========= Color =========

    /**
     * Sets the stroke (border) color of the shape.
     *
     * @param color the stroke color to apply
     */
    void setStrokeColor(Color color);

    /**
     * Returns the stroke (border) color of the shape.
     *
     * @return the stroke color
     */
    Color getStrokeColor();

    /**
     * Indicates whether the shape supports fill color.
     *
     * @return {@code true} if the shape can be filled, {@code false} otherwise
     */
    boolean canHaveFill();

    /**
     * Sets the fill color of the shape.
     *
     * @param fill the fill color to apply
     */
    void setFillColor(Color fill);

    /**
     * Gets the fill color of the shape.
     *
     * @return the current fill color
     */
    Color getFillColor();

    /**
     * Sets a custom stroke width for the shape.
     *
     * @param strokeWidth the stroke width to set
     */
    void setCustomStrokeWidth(double strokeWidth);

    /**
     * Returns the custom stroke width of the shape.
     *
     * @return the current stroke width
     */
    double getCustomStrokeWidth();

    // ========= Children (Composite) =========

    /**
     * Returns a list of child shapes for composite shapes.
     * For simple shapes, this returns an empty list.
     *
     * @return list of child shapes
     */
    default List<ShapeInterface> getChildren() {
        return Collections.emptyList();
    }

    // ========= Geometry: Resize & Stretch =========

    /**
     * Returns the width of the shape.
     *
     * @return the width
     */
    double getWidth();

    /**
     * Returns the height of the shape.
     *
     * @return the height
     */
    double getHeight();

    /**
     * Resizes the shape proportionally to a new size.
     *
     * @param newSize the new size to apply
     */
    void resize(double newSize);

    /**
     * Sets the width and height of the shape.
     *
     * @param width  the new width
     * @param height the new height
     */
    void setSize(double width, double height);

    /**
     * Returns the current stretched width of the shape.
     *
     * @return the stretched width
     */
    double getStretchWidth();

    /**
     * Returns the current stretched height of the shape.
     *
     * @return the stretched height
     */
    double getStretchHeight();

    /**
     * Applies stretching to the shape.
     *
     * @param width  the stretch width
     * @param height the stretch height
     */
    void stretch(double width, double height);

    /**
     * Resets stretching applied to the shape.
     *
     * @param width  the original width
     * @param height the original height
     */
    void resetStretch(double width, double height);

    /**
     * Returns the effective size of the shape used for scaling purposes.
     *
     * @return the effective size
     */
    double getEffectiveSize();

    /**
     * Returns the maximum allowed value for a size slider.
     *
     * @return max slider value
     */
    default double getMaxSliderSize() {
        return 350;
    }

    /**
     * Returns the minimum allowed value for a size slider.
     *
     * @return min slider value
     */
    default double getMinSliderSize() {
        return Math.max(1, getEffectiveSize() * 0.1);
    }

    /**
     * Formats a value to be displayed as slider label.
     *
     * @param value the value to format
     * @return formatted label string
     */
    default String getSliderLabel(double value) {
        return String.format("%.1f", value);
    }

    // ========= Drawing & Position =========

    /**
     * Returns the X coordinate of the shape.
     *
     * @return X coordinate
     */
    double getX();

    /**
     * Returns the Y coordinate of the shape.
     *
     * @return Y coordinate
     */
    double getY();

    /**
     * Sets the X coordinate of the shape.
     *
     * @param X the new X coordinate
     */
    void setX(double X);

    /**
     * Sets the Y coordinate of the shape.
     *
     * @param Y the new Y coordinate
     */
    void setY(double Y);

    /**
     * Returns the list of points that define the shape geometry.
     *
     * @return array of point coordinates
     */
    Double[] getPoints();

    /**
     * Draws the shape at a given (X, Y) position.
     *
     * @param X the X coordinate
     * @param Y the Y coordinate
     */
    void draw(double X, double Y);

    /**
     * Draws the shape using a list of points.
     *
     * @param points the point array
     */
    void draw(Double[] points);

    /**
     * Sets the point list for the shape. Default is no-op.
     *
     * @param points array of points to set
     */
    default void setPoints(Double[] points) {
        return;
    }

    // ========= Serialization =========

    /**
     * Returns a string representation of the shape for persistence.
     *
     * @return string representation
     */
    @Override
    String toString();

    // ========= JavaFX Node Access =========

    /**
     * Returns the JavaFX {@code Node} representing the shape.
     *
     * @return the visual node
     */
    Node getNode();

    // ========= Movement =========

    /**
     * Moves the shape by a given delta in X and Y directions.
     *
     * @param deltaX horizontal offset
     * @param deltaY vertical offset
     */
    void moveBy(double deltaX, double deltaY);

    /**
     * Returns the current shape position as a {@code Point2D}.
     *
     * @return current position
     */
    default Point2D getRestorePosition() {
        return new Point2D(getX(), getY());
    }

    /**
     * Restores the shape position to the specified point.
     *
     * @param point the point to restore to
     */
    default void setRestorePosition(Point2D point) {
        draw(point.getX(), point.getY());
    }

    // ========= Text =========

    /**
     * Gets the text associated with the shape.
     *
     * @return text content
     */
    String getText();

    /**
     * Sets the text for the shape.
     *
     * @param content the text to set
     */
    void setText(String content);

    // ========= Mirroring =========

    /**
     * Checks if the shape can be moved within given boundaries.
     *
     * @param deltaX     horizontal movement
     * @param deltaY     vertical movement
     * @param paneWidth  width of the drawing pane
     * @param paneHeight height of the drawing pane
     * @param zoom       current zoom level
     * @param margin     margin constraint
     * @return true if movement is allowed, false otherwise
     */
    boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin);

    /**
     * Mirrors the shape vertically upwards.
     */
    void mirrorUp();

    /**
     * Mirrors the shape vertically downwards.
     */
    void mirrorDown();

    /**
     * Mirrors the shape horizontally to the left.
     */
    void mirrorLeft();

    /**
     * Mirrors the shape horizontally to the right.
     */
    void mirrorRight();

    /**
     * Simulates a mirror operation and returns the resulting point if applicable.
     *
     * @param direction the mirror direction
     * @return an optional result point of the mirrored shape
     */
    Optional<Point2D> simulateMirror(MirrorDirectionEnum direction);

    /**
     * Mirrors the shape horizontally around its center.
     */
    void mirrorHorizontallyAroundCenter();

    /**
     * Mirrors the shape vertically around its center.
     */
    void mirrorVerticallyAroundCenter();

    // ========= Scale =========

    /**
     * Sets the scale on the X axis.
     *
     * @param scale the scale factor
     */
    void setScaleX(double scale);

    /**
     * Gets the scale on the X axis.
     *
     * @return scale factor
     */
    double getScaleX();

    /**
     * Sets the scale on the Y axis.
     *
     * @param scale the scale factor
     */
    void setScaleY(double scale);

    /**
     * Gets the scale on the Y axis.
     *
     * @return scale factor
     */
    double getScaleY();

    // ========= Rotation =========

    /**
     * Sets the rotation angle of the shape.
     *
     * @param angle the angle in degrees
     */
    void setAngle(double angle);

    /**
     * Gets the current rotation angle of the shape.
     *
     * @return angle in degrees
     */
    double getAngle();
}
