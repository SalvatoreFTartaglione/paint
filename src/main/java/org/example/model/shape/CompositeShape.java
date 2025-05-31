package org.example.model.shape;

import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CompositeShape implements CompositeShapeInterface {
    private final Group groupNode = new Group();
    private final List<ShapeInterface> children = new ArrayList<>();

    public CompositeShape(List<ShapeInterface> shapes) {
        // 1) calcola l’origin del gruppo
        double minX = shapes.stream()
                .mapToDouble(ShapeInterface::getX)
                .min().orElse(0);
        double minY = shapes.stream()
                .mapToDouble(ShapeInterface::getY)
                .min().orElse(0);

        // 2) posiziona il Group
        groupNode.setLayoutX(minX);
        groupNode.setLayoutY(minY);

        // 3) integra i figli con coordinate relative al group
        for (ShapeInterface s : shapes) {
            // spostali in locale rispetto al group
            s.moveBy(-minX, -minY);
            children.add(s);
            groupNode.getChildren().add(s.getNode());
        }

        // permetti al Group di intercettare eventi anche dove non ci sono shape
        groupNode.setPickOnBounds(true);

        // evita che i figli intercettino il mouse
        for (Node child : groupNode.getChildren()) {
            child.setMouseTransparent(true);
        }
        groupNode.setUserData(this); // per riconoscere il composite
    }

    @Override
    public void addChild(ShapeInterface child) {
        children.add(child);
        groupNode.getChildren().add(child.getNode());
    }

    @Override
    public void removeChild(ShapeInterface child) {
        children.remove(child);
        groupNode.getChildren().remove(child.getNode());
    }

    @Override
    public boolean canHaveFill() {
        return true;
    }

    @Override
    public List<ShapeInterface> getChildren() {
        return this.children;  // la lista dei ShapeInterface raggruppati
    }

    @Override
    public Node getNode() {
        return groupNode;
    }

    @Override
    public ShapeInterface cloneShape() {
        List<ShapeInterface> clones = new ArrayList<>();
        for (ShapeInterface s : children) {
            clones.add(s.cloneShape());
        }
        return new CompositeShape(clones);
    }

    @Override
    public void moveBy(double dx, double dy) {
        // sposto il container, non i figli
        groupNode.setLayoutX(groupNode.getLayoutX() + dx);
        groupNode.setLayoutY(groupNode.getLayoutY() + dy);
    }

    @Override
    public void setStrokeColor(Color color) {
        for (ShapeInterface s : children) {
            s.setStrokeColor(color);
        }
    }

    @Override
    public Color getStrokeColor() {
        return children.isEmpty() ? Color.BLACK : children.get(0).getStrokeColor();
    }

    @Override
    public void setFillColor(Color fill) {
        for (ShapeInterface s : children) {
            if (s.canHaveFill()) {
                s.setFillColor(fill);
            }
        }
    }

    @Override
    public Color getFillColor() {
        return children.isEmpty() ? Color.TRANSPARENT : children.get(0).getFillColor();
    }

    @Override
    public boolean isGroup() {
        return true;
    }

    @Override
    public double getWidth() {
        return groupNode.getBoundsInParent().getWidth();
    }

    @Override
    public double getHeight() {
        return groupNode.getBoundsInParent().getHeight();
    }

    @Override
    public void setSize(double width, double height) {
        double currentW = getWidth(), currentH = getHeight();
        double sx = currentW > 0 ? width / currentW : 1;
        double sy = currentH > 0 ? height / currentH : 1;
        double scale = Math.min(sx, sy);
        for (ShapeInterface s : children) {
            s.scaleToSize(s.getEffectiveSize() * scale);
        }
    }

    @Override
    public double getX() {
        return groupNode.getLayoutX();
    }

    @Override
    public void setX(double x) {
        groupNode.setLayoutX(x);
    }

    @Override
    public double getY() {
        return groupNode.getLayoutY();
    }

    @Override
    public void setY(double y) {
        groupNode.setLayoutY(y);
    }

    @Override
    public Double[] getPoints() {
        return null;
    }

    @Override
    public void draw(double X, double Y) {
        setX(X);
        setY(Y);
    }

    @Override
    public void draw(Double[] points) {
        // non necessario per il composite
    }

    @Override
    public double getCustomStrokeWidth() {
        return children.isEmpty() ? 1 : children.get(0).getCustomStrokeWidth();
    }

    @Override
    public void setCustomStrokeWidth(double w) {
        for (ShapeInterface s : children) {
            s.setCustomStrokeWidth(w);
        }
    }

    @Override
    public double getEffectiveSize() {
        return Math.max(getWidth(), getHeight());
    }

    @Override
    public void scaleToSize(double scale) {
        for (ShapeInterface s : children) {
            s.scaleToSize(scale);
        }
    }

    @Override
    public String getText() {
        throw new UnsupportedOperationException("CompositeShape non gestisce testo");
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("CompositeShape non gestisce testo");
    }

    //QUIII
    @Override
    public double getMaxSliderSize() {
        // Default fornito dall’interfaccia
        return CompositeShapeInterface.super.getMaxSliderSize();
    }

    @Override
    public double getMinSliderSize() {
        // Default fornito dall’interfaccia
        return CompositeShapeInterface.super.getMinSliderSize();
    }

    @Override
    public String getSliderLabel(double value) {
        // Default fornito dall’interfaccia
        return CompositeShapeInterface.super.getSliderLabel(value);
    }

    @Override
    public Point2D getRestorePosition() {
        // Default fornito dall’interfaccia
        return CompositeShapeInterface.super.getRestorePosition();
    }

    @Override
    public void setRestorePosition(Point2D point) {
        // Default fornito dall’interfaccia
        CompositeShapeInterface.super.setRestorePosition(point);
    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY,
                             double paneWidth, double paneHeight,
                             double zoom, double margin) {
        // Simile a una singola shape: verifica che il bounding del gruppo stia dentro i limiti
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double halfW = (getWidth() / 2 + margin) / zoom;
        double halfH = (getHeight() / 2 + margin) / zoom;

        double minX = halfW;
        double maxX = (paneWidth / zoom) - halfW;
        double minY = halfH;
        double maxY = (paneHeight / zoom) - halfH;

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }

    @Override
    public void mirrorUp() {
        // flip orizzontale rispetto al bordo superiore del gruppo
        double h = getHeight();                     // altezza attuale del gruppo
        groupNode.setScaleY(groupNode.getScaleY() * -1);
        groupNode.setTranslateY(groupNode.getTranslateY() + h);
    }

    @Override
    public void mirrorDown() {
        // flip orizzontale rispetto al bordo inferiore del gruppo
        double h = getHeight();
        groupNode.setScaleY(groupNode.getScaleY() * -1);
        groupNode.setTranslateY(groupNode.getTranslateY() - h);
    }

    @Override
    public void mirrorLeft() {
        // flip verticale rispetto al bordo sinistro del gruppo
        double w = getWidth();                      // larghezza attuale del gruppo
        groupNode.setScaleX(groupNode.getScaleX() * -1);
        groupNode.setTranslateX(groupNode.getTranslateX() + w);
    }

    @Override
    public void mirrorRight() {
        // flip verticale rispetto al bordo destro del gruppo
        double w = getWidth();
        groupNode.setScaleX(groupNode.getScaleX() * -1);
        groupNode.setTranslateX(groupNode.getTranslateX() - w);
    }

    @Override
    public void setTextScaleX(double scale) {
        // Applica la scala X del testo a ciascun figlio
        for (ShapeInterface s : children) {
            s.setTextScaleX(scale);
        }
    }

    @Override
    public double getTextScaleX() {
        return children.isEmpty() ? 1.0 : children.get(0).getTextScaleX();
    }

    @Override
    public void setTextScaleY(double scale) {
        // Applica la scala Y del testo a ciascun figlio
        for (ShapeInterface s : children) {
            s.setTextScaleY(scale);
        }
    }

    @Override
    public double getTextScaleY() {
        return children.isEmpty() ? 1.0 : children.get(0).getTextScaleY();
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {
        // 1) prendi il bounding locale del Group
        Bounds b = groupNode.getBoundsInLocal();
        double centerX = b.getMinX() + b.getWidth() / 2;

        // 2) inverte la scala sull’asse X
        groupNode.setScaleX(groupNode.getScaleX() * -1);

        // 3) trasla di 2*centerX (per mantenere il centro al suo posto)
        groupNode.setTranslateX(groupNode.getTranslateX() + 2 * centerX);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        // 1) prendi il bounding locale del Group
        Bounds b = groupNode.getBoundsInLocal();
        double centerY = b.getMinY() + b.getHeight() / 2;

        // 2) inverte la scala sull’asse Y
        groupNode.setScaleY(groupNode.getScaleY() * -1);

        // 3) trasla di 2*centerY (per mantenere il centro al suo posto)
        groupNode.setTranslateY(groupNode.getTranslateY() + 2 * centerY);
    }
 ///fino a qui
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Group:\n");
        for (ShapeInterface s : children) {
            sb.append("  ").append(s).append("\n");
        }
        return sb.toString();
    }

}