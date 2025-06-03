package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.shape.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class ChangeStrokeColorCommandTest {

    private ChangesOnShapeReceiver receiver;

    @BeforeEach
    public void setup() {
        receiver = new ChangesOnShapeReceiver(); // può essere finto/stateless
    }

    static Stream<ShapeInterface> shapeProvider() {
        return Stream.of(
                new RectangleShape(Color.BLACK, Color.LIGHTGRAY, 100, 60, 2, 0.0),
                new EllipseShape(Color.BLACK, Color.YELLOW, 2.0, 50.0, 30.0, 0.0),
                new LineShape(Color.BLACK, 2.0, 0.0) {{
                    draw(new Double[]{0.0, 0.0, 100.0, 100.0});
                }},
                new TextShape("Test", Color.BLACK, Color.TRANSPARENT, 0, 0, 16, 1.0, 1.0, 0.0)
        );
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testChangeAndUndoStrokeColor(ShapeInterface shape) {
        Color original = shape.getStrokeColor();
        Color newColor = Color.DARKCYAN;

        CommandInterface cmd = new ChangeStrokeColorCommand(shape, newColor, receiver);
        cmd.executeCommand();
        assertEquals(newColor, shape.getStrokeColor(), "Stroke color should be updated after execution");

        cmd.undoCommand();
        assertEquals(original, shape.getStrokeColor(), "Stroke color should be restored after undo");
    }
}
