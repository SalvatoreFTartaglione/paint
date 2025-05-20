package org.example.model.shape;

import javafx.scene.Node;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.CustomMenuItem;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.commands.ChangeFillColorCommand;
import org.example.model.commands.ChangeStrokeColorCommand;
import org.example.model.commands.Command;
import org.example.model.commands.UndoManager;

public class ContextMenuCreator {

    public void getContextMenu(Pane drawingPane, ContextMenu contextMenu, UndoManager undoManager, Color fillColor, Color strokeColor) {
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
            Command cmd = new ChangeFillColorCommand(this, changeFillColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        changeStrokeColorPicker.setOnAction(e -> {
            Command cmd = new ChangeStrokeColorCommand(this, changeStrokeColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });
    }

    public Node findShapeAt(Pane drawingPane, double x, double y) {
        for (Node node : drawingPane.getChildren()) {
            if (node instanceof ShapeInterface) {
                ShapeInterface shape = (ShapeInterface) node;
                if (Math.abs(shape.getX() - x) <= 40 && Math.abs(shape.getY() - y) <= 80) {
                    return node;
                }
            }
        }
        return null;
    }
}
