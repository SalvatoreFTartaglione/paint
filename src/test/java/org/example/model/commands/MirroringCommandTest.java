package org.example.model.commands;

import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.concreteCommands.MirroringCommand;
import org.example.model.commands.receivers.PositionManagerReceiver;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class MirroringCommandTest {

    private DrawingContextDTO contextDTO;

    @BeforeEach
    public void setup() {
        contextDTO = new DrawingContextDTO();
    }

    static Stream<MirrorDirectionEnum> mirrorDirections() {
        return Stream.of(
                MirrorDirectionEnum.UP,
                MirrorDirectionEnum.DOWN,
                MirrorDirectionEnum.LEFT,
                MirrorDirectionEnum.RIGHT,
                MirrorDirectionEnum.HORIZONTAL,
                MirrorDirectionEnum.VERTICAL
        );
    }

    @ParameterizedTest
    @MethodSource("mirrorDirections")
    public void testExecuteAndUndoMirror(MirrorDirectionEnum direction) {
        // Setup shape
        RectangleShape shape = new RectangleShape(Color.BLACK, Color.LIGHTBLUE, 100, 60, 2, 0.0);
        shape.draw(100, 100);

        PositionManagerReceiver receiver = new PositionManagerReceiver(shape);
        MirroringCommand command = new MirroringCommand(receiver, direction, contextDTO);

        double originalX = shape.getX();
        double originalY = shape.getY();
        double originalScaleX = shape.getScaleX();
        double originalScaleY = shape.getScaleY();

        command.executeCommand();

        double newScaleX = shape.getScaleX();
        double newScaleY = shape.getScaleY();

        boolean scaleChanged = newScaleX != originalScaleX || newScaleY != originalScaleY;
        boolean expectScaleChange = (direction == MirrorDirectionEnum.HORIZONTAL || direction == MirrorDirectionEnum.VERTICAL);

        if (expectScaleChange) {
            assertTrue(scaleChanged, "Scale should change after mirroring in direction: " + direction);
        } else {
            assertEquals(originalScaleX, newScaleX, 0.001, "ScaleX should remain unchanged for direction: " + direction);
            assertEquals(originalScaleY, newScaleY, 0.001, "ScaleY should remain unchanged for direction: " + direction);
        }

        command.undoCommand();

        assertEquals(originalX, shape.getX(), 0.01, "X should be restored after undo");
        assertEquals(originalY, shape.getY(), 0.01, "Y should be restored after undo");

        if (expectScaleChange) {
            // Tolerance used to allow floating point restoration issues
            assertEquals(originalScaleX, shape.getScaleX(), 0.001, "ScaleX should be restored after undo");
            assertEquals(originalScaleY, shape.getScaleY(), 0.001, "ScaleY should be restored after undo");
        }
    }
}
