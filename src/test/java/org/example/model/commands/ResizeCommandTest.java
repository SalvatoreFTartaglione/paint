package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ResizeCommandTest {

    private double newSize = 200;

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.GREEN, 100, 60, 2, 0.0);
        rect.draw(100, 100);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.GREEN, 2, 100, 60, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Resize", Color.BLACK, Color.GREEN, 100, 60, 14, 1.0, 1.0, 0.0);
        text.draw(100, 100);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{100.0, 100.0, 200.0, 150.0});

        return Stream.of(rect, ellipse, text, line);
    }

    @BeforeEach
    public void resetZoomBeforeEach() {
        // No shared state, but placeholder if needed in the future.
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandChangesSize(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);

        double oldWidth = shape.getWidth();
        double oldHeight = shape.getHeight();

        ResizeCommand command = new ResizeCommand(receiver, newSize, oldWidth, oldHeight);

        command.executeCommand();

        assertNotEquals(oldWidth, shape.getWidth(), 0.01);
        assertNotEquals(oldHeight, shape.getHeight(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRestoresOriginalSize(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);

        double oldWidth = shape.getWidth();
        double oldHeight = shape.getHeight();

        ResizeCommand command = new ResizeCommand(receiver, newSize, oldWidth, oldHeight);

        command.executeCommand();
        command.undoCommand();

        assertEquals(oldWidth, shape.getWidth(), 0.01);
        assertEquals(oldHeight, shape.getHeight(), 0.01);
    }
}
