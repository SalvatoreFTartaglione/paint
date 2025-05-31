package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class TextShape implements ShapeInterface {

    private final Text textNode;
    private Double[] points = new Double[2]; // X, Y
    private double baseWidth;
    private double baseHeight;
    private boolean isFlippedX = false;
    private boolean isFlippedY = false;

    public TextShape(String content, Color strokeColor, Color fillingColor, double width, double height, double strokeWidth, double scaleX, double scaleY) {
        textNode = new Text(content);
        textNode.setStroke(strokeColor);
        textNode.setFill(fillingColor);
        textNode.setX(width);
        textNode.setY(height);
        textNode.setFont(Font.font(strokeWidth));
        baseWidth = textNode.getLayoutBounds().getWidth();
        baseHeight = textNode.getLayoutBounds().getHeight();
        setTextScaleX(scaleX);
        setTextScaleY(scaleY);
    }

    @Override
    public boolean canHaveFill() {
        return true;
    }

    @Override
    public void draw(double X, double Y) {
        textNode.setX(X);
        textNode.setY(Y);
        points[0] = X;
        points[1] = Y;
    }

    @Override
    public void draw(Double[] points) {
        if (points.length >= 2) {
            draw(points[0], points[1]);
        }
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {
        setX(getX() + deltaX);
        setY(getY() + deltaY);
    }

    @Override
    public ShapeInterface cloneShape() {
        TextShape clone = new TextShape(getText(), getStrokeColor(), getFillColor(), getWidth(), getHeight(), getCustomStrokeWidth(), getTextScaleX(), getTextScaleY());
        clone.draw(getX(), getY());
        return clone;
    }

    @Override
    public Color getStrokeColor() {
        return (Color) textNode.getStroke();
    }

    @Override
    public void setStrokeColor(Color stroke) {
        textNode.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color) textNode.getFill();
    }

    @Override
    public void setFillColor(Color fill) {
        textNode.setFill(fill);
    }

    @Override
    public double getWidth() {
        return textNode.getLayoutBounds().getWidth();
    }

    @Override
    public double getHeight() {
        return textNode.getLayoutBounds().getHeight();
    }

    @Override
    public void setSize(double width, double height) {
        // Sicurezza: evita divisione per 0
        if (baseWidth == 0 || baseHeight == 0) {
            baseWidth = textNode.getLayoutBounds().getWidth();
            baseHeight = textNode.getLayoutBounds().getHeight();
            if (baseWidth == 0 || baseHeight == 0) return; // ancora non pronto
        }

        double scaleX = width / baseWidth;
        double scaleY = height / baseHeight;

        // Usa scala uniforme per evitare schiacciamenti
        double uniformScale = Math.min(scaleX, scaleY);

        textNode.setScaleX(uniformScale);
        textNode.setScaleY(uniformScale);
    }


    @Override
    public double getX() {
        return textNode.getX();
    }

    @Override
    public void setX(double x) {
        textNode.setX(x);
        points[0] = x;
    }

    @Override
    public double getY() {
        return isFlippedY ? textNode.getY() - baseHeight : textNode.getY();
    }

    @Override
    public void setY(double y) {
        // Se ribaltato verticalmente, compensa perché Y è baseline
        if (isFlippedY) {
            textNode.setY(y + baseHeight);
        } else {
            textNode.setY(y);
        }
        points[1] = y;
    }


    @Override
    public Double[] getPoints() {
        return points;
    }

    @Override
    public double getCustomStrokeWidth() {
        return textNode.getFont().getSize();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        textNode.setFont(Font.font(strokeWidth));
    }

    @Override
    public Node getNode() {
        return textNode;
    }

    @Override
    public double getEffectiveSize() {
        return getCustomStrokeWidth();
    }

    @Override
    public void scaleToSize(double scale) {
        textNode.setFont(Font.font(scale));
    }

    @Override
    public double getMaxSliderSize() {
        return Math.max(120, getEffectiveSize() * 0.7);
    }

    @Override
    public double getMinSliderSize() {
        return Math.max(20, getEffectiveSize() * 0.1);
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("%.0f pt", value);
    }

    @Override
    public String getText() {
        return textNode.getText() == null ? "" : textNode.getText();
    }

    @Override
    public void setText(String content) {
        textNode.setText(content);
    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double width = getWidth();
        double height = getHeight();

        double minX;
        double maxX;
        double minY;
        double maxY;
        if (isFlippedX || isFlippedY) {
            minX = (width / 2 + (margin - 75)) / zoom;
            maxX = (paneWidth - width / 2 - (margin - 75)) / zoom;
            minY = (height / 2 + (margin - 75)) / zoom;
            maxY = (paneHeight - height / 2 - (margin - 75)) / zoom;
        }else {
            minX = (width / 2 + (margin)) / zoom;
            maxX = (paneWidth - width / 2 - (margin)) / zoom;
            minY = (height / 2 + (margin)) / zoom;
            maxY = (paneHeight - height / 2 - (margin)) / zoom;
        }

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }


    @Override
    public void mirrorUp() {
        textNode.setScaleY(-textNode.getScaleY());
        isFlippedY = !isFlippedY;

        // Aggiorna anche l'array points per mantenere la coerenza
        if (points[1] != null) {
            points[1] = textNode.getY();
        }
    }

    @Override
    public void mirrorDown() {
        textNode.setScaleY(-textNode.getScaleY());
        isFlippedY = !isFlippedY;

        if (points[1] != null) {
            points[1] = textNode.getY();
        }
    }

    @Override
    public void mirrorLeft() {
        textNode.setScaleX(-textNode.getScaleX());
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = textNode.getX();
        }
    }

    @Override
    public void mirrorRight() {
        textNode.setScaleX(-textNode.getScaleX());
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = textNode.getX();
        }
    }

    @Override
    public void setTextScaleX(double scale) {
        textNode.setScaleX(scale);
    }

    @Override
    public double getTextScaleX() {
        return textNode.getScaleX();
    }

    @Override
    public void setTextScaleY(double scale) {
        textNode.setScaleY(scale);
    }

    @Override
    public double getTextScaleY() {
        return textNode.getScaleY();
    }

    public double getRawX() {
        return textNode.getX(); // senza compensazioni
    }

    public double getRawY() {
        return textNode.getY(); // senza compensazioni
    }

    @Override
    public Point2D getRestorePosition() {
        return new Point2D(textNode.getX(), textNode.getY()); // Posizione effettiva
    }

    @Override
    public void setRestorePosition(Point2D point) {
        draw(point.getX(), point.getY()); // Usa draw come prima
    }



    @Override
    public void mirrorHorizontallyAroundCenter() {
        textNode.setScaleX(textNode.getScaleX() * -1);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        textNode.setScaleY(textNode.getScaleY() * -1);
    }


    @Override
    public String toString() {
        return "Text;" + textNode.getText() + ";" + textNode.getStroke() + ";" + textNode.getFill() + ";" +
                textNode.getX() + ";" + textNode.getY() + ";" + getCustomStrokeWidth() + ";" + getTextScaleX() + ";" + getTextScaleY() + "\n";
    }




}
