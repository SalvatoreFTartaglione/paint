package org.example.model.commands.concreteCommandTest;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.concreteCommands.CutCommand;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CutCommandTest {

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
        RectangleShape rectangle = new RectangleShape(Color.BLACK, Color.LIGHTBLUE, 80, 50, 2, 0.0);
        rectangle.draw(100, 100);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.LIGHTBLUE, 2, 40, 20, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("cut", Color.BLACK, Color.LIGHTBLUE, 80, 30, 14, 1.0, 1.0, 0.0);
        text.draw(100, 100);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{100.0, 100.0, 150.0, 150.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.LIGHTBLUE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{100.0, 100.0, 150.0, 150.0, 200.0, 100.0});


        return Stream.of(rectangle, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandRemovesShapeAndKeepsCopy(ShapeInterface shape) {
        drawingPane.getChildren().add(shape.getNode());
        CutCommand command = new CutCommand(shape, receiver, dto);

        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be in pane before cut");

        command.executeCommand();

        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed from pane after cut");
        assertTrue(receiver.hasShape(), "Receiver should hold a cloned shape");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRestoresShape(ShapeInterface shape) {
        drawingPane.getChildren().add(shape.getNode());
        CutCommand command = new CutCommand(shape, receiver, dto);

        command.executeCommand();
        assertFalse(drawingPane.getChildren().contains(shape.getNode()), "Shape should be removed");

        command.undoCommand();

        assertTrue(drawingPane.getChildren().contains(shape.getNode()), "Shape should be restored after undo");
    }
}
