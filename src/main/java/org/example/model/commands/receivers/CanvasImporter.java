package org.example.model.commands.receivers;

import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.model.factory.concreteCreator.*;
import org.example.model.factory.concreteProduct.CompositeShape;
import org.example.model.factory.product.ShapeInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CanvasImporter {

    private Pane canvas;

    public CanvasImporter(Pane canvas) {
        this.canvas = canvas;
    }

    public void importAsText(File file) throws IOException {

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 0) continue;
                // 1) Se la riga comincia con "Group:", richiamo il metodo che importa un CompositeShape
                if (line.startsWith("Group:")) {
                    StringBuilder fullGroup = new StringBuilder(line);

                    // "mark" per poter ritornare indietro, se la prossima non è una riga di gruppo
                    reader.mark(4096);
                    String next;
                    while ((next = reader.readLine()) != null) {
                        String t = next.trim();
                        // Se la riga inizia con "|" (opzionalmente spazi poi pipe), la considero
                        // parte del blocco Composite e la appendo a fullGroup
                        if (t.startsWith("|")) {
                            // tolgo la pipe iniziale e eventuali spazi, poi aggiungo " | <testo>"
                            fullGroup.append(" | ").append(t.substring(1).trim());
                            reader.mark(4096); // aggiorno il punto di mark
                        } else {
                            // se la riga NON inizia con "|", la resetto: la rileggerà il while esterno
                            reader.reset();
                            break;
                        }
                    }

                    // Ora fullGroup contiene tutta la descrizione del Composite, es:
                    //   "Group:258.5714;112.8571 : Ellipse;…;0.0;24.28…;0.0 | Line;… | Rectangle;… | Text;…"
                    CompositeShape composite = importCompositeGroup(fullGroup.toString());
                    if (composite != null) {
                        Node groupNode = composite.getNode();
                        groupNode.setUserData(composite);
                        canvas.getChildren().add(groupNode);
                    }
                    continue; // passo alla prossima iterazione, non va processata come shape singola
                }
                //2) se è una singola shape, viene fatto questo
                String shapeType = parts[0];
                ShapeInterface shape = null;

                switch (shapeType) {
                    case "Line":
                        if (parts.length != 8) continue; // Line + 6 params
                        Color strokeColorL = Color.web(parts[1]);
                        double endX = Double.parseDouble(parts[2]);
                        double endY = Double.parseDouble(parts[3]);
                        double startX = Double.parseDouble(parts[4]);
                        double startY = Double.parseDouble(parts[5]);
                        double strokeWidthL = Double.parseDouble(parts[6]);
                        double angleL = Double.parseDouble(parts[7]);

                        LineCreator lineCreator = new LineCreator();
                        lineCreator.creationOfShape(strokeColorL, (Color) null, (int) strokeWidthL, 0, 0, null, 0.0, 0.0, angleL);
                        shape = lineCreator.getShape();
                        shape.setStrokeColor(strokeColorL);
                        shape.draw(new Double[]{startX, startY, endX, endY});
                        break;

                    case "Rectangle":
                        if (parts.length != 9) continue; // Rectangle + 7 params
                        Color strokeColorR = Color.web(parts[1]);
                        Color fillColorR = Color.web(parts[2]);
                        double strokeWidthR = Double.parseDouble(parts[3]);
                        double widthR = Double.parseDouble(parts[4]);
                        double heightR = Double.parseDouble(parts[5]);
                        double xR = Double.parseDouble(parts[6]);
                        double yR = Double.parseDouble(parts[7]);
                        double angleR = Double.parseDouble(parts[8]);

                        RectangleCreator rectangleCreator = new RectangleCreator();
                        rectangleCreator.creationOfShape(strokeColorR, fillColorR, (int) strokeWidthR, widthR, heightR, null, 0.0, 0.0, angleR);
                        shape = rectangleCreator.getShape();
                        shape.draw(xR, yR);
                        break;

                    case "Ellipse":
                        if (parts.length != 9) continue;
                        Color strokeColorE = Color.web(parts[1]);
                        Color fillColorE = Color.web(parts[2]);
                        double strokeWidthE = Double.parseDouble(parts[3]);
                        double radiusX = Double.parseDouble(parts[4]);
                        double radiusY = Double.parseDouble(parts[5]);
                        double centerX = Double.parseDouble(parts[6]);
                        double centerY = Double.parseDouble(parts[7]);
                        double angleE = Double.parseDouble(parts[8]);

                        EllipseCreator ellipseCreator = new EllipseCreator();
                        ellipseCreator.creationOfShape(strokeColorE, fillColorE, (int) strokeWidthE, radiusX, radiusY, null, 0.0, 0.0, angleE);
                        shape = ellipseCreator.getShape();
                        shape.draw(centerX, centerY);
                        break;

                    case "Text":
                        if (parts.length != 10) continue;
                        String textT = parts[1];
                        Color strokeColorT = Color.web(parts[2]);
                        Color fillColorT = Color.web(parts[3]);
                        double xT = Double.parseDouble(parts[4]);
                        double yT = Double.parseDouble(parts[5]);
                        double fontSize = Double.parseDouble(parts[6]);        // aka strokeWidth
                        double scaleX = Double.parseDouble(parts[7]);
                        double scaleY = Double.parseDouble(parts[8]);
                        double angleT = Double.parseDouble(parts[9]);

                        TextCreator textCreator = new TextCreator();
                        textCreator.creationOfShape(strokeColorT, fillColorT, (int) fontSize, xT, yT, textT, scaleX, scaleY, angleT);
                        shape = textCreator.getShape();
                        shape.draw(xT, yT);
                        break;

                    case "Polygon":
                        if (parts.length != 8) continue;
                        Color strokeColorP = Color.web(parts[1]);
                        Color fillColorP = Color.web(parts[2]);
                        double strokeWidthP = Double.parseDouble(parts[3]);
                        double scaleXP = Double.parseDouble(parts[4]);
                        double scaleYP = Double.parseDouble(parts[5]);
                        double angleP = Double.parseDouble(parts[7]);
                        String pointStr = parts[6].replace("[", "").replace("]", "");
                        String[] pointValues = pointStr.split(",");
                        Double[] pointsP = new Double[pointValues.length];
                        for (int i = 0; i < pointValues.length; i++) {
                            pointsP[i] = Double.parseDouble(pointValues[i].trim());
                        }

                        PolygonCreator polygonCreator = new PolygonCreator();
                        polygonCreator.creationOfShape(strokeColorP, fillColorP, (int) strokeWidthP, 0, 0, null, scaleXP, scaleYP, angleP);
                        shape = polygonCreator.getShape();
                        shape.draw(pointsP);
                        break;

                    default:
                        System.err.println(": " + shapeType);
                        continue;
                }
                if (shape != null) {
                    Node n = shape.getNode();
                    n.setUserData(shape);
                    canvas.getChildren().add(n);
                    System.out.println(canvas.getChildren().size());
                }else {
                    throw new NullPointerException("Shape is null: " + shape);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private CompositeShape importCompositeGroup(String firstLine) {
        // 1) rimuovo "Group:" e ottengo qualcosa come "120.0;80.0 : Ellipse;… | Line;…"
        String withoutPrefix = firstLine.substring("Group:".length()).trim();

        // 2) splitto su ":" (con spazi attorno) in al massimo 2 parti
        String[] leftRight = withoutPrefix.split("\\s*:\\s*", 2);
        if (leftRight.length < 2) {
            System.err.println("Formato CompositeShape non valido: " + firstLine);
            return null;
        }

        // 3) leftRight[0] = "120.0;80.0" → ricavo groupX, groupY
        String coordPart = leftRight[0].trim();
        String[] xy = coordPart.split(";");
        if (xy.length != 2) {
            System.err.println("Coordinate gruppo non valide: " + coordPart);
            return null;
        }
        double groupX = Double.parseDouble(xy[0]);
        double groupY = Double.parseDouble(xy[1]);

        // 4) leftRight[1] = "Ellipse;… | Line;… | …" → testo dei figli
        String childrenText = leftRight[1].trim();

        // 5) suddivido su "|" per ottenere ciascuna definizione di figlio
        String[] childStrings = childrenText.split("\\s*\\|\\s*");

        // 6) ricreo ogni child invocando lo stesso switch‐case delle shape singole,
        //    ma passandogli le coordinate RELATIVE (parts[6], parts[7]) direttamente in draw()
        List<ShapeInterface> children = new ArrayList<>();
        for (String childLine : childStrings) {
            String[] parts = childLine.trim().split(";");
            if (parts.length == 0) continue;

            String shapeType = parts[0];
            ShapeInterface shape = null;

            switch (shapeType) {
                case "Line":
                    if (parts.length != 8) break;
                {
                    Color strokeColorL = Color.web(parts[1]);
                    double endX        = Double.parseDouble(parts[2]);
                    double endY        = Double.parseDouble(parts[3]);
                    double startX      = Double.parseDouble(parts[4]);
                    double startY      = Double.parseDouble(parts[5]);
                    double strokeWidthL= Double.parseDouble(parts[6]);
                    double angleL      = Double.parseDouble(parts[7]);

                    LineCreator lineCreator = new LineCreator();
                    lineCreator.creationOfShape(
                            strokeColorL,
                            (Color) null,
                            (int) strokeWidthL,
                            0, 0,
                            null,
                            0.0, 0.0,
                            angleL
                    );
                    shape = lineCreator.getShape();
                    shape.setStrokeColor(strokeColorL);
                    // draw con startX,startY,endX,endY come RELATIVE al gruppo
                    shape.draw(new Double[]{startX, startY, endX, endY});
                }
                break;

                case "Rectangle":
                    if (parts.length != 9) break;
                {
                    Color strokeColorR   = Color.web(parts[1]);
                    Color fillColorR     = Color.web(parts[2]);
                    double strokeWidthR  = Double.parseDouble(parts[3]);
                    double widthR        = Double.parseDouble(parts[4]);
                    double heightR       = Double.parseDouble(parts[5]);
                    double xR            = Double.parseDouble(parts[6]);
                    double yR            = Double.parseDouble(parts[7]);
                    double angleR        = Double.parseDouble(parts[8]);

                    RectangleCreator rectangleCreator = new RectangleCreator();
                    rectangleCreator.creationOfShape(
                            strokeColorR,
                            fillColorR,
                            (int) strokeWidthR,
                            widthR, heightR,
                            null,
                            0.0, 0.0,
                            angleR
                    );
                    shape = rectangleCreator.getShape();
                    // draw con xR,yR come RELATIVE al gruppo
                    shape.draw(xR, yR);
                }
                break;

                case "Ellipse":
                    if (parts.length != 9) break;
                {
                    Color strokeColorE   = Color.web(parts[1]);
                    Color fillColorE     = Color.web(parts[2]);
                    double strokeWidthE  = Double.parseDouble(parts[3]);
                    double radiusX       = Double.parseDouble(parts[4]);
                    double radiusY       = Double.parseDouble(parts[5]);
                    double centerX       = Double.parseDouble(parts[6]);
                    double centerY       = Double.parseDouble(parts[7]);
                    double angleE        = Double.parseDouble(parts[8]);

                    EllipseCreator ellipseCreator = new EllipseCreator();
                    ellipseCreator.creationOfShape(
                            strokeColorE,
                            fillColorE,
                            (int) strokeWidthE,
                            radiusX, radiusY,
                            null,
                            0.0, 0.0,
                            angleE
                    );
                    shape = ellipseCreator.getShape();
                    // draw con centerX,centerY come RELATIVE al gruppo
                    shape.draw(centerX, centerY);
                }
                break;

                case "Text":
                    if (parts.length != 10) break;
                {
                    String textT         = parts[1];
                    Color strokeColorT   = Color.web(parts[2]);
                    Color fillColorT     = Color.web(parts[3]);
                    double xT            = Double.parseDouble(parts[4]);
                    double yT            = Double.parseDouble(parts[5]);
                    double fontSize      = Double.parseDouble(parts[6]);
                    double scaleX        = Double.parseDouble(parts[7]);
                    double scaleY        = Double.parseDouble(parts[8]);
                    double angleT        = Double.parseDouble(parts[9]);

                    TextCreator textCreator = new TextCreator();
                    textCreator.creationOfShape(
                            strokeColorT,
                            fillColorT,
                            (int) fontSize,
                            xT, yT,
                            textT,
                            scaleX, scaleY,
                            angleT
                    );
                    shape = textCreator.getShape();
                    // draw con xT,yT come RELATIVE al gruppo
                    shape.draw(xT, yT);
                }
                break;

                case "Polygon":
                    if (parts.length != 8) break;
                {
                    Color strokeColorP   = Color.web(parts[1]);
                    Color fillColorP     = Color.web(parts[2]);
                    double strokeWidthP  = Double.parseDouble(parts[3]);
                    double scaleXP       = Double.parseDouble(parts[4]);
                    double scaleYP       = Double.parseDouble(parts[5]);
                    String pointStr      = parts[6].replace("[", "").replace("]", "");
                    String[] pointValues = pointStr.split(",");
                    Double[] pointsP     = new Double[pointValues.length];
                    for (int i = 0; i < pointValues.length; i++) {
                        pointsP[i] = Double.parseDouble(pointValues[i].trim());
                    }
                    double angleP = Double.parseDouble(parts[7]);

                    PolygonCreator polygonCreator = new PolygonCreator();
                    polygonCreator.creationOfShape(
                            strokeColorP,
                            fillColorP,
                            (int) strokeWidthP,
                            0, 0,
                            null,
                            scaleXP, scaleYP,
                            angleP
                    );
                    shape = polygonCreator.getShape();
                    // draw con punti come RELATIVI al gruppo
                    shape.draw(pointsP);
                }
                break;

                default:
                    System.err.println("Tipo nel gruppo non gestito: " + shapeType);
                    break;
            }

            if (shape != null) {
                children.add(shape);
            }
        }

        // 7) Se ho almeno un figlio, li inserisco in un CompositeShape vuoto
        if (children.isEmpty()) {
            return null;
        }
        CompositeShape composite = new CompositeShape();
        for (ShapeInterface child : children) {
            composite.addChild(child);
        }
        // 8) Alla fine posiziono l’intero gruppo nelle coordinate assolute (groupX, groupY)
        composite.setX(groupX);
        composite.setY(groupY);

        return composite;
    }
}


