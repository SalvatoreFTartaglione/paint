package org.example.model.receivers;

import org.example.model.shape.ShapeInterface;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class LibraryExporterReceiver {

    private FavoriteManagerReceiver favoriteManagerReceiver;

    public LibraryExporterReceiver(FavoriteManagerReceiver favoriteManagerReceiver) {
        this.favoriteManagerReceiver = favoriteManagerReceiver;
    }

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

