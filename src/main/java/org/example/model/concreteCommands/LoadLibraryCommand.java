package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.LibraryImporterReceiver;

import java.io.File;
import java.io.IOException;

/**
 * Command that replaces the favorite shapes with the content of a library file.
 * If the file is invalid an {@link IOException} is thrown and the favorites are left untouched.
 * <p>
 * The command is not undoable.
 */
public class LoadLibraryCommand implements CommandInterface {

    private final LibraryImporterReceiver libraryImporterReceiver;
    private final File file;

    /**
     * @param libraryImporterReceiver the receiver that reads the library
     * @param file                    the library file, already chosen by the user
     */
    public LoadLibraryCommand(LibraryImporterReceiver libraryImporterReceiver, File file) {
        this.libraryImporterReceiver = libraryImporterReceiver;
        this.file = file;
    }

    @Override
    public void executePossibleExceptionCommand() throws IOException {
        libraryImporterReceiver.importLibrary(file);
    }

    @Override
    public void executeCommand() {
        throw new UnsupportedOperationException("Use executePossibleExceptionCommand(): loading can fail.");
    }

    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
