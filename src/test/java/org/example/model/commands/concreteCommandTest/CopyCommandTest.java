package org.example.model.commands.concreteCommandTest;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.concreteCommands.CopyCommand;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class CopyCommandTest {

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
        RectangleShape rectangle = new RectangleShape(Color.BLACK, Color.BLUE, 80, 40, 2, 0.0);
        rectangle.draw(70, 70);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2, 40, 20, 0.0);
        ellipse.draw(60, 60);

        TextShape text = new TextShape("copy", Color.BLACK, Color.BLUE, 80, 20, 16, 1.0, 1.0, 0.0);
        text.draw(80, 80);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{50.0, 50.0, 100.0, 100.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.BLUE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{100.0, 100.0, 150.0, 150.0, 200.0, 100.0});


        return Stream.of(rectangle, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testCopyCommandStoresShapeInReceiver(ShapeInterface originalShape) {
        drawingPane.getChildren().add(originalShape.getNode());

        CopyCommand command = new CopyCommand(originalShape, receiver);
        command.executeCommand();

        assertTrue(receiver.hasShape(), "Receiver should hold a copied shape");
        ShapeInterface copiedShape = receiver.getCopy();

        assertNotNull(copiedShape, "Copied shape should not be null");
        assertNotSame(originalShape, copiedShape, "Copied shape should be a new instance");
        assertEquals(originalShape.getClass(), copiedShape.getClass(), "Copied shape should be of the same class");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandClearsReceiver(ShapeInterface originalShape) {
        CopyCommand command = new CopyCommand(originalShape, receiver);
        command.executeCommand();

        assertTrue(receiver.hasShape(), "Receiver should contain a shape after copy");

        command.undoCommand();
        assertFalse(receiver.hasShape(), "Receiver should not contain shape after undo");
    }
}
