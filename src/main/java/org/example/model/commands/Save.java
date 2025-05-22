package org.example.model.commands;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import org.example.model.receivers.CanvasExporter;
import org.example.model.shape.ShapeInterface;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

public class Save implements CommandInterface {

    private CanvasExporter canvasExporter;
    private final Pane drawingPane;

    public Save(Pane drawingPane) {
        this.drawingPane = drawingPane;
    }


    @Override
    public void executeCommand() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("drawing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));
        File file = fileChooser.showSaveDialog(null);
        canvasExporter = new CanvasExporter(drawingPane);
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



