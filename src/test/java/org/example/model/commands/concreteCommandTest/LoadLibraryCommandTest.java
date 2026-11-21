package org.example.model.commands.concreteCommandTest;

import org.example.model.concreteCommands.LoadLibraryCommand;
import org.example.model.receivers.LibraryImporterReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LoadLibraryCommandTest {

    private LoadLibraryCommand command;

    @BeforeEach
    public void setUp() {
        // The receiver is a placeholder: only methods that do not use it are tested here
        command = new LoadLibraryCommand(new LibraryImporterReceiver(null), new java.io.File("Library.txt"));
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(command.isUndoable(), "LoadLibraryCommand should return false for isUndoable()");
    }

    @Test
    public void testExecuteCommandThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> command.executeCommand(),
                "executeCommand should throw UnsupportedOperationException");
    }

    @Test
    public void testUndoCommandThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> command.undoCommand(),
                "undoCommand should throw UnsupportedOperationException");
    }
}
