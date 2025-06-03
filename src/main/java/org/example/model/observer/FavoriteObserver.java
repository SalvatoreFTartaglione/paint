package org.example.model.observer;

import java.util.Set;

/**
 * Interface representing an observer that listens for updates to a list of favorite items.
 *
 * Implementations of this interface can be registered to observe changes in a collection
 * of favorite items. When the favorite list changes, the observer will be notified through
 * a callback method.
 */
public interface FavoriteObserver {

    /**
     * Called when the list of favorite items is updated.
     *
     * This method is used to notify observers about changes in the favorite items collection.
     * The updated set of favorite item names is provided as an argument to the method.
     * Observers can use this information to react to changes accordingly.
     *
     * @param updatedNames the updated set of favorite item names after the change
     */
    void onFavoriteListChanged(Set<String> updatedNames);
}
