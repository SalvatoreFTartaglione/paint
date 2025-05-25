package org.example.model.receivers;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.shape.*;

public class AddToCanvas {

    private final DrawingContextDTO drawingDTO;

    public AddToCanvas(DrawingContextDTO drawingDTO) {
        this.drawingDTO = drawingDTO;
    }

    public void add() {
        ShapeOption selectedShape = drawingDTO.getShapeOption();

        switch (selectedShape.getName()) {
            /*case "Text" : {
                // coordinate locali del clic sul Canvas
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                double x = localCoords.getX();
                double y = localCoords.getY();

                // Crea il TextShape
                AbstractShape abstractText = createText(drawingDTO.getBorderColor(), 0, 20, 0); // 20 = font size
                ShapeInterface shapeInterface = abstractText.getShape();
                TextShape textShape = (TextShape) shapeInterface;

                // Imposta il contenuto del testo se disponibile
                if (drawingDTO.getTextContent() != null && !drawingDTO.getTextContent().isBlank()) {
                    textShape.setText(drawingDTO.getTextContent());
                }

                // Posiziona la shape
                textShape.draw(x, y);
                textShape.getNode().setUserData(shapeInterface);
                drawingDTO.getDrawingPane().getChildren().add(textShape.getNode());
                break;
            }
*/
            case "Ellipse": {
                Point2D sceneCoords = new Point2D(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(sceneCoords);

                double x = localCoords.getX();
                double y = localCoords.getY();

                AbstractShape abstractEllipse = createEllipse(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40);
                ShapeInterface shapeInterface = abstractEllipse.getShape();
                EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                ellipseShape.draw(x, y);
                ellipseShape.getNode().setUserData(shapeInterface);
                drawingDTO.getDrawingPane().getChildren().add(ellipseShape.getNode());
                break;
            }
            case "Line": {
                Point2D sceneCoords = new Point2D(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(sceneCoords);
                double x = localCoords.getX();
                double y = localCoords.getY();
                if (drawingDTO.getStartPoint() == null) {
                    drawingDTO.setStartPoint(localCoords);
                } else {
                    LineCreator lineCreator = new LineCreator();
                    lineCreator.creationOfShape(drawingDTO.getBorderColor(), null, 5, 0, 0);
                    ShapeInterface shapeInterface = lineCreator.getShape();
                    Double[] points = {x, y, drawingDTO.getStartPoint().getX(), drawingDTO.getStartPoint().getY()};
                    LineShape lineShape = (LineShape) shapeInterface;
                    lineShape.draw(points);
                    lineShape.getNode().setUserData(shapeInterface);
                    drawingDTO.getDrawingPane().getChildren().add(lineShape.getNode());
                    drawingDTO.setStartPoint(null);
                }
                break;
            }
            case "Rectangle": {
                Point2D sceneCoords = new Point2D(drawingDTO.getSceneX(), drawingDTO.getSceneY());
                Point2D localCoords = drawingDTO.getDrawingPane().sceneToLocal(sceneCoords);
                double x = localCoords.getX();
                double y = localCoords.getY();
                AbstractShape abstractRectangle = createRectangle(drawingDTO.getBorderColor(), drawingDTO.getFillColor(), 4, 80, 40);
                ShapeInterface shapeInterface = abstractRectangle.getShape();
                RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                rectangleShape.draw(x, y);
                rectangleShape.getNode().setUserData(shapeInterface);
                drawingDTO.getDrawingPane().getChildren().add(rectangleShape.getNode());
                break;
            }
        }
    }

    public void addClonedShape(ShapeInterface shape) {
        shape.getNode().setUserData(shape); // Serve per il context menu
        drawingDTO.getDrawingPane().getChildren().add(shape.getNode()); // Aggiunge la shape al canvas
    }

    private static AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return rectangle;
    }

    private static AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return ellipse;
    }

    /*private static AbstractShape createText(Color strokeColor, int borderWidth, double width, double height) {
        AbstractShape text = new TextShapeCreator();
        text.creationOfShape(strokeColor,null, borderWidth, width, height);
        return text;
    }*/
}
