package org.example.model.commands;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import org.example.model.receivers.GroupReceiver;
import org.example.model.shape.CompositeShapeInterface;
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
    private CompositeShapeInterface group;
    private final List<ShapeInterface> children;

    // Per ciascuna shape, memorizzo la posizione assoluta (X,Y) sul drawingPane
    private final Map<ShapeInterface, Point2D> originalPositions = new HashMap<>();

    // (Opzionale) se le shape hanno punti interni da ripristinare
    private final Map<ShapeInterface, Double[]> originalPoints = new HashMap<>();

    public UngroupCommand(GroupReceiver receiver, CompositeShapeInterface group) {
        this.receiver = receiver;
        this.group    = group;
        this.children = List.copyOf(group.getChildren());
    }

    @Override
    public void executeCommand() {
        // ---------------------------------------------------------
        // 1) Per ciascuna figlia, calcolo la posizione assoluta in coordinate del drawingPane:
        //    a) Prendo il nodo JavaFX (Node) della shape.
        //    b) Converto un suo punto di riferimento in coordinate di scena (sceneX, sceneY).
        //    c) Converto quel (sceneX, sceneY) in coordinate locali del drawingPane.
        //    d) Memorizzo il risultato in originalPositions.
        // ---------------------------------------------------------
        for (ShapeInterface s : children) {
            Node childNode = s.getNode();

            // (a) Scelgo come punto di riferimento l'origine (0,0) locale del nodo
            Point2D localZero = new Point2D(0, 0);

            // (b) Converto in coordinate di scena:
            Point2D scenePoint = childNode.localToScene(localZero);

            // (c) Converto quelle sceneCoord in coordinate del drawingPane.
            //     Per farlo, devo sapere a quale nodo appartiene:
            //     ecco l’idea: prendo il parent finale, cioè il drawingPane.
            //     Se GroupReceiver reinserisce le figlie direttamente in drawingPane,
            //     basta fare: drawingPane.sceneToLocal(scenePoint).
            //     Ma qui non abbiamo direttamente riferimento a drawingPane dentro il comando,
            //     quindi salveremo "scenePoint" e le ricaveremo in controller.
            originalPositions.put(s, scenePoint);

            // (Opzionale) salvo anche punti interni (per undo)
            Double[] pts = s.getPoints();
            if (pts != null) {
                originalPoints.put(s, pts.clone());
            }
        }

        // ---------------------------------------------------------
        // 2) Eseguo lo sgrouping reale: il GroupReceiver toglie il Composite
        //    e reinserisce le shape figlie come figli diretti del drawingPane.
        // ---------------------------------------------------------
        receiver.ungroup(group);
    }

    @Override
    public void undoCommand() {
        // ---------------------------------------------------------
        // Se faccio undo, ricostruisco il gruppo esattamente come prima:
        // a) Chiedo a receiver di raggruppare di nuovo le shape (children).
        // b) Per ciascuna figlia, imposto mouseTransparent = true e pickOnBounds = false
        //    (perché ora è dentro il gruppo e vogliamo che clicchi il Composite).
        // c) Ripristino eventuali punti geometrici interni.
        // ---------------------------------------------------------
        CompositeShapeInterface newGroup = receiver.group(children);
        this.group = newGroup;

        for (ShapeInterface s : children) {
            Node node = s.getNode();
            node.setTranslateX(0);
            node.setTranslateY(0);

            // Dentro il gruppo, i figli devono essere mouseTransparent
            // e non intercettare pick sui bounds, perché lo facciamo gestire al Composite.
            node.setMouseTransparent(true);
            node.setPickOnBounds(false);

            // Ripristino punti interni, se esistono
            Double[] pts = originalPoints.get(s);
            if (pts != null) {
                s.draw(pts);
            }
        }
    }

    /**
     * Restituisce l'elenco di shape "liberate" dal gruppo.
     */
    public List<ShapeInterface> getChildren() {
        return children;
    }

    /**
     * Restituisce la mappa [shape → Point2D(sceneX, sceneY)] salvata prima dell'ungroup.
     * NOTA: qui il Point2D è ancora in coordinate di scena, non in coordinate del drawingPane!
     */
    public Map<ShapeInterface, Point2D> getOriginalScenePositions() {
        return originalPositions;
    }
}
