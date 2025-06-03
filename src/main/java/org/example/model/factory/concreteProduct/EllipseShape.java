package org.example.model.factory.concreteProduct;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;

import java.util.Optional;


public class EllipseShape implements ShapeInterface {

    private Ellipse ellipseShape;

    public EllipseShape(Color stroke, Color fill, double strokeWidth, double width, double height, double angle) {
        this.ellipseShape = new Ellipse();
        ellipseShape.setStroke(stroke);
        ellipseShape.setFill(fill);
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
        ellipseShape.setStrokeWidth(strokeWidth);
        ellipseShape.setRotate(angle);
    }

    public void draw(double x, double y) {
        ellipseShape.setCenterX(x);
        ellipseShape.setCenterY(y);
    }

    @Override
    public boolean canHaveFill() {
        return true;
    }

    @Override
    public void draw(Double[] points) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public ShapeInterface cloneShape() {
        EllipseShape clone = new EllipseShape(
                this.getStrokeColor(),
                this.getFillColor(),
                this.getCustomStrokeWidth(),
                this.getWidth(),
                this.getHeight(),
                this.getAngle()
        );

        clone.setX(this.getX());
        clone.setY(this.getY());
        return clone;
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {
        ellipseShape.setCenterX(ellipseShape.getCenterX() + deltaX);
        ellipseShape.setCenterY(ellipseShape.getCenterY() + deltaY);
    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        double newX = getX() + deltaX;
        double newY = getY() + deltaY;

        double minX = (getWidth() / 2 + margin) / zoom;
        double maxX = (paneWidth - getWidth() / 2 - margin) / zoom;
        double minY = (getHeight() / 2 + margin) / zoom;
        double maxY = (paneHeight - getHeight() / 2 - margin) / zoom;

        return newX >= minX && newX <= maxX && newY >= minY && newY <= maxY;
    }

    @Override
    public void mirrorUp() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointYUpFromCenterOfEllipse = new Point2D(centerOfEllipse.getX(), centerOfEllipse.getY() - (getHeight()));

        double distance = centerOfEllipse.getY() - pointYUpFromCenterOfEllipse.getY();
        double mirroredY = pointYUpFromCenterOfEllipse.getY() - distance;

        ellipseShape.setCenterY(mirroredY);
    }

    @Override
    public void mirrorDown() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointXDownFromCenterOfEllipse = new Point2D(centerOfEllipse.getX(), centerOfEllipse.getY() + (getHeight()));

        double distance = pointXDownFromCenterOfEllipse.getY() - centerOfEllipse.getY();
        double mirroredY = pointXDownFromCenterOfEllipse.getY() + distance;

        ellipseShape.setCenterY(mirroredY);
    }

    @Override
    public void mirrorLeft() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointLeft = new Point2D(centerOfEllipse.getX() - getWidth(), centerOfEllipse.getY());

        double distance = centerOfEllipse.getX() - pointLeft.getX();
        double mirroredX = pointLeft.getX() - distance;

        ellipseShape.setCenterX(mirroredX);
    }

    @Override
    public void mirrorRight() {
        Point2D centerOfEllipse = new Point2D(getX(), getY());
        Point2D pointRight = new Point2D(centerOfEllipse.getX() + getWidth(), centerOfEllipse.getY());

        double distance = pointRight.getX() - centerOfEllipse.getX();
        double mirroredX = pointRight.getX() + distance;

        ellipseShape.setCenterX(mirroredX);
    }

    @Override
    public void setScaleX(double scale) {
        ellipseShape.setScaleX(scale);
    }

    @Override
    public double getScaleX() {
        return ellipseShape.getScaleX();
    }

    @Override
    public void setScaleY(double scale) {
        ellipseShape.setScaleY(scale);
    }

    @Override
    public double getScaleY() {
        return ellipseShape.getScaleY();
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {
        ellipseShape.setScaleX(ellipseShape.getScaleX() * -1);
    }

    @Override
    public void mirrorVerticallyAroundCenter() {
        ellipseShape.setScaleY(ellipseShape.getScaleY() * -1);
    }

    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        double centerX = getX();
        double centerY = getY();
        double mirroredX = centerX;
        double mirroredY = centerY;

        switch (direction) {
            case UP:
                mirroredY = centerY - 2 * getHeight();
                break;

            case DOWN:
                mirroredY = centerY + 2 * getHeight();
                break;

            case LEFT:
                mirroredX = centerX - 2 * getWidth();
                break;

            case RIGHT:
                mirroredX = centerX + 2 * getWidth();
                break;

            case HORIZONTAL:
            case VERTICAL:
                return Optional.of(new Point2D(centerX, centerY));
        }

        return Optional.of(new Point2D(mirroredX, mirroredY));

    }


    @Override
    public Color getStrokeColor() {
       return (Color) ellipseShape.getStroke();
    }

    @Override
    public void setStrokeColor(Color stroke) {
        ellipseShape.setStroke(stroke);
    }

    @Override
    public Color getFillColor() {
        return (Color) ellipseShape.getFill();
    }

    @Override
    public void setFillColor(Color fill){
        ellipseShape.setFill(fill);
    }

    @Override
    public double getX() {
        return ellipseShape.getCenterX();
    }

    @Override
    public void setX(double x) {
       ellipseShape.setCenterX(x);
    }

    @Override
    public double getY() {
        return ellipseShape.getCenterY();
    }

    @Override
    public void setY(double y) {
        ellipseShape.setCenterY(y);
    }

    @Override
    public double getWidth() {
        return ellipseShape.getRadiusX();
    }

    @Override
    public double getHeight() {
        return ellipseShape.getRadiusY();
    }

    @Override
    public void setSize(double width, double height){
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
    }

    @Override
    public void resize(double newSize) {
        // Mantieni l'aspect ratio dell'ellisse
        double currentMax = Math.max(getWidth(), getHeight());
        if (currentMax > 0) {
            double scale = newSize / currentMax;
            setSize(getWidth() * scale, getHeight() * scale);
        } else {
            setSize(newSize, newSize);
        }
    }

    @Override
    public double getStretchWidth() {
        return this.getWidth();
    }

    @Override
    public double getStretchHeight() {
        return this.getHeight();
    }

    @Override
    public void stretch(double width, double height){
        ellipseShape.setRadiusX(width);
        ellipseShape.setRadiusY(height);
    }

    @Override
    public void resetStretch(double width, double height) {
        this.setSize(width, height);
    }


    @Override
    public double getCustomStrokeWidth() {
        return ellipseShape.getStrokeWidth();
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {
        ellipseShape.setStrokeWidth(strokeWidth);
    }

    @Override
    public Double[] getPoints(){
        return null;
    }

    @Override
    public Node getNode() {
        return this.ellipseShape;
    }

    @Override
    public double getEffectiveSize() {
        // Per i cerchi/ellissi, usa il diametro maggiore
        return Math.max(getWidth(), getHeight());
    }

    @Override
    public String getSliderLabel(double value) {
        return String.format("Diameter: %.1f", value);
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void setText(String content) {
        throw new UnsupportedOperationException("Feature not supported.");
    }

    @Override
    public void setAngle(double angle) {
        ellipseShape.setRotate(angle);
    }

    @Override
    public double getAngle() {
        return ellipseShape.getRotate();
    }

    @Override
    public String toString() {
        return "Ellipse" + ";" + ellipseShape.getStroke() + ";" + ellipseShape.getFill() + ";" + ellipseShape.getStrokeWidth() + ";"
                + ellipseShape.getRadiusX() + ";" + ellipseShape.getRadiusY() + ";" + ellipseShape.getCenterX() + ";" +
                ellipseShape.getCenterY() + ";" + this.getAngle() + "\n";
    }
}
