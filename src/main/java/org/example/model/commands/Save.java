package org.example.model.commands;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import org.example.model.shape.ShapeInterface;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.io.*;

public class Save implements CommandInterface {

    private Pane canvas;
    private Stage primaryStage;

    public Save(Pane canvas) {
        this.canvas = canvas;
    }

    @Override
    public void executeCommand(Node unused) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.setInitialFileName("drawing");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png"));

        File file = fileChooser.showSaveDialog(null);

        if (file != null && file.getName().endsWith(".txt")) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (Node node : canvas.getChildren()) {
                    if (node instanceof ShapeInterface) {
                        writer.write(((ShapeInterface) node).toString());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(file != null && file.getName().endsWith(".png")){
            try {
                Background background = canvas.getBackground();
                BackgroundFill bf = new BackgroundFill(null, null, null);
                Background bNew = new Background(bf, bf);
                canvas.setBackground(bNew);
                WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
                canvas.setBackground(background);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

/*
    @Override
    public void exeCommand(Node node, double x, double y) {
        // Not used for save
    }
*/

}



