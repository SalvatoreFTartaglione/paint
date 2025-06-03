package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.receivers.PositionManagerReceiver;
import org.example.model.shape.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class BringForwardCommandTest {

    static Stream<ShapeInterface> shapeProvider() {
        return Stream.of(
                new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0),
                new EllipseShape(Color.BLACK, Color.RED, 2.0, 50.0, 30.0, 0.0),
                new TextShape("Front", Color.BLACK, Color.RED, 0, 0, 16, 1.0, 1.0, 0.0),
                new LineShape(Color.BLACK, 2.0, 0.0) {{
                    draw(new Double[]{0.0, 0.0, 100.0, 100.0});
                }},
                new PolygonShape(Color.BLACK, Color.RED, 2.0, 1.0, 1.0, 0.0) {{
                    draw(new Double[]{100.0, 100.0, 150.0, 150.0, 200.0, 100.0});
                }}
                //not implemented
//                new CompositeShape(List.of(
//                        new RectangleShape(Color.BLACK, Color.RED, 100, 50, 2, 0.0) {{
//                            draw(100, 100);
//                        }},
//                        new EllipseShape(Color.BLACK, Color.BLUE, 2, 40, 30, 0.0) {{
//                            draw(150, 150);
//                        }}
//                ))
        );
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testBringForwardAndUndo(ShapeInterface shapeFront) {
        ShapeInterface shapeBack = new RectangleShape(Color.GRAY, Color.LIGHTGRAY, 100, 60, 2, 0.0);
        shapeBack.draw(50, 50);
        shapeFront.draw(50, 50);

        Pane drawingPane = new Pane();
        drawingPane.getChildren().addAll(shapeBack.getNode(), shapeFront.getNode());

        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(1));

        PositionManagerReceiver receiver = new PositionManagerReceiver(shapeBack);
        BringForwardCommand cmd = new BringForwardCommand(receiver);

        cmd.executeCommand();

        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(1));

        cmd.undoCommand();
        assertEquals(shapeBack.getNode(), drawingPane.getChildren().get(0));
        assertEquals(shapeFront.getNode(), drawingPane.getChildren().get(1));
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testThrowsIfNodeNotInPane(ShapeInterface shape) {
        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        BringForwardCommand cmd = new BringForwardCommand(receiver);

        Exception exception = assertThrows(IllegalStateException.class, cmd::executeCommand);
        assertTrue(exception.getMessage().contains("Node does not have a Pane parent"));
    }
}
