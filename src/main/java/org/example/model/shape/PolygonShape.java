package org.example.model.shape;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.paint.Color;

import java.util.Optional;

public class PolygonShape implements ShapeInterface {
    @Override
    public ShapeInterface cloneShape() {
        return null;
    }

    @Override
    public void setStrokeColor(Color color) {

    }

    @Override
    public boolean canHaveFill() {
        return false;
    }

    @Override
    public Color getStrokeColor() {
        return null;
    }

    @Override
    public void setCustomStrokeWidth(double strokeWidth) {

    }

    @Override
    public double getCustomStrokeWidth() {
        return 0;
    }

    @Override
    public void setFillColor(Color fill) {

    }

    @Override
    public Color getFillColor() {
        return null;
    }

    @Override
    public double getWidth() {
        return 0;
    }

    @Override
    public double getHeight() {
        return 0;
    }

    @Override
    public void resize(double newSize) {

    }

    @Override
    public void setSize(double width, double height) {

    }

    @Override
    public double getStretchWidth() {
        return 0;
    }

    @Override
    public double getStretchHeight() {
        return 0;
    }

    @Override
    public void stretch(double width, double height) {

    }

    @Override
    public void resetStretch(double width, double height) {

    }

    @Override
    public double getEffectiveSize() {
        return 0;
    }

    @Override
    public double getX() {
        return 0;
    }

    @Override
    public double getY() {
        return 0;
    }

    @Override
    public void setX(double X) {

    }

    @Override
    public void setY(double Y) {

    }

    @Override
    public Double[] getPoints() {
        return new Double[0];
    }

    @Override
    public void draw(double X, double Y) {

    }

    @Override
    public void draw(Double[] points) {

    }

    @Override
    public Node getNode() {
        return null;
    }

    @Override
    public void moveBy(double deltaX, double deltaY) {

    }

    @Override
    public String getText() {
        return "";
    }

    @Override
    public void setText(String content) {

    }

    @Override
    public boolean canMoveBy(double deltaX, double deltaY, double paneWidth, double paneHeight, double zoom, double margin) {
        return false;
    }

    @Override
    public void mirrorUp() {

    }

    @Override
    public void mirrorDown() {

    }

    @Override
    public void mirrorLeft() {

    }

    @Override
    public void mirrorRight() {

    }

    @Override
    public void setTextScaleX(double scale) {

    }

    @Override
    public double getTextScaleX() {
        return 0;
    }

    @Override
    public void setTextScaleY(double scale) {

    }

    @Override
    public double getTextScaleY() {
        return 0;
    }

    @Override
    public void mirrorHorizontallyAroundCenter() {

    }

    @Override
    public void mirrorVerticallyAroundCenter() {

    }

    @Override
    public Optional<Point2D> simulateMirror(MirrorDirectionEnum direction) {
        return Optional.empty();
    }
}
