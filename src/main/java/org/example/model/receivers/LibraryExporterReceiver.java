package org.example.model.receivers;

import org.example.model.factory.product.ShapeInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * The LibraryExporterReceiver class is responsible for exporting a collection of favorite shapes
 * to an external file. It interacts with a FavoriteManagerReceiver instance to retrieve the favorite
 * shapes and writes them to a specified file in a structured format.
 */
public class LibraryExporterReceiver {

    private FavoriteManagerReceiver favoriteManagerReceiver;

    /**
     * Constructs a LibraryExporterReceiver instance with the specified FavoriteManagerReceiver.
     * The FavoriteManagerReceiver is used to manage and retrieve favorite shapes for exporting.
     *
     * @param favoriteManagerReceiver the FavoriteManagerReceiver instance used to manage
     *                                favorite shapes and retrieve them for the library export process
     */
    public LibraryExporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

    /**
     * Exports the library of favorite shapes to the specified file. The method retrieves the favorites
     * from the FavoriteManagerReceiver and writes them to the file in a structured format. The output
     * begins with a fixed header line followed by the favorite shapes represented as key-value pairs.
     *
     * @param file the file to which the library will be exported
     * @throws IOException if an I/O error occurs during writing to the file
     */
    public void exportLibrary(File file) throws IOException {
        Map<String, ShapeInterface> favoritesMap = favoriteManagerReceiver.getMapFavorites();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Prima riga fissa
            writer.write("My Custom Library");
            writer.newLine();

            for (Map.Entry<String, ShapeInterface> entry : favoritesMap.entrySet()) {
                String key = entry.getKey();
                ShapeInterface shape = entry.getValue();

                writer.write(key + ": " + shape.toString());
            }
        }
    }
}

