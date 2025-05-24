package org.example.model.shape;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.paint.Color;
import org.example.model.commands.UndoManager;

public interface ShapeInterface {

    // utils method
    ContextMenuCreator.ContextMenuWithPickers createContextMenu(Color fillColor, Color strokeColor);

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

    // Draws the shape
    void draw(double X, double Y);
    void draw(Double[] points);

    // Simplified serialization
    String toString();
    Node getNode();
}