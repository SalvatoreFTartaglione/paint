package org.example.model.commands.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.LibraryExporterReceiver;

import java.io.File;
import java.io.IOException;

public class SavaLibraryCommand implements CommandInterface {

    private LibraryExporterReceiver libraryExporterReceiver;

    public SavaLibraryCommand(LibraryExporterReceiver libraryExporterReceiver) {
        this.libraryExporterReceiver = libraryExporterReceiver;
    }

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

    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
