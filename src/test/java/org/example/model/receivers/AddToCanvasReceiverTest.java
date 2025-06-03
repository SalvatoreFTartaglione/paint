package org.example.model.receivers;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AddToCanvasReceiverTest {

    private DrawingContextDTO dto;
    private AddToCanvasReceiver receiver;
    private Pane pane;

    @BeforeEach
    public void setUp() {
        pane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(pane);
        dto.setSceneX(100);
        dto.setSceneY(100);
        dto.setBorderColor(Color.BLACK);
        dto.setFillColor(Color.BLUE);
        dto.setFontSize(16);
        dto.setText("Hello");
        receiver = new AddToCanvasReceiver(dto);
    }

    @Test
    public void testAddRectangle() {
        dto.setShapeOption(new ShapeOption("Rectangle", null));
        ShapeInterface shape = receiver.addShape();
        assertNotNull(shape);
        assertTrue(pane.getChildren().contains(shape.getNode()));
    }

    @Test
    public void testAddEllipse() {
        dto.setShapeOption(new ShapeOption("Ellipse", null));
        ShapeInterface shape = receiver.addShape();
        assertNotNull(shape);
        assertTrue(pane.getChildren().contains(shape.getNode()));
    }

    @Test
    public void testAddText() {
        dto.setShapeOption(new ShapeOption("Text", null));
        ShapeInterface shape = receiver.addShape();
        assertNotNull(shape);
        assertTrue(pane.getChildren().contains(shape.getNode()));
    }
}
