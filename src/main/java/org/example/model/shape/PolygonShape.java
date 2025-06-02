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

    public PolygonShape(Color stroke, Color fill, double strokeWidth, double ScaleX, double ScaleY, double angle) {
        this.polygonShape = new Polygon();
        polygonShape.setStroke(stroke);
        polygonShape.setFill(fill);
        polygonShape.setStrokeWidth(strokeWidth);
        polygonShape.setScaleX(ScaleX);
        polygonShape.setScaleY(ScaleY);
        polygonShape.setRotate(angle);
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
        PolygonShape clone = new PolygonShape(this.getStrokeColor(), this.getFillColor(), this.getCustomStrokeWidth(), this.getScaleX(), this.getScaleY(), this.getAngle());
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
        if (points == null || points.length < 4) return;
        double minY = Double.MAX_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            if (points[i] < minY) {
                minY = points[i];
            }
        }
        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * minY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }


    @Override
    public void mirrorDown() {
        if (points == null || points.length < 4) return;

        double maxY = Double.MIN_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            if (points[i] > maxY) {
                maxY = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * maxY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    @Override
    public void mirrorLeft() {
        if (points == null || points.length < 4) return;

        double minX = Double.MAX_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            if (points[i] < minX) {
                minX = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * minX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    @Override
    public void mirrorRight() {
        if (points == null || points.length < 4) return;

        double maxX = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            if (points[i] > maxX) {
                maxX = points[i];
            }
        }

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * maxX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
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
        if (points == null || points.length < 4) return;

        double minX = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        for (int i = 0; i < points.length; i += 2) {
            minX = Math.min(minX, points[i]);
            maxX = Math.max(maxX, points[i]);
        }
        double centerX = (minX + maxX) / 2.0;

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = 2 * centerX - points[i];
            } else {
                mirroredPoints[i] = points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        if (points == null || points.length < 4) return;

        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (int i = 1; i < points.length; i += 2) {
            minY = Math.min(minY, points[i]);
            maxY = Math.max(maxY, points[i]);
        }
        double centerY = (minY + maxY) / 2.0;

        Double[] mirroredPoints = new Double[points.length];
        for (int i = 0; i < points.length; i++) {
            if (i % 2 == 0) {
                mirroredPoints[i] = points[i];
            } else {
                mirroredPoints[i] = 2 * centerY - points[i];
            }
        }

        this.points = mirroredPoints;
        polygonShape.getPoints().clear();
        polygonShape.getPoints().addAll(Arrays.asList(mirroredPoints));
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

    public void setPoints(Double[] points) {
        polygonShape.getPoints().clear();
        for (Double point : points) {
            polygonShape.getPoints().add(point);
        }
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
        if (points == null || points.length < 2) return Optional.empty();

        Double[] simulated = Arrays.copyOf(points, points.length);

        switch (direction) {
            case UP: {
                double minY = Double.MAX_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    minY = Math.min(minY, simulated[i]);
                }
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * minY - simulated[i];
                }
                break;
            }
            case DOWN: {
                double maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    maxY = Math.max(maxY, simulated[i]);
                }
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * maxY - simulated[i];
                }
                break;
            }
            case LEFT: {
                double minX = Double.MAX_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    minX = Math.min(minX, simulated[i]);
                }
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * minX - simulated[i];
                }
                break;
            }
            case RIGHT: {
                double maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    maxX = Math.max(maxX, simulated[i]);
                }
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * maxX - simulated[i];
                }
                break;
            }
            case HORIZONTAL: {
                double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    minX = Math.min(minX, simulated[i]);
                    maxX = Math.max(maxX, simulated[i]);
                }
                double centerX = (minX + maxX) / 2.0;
                for (int i = 0; i < simulated.length; i += 2) {
                    simulated[i] = 2 * centerX - simulated[i];
                }
                break;
            }
            case VERTICAL: {
                double minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    minY = Math.min(minY, simulated[i]);
                    maxY = Math.max(maxY, simulated[i]);
                }
                double centerY = (minY + maxY) / 2.0;
                for (int i = 1; i < simulated.length; i += 2) {
                    simulated[i] = 2 * centerY - simulated[i];
                }
                break;
            }
        }

        // Trova il punto estremo in base alla direzione
        Point2D result = null;

        switch (direction) {
            case LEFT:
            case HORIZONTAL: {
                double minX = Double.MAX_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    if (simulated[i] < minX) {
                        minX = simulated[i];
                        result = new Point2D(simulated[i], simulated[i + 1]);
                    }
                }
                break;
            }
            case RIGHT: {
                double maxX = Double.MIN_VALUE;
                for (int i = 0; i < simulated.length; i += 2) {
                    if (simulated[i] > maxX) {
                        maxX = simulated[i];
                        result = new Point2D(simulated[i], simulated[i + 1]);
                    }
                }
                break;
            }
            case UP:
            case VERTICAL: {
                double minY = Double.MAX_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    if (simulated[i] < minY) {
                        minY = simulated[i];
                        result = new Point2D(simulated[i - 1], simulated[i]);
                    }
                }
                break;
            }
            case DOWN: {
                double maxY = Double.MIN_VALUE;
                for (int i = 1; i < simulated.length; i += 2) {
                    if (simulated[i] > maxY) {
                        maxY = simulated[i];
                        result = new Point2D(simulated[i - 1], simulated[i]);
                    }
                }
                break;
            }
        }

        return Optional.ofNullable(result);
    }




    @Override
    public void setAngle(double angle) {
        polygonShape.setRotate(angle);
    }

    @Override
    public double getAngle() {
        return polygonShape.getRotate();
    }

    @Override
    public String toString() {
        return "Polygon" + ";" + this.getStrokeColor() + ";" + this.getFillColor() + ";" +
                this.getCustomStrokeWidth() + ";" + this.getScaleX() + ";" + this.getScaleY() + ";" + Arrays.toString(this.getPoints())
                + ";" + this.getAngle() + '\n';
    }

}
