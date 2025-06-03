package org.example.model.commands;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.concreteCommands.AddShapeCommand;
import org.example.model.commands.receivers.AddToCanvasReceiver;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class AddShapeCommandTest {

    private DrawingContextDTO dto;
    private Pane drawingPane;
    private AddToCanvasReceiver receiver;

    @BeforeEach
    public void setUp() {
        drawingPane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(drawingPane);
        dto.setBorderColor(Color.BLACK);
        dto.setFillColor(Color.RED);
        dto.setSceneX(100);
        dto.setSceneY(100);
    }

    static Stream<String> shapeOptions() {
        return Stream.of("Rectangle", "Ellipse", "Text");
        // Line is excluded because it requires two separate clicks (start and end point) to be drawn, making it incompatible with single-step command execution in unit tests
        // Polygon is excluded as they require either animation support (Timeline) or pre-built shapes, which complicate unit testing without full JavaFX initialization
    }

    @ParameterizedTest
    @MethodSource("shapeOptions")
    public void testExecuteCommandAddShape(String shapeType) {
        dto.setShapeOption(new ShapeOption(shapeType, ""));
        receiver = new AddToCanvasReceiver(dto);
        AddShapeCommand cmd = new AddShapeCommand(dto, receiver);

        assertEquals(0, drawingPane.getChildren().size());

        cmd.executeCommand();

        assertEquals(1, drawingPane.getChildren().size());
        Node node = drawingPane.getChildren().get(0);
        assertNotNull(node.getUserData());
        assertTrue(node.getUserData() instanceof ShapeInterface);

        ShapeInterface createdShape = cmd.getShape();
        assertNotNull(createdShape, "Shape should not be null after execution");
        assertEquals(createdShape.getNode(), drawingPane.getChildren().get(0), "Shape node should be added to pane");
    }
}
