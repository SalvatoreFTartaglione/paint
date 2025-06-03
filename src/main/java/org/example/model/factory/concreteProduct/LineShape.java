package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Optional;

/**
 * Represents a line shape with various configurable properties such as stroke color, stroke width, angle*/
public class LineShape implements ShapeInterface {

    private Line lineShape;
    private Double[] points;


    /**
     * Constructs a LineShape with the specified stroke color, stroke width, and rotation angle.
     *
     * @param stroke the color of the line's stroke
     * @param strokeWidth the width of the line's stroke
     * @param angle the rotation angle of the line in degrees
     */
    public LineShape(Color stroke, double strokeWidth, double angle) {
        lineShape = new Line();
        lineShape.setStroke(stroke);
        lineShape.setStrokeWidth(strokeWidth);
        lineShape.setRotate(angle);
    }


    /**
     * Draws the line shape by updating its starting and ending coordinates
     * based on the specified X and Y values. This method modifies the internal
     * points array and the graphical representation of the line shape.
     *
     * @param X the new X-coordinate for the starting point of the line
     * @param Y the new Y-coordinate for the starting point of the line
     */
    @Override
    public void draw(double X, double Y) {
        lineShape.setStartX(this.points[0]);
        lineShape.setStartY(this.points[1]);
        lineShape.setEndX(this.points[2]);
        lineShape.setEndY(this.points[3]);
        this.points[0] = X;
        this.points[1] = Y;
        this.points[2] = X + (lineShape.getEndX() - lineShape.getStartX());
        this.points[3] = Y + (lineShape.getEndY() - lineShape.getStartY());
        lineShape.setStartX(this.points[0]);
        lineShape.setStartY(this.points[1]);
        lineShape.setEndX(this.points[2]);
        lineShape.setEndY(this.points[3]);
    }

    /**
     * Determines whether the LineShape can have a fill color applied to it.
     * As LineShape represents a simple line, it cannot have a fill color.
     *
     * @return false always, indicating that LineShape objects cannot have a fill color.
     */
    @Override
    public boolean canHaveFill() {
        return false;
    }

    /**
     * @param points
     * It assigns to the selected shape (in this case the line) the properties obtained thanks to the "set" methods
     * and the coordinates passed as a parameter
     */
    @Override
    public void draw(Double[] points) {
        lineShape.setStartX(points[0]);
        lineShape.setStartY(points[1]);
        lineShape.setEndX(points[2]);
        lineShape.setEndY(points[3]);
        this.setPoints(points);
    }

    /**
     * Retrieves the points defining the line shape. Specifically, it returns
     * an array containing the X and Y coordinates of the start and end points of the line.
     *
     * @return an array of Double values representing the coordinates of the start (X, Y)
     *         and end (X, Y) points of the line.
     */
    @Override
    public Double[] getPoints(){
        //return this.points;
        return new Double[] {
                lineShape.getStartX(),
                lineShape.getStartY(),
                lineShape.getEndX(),
                lineShape.getEndY()
        };
    }

    /**
     * Moves the line segment by specified offsets along the x-axis and y-axis.
     */
    public void moveBy(double deltaX, double deltaY) {
        // Moves both points of the line
        lineShape.setStartX(lineShape.getStartX() + deltaX);
        lineShape.setStartY(lineShape.getStartY() + deltaY);
        lineShape.setEndX(lineShape.getEndX() + deltaX);
        lineShape.setEndY(lineShape.getEndY() + deltaY);
    }

    /**
     * Creates and returns a deep copy of the current LineShape object.
     * The copied object includes the stroke color, stroke width, rotation angle,
     * and a deep copy of the points array if it exists.
     *
     * @return a new LineShape object that is a deep copy of the current instance.
     */
    @Override
    public ShapeInterface cloneShape() {
        LineShape cloned = new LineShape(this.getStrokeColor(), this.getCustomStrokeWidth(), this.getAngle());

        if (this.points != null && this.points.length == 4) {
            // Copia profonda dell'array dei punti
            Double[] copiedPoints = new Double[] {
                    this.points[0], this.points[1],
                    this.points[2], this.points[3]
            };

            cloned.draw(copiedPoints);  // inizializza il nodo grafico e setta anche cloned.points
        }

        return cloned;
    }

    /**
     * Retrieves the stroke color of the line.
     *
     * @return the stroke color of the line as a {@link Color} object
     */
    @Override
    public Color getStrokeColor() {
        return (Color) lineShape.getStroke();
    }

    /**
     * Sets a custom stroke width for the line shape.
     *
     * @param strokeWidth the new width of the line's stroke
     */
    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        lineShape.setStrokeWidth(strokeWidth);
    }

    /**
     * Sets the stroke color of the line shape.
     *
     * @param stroke the new stroke color to be applied to the line
     */
    @Override
    public void setStrokeColor(Color stroke) {
        lineShape.setStroke(stroke);
    }

    /**
     * @return double
     * Returns the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public double getX() {
        return lineShape.getStartX();
    }

    /**
     * @param point
     * Sets the coordinate along the abscissa axis of the start point of the line
     */
    @Override
    public void setX(double point) {
        lineShape.setStartX(point);

    }

    /**
     * @return double
     * Returns the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public double getY() {
        return lineShape.getStartY();
    }

    /**
     * @param point
     * Sets the coordinate along the ordinate axis of the start point of the line
     */
    @Override
    public void setY(double point) {
        lineShape.setStartY(point);

    }

    /**
     * Calculates and retrieves the width of the line shape.
     * The width is determined as the difference between the
     * ending X-coordinate and the starting X-coordinate.
     *
     * @return the width of the line as a double, calculated as the difference between
     *         the end X and start X values of the line.
     */
    @Override
    public double getWidth() {
        // Restituisce la differenza lungo l'asse X
        return lineShape.getEndX() - lineShape.getStartX();
    }

    /**
     * Calculates and retrieves the height of the line shape.
     * The height is determined as the*/
    @Override
    public double getHeight() {
        // Restituisce la differenza lungo l'asse Y
        return lineShape.getEndY() - lineShape.getStartY();
    }

    /**
     * Updates the size and orientation of the line shape by adjusting its width and height.
     * The method maintains the original orientation of the line while scaling it to the specified dimensions.
     * If the current length of the line is zero, a default horizontal orientation is set.
     *
     * @param width  the desired width of the line, which represents the horizontal component
     * @param height the desired height of the line, which represents the vertical component
     */
    @Override
    public void setSize(double width, double height) {
        // Calcola il vettore corrente (dx, dy)
        double currentDx = getWidth();
        double currentDy = getHeight();

        // Calcola la lunghezza attuale della linea
        double currentLength = Math.hypot(currentDx, currentDy);

        // Calcola la lunghezza desiderata dal nuovo (width, height)
        double desiredLength = Math.hypot(width, height);

        // Se la linea è "degenerata" (lunghezza zero) impostiamo un orientamento predefinito, per esempio orizzontale
        if (currentLength == 0) {
            lineShape.setEndX(lineShape.getStartX() + desiredLength);
            lineShape.setEndY(lineShape.getStartY());
            return;
        }

        // Calcola il fattore di scala in modo da mantenere l'orientamento
        double scale = desiredLength / currentLength;

        // Nuove componenti della differenza mantenendo l'orientamento esistente
        double newDx = currentDx * scale;
        double newDy = currentDy * scale;

        // Aggiorna il punto finale della linea mantenendo invariato il punto di partenza
        lineShape.setEndX(lineShape.getStartX() + newDx);
        lineShape.setEndY(lineShape.getStartY() + newDy);
    }

    /**
     * Resizes the current line shape to the specified size, maintaining its original
     * orientation. If the line is degenerate (length is zero), it is defaulted to
     * a horizontal line with the given size.
     *
     * @param newSize the desired length to which the line shape should be resized
     */
    @Override
    public void resize(double newSize) {
        double currentLength = Math.hypot(getWidth(), getHeight());

        if (currentLength <= 0) {
            // Se la linea è degenerata, crea una linea orizzontale
            lineShape.setEndX(lineShape.getStartX() + newSize);
            lineShape.setEndY(lineShape.getStartY());
        } else {
            // Scala mantenendo l'orientamento
            double scale = newSize / currentLength;
            double newDx = getWidth() * scale;
            double newDy = getHeight() * scale;

            lineShape.setEndX(lineShape.getStartX() + newDx);
            lineShape.setEndY(lineShape.getStartY() + newDy);
        }

        // Aggiorna l'array dei punti se esiste
        if (this.points != null && this.points.length >= 4) {
            this.points[2] = lineShape.getEndX();
            this.points[3] = lineShape.getEndY();
        }
    }

    /**
     * Calculates and returns the stretched width of the line shape.
     * The stretched width is computed as the Euclidean distance between
     * the starting and ending points of the line.
     *
     * @return the stretched width of the line as a double, calculated
     *         using the distance formula between the start and end points.
     */
    @Override
    public double getStretchWidth() {
        double deltaX = lineShape.getEndX() - lineShape.getStartX();
        double deltaY = lineShape.getEndY() - lineShape.getStartY();
        return Math.hypot(deltaX, deltaY);
    }

    /**
     * Retrieves the stretched height of the line shape.
     * The stretched height is determined as the current stroke width of the line.
     *
     * @return the stretched height of the line as a double, equivalent to the stroke width.
     */
    @Override
    public double getStretchHeight() {
        return lineShape.getStrokeWidth();
    }

    @Override
    public void stretch(double width, double height) {
        // Imposta lo spessore della linea
        lineShape.setStrokeWidth(height);

        // Calcola il centro attuale della linea
        double centerX = (lineShape.getStartX() + lineShape.getEndX()) / 2.0;
        double centerY = (lineShape.getStartY() + lineShape.getEndY()) / 2.0;

        // Calcola la lunghezza attuale
        double deltaX = lineShape.getEndX() - lineShape.getStartX();
        double deltaY = lineShape.getEndY() - lineShape.getStartY();
        double currentLength = Math.hypot(deltaX, deltaY);

        if (currentLength <= 0) {
            // Se la linea è degenerata, crea una linea orizzontale con la nuova lunghezza
            // centrata nella posizione attuale
            lineShape.setStartX(centerX - width / 2.0);
            lineShape.setStartY(centerY);
            lineShape.setEndX(centerX + width / 2.0);
            lineShape.setEndY(centerY);
        } else {
            // Calcola il vettore unitario della direzione attuale
            double unitX = deltaX / currentLength;
            double unitY = deltaY / currentLength;

            // Calcola i nuovi punti mantenendo il centro fisso
            double halfWidth = width / 2.0;
            lineShape.setStartX(centerX - unitX * halfWidth);
            lineShape.setStartY(centerY - unitY * halfWidth);
            lineShape.setEndX(centerX + unitX * halfWidth);
            lineShape.setEndY(centerY + unitY * halfWidth);
        }

        // Aggiorna l'array dei punti se esiste
        if (this.points != null && this.points.length >= 4) {
            this.points[0] = lineShape.getStartX();
            this.points[1] = lineShape.getStartY();
            this.points[2] = lineShape.getEndX();
            this.points[3] = lineShape.getEndY();
        }
    }

    /**
     * Resets the stretch dimensions of the line shape to the specified width and height.
     * This method adjusts both the length (width) and thickness (height) of the line shape.
     *
     * @param width  the new desired width of the line, which represents the horizontal component
     * @param height the new desired height of the line, which represents the vertical component
     */
    @Override
    public void resetStretch(double width, double height) {
        // Ripristina sia la lunghezza che lo spessore
        stretch(width, height);
    }


    /**
     * Retrieves the custom stroke width of the line shape.
     *
     * @return the stroke width of the line shape as a double
     */
    @Override
    public double getCustomStrokeWidth() {
        return lineShape.getStrokeWidth();
    }

    /**
     * Retrieves the fill color of the shape. Since LineShape represents a simple line
     * with no fillable area, this method always returns null.
     *
     * @return null, as LineShape does not support fill color.
     */
    @Override
    public Color getFillColor() {
        return null;
    }

    /**
     * Sets the fill color of the shape. This method is overridden to explicitly
     * throw an UnsupportedOperationException since LineShape does not support
     * a fill color due to its nature as a simple line.
     *
     * @param fill the color to be used as the fill color; however, this operation
     *             is not applicable for LineShape and will always result in an exception.
     */
    @Override
    public void setFillColor(Color fill) {
        throw new UnsupportedOperationException("Feature not supported.");
    }


    /**
     * Retrieves the graphical node representation of the LineShape.
     *
     * @return a {@link Node} object*/
    @Override
    public Node getNode() {
        return this.lineShape;
    }

    /**
     * @param points
     * Sets the coordinate of the line and inserts them in an array of double
     */
    @Override
    public void setPoints(Double[] points){
        this.points = points;
    }

    /**
     * Calculates and returns the effective size of the line shape.
     * The effective size is determined as the Euclidean length of the line
     * based on its width and height using the distance*/
    @Override
    public double getEffectiveSize() {
        // Per le linee, la dimensione effettiva è la lunghezza
        double length = Math.hypot(getWidth(), getHeight());
        return length > 0 ? length : 50; // Default se la linea è degenerata
    }

    /**
     * Calculates and retrieves the maximum slider size for the line shape.
     * The maximum slider*/
    @Override
    public double getMaxSliderSize() {
        return Math.max(200, getEffectiveSize()*2);
    }

    /**
     * Retrieves the minimum slider size for the LineShape.
     * This value represents the smallest allowable size for the slider.
     *
     * @return the minimum slider size as a double, which is fixed at 30.
     */
    @Override
    public double getMinSliderSize() {
        return 30;
    }

    /**
     * Generates a formatted label for a slider, representing the value as a string.
     *
     * @param value the current value of the slider
     * @return a formatted string representing the slider value, prefixed with "Length: "
     */
    @Override
    public String getSliderLabel(double value) {
        return String.format("Length: %.1f", value);
    }

    /**
     * Retrieves the text representation associated with this LineShape.
     *
     * @return a String containing the text representation of the LineShape,
     *         or null if no text is associated.
     */
    @Override
    public String getText() {
        return null;
    }

    /**
     * Sets the text content for the object.
     * This method is not supported and will throw an UnsupportedOperationException when invoked.
     *
     * @param content the text content to be set
     */
    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Determines whether the line can be moved by the specified delta values
     * without exceeding the bounds of a defined pane area, considering zoom level
     * and margin constraints.
     *
     * @param deltaX the horizontal movement offset
     * @param deltaY the vertical movement offset
     * @param paneWidth the width of the pane
     * @param paneHeight the height of the pane
     * @param zoom the current zoom factor
     * @param margin the margin around the pane that should not be exceeded
     * @return true if the line can be moved*/
    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        // Ottieni i punti attuali
        double x1 = lineShape.getStartX() + deltaX;
        double y1 = lineShape.getStartY() + deltaY;
        double x2 = lineShape.getEndX() + deltaX;
        double y2 = lineShape.getEndY() + deltaY;

        double minX = margin / zoom;
        double maxX = (paneWidth - margin) / zoom;
        double minY = margin / zoom;
        double maxY = (paneHeight - margin) / zoom;

        boolean isWithinBounds =
                x1 >= minX && x1 <= maxX &&
                        x2 >= minX && x2 <= maxX &&
                        y1 >= minY && y1 <= maxY &&
                        y2 >= minY && y2 <= maxY;

        return isWithinBounds;
    }

    /**
     * Mirrors the existing line shape upwards based on its current position.
     * The method calculates a vertical mirroring transformation that reflects
     * the line segment across a horizontal axis created by its vertical midpoint.
     * Updates the start*/
    @Override
    public void mirrorUp() {
        Point2D end = new Point2D(lineShape.getStartX(), lineShape.getStartY());
        Point2D start = new Point2D(lineShape.getEndX(), lineShape.getEndY());
        Point2D upperPoint = start.getY() <= end.getY() ? start : end;

        double centerY = (start.getY() + end.getY()) / 2;

        double mirroredCenterY = upperPoint.getY() - (centerY - upperPoint.getY());

        double offsetStartY = start.getY() - centerY;
        double offsetEndY = end.getY() - centerY;
        double newStartY = mirroredCenterY + offsetStartY;
        double newEndY = mirroredCenterY + offsetEndY;

        lineShape.setStartY(newStartY);
        lineShape.setEndY(newEndY);
    }


    /**
     * Mirrors a line shape along the vertical axis passing through its lower point.
     *
     * This method flips the current line shape vertically around an axis determined by its
     * lower endpoint in the Y-direction. The line's Y-coordinates are recalculated to create
     * the mirrored effect while maintaining their horizontal positions unchanged.
     */
    @Override
    public void mirrorDown() {
        Point2D end = new Point2D(lineShape.getStartX(), lineShape.getStartY());
        Point2D start = new Point2D(lineShape.getEndX(), lineShape.getEndY());
        Point2D lowerPoint = start.getY() >= end.getY() ? start : end;

        double centerY = (start.getY() + end.getY()) / 2;

        double mirroredCenterY = lowerPoint.getY() + (lowerPoint.getY() - centerY);

        double offsetStartY = start.getY() - centerY;
        double offsetEndY = end.getY() - centerY;
        double newStartY = mirroredCenterY + offsetStartY;
        double newEndY = mirroredCenterY + offsetEndY;

        lineShape.setStartY(newStartY);
        lineShape.setEndY(newEndY);
    }


    /**
     * Reflects the current line horizontally to create a mirror image on the left side.
     * The method calculates the horizontal center of the line and the width. It then
     * computes a vertical axis of reflection to determine new coordinates for the start
     * and end points of the line, effectively flipping the line horizontally.
     *
     * The operation modifies the start and end X-coordinates of the `lineShape`,
     * leaving the Y-coordinates unchanged.
     */
    @Override
    public void mirrorLeft() {
        // Calcola centro
        double centerX = (lineShape.getStartX() + lineShape.getEndX()) / 2;

        // Calcola distanza orizzontale metà (distanza dal centro all'estremità)
        double halfWidth = Math.abs(lineShape.getEndX() - lineShape.getStartX()) / 2;

        // Asse di riflessione: centro - halfWidth
        double axisX = centerX - halfWidth;

        // Riflette entrambi i punti rispetto all'asse verticale
        double newStartX = 2 * axisX - lineShape.getStartX();
        double newEndX = 2 * axisX - lineShape.getEndX();

        lineShape.setStartX(newStartX);
        lineShape.setEndX(newEndX);
    }

    /**
     * Mirrors the line shape horizontally around its right-side extension.
     * The method calculates a vertical axis of reflection based on the current
     * position and dimensions of the line shape. It then adjusts the start and
     * end X-coordinates of the line shape to their mirrored positions relative
     * to the calculated axis.
     */
    @Override
    public void mirrorRight() {
        double centerX = (lineShape.getStartX() + lineShape.getEndX()) / 2;
        double halfWidth = Math.abs(lineShape.getEndX() - lineShape.getStartX()) / 2;

        double axisX = centerX + halfWidth;

        double newStartX = 2 * axisX - lineShape.getStartX();
        double newEndX = 2 * axisX - lineShape.getEndX();

        lineShape.setStartX(newStartX);
        lineShape.setEndX(newEndX);
    }

    /**
     * Sets the horizontal scale factor for the line shape.
     *
     * @*/
    @Override
    public void setScaleX(double scale) {
        lineShape.setScaleX(scale);
    }

    /**
     * Retrieves the horizontal scaling factor for the line shape.
     *
     * @return the horizontal scale factor as a double value
     */
    @Override
    public double getScaleX() {
        return lineShape.getScaleX();
    }

    /**
     * Sets the vertical scale factor for the line shape.
     *
     * @param scale the vertical scale factor to be applied to the line shape
     */
    @Override
    public void setScaleY(double scale) {
        lineShape.setScaleY(scale);
    }

    /**
     * Retrieves the scale factor along the Y-axis for the line shape.
     *
     * @return the Y-axis scale factor of the line shape as a double value.
     */
    @Override
    public double getScaleY() {
        return lineShape.getScaleY();
    }

    /**
     * Mirrors the line shape horizontally around its vertical center.
     *
     * This method calculates the vertical center of the line by averaging its start and end Y-coordinates.
     * It then adjusts the line's start and end Y-coordinates so that the line is flipped horizontally
     * around this computed center point, effectively mirroring its position while preserving its length.
     */
    @Override
    public void mirrorHorizontallyAroundCenter() {
        double centerY = (lineShape.getStartY() + lineShape.getEndY()) / 2;
        double offsetStartY = lineShape.getStartY() - centerY;
        double offsetEndY = lineShape.getEndY() - centerY;

        lineShape.setStartY(centerY - offsetStartY);
        lineShape.setEndY(centerY - offsetEndY);
    }

    /**
     * Mirrors the line shape vertically around its center point along the x-axis.
     *
     * This method calculates the center x-coordinate of the line shape by averaging
     * its start and end x-coordinates. The positions of the start and end points
     * are then adjusted such that they are mirrored along this center line.
     */
    @Override
    public void mirrorVerticallyAroundCenter() {
        double centerX = (lineShape.getStartX() + lineShape.getEndX()) / 2;
        double offsetStartX = lineShape.getStartX() - centerX;
        double offsetEndX = lineShape.getEndX() - centerX;

        lineShape.setStartX(centerX - offsetStartX);
        lineShape.setEndX(centerX - offsetEndX);
    }

    /**
     * Simulates the mirroring of a geometric line shape's center point based on a specified mirror direction.
     *
     * @param direction the direction in which the mirroring should occur, specified by the MirrorDirectionEnum
     * @return an {@code Optional<Point2D>} containing the new center point after being mirrored, or empty if
     *         no change is necessary for the specified direction
     */
    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double startX = lineShape.getStartX();
        double startY = lineShape.getStartY();
        double endX = lineShape.getEndX();
        double endY = lineShape.getEndY();

        double centerX = (startX + endX) / 2;
        double centerY = (startY + endY) / 2;

        double mirroredCenterX = centerX;
        double mirroredCenterY = centerY;

        switch (direction) {
            case UP: {
                double upperY = Math.min(startY, endY);
                double distance = centerY - upperY;
                mirroredCenterY = upperY - distance;
                break;
            }
            case DOWN: {
                double lowerY = Math.max(startY, endY);
                double distance = lowerY - centerY;
                mirroredCenterY = lowerY + distance;
                break;
            }
            case LEFT: {
                double leftX = Math.min(startX, endX);
                double distance = centerX - leftX;
                mirroredCenterX = leftX - distance;
                break;
            }
            case RIGHT: {
                double rightX = Math.max(startX, endX);
                double distance = rightX - centerX;
                mirroredCenterX = rightX + distance;
                break;
            }
            case HORIZONTAL:
            case VERTICAL:
                // Nessun cambiamento della posizione nel canvas
                return Optional.of(new Point2D(centerX, centerY));
        }

        return Optional.of(new Point2D(mirroredCenterX, mirroredCenterY));
    }

    /**
     * Sets the angle of rotation for the line shape.
     *
     * @param angle the angle in degrees to rotate the line shape
     */
    @Override
    public void setAngle(double angle) {
        lineShape.setRotate(angle);
    }

    /**
     * Retrieves the current rotational angle of the lineShape.
     *
     * @return the angle of rotation in degrees as a double value.
     */
    @Override
    public double getAngle() {
        return lineShape.getRotate();
    }

    /**
     * @return String
     * Prints the propeties of the shape
     */
    @Override
    public String toString() {
        return "Line" + ";" + lineShape.getStroke() + ";" + lineShape.getEndX() + ";" + lineShape.getEndY() + ";"
                + lineShape.getStartX() + ";" + lineShape.getStartY() + ";" + lineShape.getStrokeWidth() + ";"
                + this.getAngle() + '\n';
    }



}
