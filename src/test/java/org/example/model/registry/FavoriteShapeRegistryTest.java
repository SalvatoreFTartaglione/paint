package org.example.model.registry;

import javafx.scene.paint.Color;
import org.example.model.observer.FavoriteObserver;
import org.example.model.shape.RectangleShape;
import org.example.model.shape.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for the FavoriteShapeRegistry singleton class.
 *
 * This class verifies:
 * - Saving and retrieving shapes.
 * - Observer notification logic.
 * - Input validation.
 * - Internal map management and cloning logic.
 */
public class FavoriteShapeRegistryTest {

    private FavoriteShapeRegistry registry;
    private ShapeInterface shape1;
    private ShapeInterface shape2;

    /**
     * A mock observer used to test notification behavior.
     * It records the call count and received names.
     */
    private static class MockObserver implements FavoriteObserver {
        public Set<String> receivedNames;
        public int callCount = 0;

        @Override
        public void onFavoriteListChanged(Set<String> names) {
            this.receivedNames = names;
            callCount++;
        }
    }

    @BeforeEach
    public void setUp() {
        registry = FavoriteShapeRegistry.getInstance();
        registry.setFavorites(new HashMap<>()); // Reset singleton state
        shape1 = new RectangleShape(Color.BLACK, Color.RED, 100, 50, 2, 0.0);
        shape2 = new RectangleShape(Color.BLACK, Color.BLUE, 80, 40, 2, 0.0);
    }

    /**
     * Tests that a saved shape can be retrieved
     * and that it is a clone (not the same instance).
     */
    @Test
    public void testSaveAndGet() {
        registry.save("Shape1", shape1);
        ShapeInterface retrieved = registry.get("Shape1");

        assertNotNull(retrieved);
        assertNotSame(shape1, retrieved, "Should return a clone, not the original reference");
        assertEquals(shape1.getStrokeColor(), retrieved.getStrokeColor());
    }

    /**
     * Ensures saving with a null name throws an exception.
     */
    @Test
    public void testSaveWithNullNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> registry.save(null, shape1));
    }

    /**
     * Ensures saving a null shape throws an exception.
     */
    @Test
    public void testSaveWithNullShapeThrows() {
        assertThrows(IllegalArgumentException.class, () -> registry.save("Invalid", null));
    }

    /**
     * Tests that removing a shape by name clears it from the registry.
     */
    @Test
    public void testRemove() {
        registry.save("Shape1", shape1);
        registry.remove("Shape1");

        assertNull(registry.get("Shape1"), "Removed shape should return null when accessed");
    }

    /**
     * Confirms that getAllNames returns all saved names.
     */
    @Test
    public void testGetAllNames() {
        registry.save("A", shape1);
        registry.save("B", shape2);

        Set<String> names = registry.getAllNames();
        assertTrue(names.contains("A"));
        assertTrue(names.contains("B"));
    }

    /**
     * Verifies that observers are correctly notified when a shape is added.
     */
    @Test
    public void testAddAndNotifyObserver() {
        MockObserver observer = new MockObserver();
        registry.addObserver(observer);

        registry.save("Shape1", shape1);

        assertEquals(1, observer.callCount, "Observer should be notified once");
        assertTrue(observer.receivedNames.contains("Shape1"));
    }

    /**
     * Verifies that removing an observer stops further notifications.
     */
    @Test
    public void testRemoveObserverStopsNotification() {
        MockObserver observer = new MockObserver();
        registry.addObserver(observer);
        registry.removeObserver(observer);

        registry.save("Shape1", shape1);
        assertEquals(0, observer.callCount, "Observer should not be notified after removal");
    }

    /**
     * Tests that setFavorites replaces all current favorites and triggers notifications.
     */
    @Test
    public void testSetFavoritesReplacesAndNotifies() {
        MockObserver observer = new MockObserver();
        registry.addObserver(observer);

        Map<String, ShapeInterface> newMap = new HashMap<>();
        newMap.put("NewShape", shape2);
        registry.setFavorites(newMap);

        assertEquals(1, observer.callCount, "Observer should be notified once on replacement");
        assertTrue(registry.getAllNames().contains("NewShape"));
        assertEquals(1, registry.getAllNames().size(), "Only the new map should remain");
    }
}
