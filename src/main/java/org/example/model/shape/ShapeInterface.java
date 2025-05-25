package org.example.model.shape;

import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import org.example.model.commands.UndoManager;

public interface ShapeInterface {

    // utils method
    ShapeInterface cloneShape();

    // Shape color management
    void setStrokeColor(Color color);

    Color getStrokeColor();

    void setCustomStrokeWidth(double strokeWidth);

    double getCustomStrokeWidth();

    void setFillColor(Color fill);

    Color getFillColor();

    // Height and Width
    double getWidth();

    double getHeight();

    void setSize(double width, double height);

    // Drawing points for the shape
    double getX();

    double getY();

    void setX(double X);

    void setY(double Y);

    Double[] getPoints();

    // Draws the shape
    void draw(double X, double Y);

    void draw(Double[] points);

    // Simplified serialization
    String toString();

    // Get access to the Node of the Shape
    Node getNode();

    // Dragging support
    void moveBy(double deltaX, double deltaY);

    /**
     * Calcola la dimensione effettiva per il ridimensionamento visuale
     *
     * @return la dimensione corrente da usare come base per lo slider
     */
    double getEffectiveSize();

    /**
     * Calcola la dimensione massima ragionevole per lo slider
     *
     * @return la dimensione massima per il range dello slider
     */
    default double getMaxSliderSize() {
        return getEffectiveSize() * 3;
    }

    /**
     * Calcola la dimensione minima ragionevole per lo slider
     *
     * @return la dimensione minima per il range dello slider
     */
    default double getMinSliderSize() {
        return Math.max(1, getEffectiveSize() * 0.1);
    }

    /**
     * Ridimensiona la shape basandosi su un valore di scala
     *
     * @param scale il fattore di scala da applicare
     */
    void scaleToSize(double scale);

    /**
     * Restituisce l'etichetta da mostrare nello slider
     *
     * @param value il valore corrente dello slider
     * @return la stringa da mostrare come label
     */
    default String getSliderLabel(double value) {
        return String.format("%.1f", value);
    }
}