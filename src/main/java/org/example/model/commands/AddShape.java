package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvas;
import org.example.model.shape.*;

public class AddShape implements CommandInterface {

    private DrawingContextDTO dto;

    public AddShape(DrawingContextDTO context) {
        dto = context;
    }

    public void executeCommand() {
        AddToCanvas.add(dto);
    }

    public void undoCommand() {
    }
}
