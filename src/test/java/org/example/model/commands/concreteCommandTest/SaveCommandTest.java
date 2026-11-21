package org.example.model.commands.concreteCommandTest;

import org.example.model.concreteCommands.SaveCommand;
import org.example.model.receivers.CanvasExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveCommandTest {

    private CanvasExporter canvasExporter;
    private SaveCommand saveCommand;

    @BeforeEach
    public void setUp() {
        // The exporter is built without a canvas: only methods that do not touch it are tested here
        canvasExporter = new CanvasExporter(null);
        saveCommand = new SaveCommand(canvasExporter, new java.io.File("drawing.txt"));
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(saveCommand.isUndoable(), "SaveCommand should return false for isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> saveCommand.undoCommand(),
                "undoCommand() must throw UnsupportedOperationException");
    }

}
