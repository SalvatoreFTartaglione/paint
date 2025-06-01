package org.example.model.shapeCreators;

import javafx.scene.paint.Color;
import org.example.model.shape.AbstractShape;
import org.example.model.shape.CompositeShape;
import org.example.model.shape.ShapeInterface;

import java.util.List;

/**
 * Factory per creare CompositeShape (gruppi di ShapeInterface).
 * Il creator si occupa di tenere temporaneamente la lista di figli
 * e, quando viene chiamato createShape(...), restituisce un nuovo
 * CompositeShape contenente quei figli.
 *
 * Nota: i parametri color, strokeWidth, ecc. non sono usati per il
 * gruppo in sé, perché il gruppo si limita a raggruppare forme già
 * costruite. Li lasciamo nel metodo solo per mantenere la firma
 * coerente con AbstractShape.
 */
public class CompositeShapeCreator extends AbstractShape {

    private List<ShapeInterface> children;

    /**
     * Imposta la lista di ShapeInterface da raggruppare.
     * Deve essere chiamato prima di createShape(...).
     */
    public void setChildren(List<ShapeInterface> children) {
        this.children = children;
    }

    /**
     * Crea un nuovo CompositeShape a partire dalla lista di figli
     * precedentemente settata con setChildren(...).
     *
     * @param strokeColor  non usato (i colori vengono presi dai figli)
     * @param fillingColor non usato
     * @param strokeWidth  non usato
     * @param width        non usato
     * @param height       non usato
     * @param content      non usato
     * @param scaleX       non usato
     * @param scaleY       non usato
     * @return una CompositeShape che contiene i figli precedentemente assegnati
     */
    @Override
    public ShapeInterface createShape(Color strokeColor,
                                      Color fillingColor,
                                      int strokeWidth,
                                      double width,
                                      double height,
                                      String content,
                                      double scaleX,
                                      double scaleY,
                                      double angle) {
        // Semplicemente restituisce un nuovo CompositeShape costruito con la lista di figli
        return new CompositeShape(children);
    }
}
