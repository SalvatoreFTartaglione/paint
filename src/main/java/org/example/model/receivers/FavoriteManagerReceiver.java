package org.example.model.receivers;

import org.example.model.registry.FavoriteShapeRegistry;
import org.example.model.shape.ShapeInterface;

public class FavoriteManagerReceiver {
    private final FavoriteShapeRegistry registry = FavoriteShapeRegistry.getInstance();
    //registry è un'istanza singleton di FavoriteShapeRegistry, che funge da contenitore centrale per tutte le shape preferite salvate nella sessione.

    public void saveAsFavorite(String name, ShapeInterface shape) {
        ShapeInterface clone = shape.cloneShape();
        registry.save(name, clone);
    }

    public void removeFavorite(String name) {
        registry.remove(name);
    }

    public ShapeInterface getFavorite(String name) {
        return registry.get(name);
    }
}
