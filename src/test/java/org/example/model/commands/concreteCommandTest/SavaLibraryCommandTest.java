package org.example.model.commands.concreteCommandTest;

import org.example.model.concreteCommands.SavaLibraryCommand;
import org.example.model.receivers.LibraryExporterReceiver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SavaLibraryCommandTest {

    private SavaLibraryCommand command;

    @BeforeEach
    public void setUp() {
        // Passiamo un receiver fittizio (non usato nei metodi che stiamo testando)
        command = new SavaLibraryCommand(new LibraryExporterReceiver(null));
    }

    @Test
    public void testIsUndoableReturnsFalse() {
        assertFalse(command.isUndoable(), "SavaLibraryCommand should return false for isUndoable()");
    }

    @Test
    public void testUndoCommandThrowsUnsupportedOperation() {
        assertThrows(UnsupportedOperationException.class, () -> command.undoCommand(),
                "undoCommand should throw UnsupportedOperationException");
    }
}
