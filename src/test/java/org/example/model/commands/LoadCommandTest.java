package org.example.model.commands;

import org.example.model.commands.concreteCommands.LoadCommand;
import org.example.model.commands.receivers.CanvasImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadCommandTest {

    private CanvasImporter canvasImporter;
    private LoadCommand loadCommand;

    @BeforeEach
    public void setUp() {
        // Creazione fittizia di un CanvasImporter con canvas nullo
        canvasImporter = new CanvasImporter(null);
        loadCommand = new LoadCommand(canvasImporter);
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(loadCommand.isUndoable(), "LoadCommand dovrebbe restituire false a isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> loadCommand.undoCommand(),
                "undoCommand() deve lanciare UnsupportedOperationException");
    }

}
