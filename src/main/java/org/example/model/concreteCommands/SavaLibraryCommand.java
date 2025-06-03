package org.example.model.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.LibraryExporterReceiver;

import java.io.File;
import java.io.IOException;

/**
 * Implements the save library functionality as a concrete command in the Command design pattern.
 * This class is responsible for facilitating the export of a library to a file, while decoupling
 * the user interaction logic from the business logic.
 * The command encapsulates the save operation and provides interaction with a file-choosing GUI.
 */
public class SavaLibraryCommand implements CommandInterface {

    private LibraryExporterReceiver libraryExporterReceiver;

    /**
     * Constructs a new SavaLibraryCommand instance with the specified LibraryExporterReceiver.
     * This command encapsulates the functionality to save a library by*/
    public SavaLibraryCommand(LibraryExporterReceiver libraryExporterReceiver) {
        this.libraryExporterReceiver = libraryExporterReceiver;
    }

    /**
     * Executes the save library command by invoking a file chooser dialog to allow the user
     * to select a save location and file name. If the user selects a valid file with the
     * ".txt" extension, the method*/
    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Library");
        fileChooser.setInitialFileName("Library");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                if (file.getName().endsWith(".txt")) {
                    libraryExporterReceiver.exportLibrary(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Undoes the action performed by the command. This method provides functionality
     * for reverting the command's effects, restoring the state to its previous condition.
     *
     * In this implementation, undoing the command is not supported. Any attempt to invoke this
     * method will result in an {@link UnsupportedOperationException}.
     *
     * @throws UnsupportedOperationException if the method is invoked, as undo functionality
     *                                       is not implemented or applicable for this command.
     */
    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Determines whether the command can be undone.
     * This method provides information about the ability to reverse
     * the effects of the command's execution.
     *
     * @return false as this command is not undoable.
     */
    @Override
    public boolean isUndoable() {
        return false;
    }
}
