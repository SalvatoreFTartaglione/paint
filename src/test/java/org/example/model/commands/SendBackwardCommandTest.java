package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

public class SendBackwardCommandTest {

    private Pane drawingPane;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.ORANGE, 100, 60, 2, 0.0);
        rect.draw(100, 100);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.CYAN, 2, 100, 60, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Test", Color.BLACK, Color.BLUE, 80, 30, 14, 1.0, 1.0, 0.0);
        text.draw(100, 100);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{100.0, 100.0, 200.0, 150.0});

        return Stream.of(rect, ellipse, text, line);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandSendsShapeToBack(ShapeInterface shapeToSendBack) {
        ShapeInterface topShape = new RectangleShape(Color.BLACK, Color.YELLOW, 100, 60, 2, 0.0);
        topShape.draw(100, 100);

        drawingPane.getChildren().addAll(shapeToSendBack.getNode(), topShape.getNode());

        assertEquals(shapeToSendBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(topShape.getNode(), drawingPane.getChildren().get(1));

        PositionManagerReceiver receiver = new PositionManagerReceiver(topShape);
        SendBackwardCommand command = new SendBackwardCommand(receiver);
        command.executeCommand();

        // After execute: topShape goes to back
        assertEquals(topShape.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeToSendBack.getNode(), drawingPane.getChildren().get(1));
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRestoresOriginalOrder(ShapeInterface shapeToSendBack) {
        ShapeInterface topShape = new RectangleShape(Color.BLACK, Color.YELLOW, 100, 60, 2, 0.0);
        topShape.draw(100, 100);

        drawingPane.getChildren().addAll(shapeToSendBack.getNode(), topShape.getNode());

        PositionManagerReceiver receiver = new PositionManagerReceiver(topShape);
        SendBackwardCommand command = new SendBackwardCommand(receiver);
        command.executeCommand();
        command.undoCommand();

        // Original order restored
        assertEquals(shapeToSendBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(topShape.getNode(), drawingPane.getChildren().get(1));
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteThrowsIfNodeHasNoPaneParent(ShapeInterface shape) {
        // Do not add shape to drawingPane
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        SendBackwardCommand command = new SendBackwardCommand(receiver);

        Exception ex = assertThrows(IllegalStateException.class, command::executeCommand);
        assertTrue(ex.getMessage().contains("Node does not have a Pane parent"));
    }
}
