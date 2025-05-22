package org.example.view;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.*;
import org.example.model.shape.*;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

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
    private UndoManager undoManager;
    private ContextMenu contextMenu = new ContextMenu();
    private Point2D localStartPoint = null;
    private DrawingContextDTO dto = new DrawingContextDTO();

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

        mainCanvasContainer.setOnContextMenuRequested(event -> {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            }
            double x = event.getX();
            double y = event.getY();

            for (Node node : drawingPane.getChildren()) {
                Object userData = node.getUserData();
                if (userData instanceof ShapeInterface) {
                    ShapeInterface shape = (ShapeInterface) userData; // cast esplicito
                    Node found = shape.findShapeAt(x, y);
                    if (found != null) {
                        selectedShapeForContextMenu = shape;
                        System.out.println("Cerco di aprire il context menu");
                        contextMenu = shape.getContextMenu(undoManager, shape.getFillColor(), shape.getStrokeColor(),shape);
                        contextMenu.show(mainCanvasContainer, event.getScreenX(), event.getScreenY());
                        break;
                    }
                }
            }
            System.out.println(drawingPane.getChildren().size());
            for (Node node : drawingPane.getChildren()) {
                ShapeInterface shape = (ShapeInterface) node.getUserData();
                System.out.println(shape.toString());
            }
        });

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

    public void setLocalStartPoint(Point2D localStartPoint) {
        this.localStartPoint = localStartPoint;
    }

    public Point2D getLocalStartPoint() {
        return localStartPoint;
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
        if (shapeComboBox.getValue() != null) {
            setParameters(mouseEvent, contextMenu, shapeComboBox, drawingPane, dto.getStartPoint(), borderColorPicker, fillColorPicker);
            AddShape addShape = new AddShape(dto);
            undoManager.executeCommand(addShape);
        }
        //shapeComboBox.setValue(null);
        //MAKE DRAGGABLE CLASSS
        //startPoint = null;
        //undoManager.executeCommand(cmd);
    }

    private void setParameters(MouseEvent mouseEvent, ContextMenu contextMenu, ComboBox<ShapeOption> shapeComboBox, Pane drawingPane, Point2D startPoint, ColorPicker borderColorPicker, ColorPicker fillColorPicker) {
        dto.setContextMenu(contextMenu);
        dto.setMouseEvent(mouseEvent);
        dto.setShapeComboBox(shapeComboBox);
        dto.setDrawingPane(drawingPane);
        dto.setStartPoint(startPoint);
        dto.setBorderColorPicker(borderColorPicker);
        dto.setFillColorPicker(fillColorPicker);
    }

    private AbstractShape createRectangle(Color strokeColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(strokeColor, fillColor, borderWidth, width, height);
        return rectangle;
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
        saveFile.executeCommand();
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
                    saveFile.executeCommand();
                    // Clear workspace from older shapes
                    drawingPane.getChildren().clear();
                    // After saving execute load
                    Load loadFile = new Load(drawingPane);
                    loadFile.executeCommand();

                } else if (result.get().getText().equals("No")) {
                    // Clear workspace from older shapes
                    drawingPane.getChildren().clear();
                    // Execute load
                    Load loadFile = new Load(drawingPane);
                    loadFile.executeCommand();
                }
            }
        }
        else {
            // Clear workspace from older shapes
            drawingPane.getChildren().clear();
            // Execute load
            Load loadFile = new Load(drawingPane);
            loadFile.executeCommand();
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

