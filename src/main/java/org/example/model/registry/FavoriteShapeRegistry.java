package org.example.model.registry;

import org.example.model.observer.FavoriteObserver;
import org.example.model.factory.product.ShapeInterface;

import java.util.*;

/**
 * The FavoriteShapeRegistry class is a singleton that provides a centralized registry
 * for storing, retrieving, and observing favorite shapes. It maintains a collection of
 * named shapes and allows notifying observers when changes occur in the registry.
 */
public class FavoriteShapeRegistry {
    private static FavoriteShapeRegistry instance;      // SINGLETON
    private final Map<String, ShapeInterface> favorites = new LinkedHashMap<>();
    private final List<FavoriteObserver> observers = new ArrayList<>();

    private FavoriteShapeRegistry() {}

    /**
     * Retrieves the singleton instance of the FavoriteShapeRegistry.
     * If the instance does not already exist, it initializes a new instance.
     *
     * @return the singleton instance of the FavoriteShapeRegistry
     */
    public static synchronized FavoriteShapeRegistry getInstance() {
        if (instance == null) {
            instance = new FavoriteShapeRegistry();
        }
        return instance;
    }

    /**
     * Saves a shape with the specified name into the favorites registry. If an entry with the
     * same name already exists, it will be replaced. After saving the shape, observers are
     * notified of the change.
     *
     * @param name the name associated with the shape to be saved; must not be null or empty
     * @param*/
    public void save(String name, ShapeInterface shape) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("The shape name must not be null or empty.");
        }

        if (shape == null) {
            throw new IllegalArgumentException("The shape must not be null.");
        }
        favorites.put(name, shape);
        notifyObservers();
    }

    /**
     * Removes a favorite shape from the registry identified by its name, if it exists.
     * After removing the shape, observers are notified*/
    public void remove(String name) {
        favorites.remove(name);
        notifyObservers();
    }

    /**
     * Retrieves a copy of the shape registered under the specified name from the favorites registry.
     * If no shape is associated with the given name, this method returns null.
     *
     * @param name the name associated with the shape to retrieve; must not be null
     * @return a clone of the shape associated with the specified name, or null if no such shape exists
     */
    public ShapeInterface get(String name) {
        ShapeInterface original = favorites.get(name);
        return original != null ? original.cloneShape() : null;
    }

    /**
     * Retrieves a set of all names currently registered in the favorites.
     *
     * @return a set containing all names of the saved favorite shapes
     */
    public Set<String> getAllNames() {
        return favorites.keySet();
    }

    /**
     * Adds an observer to the list of observers.
     *
     * The observer will be notified of any changes to the favorite items
     * within the registry. Adding the same observer multiple times will result
     * in the observer being added only once.
     *
     * @param observer the observer to be added; must not be null
     */
    //Attach(observer)
    public void addObserver(FavoriteObserver observer) {
        if (observer != null) {
            observers.add(observer);
        }
    }

    /**
     * Removes an observer from the list of observers if it is currently registered.
     * After removal, the observer will no longer receive updates when changes occur
     * in the favorites collection.
     *
     * @param observer the observer to be removed; must not be null
     */
    //Detach(observer)
    public void removeObserver(FavoriteObserver observer) {
        if (observer != null) {
            observers.remove(observer);
        }
    }

    /**
     * Notifies all registered observers about changes in the list of favorite items.
     *
     * This method is responsible for informing all observers that the state of the favorites registry
     * has been updated. It compiles a set of all the names of the current favorites and sends this
     * set to each registered observer through their `onFavoriteListChanged` method.
     *
     * Observers are assumed to implement the {@code FavoriteObserver} interface, which defines
     **/
    //Notify()
    private void notifyObservers() {
        Set<String> names = new LinkedHashSet<>(favorites.keySet());
        for (FavoriteObserver o : observers) {
            o.onFavoriteListChanged(names);
        }
    }

    /**
     * Retrieves the current mapping of favorite shapes.
     * The map contains string keys representing shape names and values that are implementations of the {@code ShapeInterface}.
     *
     * @return a map of favorite shape names to their corresponding {@code ShapeInterface} implementations
     */
    public Map<String, ShapeInterface> getFavorites() {
        return favorites;
    }

    /**
     * Replaces the current collection of favorite shapes with the provided map and notifies observers of the change.
     *
     * This method clears the existing mapping of favorite shapes and replaces it with the entries
     * in the provided map. After the update, all registered observers are notified to reflect the changes.
     *
     * @param map a map containing the new entries of favorite shapes, where the keys are shape names
     *            and the values are implementations of the {@code ShapeInterface}; must not be null
     */
    public void setFavorites(Map<String, ShapeInterface> map) {
        this.favorites.clear();
        this.favorites.putAll(map);
        notifyObservers();
    }
}
