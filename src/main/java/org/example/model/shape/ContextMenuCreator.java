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

        public ContextMenuWithPickers(ContextMenu contextMenu, ColorPicker fillColorPicker, ColorPicker strokeColorPicker) {
            this.contextMenu = contextMenu;
            this.fillColorPicker = fillColorPicker;
            this.strokeColorPicker = strokeColorPicker;
        }
    }

    public ContextMenuWithPickers create(Color fillColor, Color strokeColor) {
        ContextMenu contextMenu = new ContextMenu();

        // Fill Color
        ColorPicker fillPicker = new ColorPicker(fillColor);
        Label fillLabel = new Label("Change Fill Color:");
        HBox fillBox = new HBox(10, fillLabel, fillPicker);
        CustomMenuItem fillColorItem = new CustomMenuItem(fillBox);
        fillColorItem.setHideOnClick(false);

        // Stroke Color
        ColorPicker strokePicker = new ColorPicker(strokeColor);
        Label strokeLabel = new Label("Change Border Color:");
        HBox strokeBox = new HBox(10, strokeLabel, strokePicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(strokeBox);
        strokeColorItem.setHideOnClick(false);

        contextMenu.getItems().addAll(fillColorItem, strokeColorItem);

        return new ContextMenuWithPickers(contextMenu, fillPicker, strokePicker);
    }

    public ContextMenuWithPickers createLineContextMenu(Color strokeColor) {
        ContextMenu contextMenu = new ContextMenu();

        Label borderLabel = new Label("Change Border Color:");
        ColorPicker strokePicker = new ColorPicker(strokeColor);
        HBox borderBox = new HBox(10, borderLabel, strokePicker);
        CustomMenuItem strokeItem = new CustomMenuItem(borderBox);
        strokeItem.setHideOnClick(false);

        contextMenu.getItems().add(strokeItem);

        return new ContextMenuWithPickers(contextMenu, null, strokePicker);
    }

}
