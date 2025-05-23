package org.example.model.shape;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import org.example.model.commands.*;

public class ContextMenuCreator {

    public static ContextMenu getContextMenu(UndoManager undoManager, Color fillColor, Color strokeColor, ShapeInterface shape/*, Pane drawingPane*/) {
        ContextMenu contextMenu = new ContextMenu();

        // Fill Color Picker
        Label fillLabel = new Label("Change Fill Color:");
        ColorPicker changeFillColorPicker = new ColorPicker(fillColor);
        HBox fillBox = new HBox(10, fillLabel, changeFillColorPicker);
        CustomMenuItem fillColorItem = new CustomMenuItem(fillBox);
        fillColorItem.setHideOnClick(false);

        // Border Color Picker
        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPicker = new ColorPicker(strokeColor);
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        // Bring Forward
        MenuItem bringForwardItem = new MenuItem("Bring Forward");
        bringForwardItem.setOnAction(e -> {
            //CommandInterface cmd = new BringForwardCommand(shape.getNode(), drawingPane);
            //undoManager.executeCommand(cmd);
        });

        // Send Backward
        MenuItem sendBackwardItem = new MenuItem("Send Backward");
        sendBackwardItem.setOnAction(e -> {
            //CommandInterface cmd = new BringBackwardCommand((shape.getNode(), drawingPane);
            //undoManager.executeCommand(cmd);
        });

        // Build the context menu for EllipseShape and RectangleShape
        contextMenu.getItems().addAll(
                fillColorItem,
                strokeColorItem,
                new SeparatorMenuItem(),
                bringForwardItem,
                sendBackwardItem
        );

        changeFillColorPicker.setOnAction(e -> {
            CommandInterface cmd = new ChangeFillColorCommand(shape, changeFillColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        changeStrokeColorPicker.setOnAction(e -> {
            CommandInterface cmd = new ChangeStrokeColorCommand(shape, changeStrokeColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        return contextMenu;
    }

    public static ContextMenu getLineContextMenu(UndoManager undoManager, Color strokeColor, ShapeInterface shape/*, Pane drawingPane*/) {
        ContextMenu contextMenu = new ContextMenu();

        // Change border color
        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPicker = new ColorPicker(strokeColor);
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        // Bring Forward
        MenuItem bringForwardItem = new MenuItem("Bring Forward");
        bringForwardItem.setOnAction(e -> {
            //CommandInterface cmd = new BringForwardCommand(shape.getNode(), drawingPane);
            //undoManager.executeCommand(cmd);
        });

        // Send Backward
        MenuItem sendBackwardItem = new MenuItem("Send Backward");
        sendBackwardItem.setOnAction(e -> {
            //CommandInterface cmd = new BringBackwardCommand((shape.getNode(), drawingPane);
            //undoManager.executeCommand(cmd);
        });

        // Build the context menu for LineShape
        contextMenu.getItems().addAll(
                strokeColorItem,
                new SeparatorMenuItem(),
                bringForwardItem,
                sendBackwardItem
        );

        changeStrokeColorPicker.setOnAction(e -> {
            CommandInterface cmd = new ChangeStrokeColorCommand(shape, changeStrokeColorPicker.getValue());
            undoManager.executeCommand(cmd);
        });

        return contextMenu;
    }

}
