package org.example.model.receivers;

import javafx.scene.paint.Color;
import org.example.model.commands.receivers.FavoriteManagerReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.registry.FavoriteShapeRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class FavoriteManagerReceiverTest {

    private FavoriteManagerReceiver receiver;

    @BeforeEach
    public void setUp() {
        receiver = new FavoriteManagerReceiver();
        // Reset the registry to avoid singleton contamination between tests
        FavoriteShapeRegistry.getInstance().setFavorites(new HashMap<>());
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BLUE, 2.0, 50.0, 30.0, 0.0);
        ellipse.draw(100, 100);

        TextShape text = new TextShape("Favorite", Color.BLACK, Color.GREEN, 80, 30, 16, 1.0, 1.0, 0.0);
        text.draw(150, 150);

        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
        line.draw(new Double[]{200.0, 200.0, 300.0, 250.0});

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.PURPLE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0});

        /*CompositeShape composite = new CompositeShape(List.of(
                rect.cloneShape(), ellipse.cloneShape()
        ));*/

        return Stream.of(rect, ellipse, text, line, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testSaveAndGetFavorite(ShapeInterface shape) {
        String name = "TestShape";
        receiver.saveAsFavorite(name, shape);

        ShapeInterface retrieved = receiver.getFavorite(name);
        assertNotNull(retrieved);
        assertNotSame(shape, retrieved, "Should be a clone, not the same instance");
        assertEquals(shape.getStrokeColor(), retrieved.getStrokeColor());
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testRemoveFavorite(ShapeInterface shape) {
        String name = "ToRemove";
        receiver.saveAsFavorite(name, shape);
        receiver.removeFavorite(name);
        assertNull(receiver.getFavorite(name), "Removed shape should return null");
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testSetAndGetMapFavorites(ShapeInterface shape) {
        Map<String, ShapeInterface> customMap = new HashMap<>();
        customMap.put("MapShape", shape);

        receiver.setMapFavorites(customMap);
        Map<String, ShapeInterface> retrievedMap = receiver.getMapFavorites();

        assertEquals(1, retrievedMap.size());
        assertTrue(retrievedMap.containsKey("MapShape"));
    }
}
