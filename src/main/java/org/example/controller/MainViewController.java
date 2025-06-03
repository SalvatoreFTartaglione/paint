package org.example.controller;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.commandInterface.CommandInterface;
import org.example.model.commands.concreteCommands.*;
import org.example.model.commands.invoker.Invoker;
import org.example.model.commands.receivers.*;
import org.example.model.observer.FavoriteObserver;
import org.example.model.registry.FavoriteShapeRegistry;
import org.example.model.contextMenu.ContextMenuActions;
import org.example.model.contextMenu.ContextMenuManager;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.utils.ZoomableScrollPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.*;

import static java.lang.Math.round;

public class MainViewController implements Initializable, FavoriteObserver {


    @FXML
    private Button saveLibraryButton;
    @FXML
    private ComboBox<String> favoritesShapeComboBox;
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
    private CanvasImporter canvasImporter;
    private CanvasExporter canvasExporter;
    private LibraryExporterReceiver libraryExporterReceiver;
    private LibraryImporterReceiver libraryImporterReceiver;
    private boolean isDragging0 = false;                                                                                // State flag to understand if user is dragging and avoid accidentally adding new shapes to the canvas
    private boolean hasDragged = false;
    private ShapeReceiver shapeReceiver = new ShapeReceiver(null);
    private double currentZoom = 1.0;
    private Pane gridPane = new Pane();
    //Usati per il rettangolo della selezione multipla
    /*private final List<ShapeInterface> currentSelection = new ArrayList<>();*/
    private Alert mirrorOutsideCanvasAlert = new Alert(Alert.AlertType.ERROR);

    private FavoriteManagerReceiver favoriteManagerReceiver = new FavoriteManagerReceiver();
    private boolean isContextMenuShowing = false;


    /**
     * Initializes the controller after the root element has been completely processed.
     *
     * @param location the location used to resolve relative paths
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Init Components
        initializeSplitPane();
        initializeShapeChoiceBox();
        initializeZoomControls();
        initializeGridControls();
        initializeAlerts();

        saveLibraryButton.disableProperty().bind(
                Bindings.isEmpty(favoritesShapeComboBox.getItems())
        );

        favoritesShapeComboBox.setDisable(true);
        contextDTO.setDrawingPane(drawingPane);

        drawingPane.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            if (evt.getButton() != MouseButton.PRIMARY) return;
        });

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

        drawingPane.addEventFilter(MouseEvent.MOUSE_RELEASED, event -> {

            if (!isDragging && event.getButton() == MouseButton.PRIMARY) {
                DrawOnCanvas(event);
            }
            else if (event.getButton() == MouseButton.SECONDARY) {
                boolean clickedOnShape = drawingPane.getChildren().stream().anyMatch(node -> node.contains(node.sceneToLocal(event.getSceneX(), event.getSceneY())));
                if (!clickedOnShape) {
                    showPasteOnlyContextMenu(event);
                }
            }
        });

        scrollPane.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> {
            if (evt.getButton() == MouseButton.PRIMARY) {
                if (contextMenu != null && contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            }
        });


        scrollPane.setPannable(false);
        scrollPane.setTarget(mainCanvasContainer);
        FavoriteShapeRegistry.getInstance().addObserver(this::onFavoriteListChanged);

        favoritesShapeComboBox.setOnAction(e -> {
            String selected = favoritesShapeComboBox.getSelectionModel().getSelectedItem();

            if (selected != null) {
                ShapeInterface shape = FavoriteShapeRegistry.getInstance().get(selected);

                if (shape != null) {
                    ShapeInterface copy = shape.cloneShape();
                    contextDTO.setShape(copy.cloneShape());
                    contextDTO.setShapeOption(new ShapeOption("Favorite", ""));
                }
            }
        });

    }

    /**
     * Aggiorna la lista dei preferiti nella combo box dei preferiti e abilita/disabilita
     * la combo box in base allo stato corrente.
     *
     * @param updatedNames insieme di nomi aggiornati degli elementi preferiti.
     */
    public void onFavoriteListChanged(Set<String> updatedNames) {
        Platform.runLater(() -> {
            favoritesShapeComboBox.getItems().setAll(updatedNames);
            ShapeOption currentOption = shapeComboBox.getValue();
            boolean isFavoriteSelected = currentOption != null && "Favorite".equals(currentOption.getName());
            boolean hasFavorites = !updatedNames.isEmpty();
            favoritesShapeComboBox.setDisable(!(isFavoriteSelected && hasFavorites));
        });
    }

    private void initializeAlerts() {
        mirrorOutsideCanvasAlert.setTitle("Shape outside of canvas");
        mirrorOutsideCanvasAlert.setHeaderText("Can not mirror here.");
        mirrorOutsideCanvasAlert.setContentText("The following action will translate the figure outside the canvas. Try again.");
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        mirrorOutsideCanvasAlert.getButtonTypes().setAll(okButton);
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

        shapeNode.setOnMouseClicked(event -> {
            if (contextMenu != null && contextMenu.isShowing()) {
                contextMenu.hide();
            }
            Object userData = shapeNode.getUserData();
            if (userData instanceof ShapeInterface) {
                ShapeInterface shape = (ShapeInterface) userData;
                selectedShapeForContextMenu = shape;

                if (event.getButton() == MouseButton.SECONDARY) {
                    try {
                        showContextMenu(shape, event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            event.consume();
        });
    }

    /**
     * Show context menu for selected shape
     */
    private void showContextMenu(ShapeInterface shape, MouseEvent event) throws IOException {
        ContextMenuActions actions = new ContextMenuActions() {

            @Override
            public void onFillColorChanged(Color newColor) {
                CommandInterface cmd = new ChangeFillColorCommand(shape, newColor, changesOnShapeReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onStrokeColorChanged(Color newColor) {
                CommandInterface cmd = new ChangeStrokeColorCommand(shape, newColor, changesOnShapeReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onSendToBack() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new SendBackwardCommand(positionManagerReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onBringToFront() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new BringForwardCommand(positionManagerReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onResize(double oldWidth, double oldHeight, double newSize) {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new ResizeCommand(positionManagerReceiver, newSize, oldWidth, oldHeight);
                invoker.execute(cmd);
            }

            public void onStretch(double oldWidth, double oldHeight, double newWidth, double newHeight) {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new StretchCommand(positionManagerReceiver, newWidth, newHeight, oldWidth, oldHeight);
                invoker.execute(cmd);
            }

            @Override
            public void onMirrorUp() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.UP, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onMirrorDown() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.DOWN, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onMirrorLeft() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.LEFT, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onMirrorRight() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.RIGHT, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onMirrorHorizontalAroundCenter() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.HORIZONTAL, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onMirrorVerticalAroundCenter() {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new MirroringCommand(positionManagerReceiver, MirrorDirectionEnum.VERTICAL, contextDTO);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onRotate(double oldAngle, double newAngle) {
                positionManagerReceiver = new PositionManagerReceiver(shape);
                CommandInterface cmd = new RotateCommand(positionManagerReceiver, oldAngle, newAngle);
                if (cmd.isExecutable()) {
                    invoker.execute(cmd);
                }else {
                    mirrorOutsideCanvasAlert.showAndWait();
                }
            }

            @Override
            public void onAddToFavoritesWithName(String name) {
                if (shape != null) {
                    if (name == null || name.trim().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Invalid Name");
                        alert.setHeaderText("Name cannot be empty");
                        alert.setContentText("Please enter a valid name for the favorite shape.");
                        alert.showAndWait();
                        return;
                    }
                    // Controlla se il nome è già nel ComboBox
                    if (favoritesShapeComboBox.getItems().contains(name)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Name Already Exists");
                        alert.setHeaderText("This name is already in the favorites list");
                        alert.setContentText("Please choose a different name to save the shape as a favorite.");
                        alert.showAndWait();
                        return;
                    }

                    CommandInterface cmd = new SaveFavoriteCommand(
                            favoriteManagerReceiver, shape, name
                    );
                    invoker.execute(cmd);
                }
            }


            @Override
            public void onCopy() {
                CommandInterface cmd = new CopyCommand(shape, shapeReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onCut() {
                CommandInterface cmd = new CutCommand(shape, shapeReceiver, contextDTO);
                invoker.execute(cmd);
            }

            @Override
            public void onPaste() {
                contextDTO.setSceneX(event.getSceneX());
                contextDTO.setSceneY(event.getSceneY());
                if(shapeReceiver.hasShape()) {
                    CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
                    invoker.execute(cmd);

                    ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
                    Node node = pastedShape.getNode();
                    attachEventHandlersToShape(node);
                    makeDraggable(pastedShape);
                }
            }

            @Override
            public void onDelete() {
                CommandInterface cmd = new DeleteCommand(contextDTO, shape, shapeReceiver);
                invoker.execute(cmd);
            }
        };

        // Create context menu
        ContextMenuManager.MenuComponents menuComponents = ContextMenuManager.createContextMenu(shape, actions);
        contextMenu = menuComponents.contextMenu;
        contextMenu.show(mainCanvasContainer, event.getScreenX(), event.getScreenY());
        isContextMenuShowing = true;
    }

    private void showPasteOnlyContextMenu(MouseEvent event){
        if (contextMenu != null && contextMenu.isShowing()) {
            contextMenu.hide();
        }
        contextDTO.setSceneX(event.getSceneX());
        contextDTO.setSceneY(event.getSceneY());

        contextMenu = new ContextMenu();

        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(e -> {
            if(shapeReceiver.hasShape()) {
                CommandInterface cmd = new PasteCommand(shapeReceiver, contextDTO);
                invoker.execute(cmd);

                ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
                if (pastedShape != null && pastedShape.getNode() != null) {
                    attachEventHandlersToShape(pastedShape.getNode());
                    makeDraggable(pastedShape);
                }
            }
        });

        contextMenu.getItems().add(pasteItem);

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
                new ShapeOption("Text", "/org/example/images/text.png"),
                new ShapeOption("Polygon", "/org/example/images/polygon.png"),
                new ShapeOption("Favorite", "/org/example/images/favorites.png")
        );

        shapeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isFavoriteSelected = newVal != null && "Favorite".equals(newVal.getName());
            boolean hasFavorites = !favoritesShapeComboBox.getItems().isEmpty();

            favoritesShapeComboBox.setDisable(!(isFavoriteSelected && hasFavorites));

            if (isFavoriteSelected && !hasFavorites) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Favorites");
                alert.setHeaderText("No saved favorite shapes.");
                alert.setContentText("Add at least one shape to favorites to use this option.");
                alert.showAndWait();

                if (!"Favorite".equals(newVal.getName())) {
                    contextDTO.setShape(null);
                }
                shapeComboBox.getSelectionModel().clearSelection();
            }
        });


        shapeComboBox.getItems().addAll(shapes);
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
                startPoint = null;
            }
        });

    }

    private void initializeSplitPane() {
        Platform.runLater(() -> {
            Node divider = splitPane.lookupAll(".split-pane-divider")
                    .stream()
                    .findFirst()
                    .orElse(null);

            if (divider != null) {
                divider.setMouseTransparent(true);
                divider.setFocusTraversable(false);
            }

            splitPane.setDividerPositions(0.3);
            splitPane.heightProperty().addListener((obs, oldVal, newVal) -> {
                splitPane.setDividerPositions(0.3);
            });
        });
    }

    private void makeDraggable(ShapeInterface shape) {
        final Point2D[] dragStart = new Point2D[1];
        final Point2D[] shapeStartPosition = new Point2D[1];

        Node node;
        if (shape != null && shape.getNode() != null) {
            node = shape.getNode();
        } else {
            return;
        }

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

            Point2D deltaInParent = node.getParent().sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())).subtract(node.getParent().sceneToLocal(dragStart[0]));

            double adjustedDeltaX = deltaInParent.getX();
            double adjustedDeltaY = deltaInParent.getY();

            double zoom = drawingPane.getScaleX();
            double paneWidth = drawingPane.getPrefWidth() * zoom;
            double paneHeight = drawingPane.getPrefHeight() * zoom;
            double extraMargin = 20;

            if (shape.canMoveBy(adjustedDeltaX, adjustedDeltaY, paneWidth, paneHeight, zoom, extraMargin)) {
                shape.moveBy(adjustedDeltaX, adjustedDeltaY);
                dragStart[0] = new Point2D(event.getSceneX(), event.getSceneY());
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
                    MoveShapeCommand moveShapeCommand = new MoveShapeCommand(shape, totalDeltaX, totalDeltaY, draggingReceiver);
                    invoker.execute(moveShapeCommand);
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
        if (isContextMenuShowing) {
            contextMenu.hide();
            isContextMenuShowing = false;
            return;
        }
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
                        textArea.setStyle("");
                        warningLabel.setText("");
                    }
                    contextDTO.setText(text);
                    contextDTO.setFontSize(fontSizeComboBox.getValue());
                }
                if ("Polygon".equals(shapeComboBox.getValue().getName())) {
                    // Add clicked points for polygon to DTO
                    contextDTO.setPolygonPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
                }

                // reset della shapeOption per evitare bug
                if (!"Favorite".equals(shapeComboBox.getValue().getName())) {
                    contextDTO.setShape(null); // protezione extra
                }

                setParameters(mouseEvent, shapeComboBox, drawingPane, contextDTO.getStartPoint(), borderColorPicker, fillColorPicker);
                addToCanvasReceiver = new AddToCanvasReceiver(contextDTO);
                CommandInterface addShape = new AddShapeCommand(contextDTO, addToCanvasReceiver);
                invoker.execute(addShape);

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
        canvasExporter = new CanvasExporter(contextDTO);
        SaveCommand saveCommandFile = new SaveCommand(canvasExporter);
        invoker.execute(saveCommandFile);
    }


    /**
     * @param event
     * Calls the function to load a file
     */
    @FXML
    private void loadFile(ActionEvent event) throws IOException {
        canvasImporter = new CanvasImporter(contextDTO);
        // Check if canvas is empty
        if (drawingPane.getChildren().stream().anyMatch(node -> node.getUserData() instanceof ShapeInterface)) {
            loadManager(canvasImporter);
            // Assign event handlers to all loaded shapes
            attachEventHandlersToAllShapes();
        }
        else {
            // Clear workspace from older shapes
            drawingPane.getChildren().clear();
            LoadCommand load = new LoadCommand(canvasImporter);
            try {
                invoker.executePossibleExceptionCommand(load);
            } catch (IOException e) {
                showErrorAlertLoadCanvas(e);
            }
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


    private void loadManager(CanvasImporter canvasImporter) throws IOException {
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
                SaveCommand saveCommandFile = new SaveCommand(canvasExporter);
                invoker.execute(saveCommandFile);
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // After saving execute load
                LoadCommand load = new LoadCommand(canvasImporter);
                try {
                    invoker.executePossibleExceptionCommand(load);
                } catch (IOException e) {
                    showErrorAlertLoadCanvas(e);
                }

            } else if (result.get().getText().equals("No")) {
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // Execute load
                LoadCommand load = new LoadCommand(canvasImporter);
                try {
                    invoker.executePossibleExceptionCommand(load);
                } catch (IOException e) {
                    showErrorAlertLoadCanvas(e);
                }
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
        attachEventHandlersToAllShapes();
    }

    public void toggleGrid(ActionEvent actionEvent) {
        gridPane.setVisible(checkBoxGrid.isSelected());
    }

    public void saveLibrary(ActionEvent actionEvent) throws IOException {
        libraryExporterReceiver = new LibraryExporterReceiver(favoriteManagerReceiver);
        SavaLibraryCommand savaLibraryCommand = new SavaLibraryCommand(libraryExporterReceiver);
        invoker.execute(savaLibraryCommand);
    }

    public void importLibrary(ActionEvent actionEvent) throws IOException {
        libraryImporterReceiver = new LibraryImporterReceiver(favoriteManagerReceiver);
        if (favoritesShapeComboBox.getItems().isEmpty()) {
            LoadLibraryCommand loadLibraryCommand = new LoadLibraryCommand(libraryImporterReceiver);
            try {
                invoker.executePossibleExceptionCommand(loadLibraryCommand);
            } catch (IOException e) {
                showErrorAlertLoadLibrary(e);
            }
        }else {
            showAlertLoadLibrary();
        }
    }

    private void showErrorAlertLoadLibrary(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Load Favorite Library");
        alert.setHeaderText(e.getMessage());
        alert.setContentText("Could not load library. Maybe you tried to load a canvas file.");

        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();

    }

    private void showErrorAlertLoadCanvas(IOException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Load Canvas Error");
        alert.setHeaderText(e.getMessage());
        alert.setContentText("Could not load canvas. Maybe you tried to load a library file.");

        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(okButton);

        alert.showAndWait();

    }

    private void showAlertLoadLibrary() throws IOException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Load Favorite Library");
        alert.setHeaderText("Library is not empty.");
        alert.setContentText("Do you want override the current library?");

        ButtonType overwriteButton = new ButtonType("Sì", ButtonBar.ButtonData.YES);
        ButtonType cancelButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        alert.getButtonTypes().setAll(overwriteButton, cancelButton);

        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get().getButtonData() == ButtonBar.ButtonData.YES) {
            favoritesShapeComboBox.getItems().clear();
            LoadLibraryCommand loadLibraryCommand = new LoadLibraryCommand(libraryImporterReceiver);
            invoker.execute(loadLibraryCommand);
        }
    }
}

