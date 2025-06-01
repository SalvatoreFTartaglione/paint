package org.example.model.contextMenu;

import javafx.scene.paint.Color;

public interface ContextMenuActions {
    void onFillColorChanged(Color newColor);
    void onStrokeColorChanged(Color newColor);
    void onSendToBack();
    void onBringToFront();
    void onCopy();
    void onCut();
    void onPaste();
    void onDelete();
    void onResize(double oldWidth, double oldHeight, double newSize);
    void onStretch(double oldWidth, double oldHeight, double newWidth, double newHeight);
    void onMirrorUp();
    void onMirrorDown();
    void onMirrorLeft();
    void onMirrorRight();
    void onMirrorHorizontalAroundCenter();
    void onMirrorVerticalAroundCenter();
    void onAddToFavoritesWithName(String name);
    void onUngroup();
    void onRotate(double oldAngle, double newAngle);
}
