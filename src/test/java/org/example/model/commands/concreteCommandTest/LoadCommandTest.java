package org.example.model.commands.concreteCommandTest;

import org.example.model.concreteCommands.LoadCommand;
import org.example.model.receivers.CanvasImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadCommandTest {

    private CanvasImporter canvasImporter;
    private LoadCommand loadCommand;

    @BeforeEach
    public void setUp() {
        // The importer is built without a canvas: only methods that do not touch it are tested here
        canvasImporter = new CanvasImporter(null);
        loadCommand = new LoadCommand(canvasImporter, new java.io.File("drawing.txt"));
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(loadCommand.isUndoable(), "LoadCommand should return false for isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> loadCommand.undoCommand(),
                "undoCommand() must throw UnsupportedOperationException");
    }

}
