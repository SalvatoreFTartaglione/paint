package org.example.controller;

import javafx.stage.FileChooser;

import java.io.File;
import java.util.Optional;

/**
 * Native file dialogs used by the controller.
 * <p>
 * They live in the controller layer (not in the commands) so that the commands stay free of UI code
 * and can be unit-tested.
 */
final class FileDialogs {

    private static final FileChooser.ExtensionFilter TXT =
            new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt");
    private static final FileChooser.ExtensionFilter PNG =
            new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");

    private FileDialogs() {
    }

    /** Asks where to save the drawing, as a text file or as a PNG image. */
    static Optional<File> chooseDrawingToSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save drawing");
        chooser.setInitialFileName("drawing");
        chooser.getExtensionFilters().addAll(TXT, PNG);
        File file = chooser.showSaveDialog(null);
        return Optional.ofNullable(file).map(f -> withExtension(f, chooser.getSelectedExtensionFilter()));
    }

    /** Asks which drawing file to open. */
    static Optional<File> chooseDrawingToOpen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Open drawing");
        chooser.getExtensionFilters().add(TXT);
        return Optional.ofNullable(chooser.showOpenDialog(null));
    }

    /** Asks where to save the shape library. */
    static Optional<File> chooseLibraryToSave() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Save library");
        chooser.setInitialFileName("Library");
        chooser.getExtensionFilters().add(TXT);
        File file = chooser.showSaveDialog(null);
        return Optional.ofNullable(file).map(f -> f.getName().toLowerCase().endsWith(".txt")
                ? f
                : new File(f.getParentFile(), f.getName() + ".txt"));
    }

    /** Asks which shape library file to open. */
    static Optional<File> chooseLibraryToOpen() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Import library");
        chooser.getExtensionFilters().add(TXT);
        return Optional.ofNullable(chooser.showOpenDialog(null));
    }

    /**
     * Appends the extension of the selected filter when the user typed a name without a known extension,
     * so that saving never silently does nothing.
     */
    private static File withExtension(File file, FileChooser.ExtensionFilter selected) {
        String name = file.getName().toLowerCase();
        if (name.endsWith(".txt") || name.endsWith(".png")) {
            return file;
        }
        String extension = (selected == PNG) ? ".png" : ".txt";
        return new File(file.getParentFile(), file.getName() + extension);
    }
}
