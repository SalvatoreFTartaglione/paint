package org.example.model.receivers;

import javafx.geometry.Point2D;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.factory.concreteProduct.LineShape;
import org.example.model.factory.product.ShapeInterface;

import java.util.Stack;

/**
 * The ShapeReceiver class is responsible for managing operations on shapes
 * such as copying, cutting, pasting, deleting, and undoing these actions
 * within a given drawing context.
 */
public class ShapeReceiver {

    private ShapeInterface shape;
    private boolean isCutted = false;
    private DrawingContextDTO drawingDTO;
    // Stack for deleted shapes (Undo task)
    private final Stack<ShapeInterface> deletedShapesStack = new Stack<>();
    private final Stack<Point2D> deletedShapePositions = new Stack<>();

    /**
     * Constructs a new ShapeReceiver object with the specified DrawingContextDTO.
     *
     * @param drawingDTO the DrawingContextDTO object used to initialize the ShapeReceiver.
     *                   If null, no initialization is performed.
     */
    public ShapeReceiver(DrawingContextDTO drawingDTO) {
        if (drawingDTO != null) {
            this.drawingDTO = drawingDTO;
        }
    }

    /**
     * Copies the given shape by cloning it and assigns the cloned shape to the current receiver.
     *
     * @param shape the ShapeInterface object to be cloned and copied.
     */
    public void copy(ShapeInterface shape) {
        this.shape = shape.cloneShape();
    }

    /**
     * Retrieves a copy of the current shape managed by the ShapeReceiver.
     *
     * @return a copy of the current ShapeInterface object if it exists; otherwise, returns null.
     */
    public ShapeInterface getCopy() {
        return shape == null ? null : shape.cloneShape();
    }

    public void undoCopy() {
        shape = null;
        isCutted = false;
    }

    /**
     * Cuts the specified shape from the drawing context by cloning the shape,
     * marking it as cut, and then deleting it from the provided drawing context.
     *
     * @param shape the ShapeInterface object to be cut. This shape is cloned
     *              and managed internally within the receiver.
     * @param contextDTO the DrawingContextDTO object representing the current
     *                   drawing context from which the shape is to be removed.
     */
    public void cut(ShapeInterface shape, DrawingContextDTO contextDTO){
        this.shape = shape.cloneShape();
        this.isCutted = true;
        this.delete(contextDTO, shape);
    }

    /**
     * Pastes a shape into the drawing context at a specified location, adapting its
     * position based on the user's input and shape type. If the shape is of type
     * LineShape, its points are recalculated to match the specified target location.
     * For other shape types, the specified position serves as the new center of the shape.
     * The pasted shape is then added to the canvas via the provided DrawingContextDTO.
     * If the shape was previously cut, it handles the undoing of the cut status.
     *
     * @param shape the ShapeInterface object to be pasted. If the shape is null, the method exits without performing any action.
     * @param drawingContextDTO the DrawingContextDTO object containing information about the drawing canvas
     *                          and the user's click coordinates. This is used to determine where the shape
     *                          should be pasted and to manage the drawing context after placing the shape.
     */
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

    /**
     * Determines whether the current shape managed by the receiver has been cut.
     *
     * @return true if the shape has been marked as cut, otherwise false.
     */
    public boolean hasCutted(){
        return this.isCutted;
    }

    /**
     *
     * @return
     */
    public boolean hasShape() {
        if(shape == null){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Removes the specified shape from the drawing pane of the provided drawing context.
     * The shape's position and reference are stored in stacks to support undo operations.
     *
     * @param drawingDTO the DrawingContextDTO object representing the current drawing context
     *                   from which the shape is to be removed.
     * @param shape      the ShapeInterface object representing the shape to be deleted.
     *                   This shape will be removed from the drawing pane and its state
     *                   will be saved for potential restoration.
     */
    public void delete(DrawingContextDTO drawingDTO, ShapeInterface shape) {
        deletedShapesStack.push(shape);
        deletedShapePositions.push(shape.getRestorePosition());

        drawingDTO.getDrawingPane().getChildren().remove(shape.getNode());
    }

    /**
     * Restores the last deleted shape from the deleted shapes stack and re-adds it to the drawing pane.
     * This method retrieves the most recently deleted shape and its position from the respective stacks.
     * If both the shape and its position are present, the shape's position is restored,
     * and it is added back to the drawing context.
     *
     * @param drawingDTO the DrawingContextDTO object that contains the drawing pane where the
     *                   shape will be restored.
     */
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
