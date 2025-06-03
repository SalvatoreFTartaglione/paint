package org.example.model.commands.concreteCommandTest;

import javafx.scene.paint.Color;
import org.example.model.concreteCommands.SaveFavoriteCommand;
import org.example.model.receivers.FavoriteManagerReceiver;
import org.example.model.factory.concreteProduct.*;
import org.example.model.factory.product.ShapeInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class SaveFavoriteCommandTest {

    private FavoriteManagerReceiver receiver;

    @BeforeEach
    public void setUp() {
        receiver = new FavoriteManagerReceiver();
        receiver.setMapFavorites(new java.util.HashMap<>()); // reset favorites
    }

    static Stream<ShapeInterface> shapeProvider() {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.BISQUE, 100, 50, 2.0, 0.0);
        rect.draw(50, 50);

        EllipseShape ellipse = new EllipseShape(Color.BLACK, Color.BISQUE, 2.0, 40, 20, 0.0);
        ellipse.draw(60, 60);

        LineShape line = new LineShape(Color.BLACK, 2.0, 0.0);
        line.draw(new Double[]{0.0, 0.0, 100.0, 100.0});

        TextShape text = new TextShape("Favorite", Color.BLACK, Color.BISQUE, 0, 0, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);

        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.BISQUE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{10.0, 10.0, 20.0, 30.0, 40.0, 10.0});

        // not implemented
//        CompositeShape composite = new CompositeShape(List.of(
//                new RectangleShape(Color.BLACK, Color.BISQUE, 100, 50, 2.0, 0.0) {{
//                    draw(100, 100);
//                }},
//                new EllipseShape(Color.BLACK, Color.BISQUE, 2.0, 40, 30, 0.0) {{
//                    draw(150, 150);
//                }}
//        ));

        return Stream.of(rect, ellipse, line, text, polygon);
    }

    @ParameterizedTest
    @MethodSource("shapeProvider")
    public void testSaveFavoriteCommandStoresClone(ShapeInterface shape) {
        String name = "Fav_" + shape.getClass().getSimpleName();

        SaveFavoriteCommand command = new SaveFavoriteCommand(receiver, shape, name);
        command.executeCommand();

        Map<String, ShapeInterface> favorites = receiver.getMapFavorites();

        assertTrue(favorites.containsKey(name), "Favorite registry should contain the shape with given name");

        ShapeInterface saved = favorites.get(name);
        assertNotNull(saved, "Saved shape should not be null");
        assertNotSame(shape, saved, "Saved shape should be a clone");
        assertEquals(shape.getClass(), saved.getClass(), "Saved shape should have the same class as the original");
    }
}
