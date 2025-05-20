package org.example.model.shape;

import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.example.model.commands.ChangeFillColorCommand;
import org.example.model.commands.ChangeStrokeColorCommand;
import org.example.model.commands.Command;
import org.example.model.commands.UndoManager;

public class ContextMenuCreator {

    public static ContextMenu getContextMenu(UndoManager undoManager, Color fillColor, Color strokeColor, ShapeInterface shape) {
        ContextMenu contextMenu = new ContextMenu();
        Label fillLabel = new Label("Change Fill Color:");
        ColorPicker changeFillColorPicker = new ColorPicker(fillColor);
        HBox fillBox = new HBox(10, fillLabel, changeFillColorPicker);
        CustomMenuItem fillColorItem = new CustomMenuItem(fillBox);
        fillColorItem.setHideOnClick(false);

        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPicker = new ColorPicker(strokeColor);
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        contextMenu.getItems().addAll(fillColorItem, strokeColorItem);

        changeFillColorPicker.setOnAction(e -> {
            Command cmd = new ChangeFillColorCommand(shape, changeFillColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        changeStrokeColorPicker.setOnAction(e -> {
            Command cmd = new ChangeStrokeColorCommand(shape, changeStrokeColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });
        return contextMenu;
    }

    public static ContextMenu getLineContextMenu(UndoManager undoManager,Color strokeColor, ShapeInterface shape) {
        ContextMenu contextMenu = new ContextMenu();
        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPicker = new ColorPicker(strokeColor);
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        contextMenu.getItems().add(strokeColorItem);

        changeStrokeColorPicker.setOnAction(e -> {
            Command cmd = new ChangeStrokeColorCommand(shape, changeStrokeColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        return contextMenu;

    }

}
