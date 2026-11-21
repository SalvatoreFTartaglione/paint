package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.LibraryExporterReceiver;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Command that exports the favorite shapes to a library file.
 * <p>
 * The command is not undoable.
 */
public class SaveLibraryCommand implements CommandInterface {

    private final LibraryExporterReceiver libraryExporterReceiver;
    private final File file;

    /**
     * @param libraryExporterReceiver the receiver that writes the library
     * @param file                    the destination file, already chosen by the user
     */
    public SaveLibraryCommand(LibraryExporterReceiver libraryExporterReceiver, File file) {
        this.libraryExporterReceiver = libraryExporterReceiver;
        this.file = file;
    }

    @Override
    public void executeCommand() {
        try {
            executePossibleExceptionCommand();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    @Override
    public void executePossibleExceptionCommand() throws IOException {
        libraryExporterReceiver.exportLibrary(file);
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
