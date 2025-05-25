package org.example.controller;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.*;
import org.example.model.receivers.*;
import org.example.model.shape.ContextMenuActions;
import org.example.model.shape.ContextMenuManager;
import org.example.model.shape.ShapeInterface;

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
    private Group mainCanvasContainer;
    @FXML
    private ContextMenu scrollPaneContextMenu;
    @FXML
    private Slider zoomSlider;
    @FXML
    private Label zoomValueLabel;
    @FXML
    private Button resetZoomButton;

    private boolean isDragging = false;
    private Point2D startPoint = null;
    private ShapeInterface selectedShapeForContextMenu = null;
    private UndoManager undoManager;
    private ContextMenu contextMenu = new ContextMenu();
    private final DrawingContextDTO contextDTO = new DrawingContextDTO();
    private AddToCanvas addToCanvas = null;
    private ChangesOnShapeReceiver changesOnShapeReceiver = new ChangesOnShapeReceiver();
    private DraggingReceiver draggingReceiver = new DraggingReceiver() {};
    private PoistionManagerReceiver poistionManagerReceiver;
    // State flag to understand if user is dragging and avoid accidentally adding new shapes to the canvas
    private boolean isDragging0 = false;
    private boolean hasDragged = false;

    private ShapeReceiver shapeReceiver = new ShapeReceiver();
    private double currentZoom = 1.0;

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
        initializeZoomControls();

        mainCanvasContainer.setOnMousePressed(event -> {
            isDragging = false;
        });

        mainCanvasContainer.setOnMouseDragged(event -> {
            isDragging = true;
        });

        scrollPane.setOnMouseReleased(event -> {
            if (!isDragging && event.getButton() == MouseButton.PRIMARY) {
                // Se clic sinistro, disegna
                DrawOnCanvas(event);
            } else if (event.getButton() == MouseButton.SECONDARY) {
                // Se clic destro, verifica se hai cliccato uno shape
                boolean clickedOnShape = false;

                for (Node node : drawingPane.getChildren()) {
                    if (node.contains(node.sceneToLocal(event.getSceneX(), event.getSceneY()))) {
                        clickedOnShape = true;
                        break;
                    }
                }

                if (!clickedOnShape) {
                    showPasteOnlyContextMenu(event);
                }
            }
        });

        // stops dragging with mouse of the drawing pane
        scrollPane.setPannable(false);
    }

    private void initializeZoomControls() {
        zoomSlider.setMin(0.1);  // 10%
        zoomSlider.setMax(2.0);  // 200%
        zoomSlider.setValue(1.0); // 100%

        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double zoomLevel = newValue.doubleValue();
            applyZoom(zoomLevel);
            updateZoomLabel(zoomLevel);
        });

        updateZoomLabel(1.0);
    }

    private void applyZoom(double zoomLevel) {
        currentZoom = zoomLevel;

        double hValue = scrollPane.getHvalue();
        double vValue = scrollPane.getVvalue();

        drawingPane.setScaleX(zoomLevel);
        drawingPane.setScaleY(zoomLevel);

        double originalWidth = 1500.0;
        double originalHeight = 1500.0;

        Platform.runLater(() -> {
            scrollPane.setHvalue(hValue);
            scrollPane.setVvalue(vValue);
        });
    }

    private void updateZoomLabel(double zoomLevel) {
        int percentage = (int) Math.round(zoomLevel * 100);
        zoomValueLabel.setText(percentage + "%");
    }

    @FXML
    private void resetZoom(ActionEvent event) {
        zoomSlider.setValue(1.0);
    }


    /**
     * NUOVO METODO: Assegna event handler diretti alla forma appena creata
     */
    private void attachEventHandlersToShape(Node shapeNode) {
        // Assegna l'event handler per il click del mouse
        shapeNode.setOnMouseClicked(event -> {
            // Nascondi eventuali menu contestuali aperti
            if (contextMenu != null && contextMenu.isShowing()) {
                contextMenu.hide();
            }

            // Ottieni la ShapeInterface dal userData del nodo
            Object userData = shapeNode.getUserData();
            if (userData instanceof ShapeInterface) {
                ShapeInterface shape = (ShapeInterface) userData;
                selectedShapeForContextMenu = shape;

                // Se è click destro, mostra il menu contestuale
                if (event.getButton() == MouseButton.SECONDARY) {
                    showContextMenu(shape, event);
                }
            }

            // Consuma l'evento per evitare che si propaghi al container
            event.consume();
        });
    }

    /**
     * Show context menu for selected shape
     */
    private void showContextMenu(ShapeInterface shape, MouseEvent event) {



        // Create context menu actions
        ContextMenuActions actions = new ContextMenuActions() {
            double oldWidth = shape.getWidth();
            double oldHeight = shape.getHeight();

            @Override
            public void onFillColorChanged(Color newColor) {
                CommandInterface cmd = new ChangeFillColorCommand(shape, newColor, changesOnShapeReceiver);
                undoManager.executeCommand(cmd);
            }

            @Override
            public void onStrokeColorChanged(Color newColor) {
                CommandInterface cmd = new ChangeStrokeColorCommand(shape, newColor, changesOnShapeReceiver);
                undoManager.executeCommand(cmd);
            }

            @Override
            public void onSendToBack() {
                poistionManagerReceiver = new PoistionManagerReceiver(shape);
                CommandInterface cmd = new SendBackwardCommand(poistionManagerReceiver);
                undoManager.executeCommand(cmd);
            }

            @Override
            public void onBringToFront() {
                poistionManagerReceiver = new PoistionManagerReceiver(shape);
                CommandInterface cmd = new BringForwardCommand(poistionManagerReceiver);
                undoManager.executeCommand(cmd);
            }

            @Override
            public void onResize(double newWidth, double newHeight) {
                poistionManagerReceiver = new PoistionManagerReceiver(shape);
                CommandInterface cmd = new ResizeCommand(poistionManagerReceiver, newWidth, newHeight, oldWidth, oldHeight);
                undoManager.executeCommand(cmd);
            }


            @Override
            public void onCopy() {
                CommandInterface cmd = new CopyComand(shape, shapeReceiver);
                undoManager.executeCommand(cmd);
            }

            @Override
            public void onCut() {
                CommandInterface cmd = new CutCommand(shape, shapeReceiver);
                undoManager.executeCommand(cmd);
            }


            @Override
            public void onPaste() {
                contextDTO.setSceneX(event.getSceneX());
                contextDTO.setSceneY(event.getSceneY());
                CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
                //undoManager.removeCommandFinished();
                undoManager.executeCommand(cmd);
            }
        };

        // Create context menu
        ContextMenuManager.MenuComponents menuComponents = ContextMenuManager.createContextMenu(shape, actions);
        contextMenu = menuComponents.contextMenu;

        // Show context menu
        contextMenu.show(mainCanvasContainer, event.getScreenX(), event.getScreenY());
    }

    private void showPasteOnlyContextMenu(MouseEvent event){
        // Chiude eventuale menu già aperto
        if (contextMenu != null && contextMenu.isShowing()) {
            contextMenu.hide();
        }
        // Aggiorna il contesto di disegno con le coordinate cliccate
        contextDTO.setSceneX(event.getSceneX());
        contextDTO.setSceneY(event.getSceneY());

        // Crea il nuovo menu contestuale
        contextMenu = new ContextMenu();

        // Crea l'azione di incolla
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(e -> {
            CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
            //undoManager.removeCommandFinished();
            undoManager.executeCommand(cmd);

            // Recupera la shape incollata, perchè senza questo non posso aprire il contextMenu su una figura incollata
            ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
            if (pastedShape != null && pastedShape.getNode() != null) {
                attachEventHandlersToShape(pastedShape.getNode());
                makeDraggable(pastedShape);
            }
        });

        // Aggiunge al menu
        contextMenu.getItems().add(pasteItem);

        // Mostra il menu nella posizione cliccata
        contextMenu.show(mainCanvasContainer, event.getScreenX(), event.getScreenY());
    }


    /**
     * Sets the undo manager for this controller.
     *
     * @param undoManager the {@code UndoManager} to associate with the canvas
     */
    public void setUndoManager(UndoManager undoManager) {
        this.undoManager = undoManager;
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

    @FXML
    private void handleEnterText(ActionEvent event) {
        String text = textArea.getText();
        if (text == null || text.trim().isEmpty()) return;

        Point2D defaultPosition = new Point2D(100, 100); // oppure dove preferisci
        contextDTO.setText(text);
        contextDTO.setSceneX(defaultPosition.getX());
        contextDTO.setSceneY(defaultPosition.getY());
        contextDTO.setBorderColor(borderColorPicker.getValue());
        contextDTO.setFillColor(fillColorPicker.getValue());
        contextDTO.setDrawingPane(drawingPane);
        contextDTO.setShapeOption(new ShapeOption("Text", ""));  // Stringa coerente con il case di AddToCanvas

        addToCanvas = new AddToCanvas(contextDTO);
        CommandInterface addTextCommand = new AddShapeCommand(contextDTO, addToCanvas);
        undoManager.executeCommand(addTextCommand);

        // Gestione degli handler
        Node lastNode = drawingPane.getChildren().get(drawingPane.getChildren().size() - 1);
        attachEventHandlersToShape(lastNode);
        makeDraggable((ShapeInterface) lastNode.getUserData());
    }



    private void makeDraggable(ShapeInterface shape) {
        final Point2D[] dragStart = new Point2D[1];
        final Point2D[] shapeStartPosition = new Point2D[1];
        Node node = shape.getNode();

        node.setOnMousePressed(event -> {
            dragStart[0] = new Point2D(event.getX(), event.getY());
            shapeStartPosition[0] = new Point2D(shape.getX(), shape.getY());
            event.consume();
        });

        node.setOnMouseDragged(event -> {
            if (!isDragging0) {
                isDragging0 = true;
            }
            hasDragged = true;
            // Get Coordinates
            double deltaX = event.getX() - dragStart[0].getX();
            double deltaY = event.getY() - dragStart[0].getY();
            //Real-time movement (without command)
            shape.moveBy(deltaX, deltaY);
            dragStart[0] = new Point2D(event.getX(), event.getY());
            event.consume();
        });

        node.setOnMouseReleased(event -> {
            if (hasDragged) {
                // Compute total displacement
                Point2D currentPosition = new Point2D(shape.getX(), shape.getY());
                double totalDeltaX = currentPosition.getX() - shapeStartPosition[0].getX();
                double totalDeltaY = currentPosition.getY() - shapeStartPosition[0].getY();
                //If there was actually a displacement, create the command
                if (Math.abs(totalDeltaX) > 1 || Math.abs(totalDeltaY) > 1) {
                    // Reset starting position
                    shape.moveBy(-totalDeltaX, -totalDeltaY);
                    // Create and execute DragCommand
                    DragCommand dragCommand = new DragCommand(shape, totalDeltaX, totalDeltaY, draggingReceiver);
                    undoManager.executeCommand(dragCommand);
                }
                // Delay to avoid interference with others clicks
                Platform.runLater(() -> {
                    isDragging0 = false;
                    hasDragged = false;
                });
            }
            event.consume();
        });
    }

    /**
     * Handles the primary mouse click to draw the selected shape on the canvas.
     * Supports Ellipse, Line, and Rectangle.
     *
     * @param mouseEvent the mouse event containing click coordinates
     */
    public void DrawOnCanvas(MouseEvent mouseEvent) {
        // Ignore the click if we just finished dragging
        if (hasDragged) {
            return;
        }
        if (shapeComboBox.getValue() != null) {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            } else {
                setParameters(mouseEvent, contextMenu, shapeComboBox, drawingPane, contextDTO.getStartPoint(), borderColorPicker, fillColorPicker);
                addToCanvas = new AddToCanvas(contextDTO);
                CommandInterface addShape = new AddShapeCommand(contextDTO, addToCanvas);
                undoManager.executeCommand(addShape);
                // Assign event handlers to newly created shape
                // Assuming AddShapeCommand adds the shape as the last element of the drawingPane
                if (!drawingPane.getChildren().isEmpty()) {
                    Node lastAddedShape = drawingPane.getChildren().get(drawingPane.getChildren().size() - 1);
                    attachEventHandlersToShape(lastAddedShape);
                    makeDraggable((ShapeInterface) lastAddedShape.getUserData());
                }
            }
        }
    }

    private void setParameters(MouseEvent mouseEvent, ContextMenu contextMenu, ComboBox<ShapeOption> shapeComboBox, Pane drawingPane, Point2D startPoint, ColorPicker borderColorPicker, ColorPicker fillColorPicker) {
        contextDTO.setDrawingPane(drawingPane);
        contextDTO.setStartPoint(startPoint);
        contextDTO.setBorderColor(borderColorPicker.getValue());
        contextDTO.setFillColor(fillColorPicker.getValue());
        contextDTO.setSceneX(mouseEvent.getSceneX());
        contextDTO.setSceneY(mouseEvent.getSceneY());
        contextDTO.setShapeOption(shapeComboBox.getValue());
    }

    /**
     * @param event
     * Calls the function to save the file
     */
    @FXML
    private void saveFile(ActionEvent event) {
        SaveCommand saveCommandFile = new SaveCommand(drawingPane);
        undoManager.executeSaveCommand(saveCommandFile);
    }


    /**
     * @param event
     * Calls the function to load a file
     */
    @FXML
    private void loadFile(ActionEvent event) {
        // Check if canvas is empty
        if ( ! drawingPane.getChildren().isEmpty() ) {
            loadManager(drawingPane);
            // Assign event handlers to all loaded shapes
            attachEventHandlersToAllShapes();
        }
        else {
            // Clear workspace from older shapes
            drawingPane.getChildren().clear();
            // Execute load
            LoadCommand load = new LoadCommand(drawingPane);
            undoManager.executeLoadCommand(load);
            // Assign event handlers to all loaded shapes and make nodes draggable
            attachEventHandlersToAllShapes();
        }
    }

    /**
     * Assign event handlers to all shapes in the drawingPane and make nodes draggable
     */
    private void attachEventHandlersToAllShapes() {
        for (Node node : drawingPane.getChildren()) {
            attachEventHandlersToShape(node);
            makeDraggable((ShapeInterface) node.getUserData());
        }
    }


    private void loadManager(Pane drawingPane){
        // Confirmation alert
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("SaveCommand Workspace");
        alert.setHeaderText("Canvas is not empty.");
        alert.setContentText("SaveCommand current workspace?");
        ButtonType saveButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
        ButtonType dontSaveButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(saveButton, dontSaveButton);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent()) {
            if (result.get().getText().equals("Yes")) {
                // Saving file
                SaveCommand saveCommandFile = new SaveCommand(drawingPane);
                undoManager.executeSaveCommand(saveCommandFile);
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // After saving execute load
                LoadCommand load = new LoadCommand(drawingPane);
                undoManager.executeLoadCommand(load);

            } else if (result.get().getText().equals("No")) {
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // Execute load
                LoadCommand load = new LoadCommand(drawingPane);
                undoManager.executeLoadCommand(load);
            }
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

