package org.example.model.receivers;

import javafx.scene.layout.Pane;
import org.example.model.shape.CompositeShapeInterface;
import org.example.model.shape.CompositeShape;
import org.example.model.shape.ShapeInterface;

import java.util.List;

/**
 * Receiver che esegue il raggruppamento e lo sgangio delle shape sul canvas
 */
public class GroupReceiver {
    private final Pane drawingPane;

    public GroupReceiver(Pane drawingPane) {
        this.drawingPane = drawingPane;
    }

    /**
     * Raggruppa le shape rimuovendole dal canvas e restituendo un CompositeShapeInterface
     */
    public CompositeShapeInterface group(List<ShapeInterface> shapes) {
        // rimuovi le shape singole
        for (ShapeInterface s : shapes) {
            drawingPane.getChildren().remove(s.getNode());
        }
        // crea il composite e lo aggiunge
        CompositeShape composite = new CompositeShape(shapes);
        drawingPane.getChildren().add(composite.getNode());
        return composite;
    }

    /**
     * Scompone il CompositeShapeInterface riportando le shape originali nel canvas
     */
    public void ungroup(CompositeShapeInterface composite) {
        // rimuovi il nodo del gruppo
        drawingPane.getChildren().remove(composite.getNode());
        // reinserisci le shape figlie
        for (ShapeInterface s : composite.getChildren()) {
            drawingPane.getChildren().add(s.getNode());
        }
    }
}
