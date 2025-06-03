package org.example.controller;

import javafx.scene.image.Image;
import java.util.Objects;

/**
 * Represents a selectable shape option in the UI, characterized by a display name and an image icon.
 * Typically used in dropdowns, toolbars, or shape pickers.
 */
public class ShapeOption {

    private final String name;

    private final Image image;

    /**
     * Constructs a new {@code ShapeOption} with the specified name and image path.
     *
     * @param name the display name of the shape
     * @param imagePath the relative path to the shape's image icon resource
     * @throws NullPointerException if the image resource is not found at the given path
     */
    public ShapeOption(String name, String imagePath) {
        this.name = name;
        this.image = new Image(Objects.requireNonNull(getClass().getResourceAsStream(imagePath)));
    }

    /**
     * Returns the display name of the shape option.
     *
     * @return the shape's name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the image icon associated with the shape option.
     *
     * @return the shape's image
     */
    public Image getImage() {
        return image;
    }

    /**
     * Returns the string representation of this shape option, which is its name.
     *
     * @return the shape's name
     */
    @Override
    public String toString() {
        return name;
    }

    /**
     * Compares this {@code ShapeOption} to another object for equality.
     * Two shape options are considered equal if they have the same name.
     *
     * @param obj the object to compare with
     * @return {@code true} if the names are equal, {@code false} otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ShapeOption other = (ShapeOption) obj;
        return name.equals(other.name);
    }

    /**
     * Returns the hash code of this shape option, based on its name.
     *
     * @return hash code of the shape option
     */
    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
