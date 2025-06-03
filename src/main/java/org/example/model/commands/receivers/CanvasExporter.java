package org.example.model.commands.receivers;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.factory.product.ShapeInterface;

import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CanvasExporter {

    private final DrawingContextDTO contextDTO;

    public CanvasExporter(DrawingContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }

    public void exportAsText(File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("My custom canvas");
            writer.newLine();
            for (Node node : contextDTO.getDrawingPane().getChildren()) {
                Object userData = node.getUserData();
                if (userData instanceof ShapeInterface) {
                    writer.write(userData.toString());
                }
            }
        }
    }

    public void exportAsPng(File file) throws IOException {
        Background originalBackground = contextDTO.getDrawingPane().getBackground();
        Background transparent = new Background(new BackgroundFill(null, null, null));
        contextDTO.getDrawingPane().setBackground(transparent);

        WritableImage image = contextDTO.getDrawingPane().snapshot(new SnapshotParameters(), null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        contextDTO.getDrawingPane().setBackground(originalBackground);
    }
}

