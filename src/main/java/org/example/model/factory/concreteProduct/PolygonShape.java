package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Arrays;
import java.util.Optional;

/**
 *
 */
public class PolygonShape implements ShapeInterface {

    private final Polygon polygonShape;
    private Double[] points;

    /**
     * Constructs a PolygonShape with the specified stroke*/
    public PolygonShape(Color stroke, Color fill, double strokeWidth, double ScaleX, double ScaleY, double angle) {
        this.polygonShape = new Polygon();
        polygonShape.setStroke(stroke);
        polygonShape.setFill(fill);
        polygonShape.setStrokeWidth(strokeWidth);
        polygonShape.setScaleX(ScaleX);
        polygonShape.setScaleY(ScaleY);
        polygonShape.setRotate(angle);
    }

    /**
     * Draws the polygon by updating its point coordinates relative to the given x and y offsets.
     * The points are adjusted based on the position of the first point in the array.
     *
     * @param x the x-coordinate offset to adjust the polygon points
     * @param y the y-coordinate offset to adjust the polygon points
     */
    @Override
    public void draw(double x, double y) {
        if (points.length < 2) return;

        double firstX = points[0];
        double firstY = points[1];

        double[] newPoints = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                newPoints[i] = (x + points[i]) - firstX;
            } else {
                newPoints[i] = (y + points[i]) - firstY;
            }
        }

        polygonShape.getPoints().clear();
        for (double point : newPoints) {
            polygonShape.getPoints().add(point);
        }
    }

    /**
     *
     */
    @Override
    public void draw(Double[] points) {
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(points));
        polygonShape.setStroke(getStrokeColor());
        polygonShape.setStrokeWidth(getCustomStrokeWidth());
        polygonShape.setFill(getFillColor());

        // Update the internal points array
        this.points = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }
    }

    /**
     * Determines if the polygon shape can have a fill color.
     *
     * @return true if the polygon*/
    @Override
    public boolean canHaveFill() {
        return true;
    }

    /**
     * Creates an identical clone of the current PolygonShape, including its properties and points.
     *
     * @return a new PolygonShape instance with the same stroke color, fill color, stroke width,
     *         scale, rotation angle, and points as the original shape.
     */
    @Override
    public ShapeInterface cloneShape() {
        PolygonShape clone = new PolygonShape(this.getStrokeColor(), this.getFillColor(), this.getCustomStrokeWidth(), this.getScaleX(), this.getScaleY(), this.getAngle());
        clone.draw(getPoints());
        return clone;
    }

    /**
     * Translates the polygon's points by adding the specified offsets to their coordinates.
     * The x-coordinates of the points are incremented by deltaX, and*/
    @Override
    public void moveBy(double deltaX, double deltaY) {
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            if (i % 2 == 0) {
                polygonShape.getPoints().set(i, polygonShape.getPoints().get(i) + deltaX);
            } else {
                polygonShape.getPoints().set(i, polygonShape.getPoints().get(i) + deltaY);
            }
        }
        // Aggiorna anche l'array interno
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                points[i] += deltaX;
            } else {
                points[i] += deltaY;
            }
        }
    }

    /**
     * Determines whether the polygon can be moved by the specified offsets
     * without exceeding the boundaries of the pane, considering margins and zoom levels.
     */
    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        // Trova i bounds del poligono
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i) + deltaX;
            double y = polygonShape.getPoints().get(i + 1) + deltaY;

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        double marginScaled = margin / zoom;
        return minX >= marginScaled && maxX <= (paneWidth - marginScaled) / zoom &&
                minY >= marginScaled && maxY <= (paneHeight - marginScaled) / zoom;
    }

    /**
     * Mirrors the polygon's points*/
    @Override
    public void mirrorUp() {
        if (points == null || points.length < 4) return;
        double minY = Double.MAX_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            if (points[i] < minY) {
                minY = points[i];
            }
        }
        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * minY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }


    /**
     * Mirrors the polygon's points vertically downwards.
     * The method calculates the maximum y-coordinate among the points and reflects
     * each point’s y-coordinate symmetrically about this maximum value.
     * The*/
    @Override
    public void mirrorDown() {
        if (points == null || points.length < 4) return;

        double maxY = Double.MIN_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            if (points[i] > maxY) {
                maxY = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * maxY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    /**
     * Mirrors the x-coordinates of the polygon's points symmetrically about the minimum
     * x-coordinate among them, effectively flipping the shape to its left.
     *
     * The method calculates the smallest x-coordinate of the points in the array
     * and then reflects each x-coordinate symmetrically about this minimum value
     * while the y-coordinates remain unchanged. After mirroring, the updated points
     * array replaces the existing one, and the graphical representation of the
     * polygon shape is updated.
     *
     * Preconditions:
     * - The array `points` should contain an even number of elements, representing x and y
     *   coordinates in pairs.
     * Postconditions:
     * - The `points` field is updated to*/
    @Override
    public void mirrorLeft() {
        if (points == null || points.length < 4) return;

        double minX = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            if (points[i] < minX) {
                minX = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * minX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    /**
     * Mirrors the x-coordinates of the polygon's points symmetrically about the maximum x-coordinate among them,
     * effectively flipping the shape to its right.
     *
     * The method calculates the largest x-coordinate of the points in the array and then reflects each x-coordinate
     * symmetrically about this maximum value while the y-coordinates remain unchanged. After mirroring, the updated
     * points array*/
    @Override
    public void mirrorRight() {
        if (points == null || points.length < 4) return;

        double maxX = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            if (points[i] > maxX) {
                maxX = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * maxX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }


    /**
     * Sets the horizontal scaling factor (scaleX) for the polygon shape, which determines
     * how much the shape is stretched or compressed horizontally.
     *
     * @param scale the new horizontal scaling factor to be applied to the polygon shape.
     *              A value of 1.0 indicates no scaling, values greater than 1.0 stretch
     *              the shape horizontally, and values between 0.0 and 1.0 compress it.
     */
    @Override
    public void setScaleX(double scale) {
        polygonShape.setScaleX(scale);
    }

    /**
     * Retrieves the horizontal scaling factor (scaleX) of the polygon shape.
     * The scaleX represents how much the shape is stretched or compressed horizontally.
     *
     * @return the horizontal scaling factor of the polygon shape.
     */
    @Override
    public double getScaleX() {
        return polygonShape.getScaleX();
    }

    /**
     * Sets the vertical scaling factor (scaleY) for the polygon shape, which determines
     * how much the shape is stretched or compressed vertically.
     *
     * @param scale the new vertical scaling factor to be applied to the polygon shape.
     *              A value of 1.0 indicates no scaling, values greater than 1.0 stretch
     *              the shape vertically, and values between 0.0 and 1.0 compress it.
     */
    @Override
    public void setScaleY(double scale) {
        polygonShape.setScaleY(scale);
    }

    /**
     * Retrieves the vertical scaling factor (scaleY) of the polygon shape.
     * The scaleY represents*/
    @Override
    public double getScaleY() {
        return polygonShape.getScaleY();
    }

    /**
     * Reflects the polygon's points horizontally around their central x-coordinate.
     *
     * The method calculates the minimum and maximum x-coordinates among the points
     * and determines the horizontal center position (centerX) by averaging these
     * values. Each point's x-coordinate is then mirrored symmetrically about this
     * centerX value while preserving its y-coordinate.
     *
     * After performing the mirroring operation, the new set of mirrored points is
     * updated in the `points` array, and the graphical representation of the
     * polygon shape (`polygonShape`) is refreshed with the modified coordinates.
     *
     * Preconditions:
     * - The `points` array must not be null and must contain at least four values
     *   representing two or more coordinate pairs (x, y).
     *
     * Postconditions:
     * - The points are updated such that their x-coordinates are mirrored
     *   horizontally around the calculated central x-coordinate.
     * - The `polygonShape` graphical representation is synchronized with the
     *   updated points.
     */
    @Override
    public void mirrorHorizontallyAroundCenter() {
        if (points == null || points.length < 4) return;

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            minX = Math.min(minX, points[i]);
            maxX = Math.max(maxX, points[i]);
        }
        double centerX = (minX + maxX) / 2.0;

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * centerX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    /**
     * Reflects the y-coordinates of the polygon's points vertically around their central y-coordinate.
     *
     * The method determines the minimum and maximum y-coordinates of the points
     * to calculate the vertical center position (centerY) by averaging these values.
     * Each point's y-coordinate is then mirrored symmetrically about this centerY value,
     * while preserving its x-coordinate.
     *
     * After performing the mirroring operation, the updated set of points is assigned
     * to the `points` field, and the graphical representation of the polygon*/
    @Override
    public void mirrorVerticallyAroundCenter() {
        if (points == null || points.length < 4) return;

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            minY = Math.min(minY, points[i]);
            maxY = Math.max(maxY, points[i]);
        }
        double centerY = (minY + maxY) / 2.0;

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * centerY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    /**
     * Retrieves the stroke color of the polygon shape.
     *
     * @return the stroke*/
    @Override
    public Color getStrokeColor() {
        return (Color) polygonShape.getStroke();
    }

    /**
     * Sets the stroke color of the polygon shape to the specified color.
     *
     * @param stroke the new color to set as the stroke for the polygon shape
     */
    @Override
    public void setStrokeColor(Color stroke) {
        polygonShape.setStroke(stroke);
    }

    /**
     *
     */
    @Override
    public Color getFillColor() {
        return (Color) polygonShape.getFill();
    }

    /**
     * Sets the fill color of the polygon shape to the specified color.
     *
     * @param fill the new fill color to set for the polygon shape
     */
    @Override
    public void setFillColor(Color fill) {
        polygonShape.setFill(fill);
    }

    /**
     * Calculates and returns the x-coordinate of the horizontal center
     * of the polygon shape based on its points.
     * If the points list is empty, the method returns 0.
     *
     * The method calculates the horizontal center as the average of the
     * smallest (minX) and largest (maxX) x-coordinates among the points.
     *
     * @return the x-coordinate of the horizontal center of the polygon shape.
     */
    @Override
    public double getX() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        return (minX + maxX) / 2;
    }

    /**
     * Updates the x-coordinate of the polygon shape by calculating the offset
     * from the current x-coordinate and moving the shape horizontally by this value.
     *
     * @param x the new x-coordinate to set for the polygon shape
     */
    @Override
    public void setX(double x) {
        double currentX = getX();
        double deltaX = x - currentX;
        moveBy(deltaX, 0);
    }

    /**
     * Calculates and returns the y-coordinate of the vertical center of the polygon shape
     * based on its points. If the points list is empty, the method returns 0.
     *
     * The method calculates the vertical center as the average of the smallest (minY)
     * and largest (maxY) y-coordinates among the points.
     *
     */
    @Override
    public double getY() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double y = polygonShape.getPoints().get(i);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        return (minY + maxY) / 2;
    }

    /**
     * Sets the y-coordinate of the object to the specified value.
     * Adjusts the object's position by calculating the difference
     * between the current y-coordinate and the provided value*/
    @Override
    public void setY(double y) {
        double currentY = getY();
        double deltaY = y - currentY;
        moveBy(0, deltaY);
    }

    /**
     * Calculates and returns the width of the polygon shape based on the
     * horizontal distance between its minimum and maximum x-coordinates.
     *
     * @return the width of the polygon shape, or 0 if the shape has no points
     */
    @Override
    public double getWidth() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        return maxX - minX;
    }

    /**
     * Calculates and returns the height of the polygon shape
     * based on the difference between the maximum and minimum
     * y-coordinates of its points.
     *
     * @return the height of the polygon shape. Returns 0 if the polygon shape has no points.
     */
    @Override
    public double getHeight() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double y = polygonShape.getPoints().get(i);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        return maxY - minY;
    }

    /**
     * Sets the size of the polygon by adjusting its width and height proportions
     * based on the specified dimensions. This method scales the polygon's points
     * while maintaining its central position.
     *
     * @param width  the desired width of the polygon
     * @param height the desired height of the polygon
     */
    @Override
    public void setSize(double width, double height) {
        double currentWidth = getWidth();
        double currentHeight = getHeight();

        if (currentWidth == 0 || currentHeight == 0) return;

        double scaleX = width / currentWidth;
        double scaleY = height / currentHeight;

        double centerX = getX();
        double centerY = getY();

        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            double y = polygonShape.getPoints().get(i + 1);

            double newX = centerX + (x - centerX) * scaleX;
            double newY = centerY + (y - centerY) * scaleY;

            polygonShape.getPoints().set(i, newX);
            polygonShape.getPoints().set(i + 1, newY);
        }
        updatePointsArray();
    }

    /**
     * Resizes the current object to fit within the specified size while
     * maintaining its original aspect ratio.
     *
     * @param newSize the new maximum size to scale the object to. The object
     *                will be resized proportionally based on its current
     *                dimensions.
     */
    @Override
    public void resize(double newSize) {
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            setSize(getWidth() * scale, getHeight() * scale);
        }
    }

    /**
     * Retrieves the stretch width of the object. The stretch width typically represents
     * the horizontal dimension of the object adjusted or scaled as needed.
     *
     * @return the stretch width as a double value
     */
    @Override
    public double getStretchWidth() {
        return getWidth();
    }

    /**
     * Retrieves the stretch height of the current object.
     *
     * @return the stretch height as a double, which typically represents the height of the object*/
    @Override
    public double getStretchHeight() {
        return getHeight();
    }

    /**
     * Adjusts the size of the object by setting its width and height.
     *
     * @param width  the new width to set for the object
     * @param height the new height to set for the object
     */
    @Override
    public void stretch(double width, double height) {
        setSize(width, height);
    }

    /**
     * Resets the dimensions of the stretchable component to the specified width and height.
     *
     * @param width  the*/
    @Override
    public void resetStretch(double width, double height) {
        setSize(width, height);
    }

    /**
     * Returns the custom stroke width of the polygon shape.
     *
     * @return the stroke width as a double value
     */
    @Override
    public double getCustomStrokeWidth() {
        return polygonShape.getStrokeWidth();
    }

    /**
     * Sets a custom stroke width for the polygon shape.
     *
     * @param strokeWidth the desired width for the stroke in double precision
     */
    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        polygonShape.setStrokeWidth(strokeWidth);
    }

    /**
     * Retrieves the points of the polygon shape as an array of Doubles.
     *
     * @return an array of Doubles representing the points of the polygon shape
     */
    @Override
    public Double[] getPoints() {
        Double[] result = new Double[polygonShape.getPoints().size()];
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            result[i] = polygonShape.getPoints().get(i);
        }
        return result;
    }

    /**
     * Sets the points for the polygon shape.
     * Clears any existing points and adds the new points*/
    public void setPoints(Double[] points) {
        polygonShape.getPoints().clear();
        for (Double point : points) {
            polygonShape.getPoints().add(point);
        }
    }

    /**
     * Retrieves the current node associated with the polygon shape.
     *
     * @return the node representing the polygon shape
     */
    @Override
    public Node getNode() {
        return polygonShape;
    }

    /**
     * Calculates and returns the effective size based on the larger dimension
     * (width or height) of the object, adjusted by a fixed additional value.
     *
     * @return the effective size as a double, which is the larger of the width
     *         or height plus 50.
     */
    @Override
    public double getEffectiveSize() {
        return Math.max(getWidth(), getHeight()) + 50;
    }

    /**
     * Generates a formatted label for a slider based on the given value.
     *
     * @param value the*/
    @Override
    public String getSliderLabel(double value) {
        return String.format("Size: %.1f", value);
    }

    /**
     * Retrieves the textual representation of the object.
     *
     * @return a String containing the text, or null if no text is available
     */
    @Override
    public String getText() {
        return null;
    }

    /**
     * Sets the text content. This method is not supported and will throw an exception
     * if invoked.
     *
     * @param content the text content to set
     */
    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Updates*/
    private void updatePointsArray() {
        points = new Double[polygonShape.getPoints().size()];
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            points[i] = polygonShape.getPoints().get(i);
        }
    }

    /**
     * Simulates the reflection of a set of 2D points across a specified mirror direction
     * and determines the extremum point in the simulated configuration based on the chosen direction.
     *
     * @param direction the direction of the mirror reflection. It can be one of the values
     *                  from the {@code MirrorDirectionEnum}, such as UP, DOWN, LEFT, RIGHT,
     *                  HORIZONTAL, or VERTICAL.
     * @return an {@code Optional} containing the extremum {@code Point2D} resulting from the
     *         reflection operation in the specified direction. If the operation cannot be performed
     *         (e.g., the points array is null or has insufficient elements), an empty {@code Optional} is returned.
     */
    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        if (points == null || points.length < 2) return Optional.empty();

        Double[] simulated = Arrays.copyOf(points, points.length);

        switch (direction) {
            case UP: {
                double minY = Double.MAX_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    minY = Math.min(minY, simulated[i]);
                }
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * minY - simulated[i];
                }
                break;
            }
            case DOWN: {
                double maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    maxY = Math.max(maxY, simulated[i]);
                }
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * maxY - simulated[i];
                }
                break;
            }
            case LEFT: {
                double minX = Double.MAX_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    minX = Math.min(minX, simulated[i]);
                }
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * minX - simulated[i];
                }
                break;
            }
            case RIGHT: {
                double maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    maxX = Math.max(maxX, simulated[i]);
                }
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * maxX - simulated[i];
                }
                break;
            }
            case HORIZONTAL: {
                double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    minX = Math.min(minX, simulated[i]);
                    maxX = Math.max(maxX, simulated[i]);
                }
                double centerX = (minX + maxX) / 2.0;
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * centerX - simulated[i];
                }
                break;
            }
            case VERTICAL: {
                double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    minY = Math.min(minY, simulated[i]);
                    maxY = Math.max(maxY, simulated[i]);
                }
                double centerY = (minY + maxY) / 2.0;
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * centerY - simulated[i];
                }
                break;
            }
        }

        // Find the extreme point according to the direction
        Point2D result = null;

        switch (direction) {
            case LEFT:
            case HORIZONTAL: {
                double minX = Double.MAX_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    if (simulated[i] < minX) {
                        minX = simulated[i];
                        result = new Point2D(simulated[i], simulated[i + 1]);
                    }
                }
                break;
            }
            case RIGHT: {
                double maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    if (simulated[i] > maxX) {
                        maxX = simulated[i];
                        result = new Point2D(simulated[i], simulated[i + 1]);
                    }
                }
                break;
            }
            case UP:
            case VERTICAL: {
                double minY = Double.MAX_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    if (simulated[i] < minY) {
                        minY = simulated[i];
                        result = new Point2D(simulated[i - 1], simulated[i]);
                    }
                }
                break;
            }
            case DOWN: {
                double maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    if (simulated[i] > maxY) {
                        maxY = simulated[i];
                        result = new Point2D(simulated[i - 1], simulated[i]);
                    }
                }
                break;
            }
        }

        return Optional.ofNullable(result);
    }


    /**
     * Sets the rotation angle for the polygon shape.
     *
     * @param angle the angle in degrees to rotate the polygon shape
     */
    @Override
    public void setAngle(double angle) {
        polygonShape.setRotate(angle);
    }

    /**
     * Retrieves the current rotation angle of the polygon shape.
     *
     * @return the rotation angle in degrees of the polygon shape
     */
    @Override
    public double getAngle() {
        return polygonShape.getRotate();
    }

    /**
     * Returns a string representation of the Polygon object, including its stroke color,
     * fill color, custom stroke width, scale factors, points array, and angle.
     *
     * @return a string containing a semicolon-separated representation of the Polygon properties.
     */
    @Override
    public String toString() {
        return "Polygon" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" +
                this.getCustomStrokeWidth() + ";" + this.getScaleX() + ";" + this.getScaleY() + ";" + Arrays.toString(this.getPoints())
                + ";" + this.getAngle() + '\n';
    }

}
