package org.example.model.shape;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;


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
        throw new UnsupportedOperationException("Unsupported operation.");
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
    public double getCustomStrokeWidth() {
        return lineShape.getStrokeWidth();
    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public void setFillColor(Color fill) {
        throw new UnsupportedOperationException("The following feature is not supported");
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
        return Math.max(300, getEffectiveSize() * 3);
    }

    @Override
    public double getMinSliderSize() {
        return 10; // Lunghezza minima per una linea
    }

    @Override
    public void scaleToSize(double newSize) {
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
    public String getSliderLabel(double value) {
        return String.format("Length: %.1f", value);
    }

    @Override
    public String getText() {
        throw new UnsupportedOperationException("Feature not supported.");
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




}
