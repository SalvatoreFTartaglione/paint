package org.example.model.commands.receiversTest;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class DraggingReceiverTest {

    private DraggingReceiver receiver;

    @BeforeEach
    public void setUp() {
        receiver = new DraggingReceiver();
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Drag", Color.BLACK, Color.GREEN, 80, 30, 16, 1.0, 1.0, 0.0);
        text.draw(150, 150);

        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
        line.draw(new Double[]{200.0, 200.0, 300.0, 250.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.PURPLE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0});

        /*CompositeShape composite = new CompositeShape(List.of(
                rect.cloneShape(), ellipse.cloneShape()
        ));*/

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testMoveShape(ShapeInterface shape) {
        double initialX = shape.getX();
        double initialY = shape.getY();
        double deltaX = 10;
        double deltaY = 15;

        receiver.moveShape(shape, deltaX, deltaY);

        assertEquals(initialX + deltaX, shape.getX(), 0.01);
        assertEquals(initialY + deltaY, shape.getY(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testResetShapePosition(ShapeInterface shape) {
        double originalX = shape.getX();
        double originalY = shape.getY();
        Point2D originalPos = new Point2D(originalX, originalY);

        receiver.moveShape(shape, 20, 30);  // apply some movement

        receiver.resetShapePosition(shape, originalPos);  // undo movement

        assertEquals(originalX, shape.getX(), 0.01);
        assertEquals(originalY, shape.getY(), 0.01);
    }
}
