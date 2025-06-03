package org.example.model.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

/**
 * Represents a command for loading and importing data onto a canvas.
 * This command utilizes the {@code CanvasImporter} to process and apply
 * the contents of a file to the canvas in the appropriate format.
 * Implements the {@code CommandInterface} as part of the Command design pattern.
 */
public class LoadCommand implements CommandInterface {

    private CanvasImporter canvasImporter;


    /**
     * Constructs a LoadCommand object with the specified CanvasImporter.
     * This command utilizes the given CanvasImporter to handle the process
     * of importing drawing data onto a canvas.
     *
     * @param canvasImporter an instance of CanvasImporter responsible for handling
     *                       the import operation of drawing data onto the canvas.
     */
    public LoadCommand(CanvasImporter canvasImporter) {
        this.canvasImporter =  canvasImporter;
    }

    /**
     * Executes the command encapsulated by the instance of the implementing class.
     * This method provides the main action associated with the specific command.
     *
     * Implementation-specific behavior is defined in concrete command classes.
     * In this case, the method is not yet implemented and throws an exception
     * indicating that it is not supported.
     *
     * @throws UnsupportedOperationException if the method is not implemented.
     */
    @Override
    public void executeCommand() {throw new UnsupportedOperationException("Not supported yet.");}

    /**
     * Executes the command to open a file dialog, allowing the user to select a text file,
     * and imports its content onto a canvas using the {@code CanvasImporter}.
     * Only files matching the "*.txt" extension filter are displayed in the dialog.
     *
     * If the user selects a file, the method delegates the file import process
     * to the {@code importAsText} method of the {@code canvasImporter}. The file's
     * content is processed and rendered onto the canvas.
     *
     * If no file is selected, the method exits without performing any action.
     *
     * @throws IOException if an I/O error occurs during the file import process or
     *                     if the file content format is invalid. The valid format
     *                     is specified by the {@code importAsText} method.
     */
    @Override
    public void executePossibleExceptionCommand() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            canvasImporter.importAsText(file);
        }
    }

    /**
     * Reverts the action performed by the command. This method provides the ability to
     * undo the operation executed by the command, effectively restoring the previous state.
     *
     * In the context of the LoadCommand, this method is not supported, and invoking it
     * will result in an {@link UnsupportedOperationException}. This indicates that the
     * command does not include functionality to undo the loading or importing of data.
     *
     * @throws UnsupportedOperationException if the method is invoked, as the feature
     *                                       is not currently implemented or supported.
     */
    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Determines whether the command can be undone.
     * This method provides information about the undoable nature
     * of the command. In the case of this command, the action
     * is not reversible.
     *
     * @return false as undoing this command is not supported.
     */
    @Override
    public boolean isUndoable() {
        return false;
    }


}


