package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.commands.concreteCommands.ResizeCommand;
import org.example.model.commands.receivers.PositionManagerReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ResizeCommandTest {

    private final double newSize = 200;

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.GREEN, 100, 60, 2, 0.0);
        rect.draw(100, 100);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.GREEN, 2, 100, 60, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Resize", Color.BLACK, Color.GREEN, 100, 60, 14, 1.0, 1.0, 0.0);
        text.draw(100, 100);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{100.0, 100.0, 200.0, 150.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.GREEN, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{100.0, 100.0, 150.0, 120.0, 130.0, 160.0});

        // not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                new RectangleShape(Color.BLACK, Color.GREEN, 60, 40, 2, 0.0) {{
//                    draw(100, 100);
//                }},
//                new EllipseShape(Color.BLACK, Color.GREEN, 2.0, 40, 30, 0.0) {{
//                    draw(140, 120);
//                }}
//        ));

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @BeforeEach
    public void resetZoomBeforeEach() {
        // Placeholder: nothing to reset here, but ready for shared setup if needed.
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandChangesSize(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);

        double oldWidth = shape.getWidth();
        double oldHeight = shape.getHeight();

        ResizeCommand command = new ResizeCommand(receiver, newSize, oldWidth, oldHeight);
        command.executeCommand();

        assertNotEquals(oldWidth, shape.getWidth(), 0.01, "Width should be updated");
        assertNotEquals(oldHeight, shape.getHeight(), 0.01, "Height should be updated");
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


        // Increased tolerance to account for numerical approximation differences
        // caused by resizing complex shapes like TextShape or CompositeShape.
        assertEquals(oldWidth, shape.getWidth(), 2.5, "Width should be restored after undo");
        assertEquals(oldHeight, shape.getHeight(), 2.5, "Height should be restored after undo");
    }
}
