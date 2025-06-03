package org.example.model.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.receivers.CanvasExporter;

import java.io.File;
import java.io.IOException;

/**
 * A command that encapsulates the action of saving the current canvas to a file.
 * Implements the Command design pattern, allowing the save operation to be executed
 * while decoupling the logic from the caller. This command supports saving in two formats:
 * plain text (".txt") and PNG image (".png").
 */
public class SaveCommand implements CommandInterface {

    private CanvasExporter canvasExporter;

    public SaveCommand(CanvasExporter canvasExporter) {
        this.canvasExporter = canvasExporter;

    }


    /**
     * Executes the save command, allowing the user to save the current canvas to a file.
     * This command opens a file chooser dialog prompting the user to select a specific
     * file path and format for saving. The supported file formats are:
     *
     * - Plain text file*/
    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("SaveCommand");
        fileChooser.setInitialFileName("drawing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                if (file.getName().endsWith(".txt")) {
                    canvasExporter.exportAsText(file);
                } else if (file.getName().endsWith(".png")) {
                    canvasExporter.exportAsPng(file);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Reverts the action performed by the command. This method provides the ability to
     * undo the operation executed by the command, effectively restoring the previous state
     * or reversing the effects of the command execution if supported.
     *
     * In the context of the SaveCommand, this method is not supported. Invoking it will
     * result in an {@link UnsupportedOperationException}. This*/
    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    /**
     * Determines whether the command can be undone.
     * This implementation always returns false, indicating that the action performed by this command
     * cannot be reversed or reverted.
     *
     * @return false as the command is not undoable.
     */
    @Override
    public boolean isUndoable() {
        return false;
    }


}



