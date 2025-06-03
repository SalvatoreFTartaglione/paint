package org.example.model.commands.concreteCommands;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.receivers.GroupReceiver;
import org.example.model.factory.product.ShapeInterface;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GroupCommand implements CommandInterface {
    private final GroupReceiver receiver;
    private final List<ShapeInterface> shapes;
    private ShapeInterface group;


    private final Map<ShapeInterface, Point2D> originalPositions = new HashMap<>();
    private final Map<ShapeInterface, Double[]> originalPoints    = new HashMap<>();

    public GroupCommand(GroupReceiver receiver, List<ShapeInterface> shapes) {
        this.receiver = receiver;
        this.shapes   = List.copyOf(shapes);
    }

    @Override
    public void executeCommand() {
        // prima del group, salvo posizioni e punti
        for (ShapeInterface s : shapes) {
            originalPositions.put(s, new Point2D(s.getX(), s.getY()));
            Double[] pts = s.getPoints();
            if (pts != null) {
                originalPoints.put(s, pts.clone());
            }
        }
        // raggruppo
        group = receiver.group(shapes);
    }

    @Override
    public void undoCommand() {
        if (group == null) return;

        // 1) scompongo
        receiver.ungroup(group);

        // 2) ripristino ogni shape
        for (ShapeInterface s : shapes) {
            Node node = s.getNode();
            // pulisco translate
            node.setTranslateX(0);
            node.setTranslateY(0);
            // riabilito eventi
            node.setMouseTransparent(false);
            node.setPickOnBounds(false);

            // se era una shape multipunto
            Double[] pts = originalPoints.get(s);
            if (pts != null) {
                // ridisegno con gli stessi punti
                s.draw(pts);
            } else {
                // altrimenti ripristino x,y
                Point2D orig = originalPositions.get(s);
                if (orig != null) {
                    s.setX(orig.getX());
                    s.setY(orig.getY());
                }
            }
        }
    }

    @Override
    public ShapeInterface getCreatedGroup() {
        return group;
    }
}
