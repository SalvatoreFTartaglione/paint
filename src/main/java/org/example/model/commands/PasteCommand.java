package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.receivers.AddToCanvas;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.LineShape;
import org.example.model.shape.ShapeInterface;

import javax.sound.sampled.Line;

public class PasteCommand implements CommandInterface {

    private final ShapeReceiver receiver;
    private final DrawingContextDTO drawingDTO;
    private ShapeInterface pastedShape;

    public PasteCommand(ShapeReceiver receiver, DrawingContextDTO drawingDTO) {
        this.receiver = receiver;
        this.drawingDTO = drawingDTO;
    }

    @Override
    public void executeCommand() {
        ShapeInterface shapeToPaste = receiver.getCopy();
        //System.out.println(shapeToPaste.toString());
        if (shapeToPaste != null) {
            pastedShape = shapeToPaste;

            // Calcola coordinate locali dal click destro
            Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(
                    drawingDTO.getSceneX(), drawingDTO.getSceneY());
            double x = localCoords.getX();
            double y = localCoords.getY();

            if (pastedShape instanceof LineShape) {
                Double[] oldPoints = pastedShape.getPoints();
                if (oldPoints == null || oldPoints.length != 4) {
                    System.err.println("Errore: punti non validi nella linea copiata");
                    return;
                }

                // Centro geometrico della linea originale
                double centerX = (oldPoints[0] + oldPoints[2]) / 2.0;
                double centerY = (oldPoints[1] + oldPoints[3]) / 2.0;

                // Delta tra il centro della linea e il punto cliccato
                double deltaX = x - centerX;
                double deltaY = y - centerY;

                // Nuovi punti traslati
                Double[] newPoints = new Double[] {
                        oldPoints[0] + deltaX, oldPoints[1] + deltaY,
                        oldPoints[2] + deltaX, oldPoints[3] + deltaY
                };

                pastedShape.draw(newPoints);

            } else {
                // Per forme non-lineari (centro ben definito)
                pastedShape.draw(x, y);
            }


            // Inserisci la forma nel DTO, come fanno i tuoi colleghi
            drawingDTO.setStartPoint(null); // reset per sicurezza
            drawingDTO.setDrawingPane(drawingDTO.getDrawingPane());
            drawingDTO.setBorderColor(pastedShape.getStrokeColor());
            drawingDTO.setFillColor(pastedShape.getFillColor());

            // Aggiunta tramite AddToCanvas
            AddToCanvas addToCanvas = new AddToCanvas(drawingDTO);
            addToCanvas.addClonedShape(pastedShape);
        }
    }

    public ShapeInterface getPastedShape() {
        return pastedShape;
    }

    @Override
    public void undoCommand() {
        if (pastedShape != null) {
            drawingDTO.getDrawingPane().getChildren().remove(pastedShape.getNode());
        }
    }
}
