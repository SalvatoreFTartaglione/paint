package org.example.model.observer;

import java.util.Set;

public interface FavoriteObserver {
    void onFavoriteListChanged(Set<String> updatedNames);
}
