package org.example.model.commands;

import javafx.stage.FileChooser;
import org.example.model.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

public class LoadCommand implements CommandInterface {

    private CanvasImporter canvasImporter;


    public LoadCommand(CanvasImporter canvasImporter) {
        this.canvasImporter =  canvasImporter;
    }

    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                canvasImporter.importAsText(file);
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


