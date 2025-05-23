package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.model.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

public class LoadCommand implements CommandInterface {

    private CanvasImporter canvasImporter;


    public LoadCommand(Pane pane) {
        this.canvasImporter = new CanvasImporter(pane);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }


}


