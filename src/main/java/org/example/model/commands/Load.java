package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.model.receivers.CanvasImporter;

import java.io.File;
import java.io.IOException;

public class Load implements CommandInterface {

    private final Pane pane;


    public Load(Pane pane) {
        this.pane = pane;
    }

    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            CanvasImporter canvasImporter = new CanvasImporter(pane);
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


