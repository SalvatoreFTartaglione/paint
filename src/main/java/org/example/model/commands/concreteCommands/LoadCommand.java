package org.example.model.commands.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

public class LoadCommand implements CommandInterface {

    private CanvasImporter canvasImporter;


    public LoadCommand(CanvasImporter canvasImporter) {
        this.canvasImporter =  canvasImporter;
    }

    @Override
    public void executeCommand() {throw new UnsupportedOperationException("Not supported yet.");}

    @Override
    public void executePossibleExceptionCommand() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            canvasImporter.importAsText(file);
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


