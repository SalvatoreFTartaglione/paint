package org.example.model.commands.concreteCommandTest;

import javafx.scene.paint.Color;
import org.example.model.concreteCommands.StretchCommand;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Parametrized test for StretchCommand.
 * Verifies stretch and undo behavior across all supported shape types.
 */
public class StretchCommandTest {

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.CORNFLOWERBLUE, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.CORNFLOWERBLUE, 2, 40, 20, 0.0);
        ellipse.draw(60, 60);

        TextShape text = new TextShape("Stretch", Color.BLACK, Color.CORNFLOWERBLUE, 80, 30, 16, 1.0, 1.0, 0.0);
        text.draw(70, 70);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{80.0, 80.0, 130.0, 100.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.CORNFLOWERBLUE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{90.0, 90.0, 140.0, 140.0, 190.0, 90.0});


        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteAndUndoStretch(ShapeInterface shape) {
        double oldWidth = shape.getStretchWidth();
        double oldHeight = shape.getStretchHeight();
        double newWidth = oldWidth + 20;
        double newHeight = oldHeight + 10;

        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        StretchCommand command = new StretchCommand(receiver, newWidth, newHeight, oldWidth, oldHeight);

        command.executeCommand();
        assertEquals(newWidth, shape.getStretchWidth(), 0.01, "Width should be stretched");
        assertEquals(newHeight, shape.getStretchHeight(), 0.01, "Height should be stretched");

        command.undoCommand();
        assertEquals(oldWidth, shape.getStretchWidth(), 0.01, "Width should be restored");
        assertEquals(oldHeight, shape.getStretchHeight(), 0.01, "Height should be restored");
    }
}
