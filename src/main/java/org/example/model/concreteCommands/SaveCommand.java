package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.CanvasExporter;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Locale;

/**
 * Command that saves the drawing to a file. The format is chosen from the file extension:
 * {@code .txt} stores the shapes so that they can be loaded again, {@code .png} exports an image.
 * <p>
 * The command is not undoable.
 */
public class SaveCommand implements CommandInterface {

    private final CanvasExporter canvasExporter;
    private final File file;

    /**
     * @param canvasExporter the receiver that writes the canvas
     * @param file           the destination file, already chosen by the user
     */
    public SaveCommand(CanvasExporter canvasExporter, File file) {
        this.canvasExporter = canvasExporter;
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
        String name = file.getName().toLowerCase(Locale.ROOT);
        if (name.endsWith(".txt")) {
            canvasExporter.exportAsText(file);
        } else if (name.endsWith(".png")) {
            canvasExporter.exportAsPng(file);
        } else {
            throw new IOException("Unsupported file type: only .txt and .png files can be saved.");
        }
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
