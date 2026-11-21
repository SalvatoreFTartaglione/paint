package org.example.model.receivers;

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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

/**
 * A utility class to export the contents of a drawing canvas to various file formats.
 * Supports exporting as plain text or an image file (PNG).
 */
public class CanvasExporter {

    private final DrawingContextDTO contextDTO;

    /**
     * Constructs a CanvasExporter with the specified drawing context.
     *
     * @param contextDTO the drawing context that provides access to the canvas and its content
     */
    public CanvasExporter(DrawingContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }

    /**
     * Exports the contents of the drawing canvas as a plain text file.
     * Each shape is represented as a string based on its user data.
     *
     * @param file the file to which the canvas contents will be exported
     * @throws IOException if an I/O error occurs during file writing
     */
    public void exportAsText(File file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(CanvasImporter.HEADER);
            writer.newLine();
            for (Node node : contextDTO.getDrawingPane().getChildren()) {
                Object userData = node.getUserData();
                if (userData instanceof ShapeInterface) {
                    writer.write(userData.toString());
                }
            }
        }
    }

    /**
     * Exports the current content of the drawing canvas as a PNG image file.
     * The canvas background is temporarily set to transparent for the snapshot
     * and then restored to its original state after the PNG has been exported.
     *
     * @param file the target file where the PNG image will be saved
     * @throws IOException if an I/O error occurs during the writing process
     */
    public void exportAsPng(File file) throws IOException {
        Background originalBackground = contextDTO.getDrawingPane().getBackground();
        Background transparent = new Background(new BackgroundFill(null, null, null));
        contextDTO.getDrawingPane().setBackground(transparent);
        try {
            WritableImage image = contextDTO.getDrawingPane().snapshot(new SnapshotParameters(), null);
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } finally {
            contextDTO.getDrawingPane().setBackground(originalBackground);
        }
    }
}

