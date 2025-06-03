package org.example.model.DTO;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.factory.product.ShapeInterface;

import java.util.ArrayList;
import java.util.List;


public class DrawingContextDTO {
    private Pane drawingPane;
    private Point2D startPoint;
    private double sceneX;
    private double sceneY;
    private ShapeOption shapeOption;
    private Color borderColor;
    private Color fillColor;
    private String textContent;
    private int fontSize;
    private double selStartX, selStartY, selEndX, selEndY; //Per il rettangolo della selezione multipla
    private ScrollPane scrollPane;
    private Group mainCanvasContainer;
    private List<Point2D> polygonPoints = new ArrayList<>();
    private static final double TOLERANCE = 25.0;
    private ShapeInterface shape;                                           // utils for favorite shapes

    /**
     * Retrieves the main container for the canvas, used to group and manage graphical elements.
     *
     * @return a {@code Group} representing the main container for the canvas
     */
    public Group getMainCanvasContainer() {
        return mainCanvasContainer;
    }

    /**
     * Sets the main container for the canvas, which is used to group and manage
     * graphical elements in the drawing context.
     *
     * @param mainCanvasContainer the {@code Group} representing the main canvas container
     */
    public void setMainCanvasContainer(Group mainCanvasContainer) {
        this.mainCanvasContainer = mainCanvasContainer;
    }

    /**
     * Retrieves the scroll pane associated with the drawing context.
     *
     * @return the {@code ScrollPane} instance representing the scrollable area.
     */
    public ScrollPane getScrollPane() {
        return scrollPane;
    }

    /**
     * Sets the scroll pane for the drawing context. The scroll pane*/
    public void setScrollPane(ScrollPane scrollPane) {
        this.scrollPane = scrollPane;
    }

    /**
     * Retrieves the drawing pane, which serves as the primary container
     * for graphical elements in the drawing context.
     *
     * @return a {@code Pane} that represents the drawing area for shapes and other elements.
     */
    public Pane getDrawingPane() {
        return drawingPane;
    }

    /**
     * Sets the drawing pane for the drawing context. The drawing pane serves as the primary container
     * for graphical elements, allowing shapes and other elements to be rendered and manipulated.
     *
     * @param drawingPane the {@code Pane} instance to be*/
    public void setDrawingPane(Pane drawingPane) {
        this.drawingPane = drawingPane;
    }

    /**
     */
    public Point2D getStartPoint() {
        return startPoint;
    }


    /**
     * Sets the starting point for a drawing operation or graphical element.
     *
     * @param startPoint the {@code Point2D} instance representing the coordinates
     *                   of the starting point
     */
    public void setStartPoint(Point2D startPoint) {
        this.startPoint = startPoint;
    }

    /**
     * Retrieves the X-coordinate of the scene associated with this context.
     *
     * @return the X-coordinate of the scene as a double.
     */
    public double getSceneX() {
        return sceneX;
    }

    /**
     * Sets the X-coordinate of the scene associated with this context.
     *
     * @param sceneX the X-coordinate of the scene as a double
     */
    public void setSceneX(double sceneX) {
        this.sceneX = sceneX;
    }

    /**
     * Retrieves the Y-coordinate of the scene associated with this context.
     *
     * @return the Y-coordinate of the scene as a double.
     */
    public double getSceneY() {
        return sceneY;
    }

    /**
     * Sets the Y-coordinate of the scene associated with this context.
     */
    public void setSceneY(double sceneY) {
        this.sceneY = sceneY;
    }

    /**
     * Retrieves the selected shape option in the drawing context.
     *
     * @return a {@code ShapeOption} representing the current shape option
     *         or selection used in the drawing context.
     */
    public ShapeOption getShapeOption() {
        return shapeOption;
    }

    /**
     *
     */
    public void setShapeOption(ShapeOption shapeOption) {
        this.shapeOption = shapeOption;
    }

    /**
     * Retrieves the border color used in the drawing context.
     *
     * @return a {@code Color} representing the current border color
     */
    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the border color for the drawing context. The border color is
     * used to render graphical elements' borders in the drawing pane.
     *
     * @param borderColor the {@code Color} representing the new border color to apply
     */
    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    /**
     * Retrieves the fill color used in the drawing context.
     *
     * @return a {@code Color} representing the current fill color
     */
    public Color getFillColor() {
        return fillColor;
    }

    /**
     * Sets the fill color for graphical elements in the drawing context.
     *
     * @param fillColor the {@code Color} representing the new fill color to apply
     */
    public void setFillColor(Color fillColor) {
        this.fillColor = fillColor;
    }

    /**
     * Retrieves the text content associated with this drawing context.
     *
     * @return a {@code String} representing the text content.
     */
    public String getText() {
        return textContent;
    }

    /**
     * Sets the textual content associated with this drawing context.
     *
     * @param textContent the {@code String} representing the new text content to be set
     */
    public void setTextContent(String textContent) {
        this.textContent = textContent;
    }

    /**
     * Sets the text content associated with this drawing context.
     *
     * @param textContent the String representing the new text content to be set
     */
    public void setText(String textContent){
        this.textContent = textContent;
    }

    /**
     * Retrieves the font size associated with the drawing context.
     *
     * @return an integer representing the font size.
     */
    public int getFontSize() {
        return fontSize;
    }

    /**
     * Sets the font size within the drawing context.
     *
     * @param fontSize the integer value representing the font size to be applied
     */
    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    //Metodi del rettangolo della selezione multipla

    /**
     * Retrieves the X-coordinate of the start of the selection in the drawing context.
     *
     * @return the X-coordinate of the selection start as a double
     */
    public double getSelStartX() { return selStartX; }

    /**
     * Sets the X-coordinate for the start of the selection in the drawing context.
     *
     * @param selStartX the X-coordinate of the selection start as a double
     */
    public void setSelStartX(double selStartX) { this.selStartX = selStartX; }

    /**
     * Retrieves the Y-coordinate of the start of the selection in the drawing context.
     *
     * @return the Y-coordinate of the selection start as a double
     */
    public double getSelStartY() { return selStartY; }

    /**
     * Sets the Y-coordinate for the start of the selection in the drawing context.
     *
     * @param selStartY the Y-coordinate of the selection start as a double
     */
    public void setSelStartY(double selStartY) { this.selStartY = selStartY; }

    /**
     * Retrieves the X-coordinate of the end of the selection in the drawing context.
     *
     * @return the*/
    public double getSelEndX() { return selEndX; }

    /**
     * Sets the X-coordinate for the end of the selection in the drawing context.
     *
     * @param selEndX the X-coordinate of the selection end as a double
     */
    public void setSelEndX(double selEndX) { this.selEndX = selEndX; }

    /**
     * Retrieves the Y-coordinate of the end of the selection in the drawing context.
     *
     * @return the Y-coordinate of the selection end as a double
     */
    public double getSelEndY() { return selEndY; }

    /**
     * Sets the Y-coordinate for the end of the selection in the drawing context.
     *
     * @param selEndY the Y-coordinate of the selection end as a double
     */
    public void setSelEndY(double selEndY) { this.selEndY = selEndY; }

    /**
     * Retrieves the current shape being used in the drawing context.
     *
     * @return a {@code ShapeInterface} instance representing the current shape.
     */
    public ShapeInterface getShape() {
        return shape;
    }

    /**
     * Sets the shape for the current drawing context. The shape is used to define
     * the graphical element being manipulated or rendered.
     *
     * @param shape the {@code ShapeInterface} instance representing the new shape
     *              to be set in the drawing context
     */
    public void setShape(ShapeInterface shape) {
        this.shape = shape;
    }

    // Polygon methods

    /**
     * Retrieves the list of points that define a polygon in the drawing context.
     *
     * @return a list of {@code Point2D} objects representing the vertices of the polygon.
     */
    public List<Point2D> getPolygonPoints() {
        return polygonPoints;
    }

    /**
     * Adds a new point to the list of points defining a polygon in the drawing context.
     *
     */
    public void setPolygonPoint(Point2D point) {
        polygonPoints.add(point);
    }

    /**
     * Resets the list of points defining a polygon in the drawing context.
     *
     * This method clears all {@code Point2D} objects from the {@code polygonPoints} list,
     * effectively removing any existing vertices of the polygon.
     */
    public void resetPolygonPoints() { polygonPoints.clear(); }

    /**
     * Determines whether a polygon can be created based on the current points.
     * This method checks if the list of points defining the polygon contains at least
     * four points, and if the first and last points are approximately equal within a
     * specified tolerance.
     *
     * @return {@code true} if the polygon can be created (i.e., the polygon points are
     * not null, have a size of at least four, and the first and last points are approximately
     * equal); otherwise {@code false}.
     */
    public boolean canCreatePolygon() {
        Point2D firstPoint = polygonPoints.get(0);
        Point2D lastPoint = polygonPoints.get(polygonPoints.size() - 1);
        // Check if points are approximately equal within tolerance
        boolean isApproxEqual = Math.abs(firstPoint.getX() - lastPoint.getX()) <= TOLERANCE &&
                Math.abs(firstPoint.getY() - lastPoint.getY()) <= TOLERANCE;

        return polygonPoints != null &&  polygonPoints.size() >= 4 && isApproxEqual;
    }

    /**
     * Clears all points that define a polygon in the drawing context.
     *
     * This method removes all {@code Point2D} objects from the {@code polygonPoints} list,
     * effectively resetting the collection of vertices for the polygon.
     */
    public void clearPolygonPoints() { polygonPoints.clear(); }
}
