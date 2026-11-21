package org.example.model.persistence;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.concreteCommands.LoadCommand;
import org.example.model.concreteCommands.SaveCommand;
import org.example.model.factory.concreteProduct.EllipseShape;
import org.example.model.factory.concreteProduct.LineShape;
import org.example.model.factory.concreteProduct.PolygonShape;
import org.example.model.factory.concreteProduct.RectangleShape;
import org.example.model.factory.concreteProduct.TextShape;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.receivers.CanvasExporter;
import org.example.model.receivers.CanvasImporter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Round-trip and robustness tests for saving and loading a drawing.
 */
public class CanvasPersistenceTest {

    @TempDir
    Path tempDir;

    private Pane pane;
    private DrawingContextDTO dto;

    @BeforeEach
    public void setUp() {
        pane = new Pane();
        dto = new DrawingContextDTO();
        dto.setDrawingPane(pane);
    }

    private void addToPane(ShapeInterface shape) {
        shape.getNode().setUserData(shape);
        pane.getChildren().add(shape.getNode());
    }

    private File write(String content) throws IOException {
        Path file = tempDir.resolve("input.txt");
        Files.write(file, content.getBytes(StandardCharsets.UTF_8));
        return file.toFile();
    }

    private List<String> serializedShapes(Pane p) {
        return p.getChildren().stream()
                .map(Node::getUserData)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Test
    public void saveThenLoadRestoresAllShapeTypesInOrder() throws IOException {
        RectangleShape rect = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        rect.draw(50, 50);
        EllipseShape ellipse = new EllipseShape(Color.BLUE, Color.YELLOW, 2, 60, 30, 0.0);
        ellipse.draw(200, 120);
        LineShape line = new LineShape(Color.GREEN, 3.0, 0.0);
        line.draw(new Double[]{10.0, 10.0, 90.0, 40.0});
        PolygonShape polygon = new PolygonShape(Color.BLACK, Color.PURPLE, 2.0, 1.0, 1.0, 0.0);
        polygon.draw(new Double[]{250.0, 250.0, 300.0, 300.0, 350.0, 250.0, 250.0, 250.0});
        TextShape text = new TextShape("Hello", Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        for (ShapeInterface s : new ShapeInterface[]{rect, ellipse, line, polygon, text}) {
            addToPane(s);
        }
        List<String> before = serializedShapes(pane);

        File file = tempDir.resolve("drawing.txt").toFile();
        new SaveCommand(new CanvasExporter(dto), file).executeCommand();

        Pane other = new Pane();
        DrawingContextDTO otherDto = new DrawingContextDTO();
        otherDto.setDrawingPane(other);
        new LoadCommand(new CanvasImporter(otherDto), file).executePossibleExceptionCommand();

        assertEquals(before, serializedShapes(other));
    }

    @Test
    public void textContainingSeparatorsSurvivesARoundTrip() throws IOException {
        String tricky = "a;b:c\\d";
        TextShape text = new TextShape(tricky, Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        addToPane(text);

        File file = tempDir.resolve("drawing.txt").toFile();
        new CanvasExporter(dto).exportAsText(file);
        new CanvasImporter(dto).importAsText(file);

        assertEquals(1, pane.getChildren().size());
        assertEquals(tricky, ((ShapeInterface) pane.getChildren().get(0).getUserData()).getText());
    }

    @Test
    public void multilineTextStaysOnOneRecord() throws IOException {
        TextShape text = new TextShape("one\ntwo", Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        addToPane(text);

        File file = tempDir.resolve("drawing.txt").toFile();
        new CanvasExporter(dto).exportAsText(file);
        new CanvasImporter(dto).importAsText(file);

        assertEquals("one\ntwo", ((ShapeInterface) pane.getChildren().get(0).getUserData()).getText());
    }

    @Test
    public void nonAsciiTextSurvivesARoundTrip() throws IOException {
        String accented = "caffè €";
        TextShape text = new TextShape(accented, Color.BLACK, Color.GREEN, 100, 40, 14, 1.0, 1.0, 0.0);
        text.draw(70, 70);
        addToPane(text);

        File file = tempDir.resolve("drawing.txt").toFile();
        new CanvasExporter(dto).exportAsText(file);
        new CanvasImporter(dto).importAsText(file);

        assertEquals(accented, ((ShapeInterface) pane.getChildren().get(0).getUserData()).getText());
    }

    @Test
    public void invalidFileLeavesTheCanvasUntouched() throws IOException {
        RectangleShape existing = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        existing.draw(50, 50);
        addToPane(existing);

        // the first record is valid, the second one is corrupted: nothing must be loaded
        File file = write(CanvasImporter.HEADER + "\n"
                + "Rectangle;0x000000ff;0xff0000ff;4.0;100;60;10;10;0.0\n"
                + "Ellipse;0x000000ff;0xff0000ff;4.0;oops;5;10;10;0\n");

        IOException e = assertThrows(IOException.class, () -> new CanvasImporter(dto).importAsText(file));
        assertTrue(e.getMessage().startsWith("Line 3"), "message should point to the faulty line: " + e.getMessage());
        assertEquals(1, pane.getChildren().size());
        assertSame(existing.getNode(), pane.getChildren().get(0));
    }

    @Test
    public void invalidNumbersColorsAndFieldCountsAreReportedAsIOException() throws IOException {
        String[] badLines = {
                "Rectangle;0x000000ff;0xff0000ff;4.0;abc;60;10;10;0.0",   // bad number
                "Rectangle;notacolor;0xff0000ff;4.0;100;60;10;10;0.0",    // bad colour
                "Rectangle;0x000000ff",                                   // too few fields
                "Hexagon;1;2;3",                                          // unknown type
                "Polygon;0x000000ff;0xff0000ff;1;1;1;[1.0, 2.0, 3.0];0"    // odd number of coordinates
        };
        for (String bad : badLines) {
            File file = write(CanvasImporter.HEADER + "\n" + bad + "\n");
            assertThrows(IOException.class, () -> new CanvasImporter(dto).importAsText(file), bad);
        }
    }

    @Test
    public void wrongHeaderIsRejected() throws IOException {
        File file = write("My Custom Library\n");
        assertThrows(IOException.class, () -> new CanvasImporter(dto).importAsText(file));
    }

    @Test
    public void loadingAnEmptyDrawingClearsTheCanvas() throws IOException {
        RectangleShape existing = new RectangleShape(Color.BLACK, Color.RED, 100, 60, 2, 0.0);
        existing.draw(50, 50);
        addToPane(existing);

        new CanvasImporter(dto).importAsText(write(CanvasImporter.HEADER + "\n"));

        assertTrue(pane.getChildren().isEmpty());
    }

    @Test
    public void saveCommandRejectsUnknownExtensions() {
        File file = tempDir.resolve("drawing.docx").toFile();
        assertThrows(IOException.class,
                () -> new SaveCommand(new CanvasExporter(dto), file).executePossibleExceptionCommand());
        assertFalse(file.exists());
    }
}
