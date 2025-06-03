package org.example.model.commands.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.LibraryImporterReceiver;

import java.io.File;
import java.io.IOException;

public class LoadLibraryCommand implements CommandInterface {

    private LibraryImporterReceiver libraryImporterReceiver;

    public LoadLibraryCommand(LibraryImporterReceiver libraryImporterReceiver) {
        this.libraryImporterReceiver = libraryImporterReceiver;
    }

    @Override
    public void executePossibleExceptionCommand() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            libraryImporterReceiver.importLibrary(file);
        }
    }

    @Override
    public void executeCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void undoCommand() { throw new UnsupportedOperationException("Not supported yet."); }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
