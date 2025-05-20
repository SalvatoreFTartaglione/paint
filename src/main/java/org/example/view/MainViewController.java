package org.example.view;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.controller.ShapeOption;
import org.example.model.commands.*;
import org.example.model.shape.*;

public class MainViewController implements Initializable {

    @FXML
    private CheckBox gridButton;
    @FXML
    private Pane drawingPane;
    @FXML
    private SplitPane splitPane;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab tabPaneDraw;
    @FXML
    private Button undoButton;
    @FXML
    private ComboBox<ShapeOption> shapeComboBox;
    @FXML
    private ImageView genericShapePhoto;
    @FXML
    private TextArea textArea;
    @FXML
    private ImageView genericTextPhoto;
    @FXML
    private Button enterTextButton;
    @FXML
    private ColorPicker fillColorPicker;
    @FXML
    private StackPane currentShape;
    @FXML
    private ImageView chooseColorPhoto;
    @FXML
    private ColorPicker borderColorPicker;
    @FXML
    private Tab tabPaneFile;
    @FXML
    private Button saveButton;
    @FXML
    private Button loadButton;
    @FXML
    private AnchorPane canvasPane;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane mainCanvasContainer;
    @FXML
    private ContextMenu scrollPaneContextMenu;

    private boolean isDragging = false;
    private Point2D startPoint = null;

    private ShapeInterface selectedShapeForContextMenu = null;
    private ContextMenu contextMenu = new ContextMenu();
    private UndoManager undoManager;

    /**
     * Initializes the controller after the root element has been completely processed.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeSplitPane();
        initializeShapeChoiceBox();
        initializeContextMenu();


        mainCanvasContainer.setOnMousePressed(event -> {
            isDragging = false;
        });

        mainCanvasContainer.setOnMouseDragged(event -> {
            isDragging = true;
        });

        mainCanvasContainer.setOnMouseReleased(event -> {
            if (!isDragging && event.getButton() == MouseButton.PRIMARY) {
                DrawOnCanvas(event);
            }
        });
    }

    /**
     * Sets the undo manager for this controller.
     *
     * @param undoManager the {@code UndoManager} to associate with the canvas
     */
    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
    }



    private void initializeContextMenu() {

        // Fill Color
        Label fillLabel = new Label("Change Fill Color:");
        ColorPicker changeFillColorPicker = new ColorPicker();
        HBox fillBox = new HBox(10, fillLabel, changeFillColorPicker);
        CustomMenuItem fillColorItem = new CustomMenuItem(fillBox);
        fillColorItem.setHideOnClick(false);

        // Stroke Color
        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPicker = new ColorPicker();
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPicker);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        contextMenu.getItems().addAll(fillColorItem, strokeColorItem);

        // Cambia colori al volo se c'è una shape selezionata
        changeFillColorPicker.setOnAction(event -> {
            if (selectedShapeForContextMenu != null) {
                Command cmd = new ChangeFillColorCommand(selectedShapeForContextMenu, changeFillColorPicker.getValue());
                undoManager.executeCommand(cmd);
            }
        });

        changeStrokeColorPicker.setOnAction(event -> {
            if (selectedShapeForContextMenu != null) {
                Command cmd = new ChangeStrokeColorCommand(selectedShapeForContextMenu, changeStrokeColorPicker.getValue());
                undoManager.executeCommand(cmd);
            }
        });

        // Mostra il menu su clic destro dentro `drawingPane`
        drawingPane.setOnMousePressed(event -> {
            contextMenu.getItems().clear();

            if (event.isSecondaryButtonDown()) {
                Node clickedShape = findShapeAt(event.getX(), event.getY());
                if (clickedShape instanceof RectangleShape || clickedShape instanceof EllipseShape) {
                    selectedShapeForContextMenu = (ShapeInterface) clickedShape;

                    // Imposta i color picker con i colori attuali della shape selezionata
                    changeFillColorPicker.setValue(selectedShapeForContextMenu.getFillColor());
                    changeStrokeColorPicker.setValue(selectedShapeForContextMenu.getStrokeColor());
                    contextMenu.getItems().addAll(fillColorItem, strokeColorItem);
                    contextMenu.show(drawingPane, event.getScreenX(), event.getScreenY());
                }
                if(clickedShape instanceof LineShape){
                    selectedShapeForContextMenu = (ShapeInterface) clickedShape;
                    changeStrokeColorPicker.setValue(selectedShapeForContextMenu.getStrokeColor());
                    contextMenu.getItems().add(strokeColorItem);
                    contextMenu.show(drawingPane, event.getScreenX(), event.getScreenY());
                }
                if(clickedShape == null){
                    selectedShapeForContextMenu = null;
                    contextMenu.hide();
                }
            }
        });

        // Impedisci la chiusura automatica quando il ColorPicker si apre
        changeFillColorPicker.setOnShowing(e -> contextMenu.setAutoHide(false));
        changeFillColorPicker.setOnHiding(e -> contextMenu.setAutoHide(true));

        changeStrokeColorPicker.setOnShowing(e -> contextMenu.setAutoHide(false));
        changeStrokeColorPicker.setOnHiding(e -> contextMenu.setAutoHide(true));

    }

    private Node findShapeAt(double x, double y) {
        for (Node node : drawingPane.getChildren()) {
            if (node instanceof ShapeInterface) {
                if (node instanceof LineShape) {
                    LineShape line = (LineShape) node;
                    if (isPointNearLine(x, y, line)) {
                        return line;
                    }
                } else {
                    ShapeInterface shape = (ShapeInterface) node;
                    if (Math.abs(shape.getX() - x) <= 40 && Math.abs(shape.getY() - y) <= 80) {
                        return node;
                    }
                }
            }
        }
        return null;
    }

    private boolean isPointNearLine(double x, double y, LineShape line) {
        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();

        double distance = pointToSegmentDistance(x, y, x1, y1, x2, y2);
        return distance <= 5;  // tolleranza clic
    }

    private double pointToSegmentDistance(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0) {
            return Math.hypot(px - x1, py - y1);
        }

        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }


    /*private void drawGrid() {
        double width = gridCanvas.getWidth();
        double height = gridCanvas.getHeight();
        double cellSize = 50; // dimensione delle celle

        var gc = gridCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height); // pulizia

        gc.setLineWidth(0.5);
        gc.setStroke(javafx.scene.paint.Color.GRAY); // colore griglia
        // linee verticali
        for (double x = 0; x <= width; x += cellSize) {
            gc.strokeLine(x, 0, x, height);
        }
        // linee orizzontali
        for (double y = 0; y <= height; y += cellSize) {
            gc.strokeLine(0, y, width, y);
        }
    }*/

    private void initializeShapeChoiceBox() {
        List<ShapeOption> shapes = List.of(
                new ShapeOption("Ellipse", "/org/example/images/ellipse.png"),
                new ShapeOption("Line", "/org/example/images/segment.png"),
                new ShapeOption("Rectangle", "/org/example/images/rectangle.png")
        );

        shapeComboBox.getItems().addAll(shapes);

        // Imposto la cell factory per mostrare nome + immagine
        shapeComboBox.setCellFactory(lv -> new ListCell<>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(ShapeOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    imageView.setImage(item.getImage());
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    setGraphic(imageView);
                }
            }
        });

        // Anche il bottone principale della ChoiceBox va personalizzato
        shapeComboBox.setButtonCell(new ListCell<ShapeOption>() {
            private final ImageView imageView = new ImageView();

            @Override
            protected void updateItem(ShapeOption item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText(item.getName());
                    imageView.setImage(item.getImage());
                    imageView.setFitHeight(20);
                    imageView.setFitWidth(20);
                    setGraphic(imageView);
                }
            }
        });

        shapeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (oldVal != null && newVal != null && !newVal.equals(oldVal)) {
                // Resetto lo stato del disegno parziale (es. punto iniziale della linea)
                startPoint = null;
            }
        });

    }



    private void initializeSplitPane() {
        Platform.runLater(() -> {
            // Cerca il divider DOPO che la scena è stata caricata
            Node divider = splitPane.lookupAll(".split-pane-divider")
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (divider != null) {
                divider.setMouseTransparent(true);  // Impedisce il trascinamento
                divider.setFocusTraversable(false); // Impedisce la selezione
            }

            // Imposta posizione iniziale
            splitPane.setDividerPositions(0.3);

            // Mantieni posizione anche al resize
            splitPane.heightProperty().addListener((obs, oldVal, newVal) -> {
                splitPane.setDividerPositions(0.3);
            });
        });
    }
/*

    private void makeDraggable(Node shape) {
        final Point2D[] dragDelta = new Point2D[1];

        shape.setOnMousePressed(event -> {
            dragDelta[0] = new Point2D(event.getSceneX(), event.getSceneY());
            event.consume();
        });

        shape.setOnMouseDragged(event -> {
            // Convert scene coords to local coords
            Parent parent = shape.getParent();
            if (parent == null) return;

            Point2D currentScene = new Point2D(event.getSceneX(), event.getSceneY());
            Point2D currentLocal = parent.sceneToLocal(currentScene);
            Point2D previousLocal = parent.sceneToLocal(dragDelta[0]);

            double deltaX = currentLocal.getX() - previousLocal.getX();
            double deltaY = currentLocal.getY() - previousLocal.getY();

            if (shape instanceof LineShape) {
                LineShape line = (LineShape) shape;
                line.setStartX(line.getStartX() + deltaX);
                line.setStartY(line.getStartY() + deltaY);
                line.setEndX(line.getEndX() + deltaX);
                line.setEndY(line.getEndY() + deltaY);
            } else {
                shape.setLayoutX(shape.getLayoutX() + deltaX);
                shape.setLayoutY(shape.getLayoutY() + deltaY);
            }

            dragDelta[0] = currentScene;
            event.consume();
        });
    }

*/


    /**
     * Handles the primary mouse click to draw the selected shape on the canvas.
     * Supports Ellipse, Line, and Rectangle.
     *
     * @param mouseEvent the mouse event containing click coordinates
     */
    public void DrawOnCanvas(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ShapeOption selectedShape = shapeComboBox.getValue();
            if (!contextMenu.isShowing()) {
                if (selectedShape != null) {
                    switch (selectedShape.getName()) {
                        case "Ellipse": {
                            // coordinate locali del clic sul Canvas
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            AbstractShape abstractEllipse = createEllipse(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                            ShapeInterface shapeInterface = abstractEllipse.getShape();
                            //controllo che la shape selezionata sia un'ellisse
                            if (shapeInterface instanceof EllipseShape) {
                                EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                                ellipseShape.draw(x, y);
                                // Aggiungi l'ellisse al contenitore del Canvas
                                Command cmd = new AddShapeCommand(drawingPane, ellipseShape);
                                undoManager.executeCommand(cmd);
                                //drawingPane.getChildren().add(ellipseShape);
                                //shapeComboBox.setValue(null);
                                //makeDraggable(ellipseShape);
                            }
                            break;
                        }
                        case "Line": {
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            if (startPoint == null) {
                                // first click: save starting point
                                startPoint = new Point2D(x, y);
                            } else {
                                // second click: save end point
                                LineCreator lineCreator = new LineCreator();
                                lineCreator.creationOfShape(borderColorPicker.getValue(), null, 5, 0, 0);
                                // Add line to container
                                ShapeInterface shapeInterface = lineCreator.getShape();
                                Double[] points = {x, y, startPoint.getX(), startPoint.getY()};
                                if (shapeInterface instanceof LineShape) {
                                    LineShape lineShape = (LineShape) shapeInterface;
                                    lineShape.draw(points);
                                    Command cmd = new AddShapeCommand(drawingPane, lineShape);
                                    undoManager.executeCommand(cmd);
                                    //drawingPane.getChildren().add(lineShape);
                                    //shapeComboBox.setValue(null);
                                    //makeDraggable(lineShape);
                                }
                                // Reset state variable
                                startPoint = null;

                            }
                            break;
                        }
                        case "Rectangle": {
                            // coordinate locali del clic sul Canvas
                            Point2D localCoords = drawingPane.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                            double x = localCoords.getX();
                            double y = localCoords.getY();
                            AbstractShape abstractRectangle = createRectangle(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                            ShapeInterface shapeInterface = abstractRectangle.getShape();
                            if (shapeInterface instanceof RectangleShape) {
                                RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                                rectangleShape.draw(x, y);
                                Command cmd = new AddShapeCommand(drawingPane, rectangleShape);
                                undoManager.executeCommand(cmd);
                                //drawingPane.getChildren().add(rectangleShape);
                                //shapeComboBox.setValue(null);
                                //makeDraggable(rectangleShape);
                            }
                            break;
                        }
                    }

                }
            }else {
                contextMenu.hide();
            }
        }
    }

    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return rectangle;
    }

    private AbstractShape createLine(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape line = new LineCreator();
        line.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return line;
    }

    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return ellipse;
    }

    public void onRightClick(MouseEvent mouseEvent) {
    }

    public void check(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.SECONDARY) {
            System.out.println(drawingPane.getChildren().size());
        }
    }

    /**
     * @param event
     * Calls the function to save the file
     */
    @FXML
    private void saveFile(ActionEvent event) {
        Save saveFile = new Save(drawingPane);
        saveFile.executeCommand(null);
    }



    /**
     * @param event
     * Calls the function to load a file
     */
    @FXML
    private void loadFile(ActionEvent event) {
        // Check if canvas is empty
        if ( ! drawingPane.getChildren().isEmpty() ) {
            // Confirmation alert
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Save Workspace");
            alert.setHeaderText("Canvas is not empty.");
            alert.setContentText("Save current workspace?");
            ButtonType saveButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType dontSaveButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(saveButton, dontSaveButton);
            Optional<ButtonType> result = alert.showAndWait();

            if (result.isPresent()) {
                if (result.get().getText().equals("Yes")) {
                    // Saving file
                    Save saveFile = new Save(drawingPane);
                    saveFile.executeCommand(null);
                    // Clear workspace from older shapes
                    drawingPane.getChildren().clear();
                    // After saving execute load
                    Load loadFile = new Load(drawingPane);
                    loadFile.executeCommand(null);

                } else if (result.get().getText().equals("No")) {
                    // Clear workspace from older shapes
                    drawingPane.getChildren().clear();
                    // Execute load
                    Load loadFile = new Load(drawingPane);
                    loadFile.executeCommand(null);
                }
            }
        }
        else {
            // Clear workspace from older shapes
            drawingPane.getChildren().clear();
            // Execute load
            Load loadFile = new Load(drawingPane);
            loadFile.executeCommand(null);
        }
    }

    /**
     * Handles the Undo button action to undo the last executed command.
     *
     * @param actionEvent the action event triggered by the undo button
     */
    @FXML
    public void undoClicked(ActionEvent actionEvent) {
        undoManager.undo();
    }


    /*@FXML
    private void onGridCheckBoxToggle(ActionEvent event) {
        if (gridButton.isSelected()) {
            drawGrid();
            gridCanvas.setVisible(true);
            gridCanvas.setDisable(false);
            gridOnLabel.setVisible(true);
            gridOffLabel.setVisible(false);
        } else {
            gridCanvas.setVisible(false);
            gridCanvas.setDisable(true);
            gridOnLabel.setVisible(false);
            gridOffLabel.setVisible(true);
        }
    }*/



}

