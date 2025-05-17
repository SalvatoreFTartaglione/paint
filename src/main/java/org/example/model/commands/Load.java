package org.example.model.commands;
/*
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import org.example.model.shape.*;

import java.io.*;
import java.util.Locale;
import java.util.Scanner;

public class Load implements CommandInterface {

    private final AnchorPane anchorPane;

    public Load(AnchorPane anchorPane) {
        this.anchorPane = anchorPane;
    }

    @Override
    public void executeCommand(Node node) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        File file = fileChooser.showOpenDialog(null);

        if (file == null) return;

        try (Scanner in = new Scanner(new BufferedReader(new FileReader(file)))) {
            in.useLocale(Locale.US);
            in.useDelimiter(";|\\n");

            while (in.hasNext()) {
                String type = in.next();  // Rectangle / Ellipse / Line
                Color stroke = Color.web(in.next());
                Color fill = Color.web(in.next());

                double width = Double.parseDouble(in.next());
                double height = Double.parseDouble(in.next());

                double x = Double.parseDouble(in.next());
                double y = Double.parseDouble(in.next());

                ShapeInterface shape;

                switch (type) {
                    case "Rectangle":
                        shape = new RectangleShape(fill, stroke, width, height);
                        shape.draw(x, y);
                        break;
                    case "Ellipse":
                        shape = new EllipseShape(stroke, fill, width, height);
                        shape.draw(x, y);
                        break;
                    case "Line":
                        shape = new LineShape(stroke, fill);
                        Double[] points = new Double[] {x, y, width, height}; // StartX, StartY, EndX, EndY
                        shape.draw(points);
                        break;
                    default:
                        continue; // ignora formati sconosciuti
                }

                anchorPane.getChildren().add((Node) shape);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
*/

