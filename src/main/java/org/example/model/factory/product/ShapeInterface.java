package org.example.model.factory.product;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import org.example.model.enums.MirrorDirectionEnum;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface ShapeInterface {

    // utils method
    ShapeInterface cloneShape();

    // Shape color management
    void setStrokeColor(Color color);

    /** Restituisce i figli di un composite; per le forme semplici torna lista vuota. */
    default List<ShapeInterface> getChildren() {
        return Collections.emptyList();
    }

    // Color utils
    boolean canHaveFill();
    Color getStrokeColor();
    void setCustomStrokeWidth(double strokeWidth);
    double getCustomStrokeWidth();
    void setFillColor(Color fill);
    Color getFillColor();

    // Resize and stretch
    double getWidth();
    double getHeight();
    void resize(double newSize);
    void setSize(double width, double height);
    double getStretchWidth();
    double getStretchHeight();
    void stretch(double width, double height);
    void resetStretch(double width, double height);
    double getEffectiveSize();
    default double getMaxSliderSize() {
        return 350;
    }
    default double getMinSliderSize() {
        return Math.max(1, getEffectiveSize() * 0.1);
    }
    default String getSliderLabel(double value) {
        return String.format("%.1f", value);
    }

    // Drawing points for the shape
    double getX();
    double getY();
    void setX(double X);
    void setY(double Y);
    Double[] getPoints();
    void draw(double X, double Y);
    void draw(Double[] points);

    // Serialization
    String toString();

    // Get access to the Node of the Shape
    Node getNode();

    // Dragging support
    void moveBy(double deltaX, double deltaY);

    default Point2D getRestorePosition() {
        return new Point2D(getX(), getY());
    }

    default void setRestorePosition(Point2D point) {
        draw(point.getX(), point.getY());
    }


    //Text operation support
    String getText();
    void setText(String content);

    // Mirroring utils
    boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin);
    void mirrorUp();
    void mirrorDown();
    void mirrorLeft();
    void mirrorRight();
    Optional<Point2D> simulateMirror(MirrorDirectionEnum direction);

    // Utils for load and save command for text shape
    void setScaleX(double scale);
    double getScaleX();
    void setScaleY(double scale);
    double getScaleY();
    void mirrorHorizontallyAroundCenter();
    void mirrorVerticallyAroundCenter();

    // Angle utils
    void setAngle(double angle);
    double getAngle();

    default void setPoints(Double[] points){
        return;
    }
}