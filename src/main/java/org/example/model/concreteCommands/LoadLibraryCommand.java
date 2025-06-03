package org.example.model.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.LibraryImporterReceiver;

import java.io.File;
import java.io.IOException;

/**
 * LoadLibraryCommand is a concrete implementation of the CommandInterface
 * which facilitates loading a custom library file using a file chooser.
 *
 * The command delegates the actual import of the selected*/
public class LoadLibraryCommand implements CommandInterface {

    private LibraryImporterReceiver libraryImporterReceiver;

    public LoadLibraryCommand(LibraryImporterReceiver libraryImporterReceiver) {
        this.libraryImporterReceiver = libraryImporterReceiver;
    }

    /**
     * Executes the command to allow the user to select a library file for import.
     * This method launches a file chooser, enabling the user to select a text file
     * with a ".txt" extension. If the user selects a file, the selected file is
     * passed*/
    @Override
    public void executePossibleExceptionCommand() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            libraryImporterReceiver.importLibrary(file);
        }
    }

    /**
     * Executes the command encapsulated by the instance of the implementing class.
     * This method is intended to provide the specific behavior associated with
     * the concrete command. However, in its current implementation, the method
     * is not supported.
     *
     * @throws UnsupportedOperationException if the method implementation
     *                                       is not provided yet.
     */
    @Override
    public void executeCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Reverts the action performed by the command. This method provides the ability
     * to undo the operation executed by the command, effectively restoring the previous state.
     *
     * In the context of the LoadLibraryCommand, this method is not supported, and invoking it
     * will result in an {@link UnsupportedOperationException}. This indicates that the command
     * does not include functionality to undo the loading of a library file.
     *
     * @throws UnsupportedOperationException if the method is invoked, as the feature
     *                                       is not currently implemented or supported.
     */
    @Override
    public void undoCommand() { throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
