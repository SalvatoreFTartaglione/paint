package org.example.model.registry;

import org.example.model.observer.FavoriteObserver;
import org.example.model.shape.ShapeInterface;

import java.util.*;

public class FavoriteShapeRegistry {
    private static FavoriteShapeRegistry instance;      // SINGLETON
    private final Map<String, ShapeInterface> favorites = new HashMap<>();
    private final List<FavoriteObserver> observers = new ArrayList<>();

    private FavoriteShapeRegistry() {}

    public static FavoriteShapeRegistry getInstance() {
        if (instance == null) {
            instance = new FavoriteShapeRegistry();
        }
        return instance;
    }

    public void save(String name, ShapeInterface shape) {
        favorites.put(name, shape);
        System.out.println("[DEBUG] Shape salvata con nome: " + name);
        System.out.println("[DEBUG] Shape: " + shape.toString());
        notifyObservers();
    }

    public void remove(String name) {
        favorites.remove(name);
        notifyObservers();
    }

    public ShapeInterface get(String name) {
        ShapeInterface original = favorites.get(name);
        return original != null ? original.cloneShape() : null;
    }

    public Set<String> getAllNames() {
        return favorites.keySet();
    }

    public void addObserver(FavoriteObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers() {
        Set<String> names = new HashSet<>(favorites.keySet());
        for (FavoriteObserver o : observers) {
            o.onFavoriteListChanged(names);
        }
    }

    public Map<String, ShapeInterface> getFavorites() {
        return favorites;
    }

    public void setFavorites(Map<String, ShapeInterface> map) {
        this.favorites.clear();
        this.favorites.putAll(map);
        notifyObservers();
    }
}
