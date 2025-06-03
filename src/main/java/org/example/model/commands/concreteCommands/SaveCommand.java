package org.example.model.commands.concreteCommands;

import javafx.stage.FileChooser;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.CanvasExporter;

import java.io.File;
import java.io.IOException;

public class SaveCommand implements CommandInterface {

    private CanvasExporter canvasExporter;

    public SaveCommand(CanvasExporter canvasExporter) {
        this.canvasExporter = canvasExporter;

    }


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

    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public boolean isUndoable() {
        return false;
    }


}



