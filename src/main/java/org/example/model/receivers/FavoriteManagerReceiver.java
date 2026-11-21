package org.example.model.receivers;

import org.example.model.registry.FavoriteShapeRegistry;
import org.example.model.factory.product.ShapeInterface;

import java.util.Map;

/**
 * The FavoriteManagerReceiver class provides an interface for managing favorite shapes.
 * It acts as a wrapper around the FavoriteShapeRegistry singleton to save, retrieve,
 * remove and manage favorite shapes*/
public class FavoriteManagerReceiver {
    private final FavoriteShapeRegistry registry = FavoriteShapeRegistry.getInstance();
    // The registry is the singleton FavoriteShapeRegistry: the central container of all the favorite shapes saved in the session.

    /**
     * Saves a shape as a favorite by cloning it and storing it in the registry with the specified name.
     *
     * @param name the name under which the favorite shape will be saved
     * @param shape the shape to*/
    public void saveAsFavorite(String name, ShapeInterface shape) {
        ShapeInterface clone = shape.cloneShape();
        registry.save(name, clone);
    }

    /**
     * Removes a favorite shape identified by its name from the registry.
     *
     * @param name the name of the favorite shape to be removed
     */
    public void removeFavorite(String name) {
        registry.remove(name);
    }

    /**
     * Retrieves a favorite shape from the registry using the provided name.
     *
     * @param name the name of the favorite shape to be retrieved
     */
    public ShapeInterface getFavorite(String name) {
        return registry.get(name);
    }

    /**
     * Retrieves a map of all favorite shapes stored in the registry.
     * The map contains the names of the favorite shapes as keys and their corresponding
     * shape instances as values.
     *
     * @return a map where the keys are names of favorite shapes,*/
    public Map<String, ShapeInterface> getMapFavorites() {
        return registry.getFavorites();
    }

    /**
     * Sets the map of favorite shapes in the registry. This method replaces the
     * existing map of favorite shapes with the provided map.
     *
     * @param map a map where the keys are names of favorite shapes and the values
     * correspond to instances of shapes to be set as favorites
     */
    public void setMapFavorites(Map<String, ShapeInterface> map) {
        registry.setFavorites(map);
    }
}
