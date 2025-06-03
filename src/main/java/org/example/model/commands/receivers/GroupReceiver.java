package org.example.model.commands.receivers;

import org.example.model.DTO.DrawingContextDTO;
import org.example.model.factory.concreteProduct.CompositeShape;
import org.example.model.factory.product.ShapeInterface;

import java.util.List;

/**
 * Receiver che esegue il raggruppamento e lo sgangio delle shape sul canvas
 */
public class GroupReceiver {
    private final DrawingContextDTO contextDTO;

    public GroupReceiver(DrawingContextDTO contextDTO) {
        this.contextDTO = contextDTO;
    }

    /**
     * Raggruppa le shape rimuovendole dal canvas e restituendo un CompositeShapeInterface
     */
    public ShapeInterface group(List<ShapeInterface> shapes) {
        // rimuovi le shape singole
        for (ShapeInterface s : shapes) {
            contextDTO.getDrawingPane().getChildren().remove(s.getNode());
        }
        // crea il composite e lo aggiunge
        CompositeShape composite = new CompositeShape(shapes);
        contextDTO.getDrawingPane().getChildren().add(composite.getNode());
        return composite;
    }

    /**
     * Scompone il CompositeShapeInterface riportando le shape originali nel canvas
     */
    public void ungroup(ShapeInterface composite) {
        // rimuovi il nodo del gruppo
        contextDTO.getDrawingPane().getChildren().remove(composite.getNode());
        // reinserisci le shape figlie
        for (ShapeInterface s : composite.getChildren()) {
            contextDTO.getDrawingPane().getChildren().add(s.getNode());
        }
    }
}
