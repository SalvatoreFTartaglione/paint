package org.example.model.shape;

import javafx.scene.paint.Color;

public interface ContextMenuActions {
    void onFillColorChanged(Color newColor);
    void onStrokeColorChanged(Color newColor);
    void onSendToBack();
    void onBringToFront();
    void onCopy();
    void onPaste();
}
