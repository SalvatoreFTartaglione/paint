package org.example.model.receivers;

import javafx.geometry.Point2D;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.LineShape;
import org.example.model.shape.ShapeInterface;
import org.example.model.shape.TextShape;

import java.util.Stack;

public class ShapeReceiver {
    private ShapeInterface shape;
    private boolean isCutted = false;
    private DrawingContextDTO drawingDTO;
    // Stack for deleted shapes (Undo task)
    private final Stack<ShapeInterface> deletedShapesStack = new Stack<>();
    private final Stack<Point2D> deletedShapePositions = new Stack<>();

    public ShapeReceiver(DrawingContextDTO drawingDTO) {
        if (drawingDTO != null) {
            this.drawingDTO = drawingDTO;
        }
    }

    public void copy(ShapeInterface shape) {
        this.shape = shape.cloneShape();
    }

    public ShapeInterface getCopy() {
        return shape == null ? null : shape.cloneShape();
    }

    public void undoCopy() {
        shape = null;
        isCutted = false;
    }

    public void cut(ShapeInterface shape, DrawingContextDTO contextDTO){
        this.shape = shape.cloneShape();
        this.isCutted = true;
        this.delete(contextDTO, shape);
    }

    public void paste(ShapeInterface shape, DrawingContextDTO drawingContextDTO){
        if (shape != null) {
            // Calcola coordinate locali dal click destro
            Point2D localCoords = drawingContextDTO.getDrawingPane().sceneToLocal(
                    drawingContextDTO.getSceneX(), drawingContextDTO.getSceneY());
            double x = localCoords.getX();
            double y = localCoords.getY();

            if (shape instanceof LineShape) {
                Double[] oldPoints = shape.getPoints();
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

                shape.draw(newPoints);

            } else {
                // Per forme non-lineari (centro ben definito)
                shape.draw(x, y);
            }


            // Inserisci la forma nel DTO, come fanno i tuoi colleghi
            drawingContextDTO.setStartPoint(null); // reset per sicurezza
            drawingContextDTO.setDrawingPane(drawingContextDTO.getDrawingPane());
            drawingContextDTO.setBorderColor(shape.getStrokeColor());
            drawingContextDTO.setFillColor(shape.getFillColor());

            // Aggiunta tramite AddToCanvas
            AddToCanvasReceiver addToCanvas = new AddToCanvasReceiver(drawingContextDTO);
            addToCanvas.addClonedShape(shape);

            if(this.hasCutted()){
                this.undoCopy();
            }
        }
    }

    public boolean hasCutted(){
        return this.isCutted;
    }

    public boolean hasShape() {
        if(shape == null){
            return false;
        }
        else{
            return true;
        }
    }

    // deletedShapes section:
    /*public void delete(DrawingContextDTO drawingDTO, ShapeInterface shape) {
        this.deletedShapesStack.push(shape);

        int indexNodeToDelete = drawingDTO.getDrawingPane().getChildren().indexOf(shape.getNode());
        if (indexNodeToDelete != -1) {
            drawingDTO.getDrawingPane().getChildren().remove(indexNodeToDelete);
        }

    }*/

    public void delete(DrawingContextDTO drawingDTO, ShapeInterface shape) {
        deletedShapesStack.push(shape);
        deletedShapePositions.push(shape.getRestorePosition());

        drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
    }


    /*public void undoDelete(DrawingContextDTO drawingDTO) {
        if (!deletedShapesStack.isEmpty()) {
            ShapeInterface restoredShape = deletedShapesStack.pop();

            if (restoredShape != null) {

                double x = restoredShape.getX();
                System.out.println("X: " + x);
                double y = restoredShape.getY();
                System.out.println("Y: " + y);

                if (restoredShape instanceof LineShape) {
                    restoredShape.draw(restoredShape.getPoints());
                } else {
                    if (restoredShape instanceof TextShape){
                        System.out.println("Sto restorando un text");
                        TextShape textShape = (TextShape) restoredShape;
                        System.out.println("ScaleX: " + textShape.getTextScaleX() + " ScaleY: " + textShape.getTextScaleY());
                    }
                    restoredShape.draw(x, y);
                }

//                // Inserisci la forma nel DTO
//                drawingDTO.setStartPoint(null); // reset per sicurezza
//                drawingDTO.setDrawingPane(drawingDTO.getDrawingPane());
//                drawingDTO.setBorderColor(restoredShape.getStrokeColor());
//                drawingDTO.setFillColor(restoredShape.getFillColor());

                 //AddToCanvasReceiver addToCanvasReceiver = new AddToCanvasReceiver(drawingDTO);
//                addToCanvasReceiver.addClonedShape(restoredShape);
                drawingDTO.getDrawingPane().getChildren().add(restoredShape.getNode());
            }

        }
    }*/

    public void undoDelete(DrawingContextDTO drawingDTO) {
        if (!deletedShapesStack.isEmpty()) {
            ShapeInterface restoredShape = deletedShapesStack.pop();
            Point2D position = deletedShapePositions.pop();

            if (restoredShape != null && position != null) {
                restoredShape.setRestorePosition(position);
                drawingDTO.getDrawingPane().getChildren().add(restoredShape.getNode());
            }
        }
    }


}
