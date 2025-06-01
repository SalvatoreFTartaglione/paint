package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.Optional;


public class LineShape implements ShapeInterface{

    private Line lineShape;
    private Double[] points;


    public LineShape(Color stroke, double strokeWidth) {
        lineShape = new Line();
        lineShape.setStroke(stroke);
        lineShape.setStrokeWidth(strokeWidth);
    }


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

    public void moveBy(double deltaX, double deltaY) {
        // Moves both points of the line
        lineShape.setStartX(lineShape.getStartX() + deltaX);
        lineShape.setStartY(lineShape.getStartY() + deltaY);
        lineShape.setEndX(lineShape.getEndX() + deltaX);
        lineShape.setEndY(lineShape.getEndY() + deltaY);
    }

    @Override
    public ShapeInterface cloneShape() {
        LineShape cloned = new LineShape(this.getStrokeColor(), this.getCustomStrokeWidth());

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

    @Override
    public Color getStrokeColor() {
        return (Color) lineShape.getStroke();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        lineShape.setStrokeWidth(strokeWidth);
    }

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

    @Override
    public double getWidth() {
        // Restituisce la differenza lungo l'asse X
        return lineShape.getEndX() - lineShape.getStartX();
    }

    @Override
    public double getHeight() {
        // Restituisce la differenza lungo l'asse Y
        return lineShape.getEndY() - lineShape.getStartY();
    }

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

    @Override
    public double getStretchWidth() {
        double deltaX = lineShape.getEndX() - lineShape.getStartX();
        double deltaY = lineShape.getEndY() - lineShape.getStartY();
        return Math.hypot(deltaX, deltaY);
    }

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

    @Override
    public void resetStretch(double width, double height) {
        // Ripristina sia la lunghezza che lo spessore
        stretch(width, height);
    }


    @Override
    public double getCustomStrokeWidth() {
        return lineShape.getStrokeWidth();
    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void setFillColor(Color fill) {
        throw new UnsupportedOperationException("Feature not supported.");
    }


    @Override
    public Node getNode() {
        return this.lineShape;
    }

    /**
     * @param points
     * Sets the coordinate of the line and inserts them in an array of double
     */
    private void setPoints(Double[] points){
        this.points = points;
    }

    @Override
    public double getEffectiveSize() {
        // Per le linee, la dimensione effettiva è la lunghezza
        double length = Math.hypot(getWidth(), getHeight());
        return length > 0 ? length : 50; // Default se la linea è degenerata
    }

    @Override
    public double getMaxSliderSize() {
        return Math.max(200, getEffectiveSize()*2);
    }

    @Override
    public double getMinSliderSize() {
        return 30;
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("Length: %.1f", value);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * @return String
     * Prints the propeties of the shape
     */
    @Override
    public String toString() {
        return "Line" + ";" + lineShape.getStroke() + ";" + lineShape.getEndX() + ";" + lineShape.getEndY() + ";"
                + lineShape.getStartX() + ";" + lineShape.getStartY() + ";" + lineShape.getStrokeWidth()+ "\n";
    }


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

    @Override
    public void setTextScaleX(double scale) {
        lineShape.setScaleX(scale);
    }

    @Override
    public double getTextScaleX() {
        return lineShape.getScaleX();
    }

    @Override
    public void setTextScaleY(double scale) {
        lineShape.setScaleY(scale);
    }

    @Override
    public double getTextScaleY() {
        return lineShape.getScaleY();
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {
        double centerY = (lineShape.getStartY() + lineShape.getEndY()) / 2;
        double offsetStartY = lineShape.getStartY() - centerY;
        double offsetEndY = lineShape.getEndY() - centerY;

        lineShape.setStartY(centerY - offsetStartY);
        lineShape.setEndY(centerY - offsetEndY);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        double centerX = (lineShape.getStartX() + lineShape.getEndX()) / 2;
        double offsetStartX = lineShape.getStartX() - centerX;
        double offsetEndX = lineShape.getEndX() - centerX;

        lineShape.setStartX(centerX - offsetStartX);
        lineShape.setEndX(centerX - offsetEndX);
    }

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


}
