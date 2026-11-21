package org.example.model.persistence;

import javafx.scene.paint.Color;
import org.example.model.concreteCommands.LoadLibraryCommand;
import org.example.model.concreteCommands.SaveLibraryCommand;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.example.model.factory.concreteProduct.TextShape;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.receivers.FavoriteManagerReceiver;
import org.example.model.receivers.LibraryExporterReceiver;
import org.example.model.receivers.LibraryImporterReceiver;
import org.example.model.registry.FavoriteShapeRegistry;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Round-trip and robustness tests for exporting and importing a shape library.
 */
public class LibraryPersistenceTest {

    @TempDir
    Path tempDir;

    private FavoriteManagerReceiver favorites;

    @BeforeEach
    public void setUp() {
        favorites = new FavoriteManagerReceiver();
        favorites.setMapFavorites(Collections.emptyMap());
    }

    @AfterEach
    public void tearDown() {
        // the registry is a singleton shared by every test
        FavoriteShapeRegistry.getInstance().setFavorites(Collections.emptyMap());
    }

    private RectangleShape rectangle() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);
        return rect;
    }

    @Test
    public void exportThenImportKeepsNamesAndShapes() throws IOException {
        favorites.saveAsFavorite("box", rectangle());
        TextShape text = new TextShape("label", Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        favorites.saveAsFavorite("label", text);

        File file = tempDir.resolve("Library.txt").toFile();
        new SaveLibraryCommand(new LibraryExporterReceiver(favorites), file).executeCommand();

        favorites.setMapFavorites(Collections.emptyMap());
        new LoadLibraryCommand(new LibraryImporterReceiver(favorites), file).executePossibleExceptionCommand();

        assertEquals(Arrays.asList("box", "label"), new ArrayList<>(favorites.getMapFavorites().keySet()));
        assertEquals("label", favorites.getFavorite("label").getText());
    }

    @Test
    public void favoriteNameWithSeparatorsSurvivesARoundTrip() throws IOException {
        String name = "resistor: 10k; \"fancy\"";
        favorites.saveAsFavorite(name, rectangle());

        File file = tempDir.resolve("Library.txt").toFile();
        new LibraryExporterReceiver(favorites).exportLibrary(file);
        favorites.setMapFavorites(Collections.emptyMap());
        new LibraryImporterReceiver(favorites).importLibrary(file);

        assertTrue(favorites.getMapFavorites().containsKey(name));
    }

    @Test
    public void textWithSeparatorsInsideALibrarySurvives() throws IOException {
        TextShape text = new TextShape("a;b:c", Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        favorites.saveAsFavorite("t", text);

        File file = tempDir.resolve("Library.txt").toFile();
        new LibraryExporterReceiver(favorites).exportLibrary(file);
        favorites.setMapFavorites(Collections.emptyMap());
        new LibraryImporterReceiver(favorites).importLibrary(file);

        assertEquals("a;b:c", favorites.getFavorite("t").getText());
    }

    @Test
    public void invalidLibraryKeepsTheCurrentFavorites() throws IOException {
        favorites.saveAsFavorite("keep me", rectangle());

        Path file = tempDir.resolve("bad.txt");
        Files.write(file, "My Custom Library\nfoo: Rectangle;0x000000ff\n".getBytes(StandardCharsets.UTF_8));

        IOException e = assertThrows(IOException.class,
                () -> new LibraryImporterReceiver(favorites).importLibrary(file.toFile()));
        assertTrue(e.getMessage().startsWith("Line 2"), e.getMessage());
        assertTrue(favorites.getMapFavorites().containsKey("keep me"));
    }

    @Test
    public void libraryWithWrongHeaderOrMissingSeparatorIsRejected() throws IOException {
        Path wrongHeader = tempDir.resolve("a.txt");
        Files.write(wrongHeader, "My custom canvas\n".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class,
                () -> new LibraryImporterReceiver(favorites).importLibrary(wrongHeader.toFile()));

        Path noSeparator = tempDir.resolve("b.txt");
        Files.write(noSeparator, "My Custom Library\nnot an entry\n".getBytes(StandardCharsets.UTF_8));
        assertThrows(IOException.class,
                () -> new LibraryImporterReceiver(favorites).importLibrary(noSeparator.toFile()));
    }

    @Test
    public void importedFavoritesKeepFileOrder() throws IOException {
        favorites.saveAsFavorite("zeta", rectangle());
        favorites.saveAsFavorite("alpha", rectangle());
        favorites.saveAsFavorite("mid", rectangle());

        File file = tempDir.resolve("Library.txt").toFile();
        new LibraryExporterReceiver(favorites).exportLibrary(file);
        favorites.setMapFavorites(Collections.emptyMap());
        new LibraryImporterReceiver(favorites).importLibrary(file);

        assertEquals(Arrays.asList("zeta", "alpha", "mid"), new ArrayList<>(favorites.getMapFavorites().keySet()));
    }

    @Test
    public void getFavoriteReturnsAnIndependentCopy() {
        favorites.saveAsFavorite("box", rectangle());
        ShapeInterface a = favorites.getFavorite("box");
        ShapeInterface b = favorites.getFavorite("box");
        assertNotSame(a, b);
    }
}
