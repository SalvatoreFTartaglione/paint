package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.commands.concreteCommands.RotateCommand;
import org.example.model.commands.receivers.PositionManagerReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Parametrized test for RotateCommand.
 * It verifies that the rotation angle is correctly applied and reverted.
 */
public class RotateCommandTest {

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.CYAN, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.CYAN, 2, 40, 20, 0.0);
        ellipse.draw(60, 60);

        TextShape text = new TextShape("Rotate", Color.BLACK, Color.CYAN, 80, 30, 16, 1.0, 1.0, 0.0);
        text.draw(70, 70);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{80.0, 80.0, 130.0, 100.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.CYAN, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{90.0, 90.0, 140.0, 140.0, 190.0, 90.0});

        // not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                new RectangleShape(Color.BLACK, Color.CYAN, 50, 30, 2, 0.0) {{ draw(20, 20); }},
//                new EllipseShape(Color.BLACK, Color.CYAN, 2, 20, 10, 0.0) {{ draw(40, 40); }}
//        ));

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteAndUndoRotate(ShapeInterface shape) {
        double originalAngle = shape.getAngle();
        double newAngle = originalAngle + 45.0;

        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        RotateCommand command = new RotateCommand(receiver, originalAngle, newAngle);

        command.executeCommand();
        assertEquals(newAngle, shape.getAngle(), 0.01, "Rotation angle should be updated");

        command.undoCommand();
        assertEquals(originalAngle, shape.getAngle(), 0.01, "Rotation angle should be restored after undo");
    }
}
