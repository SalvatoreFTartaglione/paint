package org.example.model.commands.receiversTest;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.concreteCommands.CutCommand;
import org.example.model.concreteCommands.DeleteCommand;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.example.model.invoker.Invoker;
import org.example.model.receivers.ShapeReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Undoing a delete or a cut must put the shape back at its original z-order position,
 * not on top of every other shape.
 */
public class ShapeReceiverZOrderTest {

    private Pane pane;
    private DrawingContextDTO dto;
    private ShapeReceiver receiver;
    private RectangleShape bottom;
    private RectangleShape middle;
    private RectangleShape top;

    private RectangleShape newRectangle() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);
        rect.getNode().setUserData(rect);
        pane.getChildren().add(rect.getNode());
        return rect;
    }

    @BeforeEach
    public void setUp() {
        pane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(pane);
        receiver = new ShapeReceiver(dto);
        bottom = newRectangle();
        middle = newRectangle();
        top = newRectangle();
    }

    @Test
    public void undoDeleteRestoresTheOriginalZOrder() {
        Invoker invoker = new Invoker();
        invoker.execute(new DeleteCommand(dto, middle, receiver));
        assertEquals(2, pane.getChildren().size());

        invoker.undo();

        assertEquals(3, pane.getChildren().size());
        assertSame(bottom.getNode(), pane.getChildren().get(0));
        assertSame(middle.getNode(), pane.getChildren().get(1));
        assertSame(top.getNode(), pane.getChildren().get(2));
    }

    @Test
    public void undoCutRestoresTheOriginalZOrder() {
        Invoker invoker = new Invoker();
        invoker.execute(new CutCommand(bottom, receiver, dto));

        invoker.undo();

        assertSame(bottom.getNode(), pane.getChildren().get(0));
        assertSame(middle.getNode(), pane.getChildren().get(1));
    }

    @Test
    public void severalDeletesAreUndoneInReverseOrder() {
        Invoker invoker = new Invoker();
        CommandInterface deleteBottom = new DeleteCommand(dto, bottom, receiver);
        CommandInterface deleteTop = new DeleteCommand(dto, top, receiver);
        invoker.execute(deleteBottom);
        invoker.execute(deleteTop);

        invoker.undo();
        invoker.undo();

        assertSame(bottom.getNode(), pane.getChildren().get(0));
        assertSame(middle.getNode(), pane.getChildren().get(1));
        assertSame(top.getNode(), pane.getChildren().get(2));
    }
}
