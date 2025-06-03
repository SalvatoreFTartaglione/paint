package org.example.model.commands;

import org.example.model.receivers.CanvasExporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveCommandTest {

    private CanvasExporter canvasExporter;
    private SaveCommand saveCommand;

    @BeforeEach
    public void setUp() {
        // Istanza con canvasExporter nullo (non necessario per i metodi testati)
        canvasExporter = new CanvasExporter(null);
        saveCommand = new SaveCommand(canvasExporter);
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(saveCommand.isUndoable(), "SaveCommand dovrebbe restituire false a isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsException() {
        assertThrows(UnsupportedOperationException.class, () -> saveCommand.undoCommand(),
                "undoCommand() deve lanciare UnsupportedOperationException");
    }

}
