package org.example.model.commands.concreteCommandTest;

import javafx.scene.paint.Color;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.concreteCommands.ChangeFillColorCommand;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.factory.concreteProduct.EllipseShape;
import org.example.model.factory.concreteProduct.PolygonShape;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.example.model.factory.concreteProduct.TextShape;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeFillColorCommandTest {

    private ChangesOnShapeReceiver receiver;

    @BeforeEach
    public void setup() {
        receiver = new ChangesOnShapeReceiver(); // stateless or dummy receiver
    }

    static Stream<ShapeInterface> shapeProvider() {
        return Stream.of(
                new RectangleShape(Color.BLACK, Color.BLUE, 100, 60, 2, 0.0),
                new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0, 0.0),
                new TextShape("Test", Color.BLACK, Color.BLUE, 0, 0, 16, 1.0, 1.0, 0.0),
                new PolygonShape(Color.BLACK, Color.BLUE, 2.0, 1.0, 1.0, 0.0) {{
                    draw(new Double[]{100.0, 100.0, 150.0, 150.0, 200.0, 100.0});
                }}
                // not implemented
//                new CompositeShape(List.of(
//                        new RectangleShape(Color.BLACK, Color.BLUE, 100, 50, 2, 0.0) {{
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
    public void testChangeAndUndoFillColor(ShapeInterface shape) {
        shape.setFillColor(Color.BLUE);  // original color
        Color newColor = Color.FIREBRICK;

        CommandInterface cmd = new ChangeFillColorCommand(shape, newColor, receiver);
        cmd.executeCommand();
        assertEquals(newColor, shape.getFillColor(), "Fill color should change after execution");

        cmd.undoCommand();
        assertEquals(Color.BLUE, shape.getFillColor(), "Fill color should revert after undo");
    }
}
