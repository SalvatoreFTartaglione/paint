package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.example.model.receivers.GroupReceiver;
import org.example.model.shape.ShapeInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Command per scomporre un gruppo di shape, salvando e ripristinando
 * la posizione assoluta (coordinate sul drawingPane) di ciascuna figlia.
 */
public class UngroupCommand implements CommandInterface {
    private final GroupReceiver receiver;
    private ShapeInterface group;
    private final List<ShapeInterface> children;

    // Per ciascuna shape, memorizzo la posizione assoluta (X,Y) sul drawingPane
    private final Map<ShapeInterface, Point2D> originalPositions = new HashMap<>();

    // (Opzionale) se le shape hanno punti interni da ripristinare
    private final Map<ShapeInterface, Double[]> originalPoints = new HashMap<>();

    public UngroupCommand(GroupReceiver receiver, ShapeInterface group) {
        this.receiver = receiver;
        this.group    = group;
        this.children = List.copyOf(group.getChildren());
    }

    @Override
    public void executeCommand() {
        if (group == null) return;
        receiver.ungroup(group);

        double offsetX = group.getX();
        double offsetY = group.getY();
        double groupRotate = group.getNode().getRotate();

        for (ShapeInterface s : children) {
            Node node = s.getNode();
            node.setTranslateX(0);
            node.setTranslateY(0);
            node.setRotate(groupRotate);

            node.setMouseTransparent(false);
            node.setPickOnBounds(false);

            node.setUserData(s);

            s.moveBy(offsetX, offsetY);
        }

    }

    @Override
    public void undoCommand() {
        // Raggruppa nuovamente le children
        ShapeInterface newGroup = receiver.group(children);

        // Ripristina posizione e rotazione originali
        newGroup.draw(group.getX(), group.getY());
        newGroup.getNode().setRotate(group.getNode().getRotate());

        // Aggiorna riferimento
        this.group = newGroup;

        for (ShapeInterface s : children) {
            Node node = s.getNode();
            node.setTranslateX(0);
            node.setTranslateY(0);

            // Devono essere trasparenti e non cliccabili direttamente
            node.setMouseTransparent(true);
            node.setPickOnBounds(false);

            // Ripristina eventuali punti geometrici
            Double[] pts = originalPoints.get(s);
            if (pts != null) {
                s.draw(pts);
            }
        }
    }



    @Override
    public List<ShapeInterface> getChildren() {
        return children;
    }


    @Override
    public Map<ShapeInterface, Point2D> getOriginalScenePositions() {
        return originalPositions;
    }
}
