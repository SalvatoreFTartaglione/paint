package org.example.model.receivers;

import org.example.model.factory.product.ShapeInterface;
import org.example.model.utils.SerializationUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

/**
 * Receiver that exports the favorite shapes (the "shape creation commands") to a library file
 * that can be shared and imported again with {@link LibraryImporterReceiver}.
 */
public class LibraryExporterReceiver {

    /** First line of every library file. */
    public static final String HEADER = "My Custom Library";

    private final FavoriteManagerReceiver favoriteManagerReceiver;

    /**
     * Creates an exporter that reads the favorites from the given receiver.
     *
     * @param favoriteManagerReceiver the receiver that manages the favorite shapes
     */
    public LibraryExporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

    /**
     * Writes all the favorite shapes to the given file, one {@code name: shape} entry per line.
     * Names are escaped, so they may contain any character.
     *
     * @param file the destination file
     * @throws IOException if an I/O error occurs while writing
     */
    public void exportLibrary(File file) throws IOException {
        Map<String, ShapeInterface> favoritesMap = favoriteManagerReceiver.getMapFavorites();

        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(), StandardCharsets.UTF_8)) {
            writer.write(HEADER);
            writer.newLine();

            for (Map.Entry<String, ShapeInterface> entry : favoritesMap.entrySet()) {
                writer.write(SerializationUtils.escape(entry.getKey()) + ": " + entry.getValue().toString());
            }
        }
    }
}
