package org.example.controller;

import javafx.scene.image.Image;
import java.util.Objects;

public class ShapeOption {
    private final String name;
    private final Image image;

    public ShapeOption(String name, String imagePath) {
        this.name = name;
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    public String getName() {
        return name;
    }

    public Image getImage() {
        return image;
    }

    @Override
    public String toString() {
        return name;
    }
}
