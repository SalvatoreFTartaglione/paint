package org.example.model.commands.receiversTest;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChangesOnShapeReceiverTest {

    private ChangesOnShapeReceiver receiver;

    @BeforeEach
    public void setUp() {
        receiver = new ChangesOnShapeReceiver();
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Text", Color.BLACK, Color.GREEN, 80, 30, 14, 1.0, 1.0, 0.0);
        text.draw(150, 150);

        // Feature Fill color not supported --> tested manually
//        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
//        line.draw(new Double[]{200.0, 200.0, 300.0, 250.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.ORANGE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0});

        /*CompositeShape composite = new CompositeShape(List.of(
                rect.cloneShape(), ellipse.cloneShape()
        ));*/

        return Stream.of(rect, ellipse, text, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testChangeStrokeColor(ShapeInterface shape) {
        Color newStroke = Color.BROWN;
        receiver.changeStrokeColor(shape, newStroke);
        assertEquals(newStroke, shape.getStrokeColor(), "Stroke color should be updated");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testChangeFillColor(ShapeInterface shape) {
        Color newFill = Color.AQUA;
        receiver.changeFillColor(shape, newFill);
        assertEquals(newFill, shape.getFillColor(), "Fill color should be updated");
    }
}
