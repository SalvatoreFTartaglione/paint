package org.example.model.shape;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class ContextMenuCreator {

    public static class ContextMenuWithPickers {

        public final ContextMenu contextMenu;
        public final ColorPicker fillColorPicker;
        public final ColorPicker strokeColorPicker;
        public final CustomMenuItem sendToBackItem;
        public final CustomMenuItem bringToFrontItem;

        public ContextMenuWithPickers(ContextMenu contextMenu, ColorPicker fillColorPicker, ColorPicker strokeColorPicker, CustomMenuItem sendToBackItem, CustomMenuItem bringToFrontItem) {
            this.contextMenu = contextMenu;
            this.fillColorPicker = fillColorPicker;
            this.strokeColorPicker = strokeColorPicker;
            this.sendToBackItem = sendToBackItem;
            this.bringToFrontItem = bringToFrontItem;
        }
    }

    public ContextMenuWithPickers createContextMenu(Color fillColor, Color strokeColor) {

        ContextMenu contextMenu = new ContextMenu();
        ColorPicker fillPicker = null;
        CustomMenuItem fillColorItem = null;

        // Fill Color (for line must be null)
        if (fillColor != null) {
            fillPicker = new ColorPicker(fillColor);
            Label fillLabel = new Label("Change Fill Color:");
            HBox fillBox = new HBox(10, fillLabel, fillPicker);
            fillColorItem = new CustomMenuItem(fillBox);
            fillColorItem.setHideOnClick(false);
        }

        // Stroke Color
        ColorPicker strokePicker = new ColorPicker(strokeColor);
        Label strokeLabel = new Label("Change Border Color:");
        HBox strokeBox = new HBox(10, strokeLabel, strokePicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(strokeBox);
        strokeColorItem.setHideOnClick(false);

        // Send to Back
        CustomMenuItem sendToBackItem = new CustomMenuItem(new Label("Send to Back"));

        // Bring to Front
        CustomMenuItem bringToFrontItem = new CustomMenuItem(new Label("Bring to Front"));

        if (fillColor != null) {
            // Add items to menu
            contextMenu.getItems().addAll(fillColorItem, strokeColorItem, sendToBackItem, bringToFrontItem);
            return new ContextMenuWithPickers(contextMenu, fillPicker, strokePicker, sendToBackItem, bringToFrontItem);
        }
        // Add items to menu
        contextMenu.getItems().addAll(strokeColorItem, sendToBackItem, bringToFrontItem);
        return new ContextMenuWithPickers(contextMenu, null, strokePicker, sendToBackItem, bringToFrontItem);
    }


}

