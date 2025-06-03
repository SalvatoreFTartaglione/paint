package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DeleteCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeReceiver receiver;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);
        receiver = new ShapeReceiver(dto);
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.ORANGE, 100, 50, 2, 0.0);
        rect.draw(100, 100);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.ORANGE, 2, 40, 20, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("delete", Color.BLACK, Color.ORANGE, 80, 30, 14, 1.0, 1.0, 0.0);
        text.draw(100, 100);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{100.0, 100.0, 150.0, 150.0});

        return Stream.of(rect, ellipse, text, line);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandRemovesShapeFromPane(ShapeInterface shape) {
        drawingPane.getChildren().add(shape.getNode());
        DeleteCommand command = new DeleteCommand(dto, shape, receiver);

        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be in pane before deletion");

        command.executeCommand();

        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed from pane after deletion");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRestoresShapeToPane(ShapeInterface shape) {
        drawingPane.getChildren().add(shape.getNode());
        DeleteCommand command = new DeleteCommand(dto, shape, receiver);

        command.executeCommand();
        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed");

        command.undoCommand();

        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be restored to pane after undo");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testShapeRestoredInSamePosition(ShapeInterface shape) {
        drawingPane.getChildren().add(shape.getNode());
        int originalIndex = drawingPane.getChildren().indexOf(shape.getNode());

        DeleteCommand command = new DeleteCommand(dto, shape, receiver);
        command.executeCommand();
        command.undoCommand();

        int restoredIndex = drawingPane.getChildren().indexOf(shape.getNode());
        assertEquals(originalIndex, restoredIndex, "Shape should be restored in the same position");
    }
}
