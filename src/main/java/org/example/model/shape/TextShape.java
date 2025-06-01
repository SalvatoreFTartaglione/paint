package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Optional;

public class TextShape implements ShapeInterface {

    private final Text textShape;
    private Double[] points = new Double[2]; // X, Y
    private double baseWidth;
    private double baseHeight;
    private boolean isFlippedX = false;
    private boolean isFlippedY = false;

    public TextShape(String content, Color strokeColor, Color fillingColor, double width, double height, double strokeWidth, double scaleX, double scaleY) {
        textShape = new Text(content);
        textShape.setStroke(strokeColor);
        textShape.setFill(fillingColor);
        textShape.setX(width);
        textShape.setY(height);
        textShape.setFont(Font.font(strokeWidth));
        baseWidth = textShape.getLayoutBounds().getWidth();
        baseHeight = textShape.getLayoutBounds().getHeight();
        setScaleX(scaleX);
        setScaleY(scaleY);
    }

    @Override
    public boolean canHaveFill() {
        return true;
    }

    @Override
    public void draw(double X, double Y) {
         textShape.setX(X);
         textShape.setY(Y);
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
        TextShape clone = new TextShape(getText(), getStrokeColor(), getFillColor(), getWidth(), getHeight(), getCustomStrokeWidth(), getScaleX(), getScaleY());
        clone.draw(getX(), getY());
        return clone;
    }

    @Override
    public Color getStrokeColor() {
        return (Color)  textShape.getStroke();
    }

    @Override
    public void setStrokeColor(Color stroke) {
         textShape.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color)  textShape.getFill();
    }

    @Override
    public void setFillColor(Color fill) {
         textShape.setFill(fill);
    }

    @Override
    public double getWidth() {
        return  this.getCustomStrokeWidth();
    }

    @Override
    public double getHeight() {
        return  this.getCustomStrokeWidth();
    }

    @Override
    public void setSize(double width, double height) {
        this.setCustomStrokeWidth(Math.max(width, height));
    }

    @Override
    public void resize(double newSize) {
        this.setCustomStrokeWidth(newSize);
    }

    @Override
    public double getStretchWidth() {
        return  textShape.getScaleX();
    }

    @Override
    public double getStretchHeight() {
        return  textShape.getScaleY();
    }

    @Override
    public void resetStretch(double width, double height) {
        textShape.setScaleX(width);
        textShape.setScaleY(height);
    }

    @Override
    public void stretch(double width, double height) {
        textShape.setScaleX(width);
        textShape.setScaleY(height);
    }


    @Override
    public double getX() {
        return  textShape.getX();
    }

    @Override
    public void setX(double x) {
         textShape.setX(x);
        points[0] = x;
    }

    @Override
    public double getY() {
        return isFlippedY ? textShape.getY() - baseHeight : textShape.getY();
    }

    @Override
    public void setY(double y) {
        // Se ribaltato verticalmente, compensa perché Y è baseline
        if (isFlippedY) {
            textShape.setY(y + baseHeight);
        } else {
            textShape.setY(y);
        }
        points[1] = y;
    }

    @Override
    public Double[] getPoints() {
        return points;
    }

    @Override
    public double getCustomStrokeWidth() {
        return  textShape.getFont().getSize();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
         textShape.setFont(Font.font(strokeWidth));
    }

    @Override
    public Node getNode() {
        return  textShape;
    }

    @Override
    public double getEffectiveSize() {
        return getCustomStrokeWidth();
    }

    @Override
    public double getMaxSliderSize() {
        return 120;
    }

    @Override
    public double getMinSliderSize() {
        return 38;
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("%.0f pt", value);
    }

    @Override
    public String getText() {
        return  textShape.getText() == null ? "" :  textShape.getText();
    }

    @Override
    public void setText(String content) {
         textShape.setText(content);
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
        textShape.setScaleY(-textShape.getScaleY());
        isFlippedY = !isFlippedY;

        // Aggiorna anche l'array points per mantenere la coerenza
        if (points[1] != null) {
            points[1] = textShape.getY();
        }
    }

    @Override
    public void mirrorDown() {
        textShape.setScaleY(-textShape.getScaleY());
        isFlippedY = !isFlippedY;

        if (points[1] != null) {
            points[1] = textShape.getY();
        }
    }

    @Override
    public void mirrorLeft() {
        textShape.setScaleX(-textShape.getScaleX());
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = textShape.getX();
        }
    }

    @Override
    public void mirrorRight() {
        textShape.setScaleX(-textShape.getScaleX());
        isFlippedX = !isFlippedX;

        if (points[0] != null) {
            points[0] = textShape.getX();
        }
    }

    @Override
    public void setScaleX(double scale) {
        textShape.setScaleX(scale);
    }

    @Override
    public double getScaleX() {
        return textShape.getScaleX();
    }

    @Override
    public void setScaleY(double scale) {
        textShape.setScaleY(scale);
    }

    @Override
    public double getScaleY() {
        return textShape.getScaleY();
    }

    public double getRawX() {
        return textShape.getX(); // senza compensazioni
    }

    public double getRawY() {
        return textShape.getY(); // senza compensazioni
    }

    @Override
    public Point2D getRestorePosition() {
        return new Point2D(textShape.getX(), textShape.getY()); // Posizione effettiva
    }

    @Override
    public void setRestorePosition(Point2D point) {
        draw(point.getX(), point.getY()); // Usa draw come prima
    }



    @Override
    public void mirrorHorizontallyAroundCenter() {
        textShape.setScaleX(textShape.getScaleX() * -1);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        textShape.setScaleY(textShape.getScaleY() * -1);
    }

    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double x = textShape.getX();
        double y = textShape.getY();

        double mirroredX = x;
        double mirroredY = y;

        switch (direction) {
            case UP: {
                mirroredY = y - 2 * getHeight(); // ipotetico specchio sopra
                break;
            }
            case DOWN: {
                mirroredY = y + 2 * getHeight(); // ipotetico specchio sotto
                break;
            }
            case LEFT: {
                mirroredX = x - 2 * getWidth(); // ipotetico specchio a sinistra
                break;
            }
            case RIGHT: {
                mirroredX = x + 2 * getWidth(); // ipotetico specchio a destra
                break;
            }
            case HORIZONTAL:
            case VERTICAL:
                // Il testo si riflette sul posto → nessuno spostamento
                return Optional.of(new Point2D(x, y));
        }

        return Optional.of(new Point2D(mirroredX, mirroredY));
    }


    @Override
    public String toString() {
        return "Text;" + textShape.getText() + ";" + textShape.getStroke() + ";" + textShape.getFill() + ";" +
                textShape.getX() + ";" + textShape.getY() + ";" + getCustomStrokeWidth() + ";" + getScaleX() + ";" + getScaleY() + "\n";
    }


}
