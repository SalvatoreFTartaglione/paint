package org.example.model.commands.concreteCommandTest;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.concreteCommands.PasteCommand;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PasteCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private ShapeReceiver receiver;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);
        dto.setSceneX(100);
        dto.setSceneY(100);
        receiver = new ShapeReceiver(dto);
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.CORNFLOWERBLUE, 80, 40, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.CORNFLOWERBLUE, 2, 40, 20, 0.0);
        ellipse.draw(60, 60);

        TextShape text = new TextShape("PasteTest", Color.BLACK, Color.CORNFLOWERBLUE, 80, 20, 16, 1.0, 1.0, 0.0);
        text.draw(70, 70);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{50.0, 50.0, 150.0, 100.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.CORNFLOWERBLUE, 2, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{50.0, 50.0, 100.0, 80.0, 80.0, 120.0});

        // not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                new RectangleShape(Color.BLACK, Color.CORNFLOWERBLUE, 60, 30, 2, 0.0) {{
//                    draw(50, 50);
//                }},
//                new EllipseShape(Color.BLACK, Color.CORNFLOWERBLUE, 2.0, 30, 30, 0.0) {{
//                    draw(90, 70);
//                }}
//        ));

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandAddsPastedShapeToPane(ShapeInterface copiedShape) {
        receiver.copy(copiedShape); // simulate a prior CopyCommand
        PasteCommand command = new PasteCommand(receiver, dto);

        assertEquals(0, drawingPane.getChildren().size(), "Pane should be initially empty");

        command.executeCommand();

        ShapeInterface pasted = command.getPastedShape();
        assertNotNull(pasted, "Pasted shape should not be null");
        assertEquals(1, drawingPane.getChildren().size(), "Pane should contain one node after paste");
        assertTrue(drawingPane.getChildren().contains(pasted.getNode()), "Pasted shape node should be in the pane");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRemovesPastedShapeFromPane(ShapeInterface copiedShape) {
        receiver.copy(copiedShape); // simulate a prior CopyCommand
        PasteCommand command = new PasteCommand(receiver, dto);

        command.executeCommand();
        ShapeInterface pasted = command.getPastedShape();

        command.undoCommand();

        assertFalse(drawingPane.getChildren().contains(pasted.getNode()), "Pasted shape should be removed after undo");
    }
}
