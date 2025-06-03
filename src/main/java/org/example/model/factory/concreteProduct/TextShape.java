package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Optional;

/**
 * Represents a text-based shape that implements the ShapeInterface, allowing manipulation
 * and rendering of a text element with various properties such as position, size, color,
 * rotation, scaling, and mirroring.
 *
 * This class encapsulates a JavaFX Text node and provides advanced functionalities for managing
 * the graphical representation of text in a view, including dragging, resizing, and flipping along
 * both axes. It also supports settings for stroke and fill colors, scaling, and rotation.
 *
 * Features include:
 * 1. Managing text content with adjustable font sizes.
 * 2. Customizing appearance: stroke width, colors, and fill properties.
 * 3. Positional adjustments via absolute coordinates or relative movements.
 * 4. Resizing and stretching capabilities along width and height.
 * 5. Capability to reflect or mirror the text layout in vertical and horizontal directions.
 * 6. Constraints for valid movement within defined bounds.
 *
 * It supports interaction with a graphical interface through the Node object it represents
 * and ensures compatibility for cloning and drawing operations.
 */
public class TextShape implements ShapeInterface {

    private final Text textShape;
    private Double[] points = new Double[2]; // X, Y
    private double baseWidth;
    private double baseHeight;
    private boolean isFlippedX = false;
    private boolean isFlippedY = false;
    private double originalY = 0;
    private double originalScaleY = 0;

    /**
     * Constructs a new TextShape object with specified text content, stroke and fill colors,
     * dimensions, stroke width, scaling factors, and rotation angle.
     *
     * @param content the text to be displayed in the shape
     * @param strokeColor the color for the text's outline (stroke)
     * @param fillingColor the color for the text's interior (fill)
     * @param width the initial X-coordinate position of the text shape
     * @param height the initial Y-coordinate position of the text shape
     * @param strokeWidth the thickness of the text's outline (stroke)
     * @param scaleX the horizontal scaling factor for the text shape
     * @param scaleY the vertical scaling factor for the text shape
     * @param angle the rotation angle for the text shape in degrees
     */
    public TextShape(String content, Color strokeColor, Color fillingColor, double width, double height, double strokeWidth, double scaleX, double scaleY, double angle) {
        textShape = new Text(content);
        textShape.setStroke(strokeColor);
        textShape.setFill(fillingColor);
        textShape.setX(width);
        textShape.setY(height);
        textShape.setFont(Font.font(strokeWidth));
        baseWidth = textShape.getLayoutBounds().getWidth();
        baseHeight = textShape.getLayoutBounds().getHeight();
        textShape.setRotate(angle);
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    /**
     * Determines whether the shape can have a fill color.
     *
     * @return true if the shape supports a fill color, otherwise false
     */
    @Override
    public boolean canHaveFill() {
        return true;
    }

    /**
     * Updates the position of the text shape to the specified coordinates.
     *
     * @param X The new X-coordinate for the text shape's position.
     * @param Y The new*/
    @Override
    public void draw(double X, double Y) {
         textShape.setX(X);
         textShape.setY(Y);
        points[0] = X;
        points[1] = Y;
    }

    /**
     * Draws the text shape using the first two coordinates provided in the array.
     *
     * @param points an array of Double values representing the coordinates for drawing the text shape.
     *               The method uses the first two values*/
    @Override
    public void draw(Double[] points) {
        if (points.length >= 2) {
            draw(points[0], points[1]);
        }
    }

    /**
     * Moves the text shape by applying the specified offsets to its current X and Y coordinates.
     *
     * @param deltaX the offset*/
    @Override
    public void moveBy(double deltaX, double deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    /**
     * Creates and returns a new instance of the TextShape object that is a replica of the current shape,
     * including its properties such as text content, stroke color, fill color, dimensions, scaling factors,
     * and rotation angle. The cloned shape is also drawn at the same position as the original shape.
     *
     * @return a new ShapeInterface instance that is a copy of the current shape
     */
    @Override
    public ShapeInterface cloneShape() {
        TextShape clone = new TextShape(getText(), getStrokeColor(), getFillColor(), getWidth(), getHeight(), getCustomStrokeWidth(), getScaleX(), getScaleY(), getAngle());
        clone.draw(getX(), getY());
        return clone;
    }

    /**
     * Retrieves the stroke color of the TextShape object.
     *
     * @return the Color object representing the stroke (outline) color of the text shape
     */
    @Override
    public Color getStrokeColor() {
        return (Color)  textShape.getStroke();
    }


    /**
     * Sets the stroke (outline) color of the text shape.
     *
     * @param stroke the Color object representing the new stroke color to be applied to the text shape
     */
    @Override
    public void setStrokeColor(Color stroke) {
         textShape.setStroke(stroke);
    }

    /**
     * Retrieves the fill color of the TextShape object.
     *
     * @return the Color object representing the fill (interior) color of the text shape
     */
    @Override
    public Color getFillColor() {
        return (Color)  textShape.getFill();
    }

    /**
     * Sets the fill color of the text shape.
     *
     * @param fill the Color object representing the fill (interior) color to be applied to the text shape
     */
    @Override
    public void setFillColor(Color fill) {
         textShape.setFill(fill);
    }

    /**
     * Retrieves the width of the text shape based on the custom stroke width.
     *
     * @return the width of the text shape as a double value
     */
    @Override
    public double getWidth() {
        return  this.getCustomStrokeWidth();
    }

    /**
     * Retrieves the height of the text shape based on its custom stroke width.
     *
     * @return the height of the*/
    @Override
    public double getHeight() {
        return  this.getCustomStrokeWidth();
    }

    /**
     * Sets the size of the text shape by adjusting its dimensions.
     * The larger dimension between width and height is used to set the custom stroke width.
     *
     * @param width the desired width of the text shape
     * @param height the desired height of the text shape
     */
    @Override
    public void setSize(double width, double height) {
        this.setCustomStrokeWidth(Math.max(width, height));
    }

    /**
     * Resizes the text shape by setting a new custom stroke width.
     *
     * @param newSize the new size to be applied as the custom stroke width of the text shape
     */
    @Override
    public void resize(double newSize) {
        this.setCustomStrokeWidth(newSize);
    }

    /**
     * Retrieves the horizontal stretch factor (scale along the X-axis) of the text shape.
     *
     * @return the horizontal scaling factor as a double value
     */
    @Override
    public double getStretchWidth() {
        return  textShape.getScaleX();
    }

    /**
     * Retrieves the vertical stretch factor (scale along the Y-axis) of the text shape.
     *
     * @return the vertical scaling factor as a double value
     */
    @Override
    public double getStretchHeight() {
        return  textShape.getScaleY();
    }

    /**
     * Resets the stretch of the text shape by updating its scaling factors.
     *
     * @param width the new horizontal scaling factor to be applied to the text shape
     * @param height the new vertical scaling factor to be applied to the text shape
     */
    @Override
    public void resetStretch(double width, double height) {
        textShape.setScaleX(width);
        textShape.setScaleY(height);
    }

    /**
     * Adjusts the horizontal and vertical scaling factors of the text shape.
     *
     * @param width the new horizontal scaling factor (scaleX) to be applied to the text shape
     * @param height the new vertical scaling factor (scaleY) to be applied to the text shape
     */
    @Override
    public void stretch(double width, double height) {
        textShape.setScaleX(width);
        textShape.setScaleY(height);
    }


    /**
     * Retrieves the X-coordinate of the TextShape object.
     *
     * @return the X-coordinate of the text shape as a double value
     */
    @Override
    public double getX() {
        return  textShape.getX();
    }

    @Override
    public void setX(double x) {
         textShape.setX(x);
        points[0] = x;
    }

    /**
     * Retrieves the Y-coordinate of the TextShape object.
     * If the shape is flipped along the Y-axis, this method compensates
     * by adjusting the Y-coordinate relative to the base height; otherwise,
     * it returns the current Y-coordinate of the shape directly.
     *
     * @return the calculated Y-coordinate of the text shape as a double value,
     *         adjusted based on the flipped state and base height if applicable.
     */
    @Override
    public double getY() {
        return isFlippedY ? textShape.getY() - baseHeight : textShape.getY();
    }

    /**
     * Sets the Y-coordinate of the text shape to the specified value.
     * If the shape is flipped along the Y-axis, the method compensates by adjusting the Y-coordinate
     * based on the base height; otherwise, it sets the value directly.
     *
     * @param*/
    @Override
    public void setY(double y) {
        // Se ribaltato verticalmente, compensa perché Y è baseline
        if (isFlippedY) {
            textShape.setY(y + baseHeight);
        } else {
            textShape.setY(y);
        }
        points[1] = y;
    }

    /**
     * Retrieves the points associated with the TextShape object.
     *
     * @return an array of Double values representing the points of the text shape
     */
    @Override
    public Double[] getPoints() {
        return points;
    }

    /**
     * Retrieves the custom stroke width of the text shape.
     * The stroke width is determined based on the font size of the text shape.
     *
     * @return the custom*/
    @Override
    public double getCustomStrokeWidth() {
        return  textShape.getFont().getSize();
    }

    /**
     * Sets the custom stroke width for the text shape.
     * This method updates the font size of the text shape to match the specified stroke width.
     *
     * @param strokeWidth the new stroke width to be applied to the text*/
    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
         textShape.setFont(Font.font(strokeWidth));
    }

    /**
     * Retrieves the Node object associated with the TextShape instance.
     *
     * @return the Node representing the graphical representation of the text shape
     */
    @Override
    public Node getNode() {
        return  textShape;
    }

    /**
     * Retrieves the effective size of the text shape.
     * The effective size is determined by the custom stroke width of the shape.
     *
     * @return the effective size of the text shape as a double value
     */
    @Override
    public double getEffectiveSize() {
        return getCustomStrokeWidth();
    }

    /**
     * Retrieves the maximum slider size for the TextShape object.
     *
     * @return the maximum slider size as a double value
     */
    @Override
    public double getMaxSliderSize() {
        return 120;
    }

    /**
     * Retrieves the minimum slider size for the TextShape object.
     *
     * @return the minimum slider size as a double value
     */
    @Override
    public double getMinSliderSize() {
        return 38;
    }

    /**
     * Generates a slider label formatted as a string using the specified value.
     * The label includes the value followed by "*/
    @Override
    public String getSliderLabel(double value) {
        return String.format("%.0f pt", value);
    }

    /**
     * Retrieves the text content from the associated text shape.
     * If the text is null, it returns an empty string.
     *
     * @return the text content of the text shape, or an empty string if the text is null.
     */
    @Override
    public String getText() {
        return  textShape.getText() == null ? "" :  textShape.getText();
    }

    /**
     * Sets the text content for the textShape.
     *
     * @param content the string content to be set as the*/
    @Override
    public void setText(String content) {
         textShape.setText(content);
    }

    /**
     * Determines if an object can be moved by the specified delta values while adhering to the constraints
     * of the pane dimensions, zoom level, and margins.
     *
     * @param deltaX the change in the X-axis position
     * @param deltaY the change in the Y-axis position
     * @param paneWidth the width of the pane in which the object resides
     * @param paneHeight the height of the pane in which the object resides
     * @param zoom the zoom factor applied to the pane
     * @param margin the margin to be considered when calculating movement constraints
     * @return true if the object can be moved to the new position within the constraints, false otherwise
     */
    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double width = getWidth();
        double height = getHeight();

        double minX;
        double maxX;
        double minY;
        double maxY;
        if (isFlippedX || isFlippedY) {
            minX = (width / 2 + (margin - 75)) / zoom;
            maxX = (paneWidth - width / 2 - (margin - 75)) / zoom;
            minY = (height / 2 + (margin - 75)) / zoom;
            maxY = (paneHeight - height / 2 - (margin - 75)) / zoom;
        }else {
            minX = (width / 2 + (margin)) / zoom;
            maxX = (paneWidth - width / 2 - (margin)) / zoom;
            minY = (height / 2 + (margin)) / zoom;
            maxY = (paneHeight - height / 2 - (margin)) / zoom;
        }

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }


    /**
     * Performs a vertical mirroring operation on the object.
     * This method adjusts the vertical position and scale of the textShape
     * object to achieve a mirror effect relative to the top edge of the current
     * component. The `isFlippedY` flag is toggled to indicate the new flipped state.
     *
     * The method applies the following transformations:
     * - Calculates the mirrored vertical position of the object.
     * - Updates the `textShape` object's position and scaling factor.
     * - Toggles the `isFlippedY` boolean property.
     * - Updates the `points` array if the reference is not null.
     */
    @Override
    public void mirrorUp() {
        double y = textShape.getY();
        double h = getHeight();

        // Calcolo speculare rispetto al bordo superiore
        double centerY = y + h / 2;
        double distance = centerY - y;
        double mirroredCenterY = y - distance;
        double newY = mirroredCenterY - h / 2;
        textShape.setScaleY(-textShape.getScaleY());  // inverte sempre
        textShape.setY(newY);                         // sposta specularmente

        isFlippedY = !isFlippedY;

        if (points[1] != null) {
            points[1] = textShape.getY();
        }
    }


    /**
     * Mirrors the graphical object vertically around its bottom edge.
     *
     * The method calculates the mirrored position of the object relative to the
     * bottom edge of its current position. It flips the object vertically
     * and reassigns its position to achieve the mirror effect.
     *
     * Key steps include:
     * - Determining the current Y-coordinate and height of the object.
     * - Computing the bottom edge position and center position.
     * - Reflecting the object vertically by flipping its scale on the Y-axis.
     * - Adjusting the Y-coordinate of the object to position it correctly after flipping.
     *
     * The method also updates the vertical flipping state and synchronizes
     * a related points array, if applicable.
     */
    @Override
    public void mirrorDown() {
        double y = textShape.getY();
        double h = getHeight();

        // Calcolo speculare rispetto al bordo inferiore
        double bottomY = y + h;
        double centerY = y + h / 2;
        double distance = bottomY - centerY;
        double mirroredCenterY = bottomY + distance;
        double newY = mirroredCenterY - h / 2;

        textShape.setScaleY(-textShape.getScaleY()); // flip verticale
        textShape.setY(newY);                         // sposta sotto
        isFlippedY = !isFlippedY;

        if (points[1] != null) {
            points[1] = newY;
        }
    }

    /**
     * Mirrors the current text shape towards the left direction by reflecting
     * the position and orientation of the shape horizontally.
     *
     * This method calculates the mirrored position of the text shape by determining
     * the center point, the distance from the center to the left edge, and then
     * applying a reflection based on this distance to position the shape symmetrically
     * to the left. It also flips the horizontal scale of the text shape to complete
     **/
    @Override
    public void mirrorLeft() {
        double x = textShape.getX();
        double w = this.getWidth();

        double centerX = x + w / 2;
        double leftX = x;
        double distance = centerX - leftX;
        double mirroredCenterX = leftX - distance;
        double newX = mirroredCenterX - w / 2;

        textShape.setScaleX(-textShape.getScaleX()); // capovolgimento orizzontale
        textShape.setX(newX);                         // spostamento simmetrico a sinistra
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = newX;
        }
    }


    /**
     * Mirrors the text shape horizontally to the right side of its current position.
     *
     * This method performs a horizontal flipping operation of the text shape
     * around its center point, and positions the mirrored shape symmetrically
     * to the right side of the original shape's location. The scale along the
     * x-axis is inverted, and the new x-coordinate of the shape is calculated
     * based on the mirrored positioning. Additionally, updates the*/
    @Override
    public void mirrorRight() {
        double x = textShape.getX();
        double w = this.getWidth();

        double centerX = x + w / 2;
        double rightX = x + w;
        double distance = rightX - centerX;
        double mirroredCenterX = rightX + distance;
        double newX = mirroredCenterX - w / 2;

        textShape.setScaleX(-textShape.getScaleX()); // capovolgimento orizzontale
        textShape.setX(newX);                         // spostamento simmetrico a destra
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = newX;
        }
    }


    /**
     * Sets the horizontal scaling factor for the text shape.
     *
     */
    @Override
    public void setScaleX(double scale) {
        textShape.setScaleX(scale);
    }

    /**
     * Retrieves the horizontal scaling factor of the text shape.
     *
     * @return the horizontal scaling factor as a double value
     */
    @Override
    public double getScaleX() {
        return textShape.getScaleX();
    }

    /**
     * Sets the vertical scale factor for the text shape.
     * Adjusts the height of the text shape by the given scale factor.
     *
     * @param scale the vertical scale factor to set; must be a valid double value.
     */
    @Override
    public void setScaleY(double scale) {
        textShape.setScaleY(scale);
    }

    /**
     * Retrieves the vertical scaling factor of the text shape.
     *
     * @return the scale factor for the y-axis as a double
     */
    @Override
    public double getScaleY() {
        return textShape.getScaleY();
    }

    /**
     * Retrieves the raw X-coordinate without any adjustments or compensations.
     *
     * @return the raw X-coordinate as a double
     */
    public double getRawX() {
        return textShape.getX(); // senza compensazioni
    }

    /**
     * Retrieves the raw Y-coordinate value of the text shape without applying any compensation.
     *
     * @return the raw Y-coordinate of the text shape
     */
    public double getRawY() {
        return textShape.getY(); // senza compensazioni
    }

    /**
     * Retrieves the restore position of the text shape.
     *
     * @return a Point2D object representing the restore position based on the x and y coordinates of the text shape
     */
    @Override
    public Point2D getRestorePosition() {
        return new Point2D(textShape.getX(), textShape.getY()); // Posizione effettiva
    }

    /**
     * Sets the restore position using the specified point.
     *
     * @param point the Point2D object containing the x and y coordinates to restore.
     */
    @Override
    public void setRestorePosition(Point2D point) {
        draw(point.getX(), point.getY()); // Usa draw come prima
    }


    /**
     * Mirrors the text shape horizontally around its center by inverting its horizontal scale.
     * The horizontal scale factor of the text shape is multiplied by -1,
     * effectively flipping it along the vertical axis.
     */
    @Override
    public void mirrorHorizontallyAroundCenter() {
        textShape.setScaleX(textShape.getScaleX() * -1);
    }

    /**
     * Mirrors the text shape vertically around its center axis.
     *
     * This method inverses the vertical scale (Y-axis scale) of the text shape,
     * effectively flipping it upside down while maintaining its center position.
     *
     * It achieves this by multiplying the current scaleY of the text shape by -1.
     */
    @Override
    public void mirrorVerticallyAroundCenter() {
        textShape.setScaleY(textShape.getScaleY() * -1);
    }

    /**
     * Sets the rotation angle for the text shape.
     *
     * @param angle the angle, in degrees, to rotate the text shape
     */
    @Override
    public void setAngle(double angle) {
        textShape.setRotate(angle);
    }

    /**
     * Retrieves the angle of rotation for the associated text shape.
     *
     * @return the angle of rotation in degrees as a double value
     */
    @Override
    public double getAngle() {
        return textShape.getRotate();
    }

    /**
     * Simulates the reflection of a text shape based on the specified mirror direction.
     * The method calculates the new position of the text shape as if it were reflected
     * by a hypothetical mirror placed in the specified direction.
     *
     * @param direction the direction of the mirror relative to the current position of the text shape.
     *                  Available directions include UP, DOWN, LEFT, RIGHT, HORIZONTAL, and VERTICAL.
     * @return an {@*/
    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double x = textShape.getX();
        double y = textShape.getY();

        double mirroredX = x;
        double mirroredY = y;

        switch (direction) {
            case UP: {
                mirroredY = y - 2 * getHeight(); // ipotetico specchio sopra
                break;
            }
            case DOWN: {
                mirroredY = y + 2 * getHeight(); // ipotetico specchio sotto
                break;
            }
            case LEFT: {
                mirroredX = x - 2 * getWidth(); // ipotetico specchio a sinistra
                break;
            }
            case RIGHT: {
                mirroredX = x + 2 * getWidth(); // ipotetico specchio a destra
                break;
            }
            case HORIZONTAL:
            case VERTICAL:
                // Il testo si riflette sul posto → nessuno spostamento
                return Optional.of(new Point2D(x, y));
        }

        return Optional.of(new Point2D(mirroredX, mirroredY));
    }


    /**
     * Converts the object's state into a string representation, including the text, text styling,
     * position, scaling, rotation, and other relevant attributes of the text shape.
     *
     * @return a string representation of the object's state.
     */
    @Override
    public String toString() {
        return "Text;" + textShape.getText() + ";" + textShape.getStroke() + ";" + textShape.getFill() + ";" +
                textShape.getX() + ";" + textShape.getY() + ";" + getCustomStrokeWidth() + ";" + getScaleX() + ";"
                + getScaleY() + ";" + this.getAngle() + '\n';
    }


}
