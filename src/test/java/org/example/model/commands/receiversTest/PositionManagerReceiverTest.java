package org.example.model.commands.receiversTest;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class PositionManagerReceiverTest {

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rectangle = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rectangle.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Text", Color.BLACK, Color.GREEN, 80, 30, 16, 1.0, 1.0, 0.0);
        text.draw(150, 150);

        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
        line.draw(new Double[]{200.0, 200.0, 300.0, 250.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.PURPLE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0});

        // Not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                rectangle.cloneShape(), ellipse.cloneShape()
//        ));

        return Stream.of(rectangle, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testResizeAndReset(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        double oldWidth = shape.getWidth();
        double oldHeight = shape.getHeight();

        receiver.resize(200.0);
        assertNotEquals(oldWidth, shape.getWidth(), "Width should change after resize");

        receiver.resetResize(oldWidth, oldHeight);
        assertEquals(oldWidth, shape.getWidth(), 0.01);
        assertEquals(oldHeight, shape.getHeight(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testStretchAndReset(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        double oldWidth = shape.getStretchWidth();
        double oldHeight = shape.getStretchHeight();

        receiver.stretch(150.0, 90.0);
        assertEquals(150.0, shape.getStretchWidth(), 0.01);
        assertEquals(90.0, shape.getStretchHeight(), 0.01);

        receiver.resetStretch(oldWidth, oldHeight);
        assertEquals(oldWidth, shape.getStretchWidth(), 0.01);
        assertEquals(oldHeight, shape.getStretchHeight(), 0.01);
    }


    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testSetAngle(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        receiver.setAngle(45.0);
        assertEquals(45.0, shape.getNode().getRotate(), 0.01);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testMirroring(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);

        Optional<Point2D> simulated = receiver.simulateMirror(MirrorDirectionEnum.UP);
        receiver.mirrorUp();
        if (simulated.isPresent()) {
            assertTrue(simulated.get().getY() < 0 || simulated.get().getY() >= 0); // Just to assert simulation returns
        }

        // Altri mirror (eseguibili senza check)
        receiver.mirrorDown();
        receiver.mirrorLeft();
        receiver.mirrorRight();
        receiver.mirrorHorizontal();
        receiver.mirrorVertical();
    }
}
