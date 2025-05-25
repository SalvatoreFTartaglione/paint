package org.example.model.shape;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ContextMenuManager {

    public static class MenuComponents  {

        public final ContextMenu contextMenu;
        public final ColorPicker fillColorPicker;
        public final ColorPicker strokeColorPicker;

        public MenuComponents(ContextMenu contextMenu, ColorPicker fillColorPicker, ColorPicker strokeColorPicker) {
            this.contextMenu = contextMenu;
            this.fillColorPicker = fillColorPicker;
            this.strokeColorPicker = strokeColorPicker;
        }
    }

    /*
    * Creates a complitely configured context menu with event handlers
     */
    public static MenuComponents createContextMenu(ShapeInterface shape, ContextMenuActions actions) {

        ContextMenu contextMenu = new ContextMenu();

        ColorPicker fillPicker = null;
        ColorPicker strokePicker = new ColorPicker(shape.getStrokeColor());

        // Fill Color (for line must be null)
        if (shape.getFillColor() != null) {
            fillPicker = createColorPickerItem(contextMenu, "Change Fill Color:", shape.getFillColor(), actions::onFillColorChanged);
        }

        // Stroke Color
        createColorPickerItem(contextMenu, "Change Border Color:", shape.getStrokeColor(), actions::onStrokeColorChanged);

        // Send to Back
        CustomMenuItem sendToBackItem = new CustomMenuItem(new Label("Send to Back"));
        sendToBackItem.setOnAction(e -> actions.onSendToBack());

        // Bring to Front
        CustomMenuItem bringToFrontItem = new CustomMenuItem(new Label("Bring to Front"));
        bringToFrontItem.setOnAction(e -> actions.onBringToFront());

        contextMenu.getItems().addAll(sendToBackItem, bringToFrontItem);

        return new MenuComponents(contextMenu, fillPicker, strokePicker);
    }

    private static ColorPicker createColorPickerItem(ContextMenu contextMenu, String labelText, Color initialColor, ColorChangeHandler handler) {
        ColorPicker colorPicker = new ColorPicker(initialColor);
        Label label = new Label(labelText);
        HBox box = new HBox(10, label, colorPicker);
        CustomMenuItem item = new CustomMenuItem(box);
        item.setHideOnClick(false);

        colorPicker.setOnAction(e -> handler.onColorChanged(colorPicker.getValue()));

        contextMenu.getItems().add(item);
        return colorPicker;
    }

    @FunctionalInterface
    private interface ColorChangeHandler {
        void onColorChanged(Color color);
    }

}

