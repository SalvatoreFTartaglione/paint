package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.model.commands.concreteCommands.MoveShapeCommand;
import org.example.model.commands.receivers.DraggingReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MoveShapeCommandTest {

    private DraggingReceiver receiver;

    @BeforeEach
    public void setUp() {
        receiver = new DraggingReceiver() {
            @Override
            public void moveShape(ShapeInterface shape, double dx, double dy) {
                shape.moveBy(dx, dy);
            }

            @Override
            public void resetShapePosition(ShapeInterface shape, Point2D originalPosition) {
                shape.setX(originalPosition.getX());
                shape.setY(originalPosition.getY());
            }
        };
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.YELLOW, 100, 60, 2, 0.0);
        rect.draw(50, 80);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.YELLOW, 2, 40, 20, 0.0);
        ellipse.draw(50, 80);

        TextShape text = new TextShape("move", Color.BLACK, Color.YELLOW, 100, 30, 14, 1.0, 1.0, 0.0);
        text.draw(50, 80);

        LineShape line = new LineShape(Color.BLACK, 2, 0.0);
        line.draw(new Double[]{50.0, 80.0, 150.0, 120.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.YELLOW, 2, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{50.0, 80.0, 100.0, 120.0, 150.0, 80.0});

        // not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                new RectangleShape(Color.BLACK, Color.BEIGE, 60, 30, 2, 0.0) {{
//                    draw(50, 80);
//                }},
//                new EllipseShape(Color.BLACK, Color.BEIGE, 2.0, 30, 30, 0.0) {{
//                    draw(110, 100);
//                }}
//        ));

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testExecuteCommandMovesShape(ShapeInterface shape) {
        Point2D initial = new Point2D(shape.getX(), shape.getY());
        MoveShapeCommand command = new MoveShapeCommand(shape, 20, 30, receiver);

        command.executeCommand();

        assertEquals(initial.getX() + 20, shape.getX(), 0.01, "X should be moved by 20");
        assertEquals(initial.getY() + 30, shape.getY(), 0.01, "Y should be moved by 30");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testUndoCommandRestoresOriginalPosition(ShapeInterface shape) {
        Point2D initial = new Point2D(shape.getX(), shape.getY());
        MoveShapeCommand command = new MoveShapeCommand(shape, 20, 30, receiver);

        command.executeCommand();
        command.undoCommand();

        assertEquals(initial.getX(), shape.getX(), 0.01, "X should be restored");
        assertEquals(initial.getY(), shape.getY(), 0.01, "Y should be restored");
    }
}
