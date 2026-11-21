package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

/**
 * Command that replaces the content of the canvas with a drawing read from a file.
 * If the file is invalid an {@link IOException} is thrown and the canvas is left untouched.
 * <p>
 * The command is not undoable.
 */
public class LoadCommand implements CommandInterface {

    private final CanvasImporter canvasImporter;
    private final File file;

    /**
     * @param canvasImporter the receiver that reads the file
     * @param file           the file to load, already chosen by the user
     */
    public LoadCommand(CanvasImporter canvasImporter, File file) {
        this.canvasImporter = canvasImporter;
        this.file = file;
    }

    @Override
    public void executeCommand() {
        throw new UnsupportedOperationException("Use executePossibleExceptionCommand(): loading can fail.");
    }

    @Override
    public void executePossibleExceptionCommand() throws IOException {
        canvasImporter.importAsText(file);
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
