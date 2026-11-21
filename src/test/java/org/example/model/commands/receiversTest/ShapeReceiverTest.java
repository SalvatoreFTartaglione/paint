package org.example.model.commands.receiversTest;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ShapeReceiverTest {

    private Pane pane;
    private DrawingContextDTO dto;

    @BeforeEach
    public void setUp() {
        pane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(pane);
        dto.setSceneX(100);
        dto.setSceneY(100);
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2, 60, 30, 0.0);
        ellipse.draw(60, 60);

        TextShape text = new TextShape("Copy", Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);

        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
        line.draw(new Double[]{100.0, 100.0, 200.0, 120.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.PURPLE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0});

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testCopyAndGetCopy(ShapeInterface shape) {
        ShapeReceiver receiver = new ShapeReceiver(dto);
        receiver.copy(shape);
        ShapeInterface copy = receiver.getCopy();

        assertNotNull(copy);
        assertNotSame(shape, copy, "Copy should be a clone");
        assertEquals(shape.getClass(), copy.getClass());
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testCutAndHasCutted(ShapeInterface shape) {
        ShapeReceiver receiver = new ShapeReceiver(dto);
        pane.getChildren().add(shape.getNode());

        receiver.cut(shape, dto);
        assertTrue(receiver.hasCutted());
        assertFalse(pane.getChildren().contains(shape.getNode()), "Shape should be removed after cut");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testPasteAddsShapeToPane(ShapeInterface shape) {
        ShapeReceiver receiver = new ShapeReceiver(dto);
        receiver.copy(shape);

        int initial = pane.getChildren().size();
        receiver.paste(receiver.getCopy(), dto);

        assertEquals(initial + 1, pane.getChildren().size(), "Pasted shape should be added to pane");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testDeleteAndUndoDeleteRestoresShape(ShapeInterface shape) {
        ShapeReceiver receiver = new ShapeReceiver(dto);
        shape.setRestorePosition(new Point2D(75, 75));
        pane.getChildren().add(shape.getNode());

        receiver.delete(dto, shape);
        assertFalse(pane.getChildren().contains(shape.getNode()), "Shape should be deleted from pane");

        receiver.undoDelete(dto);
        assertTrue(pane.getChildren().contains(shape.getNode()), "Shape should be restored after undo");
    }
}
