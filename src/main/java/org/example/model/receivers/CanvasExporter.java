package org.example.model.receivers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import org.example.model.shape.ShapeInterface;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CanvasExporter {

    private final Pane canvas;

    public CanvasExporter(Pane canvas) {
        this.canvas = canvas;
    }

    public void exportAsText(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            System.out.println("Dimension = " + canvas.getChildren().size());
            for (Node node : canvas.getChildren()) {
                Object userData = node.getUserData();
                if (userData instanceof ShapeInterface) {
                    writer.write(userData.toString());
                }
            }
        }
    }

    public void exportAsPng(File file) throws IOException {
        Background originalBackground = canvas.getBackground();
        Background transparent = new Background(new BackgroundFill(null, null, null));
        canvas.setBackground(transparent);

        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        canvas.setBackground(originalBackground);
    }
}

