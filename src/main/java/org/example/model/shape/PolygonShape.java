package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import java.util.Arrays;
import java.util.Optional;

public class PolygonShape implements ShapeInterface {

    private final Polygon polygonShape;
    private Double[] points;

    public PolygonShape(Color stroke, Color fill, double strokeWidth, double ScaleX, double ScaleY) {
        this.polygonShape = new Polygon();
        polygonShape.setStroke(stroke);
        polygonShape.setFill(fill);
        polygonShape.setStrokeWidth(strokeWidth);
        polygonShape.setScaleX(ScaleX);
        polygonShape.setScaleY(ScaleY);
    }

    @Override
    public void draw(double x, double y) {
        if (points.length < 2) return;

        double firstX = points[0];
        double firstY = points[1];

        double[] newPoints = new double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                newPoints[i] = (x + points[i]) - firstX;
            } else {
                newPoints[i] = (y + points[i]) - firstY;
            }
        }

        polygonShape.getPoints().clear();
        for (double point : newPoints) {
            polygonShape.getPoints().add(point);
        }
    }

    @Override
    public void draw(Double[] points) {
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(points));
        polygonShape.setStroke(getStrokeColor());
        polygonShape.setStrokeWidth(getCustomStrokeWidth());
        polygonShape.setFill(getFillColor());

        // Aggiorna l'array interno dei punti
        this.points = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            this.points[i] = points[i];
        }
    }

    @Override
    public boolean canHaveFill() {
        return true;
    }

    @Override
    public ShapeInterface cloneShape() {
        PolygonShape clone = new PolygonShape(this.getStrokeColor(), this.getFillColor(), this.getCustomStrokeWidth(), this.getScaleX(), this.getScaleY());
        clone.draw(getPoints());
        return clone;
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            if (i % 2 == 0) {
                polygonShape.getPoints().set(i, polygonShape.getPoints().get(i) + deltaX);
            } else {
                polygonShape.getPoints().set(i, polygonShape.getPoints().get(i) + deltaY);
            }
        }
        // Aggiorna anche l'array interno
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                points[i] += deltaX;
            } else {
                points[i] += deltaY;
            }
        }
    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;

        // Trova i bounds del poligono
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i) + deltaX;
            double y = polygonShape.getPoints().get(i + 1) + deltaY;

            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }

        double marginScaled = margin / zoom;
        return minX >= marginScaled && maxX <= (paneWidth - marginScaled) / zoom &&
                minY >= marginScaled && maxY <= (paneHeight - marginScaled) / zoom;
    }

    @Override
    public void mirrorUp() {
        double centerY = getY();
        double height = getHeight();
        double mirroredY = centerY - height;

        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double currentY = polygonShape.getPoints().get(i);
            double distance = centerY - currentY;
            polygonShape.getPoints().set(i, mirroredY - distance);
        }
        this.updatePointsArray();
    }

    @Override
    public void mirrorDown() {
        double centerY = getY();
        double height = getHeight();
        double mirroredY = centerY + height;

        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double currentY = polygonShape.getPoints().get(i);
            double distance = currentY - centerY;
            polygonShape.getPoints().set(i, mirroredY + distance);
        }
        this.updatePointsArray();
    }

    @Override
    public void mirrorLeft() {
        double centerX = getX();
        double width = getWidth();
        double mirroredX = centerX - width;

        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double currentX = polygonShape.getPoints().get(i);
            double distance = centerX - currentX;
            polygonShape.getPoints().set(i, mirroredX - distance);
        }
        this.updatePointsArray();
    }

    @Override
    public void mirrorRight() {
        double centerX = getX();
        double width = getWidth();
        double mirroredX = centerX + width;

        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double currentX = polygonShape.getPoints().get(i);
            double distance = currentX - centerX;
            polygonShape.getPoints().set(i, mirroredX + distance);
        }
        this.updatePointsArray();
    }

    @Override
    public void setScaleX(double scale) {
        polygonShape.setScaleX(scale);
    }

    @Override
    public double getScaleX() {
        return polygonShape.getScaleX();
    }

    @Override
    public void setScaleY(double scale) {
        polygonShape.setScaleY(scale);
    }

    @Override
    public double getScaleY() {
        return polygonShape.getScaleY();
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {
        polygonShape.setScaleX(polygonShape.getScaleX() * -1);
        this.updatePointsArray();
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        polygonShape.setScaleY(polygonShape.getScaleY() * -1);
        this.updatePointsArray();
    }

    @Override
    public Color getStrokeColor() {
        return (Color) polygonShape.getStroke();
    }

    @Override
    public void setStrokeColor(Color stroke) {
        polygonShape.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color) polygonShape.getFill();
    }

    @Override
    public void setFillColor(Color fill) {
        polygonShape.setFill(fill);
    }

    @Override
    public double getX() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        return (minX + maxX) / 2;
    }

    @Override
    public void setX(double x) {
        double currentX = getX();
        double deltaX = x - currentX;
        moveBy(deltaX, 0);
    }

    @Override
    public double getY() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double y = polygonShape.getPoints().get(i);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        return (minY + maxY) / 2;
    }

    @Override
    public void setY(double y) {
        double currentY = getY();
        double deltaY = y - currentY;
        moveBy(0, deltaY);
    }

    @Override
    public double getWidth() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
        }
        return maxX - minX;
    }

    @Override
    public double getHeight() {
        if (polygonShape.getPoints().isEmpty()) return 0;

        double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (int i = 1; i < polygonShape.getPoints().size(); i += 2) {
            double y = polygonShape.getPoints().get(i);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        return maxY - minY;
    }

    @Override
    public void setSize(double width, double height) {
        double currentWidth = getWidth();
        double currentHeight = getHeight();

        if (currentWidth == 0 || currentHeight == 0) return;

        double scaleX = width / currentWidth;
        double scaleY = height / currentHeight;

        double centerX = getX();
        double centerY = getY();

        for (int i = 0; i < polygonShape.getPoints().size(); i += 2) {
            double x = polygonShape.getPoints().get(i);
            double y = polygonShape.getPoints().get(i + 1);

            double newX = centerX + (x - centerX) * scaleX;
            double newY = centerY + (y - centerY) * scaleY;

            polygonShape.getPoints().set(i, newX);
            polygonShape.getPoints().set(i + 1, newY);
        }
        updatePointsArray();
    }

    @Override
    public void resize(double newSize) {
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            setSize(getWidth() * scale, getHeight() * scale);
        }
    }

    @Override
    public double getStretchWidth() {
        return getWidth();
    }

    @Override
    public double getStretchHeight() {
        return getHeight();
    }

    @Override
    public void stretch(double width, double height) {
        setSize(width, height);
    }

    @Override
    public void resetStretch(double width, double height) {
        setSize(width, height);
    }

    @Override
    public double getCustomStrokeWidth() {
        return polygonShape.getStrokeWidth();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        polygonShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public Double[] getPoints() {
        Double[] result = new Double[polygonShape.getPoints().size()];
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            result[i] = polygonShape.getPoints().get(i);
        }
        return result;
    }

    @Override
    public Node getNode() {
        return polygonShape;
    }

    @Override
    public double getEffectiveSize() {
        return Math.max(getWidth(), getHeight()) + 50;
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("Size: %.1f", value);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    private void updatePointsArray() {
        points = new Double[polygonShape.getPoints().size()];
        for (int i = 0; i < polygonShape.getPoints().size(); i++) {
            points[i] = polygonShape.getPoints().get(i);
        }
    }

    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return "Polygon" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" +
                this.getCustomStrokeWidth() + ";" + this.getScaleX() + ";" + this.getScaleY() + ";" + Arrays.toString(this.getPoints()) + "\n";
    }

}
