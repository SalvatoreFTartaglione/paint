package org.example.model.commands;

import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import org.example.model.receivers.CanvasExporter;

import java.io.File;
import java.io.IOException;

public class Save implements CommandInterface {

    private CanvasExporter canvasExporter;

    public Save(Pane drawingPane) {
        canvasExporter = new CanvasExporter(drawingPane);
    }


    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
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
        throw new UnsupportedOperationException("Not supported yet.");
    }


}



