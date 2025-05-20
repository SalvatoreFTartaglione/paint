package org.example.controller;

import javafx.scene.image.Image;
import java.util.Objects;

public class ShapeOption {
    private final String name;
    private final Image image;

    /**
     * Constructs a new {@code ShapeOption} with the specified name and image path.
     *
     * @param name the display name of the shape
     * @param imagePath the relative path to the shape's image icon
     * @throws NullPointerException if the image resource is not found
     */
    public ShapeOption(String name, String imagePath) {
        this.name = name;
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * @return the name of the shape option
     */
    public String getName() {
        return name;
    }

    /**
     * @return the image icon representing the shape
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the shape's name for display purposes.
     *
     * @return the name of the shape option
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Checks if two {@code ShapeOption} instances are equal by comparing their names.
     *
     * @param obj the object to compare
     * @return true if the names are equal; false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShapeOption other = (ShapeOption) obj;
        return name.equals(other.name); // o confronta su base più ampia se necessario
    }

    /**
     * @return the hash code based on the shape's name
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }

}
