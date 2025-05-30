package org.example.controller;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.*;
import org.example.model.receivers.*;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.ContextMenuActions;
import org.example.model.shape.ContextMenuManager;
import org.example.model.shape.ShapeInterface;
import org.example.model.ZoomableScrollPane;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static java.lang.Math.round;

public class MainViewController implements Initializable {


    @FXML
    private ComboBox<ShapeInterface> favoritesShapeComboBox;
    @FXML
    private CheckBox checkBoxGrid;
    @FXML
    private ComboBox<Integer> fontSizeComboBox;
    @FXML
    private Label gridValueLabel;
    @FXML
    private Slider gridSlider;
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
    private ZoomableScrollPane scrollPane;
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
    @FXML
    private Label warningLabel;



    private boolean isDragging = false;
    private Point2D startPoint = null;
    private ShapeInterface selectedShapeForContextMenu = null;
    private Invoker invoker;
    private ContextMenu contextMenu = new ContextMenu();
    private final DrawingContextDTO contextDTO = new DrawingContextDTO();
    private AddToCanvasReceiver addToCanvasReceiver = null;
    private ChangesOnShapeReceiver changesOnShapeReceiver = new ChangesOnShapeReceiver();
    private DraggingReceiver draggingReceiver = new DraggingReceiver() {};
    private PositionManagerReceiver positionManagerReceiver;
    // State flag to understand if user is dragging and avoid accidentally adding new shapes to the canvas
    private boolean isDragging0 = false;
    private boolean hasDragged = false;
    private ShapeReceiver shapeReceiver = new ShapeReceiver(null);
    private double currentZoom = 1.0;
    private static final double CLICK_MARGIN = 5.0; // distanza minima dai bordi
    private Pane gridPane = new Pane();
    private ObservableList<ShapeInterface> favoritesShapeList = FXCollections.observableArrayList();


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
        initializeGridControls();


        fontSizeComboBox.setValue(56);
        fontSizeComboBox.setOnAction(event -> {
            Integer selected = fontSizeComboBox.getValue();
            if (selected != null) {
                contextDTO.setFontSize(selected.intValue());
            }
        });

        mainCanvasContainer.setOnMousePressed(event -> {
            isDragging = false;
        });

        mainCanvasContainer.setOnMouseDragged(event -> {
            isDragging = true;
        });

        drawingPane.setOnMouseReleased(event -> {
            double x = event.getX();
            double y = event.getY();
            double paneWidth = drawingPane.getWidth();
            double paneHeight = drawingPane.getHeight();

            // Verifica che il click non sia troppo vicino ai bordi
            boolean isWithinSafeArea =
                    x > CLICK_MARGIN && x < (paneWidth - CLICK_MARGIN) &&
                            y > CLICK_MARGIN && y < (paneHeight - CLICK_MARGIN);

            if (!isWithinSafeArea) {
                return; // Ignora il click
            }

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
        scrollPane.setTarget(mainCanvasContainer);

    }

    private void initializeGridControls() {
        gridSlider.setValue(50);
        gridValueLabel.setText(round(gridSlider.getValue()) +" px");
        gridSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            createGrid(newValue.doubleValue(), gridPane);
            gridValueLabel.setText(round(gridSlider.getValue()) +" px");
        });
        gridPane.setPrefSize(5000.0, 5000.0);
        createGrid(50.0, gridPane);
        gridPane.setMouseTransparent(true);
        mainCanvasContainer.getChildren().add(0, gridPane);
        drawingPane.setStyle("-fx-border-color: black; -fx-border-width: 5px; -fx-border-style: solid;");
        gridPane.setVisible(false);
    }

    private void createGrid(double cellSize, Pane gridPane) {

        gridPane.getChildren().clear();

        double width = gridPane.getPrefWidth();
        double height = gridPane.getPrefHeight();

        for (double x = 0; x <= width; x += cellSize) {
            Line vLine = new Line(x, 0, x, height);
            vLine.setStroke(Color.GRAY);
            vLine.setStrokeWidth(1);
            gridPane.getChildren().add(vLine);
        }

        for (double y = 0; y <= height; y += cellSize) {
            Line hLine = new Line(0, y, width, y);
            hLine.setStroke(Color.GRAY);
            hLine.setStrokeWidth(1);
            gridPane.getChildren().add(hLine);
        }
    }

    public void resetGrid(ActionEvent actionEvent) {
        gridSlider.setValue(50);
        createGrid(50.0, gridPane);
    }

    private void initializeZoomControls() {
        zoomSlider.setMin(0.20);
        zoomSlider.setMax(2.0);
        zoomSlider.setValue(1.0);

        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double zoomLevel = newValue.doubleValue();
            scrollPane.setZoom(zoomLevel);
            updateZoomLabel(zoomLevel);
        });

        updateZoomLabel(1.0);
    }

    private void updateZoomLabel(double zoomLevel) {
        int percentage = (int) round(zoomLevel * 100);
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
                invoker.executeCommand(cmd);
            }

            @Override
            public void onStrokeColorChanged(Color newColor) {
                CommandInterface cmd = new ChangeStrokeColorCommand(shape, newColor, changesOnShapeReceiver);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onSendToBack() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new SendBackwardCommand(positionManagerReceiver);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onBringToFront() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new BringForwardCommand(positionManagerReceiver);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onResize(double newWidth, double newHeight) {
                /*positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new ResizeCommand(positionManagerReceiver, newWidth, newHeight, oldWidth, oldHeight);
                invoker.executeCommand(cmd);*/
            }

            @Override
            public void onMirrorUp() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, true, false, false, false, false, false);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onMirrorDown() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, false, true, false,false, false, false);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onMirrorLeft() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, false, false, true, false, false, false);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onMirrorRight() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, false, false, false, true, false, false);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onMirrorHorizontalAroundCenter() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, false, false, false, false, true, false);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onMirrorVerticalAroundCenter() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, false, false, false, false, false, true);
                invoker.executeCommand(cmd);
            }

            @Override
            public void onAddToFavorites() {

            }


            @Override
            public void onCopy() {
                CommandInterface cmd = new CopyCommand(shape, shapeReceiver);
                invoker.executeCopyCommand(cmd);
            }

            @Override
            public void onCut() {
                CommandInterface cmd = new CutCommand(shape, shapeReceiver, contextDTO);
                invoker.executeCommand(cmd);
            }


            @Override
            public void onPaste() {
                contextDTO.setSceneX(event.getSceneX());
                contextDTO.setSceneY(event.getSceneY());
                if(shapeReceiver.hasShape()) {
                    CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
                    invoker.executeCommand(cmd);

                    ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
                    Node node = pastedShape.getNode();
                    attachEventHandlersToShape(node);
                    makeDraggable(pastedShape);
                }
            }

            @Override
            public void onDelete() {
                CommandInterface cmd = new DeleteCommand(contextDTO, shape, shapeReceiver);
                invoker.executeCommand(cmd);
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
            if(shapeReceiver.hasShape()) {
                CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
                invoker.executeCommand(cmd);

                // Recupera la shape incollata, perchè senza questo non posso aprire il contextMenu su una figura incollata
                ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
                if (pastedShape != null && pastedShape.getNode() != null) {
                    attachEventHandlersToShape(pastedShape.getNode());
                    makeDraggable(pastedShape);
                }
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
     * @param invoker the {@code Invoker} to associate with the canvas
     */
    public void setUndoManager(Invoker invoker) {
        this.invoker = invoker;
    }

    private void initializeShapeChoiceBox() {
        List<ShapeOption> shapes = List.of(
                new ShapeOption("Ellipse", "/org/example/images/ellipse.png"),
                new ShapeOption("Line", "/org/example/images/segment.png"),
                new ShapeOption("Rectangle", "/org/example/images/rectangle.png"),
                new ShapeOption("Text", "/org/example/images/text.png")
        );

        shapeComboBox.getItems().addAll(shapes);

        // Set the cell factory to show name + image
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

    private void makeDraggable(ShapeInterface shape) {
        final Point2D[] dragStart = new Point2D[1];
        final Point2D[] shapeStartPosition = new Point2D[1];
        Node node = shape.getNode();

        node.setOnMousePressed(event -> {
            dragStart[0] = new Point2D(event.getSceneX(), event.getSceneY());
            shapeStartPosition[0] = new Point2D(shape.getX(), shape.getY());
            event.consume();
        });

        node.setOnMouseDragged(event -> {
            if (!isDragging0) {
                isDragging0 = true;
            }
            hasDragged = true;

            double deltaX = event.getSceneX() - dragStart[0].getX();
            double deltaY = event.getSceneY() - dragStart[0].getY();

            double zoom = drawingPane.getScaleX();
            double paneWidth = drawingPane.getPrefWidth() * zoom;
            double paneHeight = drawingPane.getPrefHeight() * zoom;
            double extraMargin = 20;

            if (shape.canMoveBy(deltaX, deltaY, paneWidth, paneHeight, zoom, extraMargin)) {
                shape.moveBy(deltaX, deltaY);
                dragStart[0] = new Point2D(event.getSceneX(), event.getSceneY()); // aggiorna con scene coords
            }

            event.consume();
        });

        node.setOnMouseReleased(event -> {
            if (hasDragged) {
                Point2D currentPosition = new Point2D(shape.getX(), shape.getY());
                double totalDeltaX = currentPosition.getX() - shapeStartPosition[0].getX();
                double totalDeltaY = currentPosition.getY() - shapeStartPosition[0].getY();

                if (Math.abs(totalDeltaX) > 1 || Math.abs(totalDeltaY) > 1) {
                    shape.moveBy(-totalDeltaX, -totalDeltaY); // rollback
                    DragCommand dragCommand = new DragCommand(shape, totalDeltaX, totalDeltaY, draggingReceiver);
                    invoker.executeCommand(dragCommand);
                }

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
     * Supports Ellipse, Line, Rectangle, and Text.
     *
     * @param mouseEvent the mouse event containing click coordinates
     */
    public void DrawOnCanvas(MouseEvent mouseEvent) {
        Label warningLabel = new Label();
        warningLabel.setTextFill(Color.RED);
        // Ignore the click if we just finished dragging
        if (hasDragged) {
            return;
        }
        if (shapeComboBox.getValue() != null) {
            if (contextMenu.isShowing()) {
                contextMenu.hide();
            } else {
                // Special handling for Text shape
                if ("Text".equals(shapeComboBox.getValue().getName())) {
                    String text = textArea.getText();
                    if (text == null || text.trim().isEmpty()) {
                        textArea.setStyle("-fx-border-color: red; -fx-border-width: 2;");
                        warningLabel.setText("Text field is empty");
                        warningLabel.setVisible(true);
                        return;
                    }
                    // Check length
                    if (text.length() > 25) {
                        textArea.setStyle("-fx-border-color: red; -fx-border-width: 4;");
                        if (warningLabel != null) {
                            warningLabel.setText("Text too long");
                            warningLabel.setVisible(true);
                            return;
                        }
                    } else {
                        textArea.setStyle(""); // Reset style
                        warningLabel.setText(""); // Clear warning
                    }
                    contextDTO.setText(text);
                    contextDTO.setFontSize(fontSizeComboBox.getValue());
                }

                setParameters(mouseEvent, shapeComboBox, drawingPane, contextDTO.getStartPoint(), borderColorPicker, fillColorPicker);
                addToCanvasReceiver = new AddToCanvasReceiver(contextDTO);
                CommandInterface addShape = new AddShapeCommand(contextDTO, addToCanvasReceiver);
                invoker.executeCommand(addShape);

                // Assign event handlers to newly created shape
                // Assuming AddShapeCommand adds the shape as the last element of the drawingPane
                if (!drawingPane.getChildren().isEmpty()) {
                    Node lastAddedShape = drawingPane.getChildren().get(drawingPane.getChildren().size() - 1);
                    attachEventHandlersToShape(lastAddedShape);
                    makeDraggable((ShapeInterface) lastAddedShape.getUserData());
                }

                // Clear text area after adding text to canvas
                if ("Text".equals(shapeComboBox.getValue().getName())) {
                    textArea.clear();
                    textArea.setStyle(""); // Clear style after draw
                    warningLabel.setText(""); // Hide warning
                }
            }
        }
    }

    private void setParameters(MouseEvent mouseEvent, ComboBox<ShapeOption> shapeComboBox, Pane drawingPane, Point2D startPoint, ColorPicker borderColorPicker, ColorPicker fillColorPicker) {
        contextDTO.setDrawingPane(drawingPane);
        contextDTO.setStartPoint(startPoint);
        contextDTO.setBorderColor(borderColorPicker.getValue());
        contextDTO.setFillColor(fillColorPicker.getValue());
        contextDTO.setSceneX(mouseEvent.getSceneX());
        contextDTO.setSceneY(mouseEvent.getSceneY());
        contextDTO.setShapeOption(shapeComboBox.getValue());
        contextDTO.setScrollPane(scrollPane);
        contextDTO.setMainCanvasContainer(mainCanvasContainer);
    }

    /**
     * @param event
     * Calls the function to save the file
     */
    @FXML
    private void saveFile(ActionEvent event) {
        SaveCommand saveCommandFile = new SaveCommand(drawingPane);
        invoker.executeSaveCommand(saveCommandFile);
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
            invoker.executeLoadCommand(load);
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
                invoker.executeSaveCommand(saveCommandFile);
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // After saving execute load
                LoadCommand load = new LoadCommand(drawingPane);
                invoker.executeLoadCommand(load);

            } else if (result.get().getText().equals("No")) {
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // Execute load
                LoadCommand load = new LoadCommand(drawingPane);
                invoker.executeLoadCommand(load);
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
        invoker.undo();
    }

    public void toggleGrid(ActionEvent actionEvent) {
        gridPane.setVisible(checkBoxGrid.isSelected());
    }

}

