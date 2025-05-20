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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShapeOption other = (ShapeOption) obj;
        return name.equals(other.name); // o confronta su base più ampia se necessario
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
