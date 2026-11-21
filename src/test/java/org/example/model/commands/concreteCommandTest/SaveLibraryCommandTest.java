package org.example.model.commands.concreteCommandTest;

import org.example.model.concreteCommands.SaveLibraryCommand;
import org.example.model.receivers.LibraryExporterReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SaveLibraryCommandTest {

    private SaveLibraryCommand command;

    @BeforeEach
    public void setUp() {
        // The receiver is a placeholder: only methods that do not use it are tested here
        command = new SaveLibraryCommand(new LibraryExporterReceiver(null), new java.io.File("Library.txt"));
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(command.isUndoable(), "SaveLibraryCommand should return false for isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> command.undoCommand(),
                "undoCommand should throw UnsupportedOperationException");
    }
}
