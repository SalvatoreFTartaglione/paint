package org.example.controller;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
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
import javafx.scene.shape.Line;
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commandInterface.CommandInterface;
import org.example.model.concreteCommands.*;
import org.example.model.invoker.Invoker;
import org.example.model.observer.FavoriteObserver;
import org.example.model.receivers.*;
import org.example.model.registry.FavoriteShapeRegistry;
import org.example.model.contextMenu.ContextMenuActions;
import org.example.model.contextMenu.ContextMenuManager;
import org.example.model.enums.MirrorDirectionEnum;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.utils.ZoomableScrollPane;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import static java.lang.Math.round;

/**
 * Controller of the main window: it wires the FXML view to the model by turning user interactions
 * into commands that are run (and recorded for undo) by the {@link Invoker}.
 */
public class MainViewController implements Initializable, FavoriteObserver {

    /** Maximum number of characters accepted for a text shape. */
    private static final int MAX_TEXT_LENGTH = 25;


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
    private DraggingReceiver draggingReceiver = new DraggingReceiver();
    private PositionManagerReceiver positionManagerReceiver;
    private boolean isDragging0 = false;                                                                                // State flag to understand if user is dragging and avoid accidentally adding new shapes to the canvas
    private boolean hasDragged = false;
    private ShapeReceiver shapeReceiver = new ShapeReceiver(null);
    private Pane gridPane = new Pane();
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
                drawOnCanvas(event);
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
     * Updates the list of favorite shapes displayed in the UI and enables or disables
     * the corresponding ComboBox depending on the current selected shape option and
     * whether there are any favorites available.
     *
     * @param updatedNames a set containing the updated names of the favorite shapes
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

    /**
     * Initializes the alert shown when a mirror operation would place a shape outside the canvas.
     * Sets the alert's title, header, and message, and configures it with a single "Ok" button.
     */
    private void initializeAlerts() {
        mirrorOutsideCanvasAlert.setTitle("Shape outside of canvas");
        mirrorOutsideCanvasAlert.setHeaderText("Can not mirror here.");
        mirrorOutsideCanvasAlert.setContentText("The following action will translate the figure outside the canvas. Try again.");
        ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        mirrorOutsideCanvasAlert.getButtonTypes().setAll(okButton);
    }

    /**
     * Initializes the grid controls for the canvas, including the grid slider and its listener.
     * Sets the initial grid size, adds the grid pane to the canvas container,
     * and ensures the grid is initially invisible and non-interactive.
     */
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

    /**
     * Creates a grid overlay on the specified pane using the given cell size.
     * Clears any existing grid lines before adding new vertical and horizontal lines.
     *
     * @param cellSize the spacing (in pixels) between grid lines
     * @param gridPane the pane on which the grid will be drawn
     */
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

    /**
     * Resets the grid size to the default value and recreates the grid with that size.
     *
     * @param actionEvent the action event triggered by the UI (e.g., a button click)
     */
    public void resetGrid(ActionEvent actionEvent) {
        gridSlider.setValue(50);
        createGrid(50.0, gridPane);
    }

    /**
     * Initializes the zoom controls, setting the minimum, maximum, and default zoom levels.
     * Adds a listener to the zoom slider to update the zoom level dynamically and
     * reflect it on the zoom label.
     */
    private void initializeZoomControls() {
        zoomSlider.setMin(0.20);
        zoomSlider.setMax(2.0);
        zoomSlider.setValue(1.0);

        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            double zoomLevel = newValue.doubleValue();
            scrollPane.setZoom(zoomLevel);
            updateZoomLabel(zoomLevel);
        });

        // Make the canvas start at the zoom level shown by the slider and its label (100%)
        scrollPane.setZoom(zoomSlider.getValue());
        updateZoomLabel(zoomSlider.getValue());
    }


    /**
     * Updates the zoom level label to show the current zoom as a percentage.
     *
     * @param zoomLevel the current zoom level, where 1.0 represents 100%
     */
    private void updateZoomLabel(double zoomLevel) {
        int percentage = (int) round(zoomLevel * 100);
        zoomValueLabel.setText(percentage + "%");
    }

    /**
     * Resets the zoom slider to the default value (1.0), which corresponds to 100% zoom.
     *
     * @param event the action event triggered by the UI (e.g., a button click)
     */
    @FXML
    private void resetZoom(ActionEvent event) {
        zoomSlider.setValue(1.0);
    }

    /**
     * Attaches mouse click event handlers to a shape node to enable context menu interaction.
     * On right-click, displays a context menu with various shape manipulation options.
     *
     * @param shapeNode the node representing the shape to which event handlers are attached
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
     * Displays a context menu for the specified shape at the mouse event's location.
     * The menu includes various shape manipulation actions such as coloring, resizing,
     * mirroring, rotating, and clipboard operations (copy, cut, paste, delete).
     *
     * This method also handles validation for favorite naming and mirrors that go out of bounds.
     *
     * @param shape the shape to which the context menu actions apply
     * @param event the mouse event triggering the context menu display
     * @throws IOException if an I/O error occurs while initializing the context menu
     */
    private void showContextMenu(ShapeInterface shape, MouseEvent event) throws IOException {
        /**
         * Defines the actions available in the context menu for a shape.
         * Each method corresponds to a specific command operation such as color changes,
         * transformations (resize, rotate, mirror), z-order adjustments, clipboard actions,
         * and favorite shape handling.
         *
         * The commands follow the Command Pattern and are executed via the {@code invoker}.
         * Some actions, like mirroring or rotating, include checks to ensure the operation is executable.
         * Alerts are shown if the operation would move the shape outside the canvas or if invalid data is provided.
         */
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
                    // Check whether the name is already in the ComboBox
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
     * Sets the {@link Invoker} responsible for managing undo/redo command execution.
     *
     * @param invoker the {@code Invoker} instance used to execute and track commands
     */
    public void setUndoManager(Invoker invoker) {
        this.invoker = invoker;
    }

    /**
     * Initializes the shape selection ComboBox with available shape options, each represented with an icon.
     *
     * Adds listeners to handle logic when "Favorite" is selected:
     * <ul>
     *   <li>Disables the favorites ComboBox if there are no saved favorites.</li>
     *   <li>Displays an alert if "Favorite" is selected but no favorites exist.</li>
     * </ul>
     *
     * Also sets up custom cell rendering to display both shape names and images.
     */
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

    /**
     * Initializes the UI layout behavior of the {@code SplitPane}, locking the divider
     * and ensuring it retains a fixed position (30%) on height changes.
     *
     * This method also disables user interaction with the divider.
     * Executed on the JavaFX application thread using {@link Platform#runLater}.
     */
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

    /**
     * Makes a shape draggable within the {@code drawingPane} by attaching mouse event handlers.
     *
     * Tracks the drag start point, computes movement deltas, checks boundary constraints,
     * and uses a {@code MoveShapeCommand} to allow undo/redo of the move.
     *
     * @param shape the shape to make draggable; must not be {@code null} and must have a node
     */
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
     * Handles mouse clicks on the drawing canvas to create and add a new shape
     * based on the current selection in the shapeComboBox.
     *
     * <p>Handles special cases such as:</p>
     * <ul>
     *   <li>Showing alerts when drawing text with empty or too long content.</li>
     *   <li>Registering polygon points.</li>
     *   <li>Executing an {@code AddShapeCommand} via the {@code Invoker} for undo/redo support.</li>
     * </ul>
     *
     * @param mouseEvent the {@code MouseEvent} triggered by clicking the canvas
     */
    public void drawOnCanvas(MouseEvent mouseEvent) {
        if (isContextMenuShowing) {
            contextMenu.hide();
            isContextMenuShowing = false;
            return;
        }
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
                        return;
                    }
                    if (text.length() > MAX_TEXT_LENGTH) {
                        textArea.setStyle("-fx-border-color: red; -fx-border-width: 4;");
                        warningLabel.setText("Text too long (max " + MAX_TEXT_LENGTH + " characters)");
                        return;
                    }
                    textArea.setStyle("");
                    warningLabel.setText("");
                    contextDTO.setText(text);
                    contextDTO.setFontSize(fontSizeComboBox.getValue());
                }
                if ("Polygon".equals(shapeComboBox.getValue().getName())) {
                    // Add clicked points for polygon to DTO
                    contextDTO.setPolygonPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
                }

                // A stale favorite shape must not leak into a different kind of shape
                if (!"Favorite".equals(shapeComboBox.getValue().getName())) {
                    contextDTO.setShape(null);
                }

                setParameters(mouseEvent, shapeComboBox, drawingPane, contextDTO.getStartPoint(), borderColorPicker, fillColorPicker);
                addToCanvasReceiver = new AddToCanvasReceiver(contextDTO);
                AddShapeCommand addShape = new AddShapeCommand(contextDTO, addToCanvasReceiver);
                invoker.execute(addShape);

                // Wire the event handlers to the shape that was actually created. Nothing is created while
                // the user is still collecting the points of a line or polygon (only preview nodes are shown).
                ShapeInterface addedShape = addShape.getShape();
                if (addShape.wasExecuted() && addedShape != null) {
                    attachEventHandlersToShape(addedShape.getNode());
                    makeDraggable(addedShape);
                }

                // Clear text area after adding text to canvas
                if ("Text".equals(shapeComboBox.getValue().getName())) {
                    textArea.clear();
                    textArea.setStyle("");
                    warningLabel.setText("");
                }
            }
        }
    }

    /**
     * Configures and updates the {@code contextDTO} with parameters required for
     * shape creation and drawing.
     *
     * @param mouseEvent         the mouse event used for coordinates
     * @param shapeComboBox      the shape selection combo box
     * @param drawingPane        the pane where shapes are drawn
     * @param startPoint         the start point of the shape, if any
     * @param borderColorPicker  color picker for border color
     * @param fillColorPicker    color picker for fill color
     */
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
     * Saves the drawing as a text file (reloadable) or as a PNG image, asking the user where.
     *
     * @param event the action event triggered by clicking the "Save" button
     */
    @FXML
    private void saveFile(ActionEvent event) {
        FileDialogs.chooseDrawingToSave().ifPresent(this::saveDrawingTo);
    }

    /**
     * Saves the drawing to the given file, showing an error alert if it cannot be written.
     *
     * @return {@code true} if the drawing was saved
     */
    private boolean saveDrawingTo(File file) {
        return runFileCommand(new SaveCommand(new CanvasExporter(contextDTO), file),
                "Save Error", "Could not save the drawing.");
    }

    /**
     * Loads a drawing from a file, replacing the content of the canvas.
     * <p>If the canvas is not empty the user is first offered to save it. Nothing is removed from the
     * canvas until a valid file has been read, so cancelling or choosing an invalid file never loses work.</p>
     *
     * @param event the action event triggered by clicking the "Load" button
     */
    @FXML
    private void loadFile(ActionEvent event) {
        if (hasShapesOnCanvas()) {
            ButtonType save = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType discard = new ButtonType("No", ButtonBar.ButtonData.NO);
            Optional<ButtonType> answer = confirm("Save Workspace", "Canvas is not empty.",
                    "Save the current workspace before loading?", save, discard, ButtonType.CANCEL);

            if (!answer.isPresent() || answer.get() == ButtonType.CANCEL) {
                return;
            }
            if (answer.get() == save) {
                Optional<File> target = FileDialogs.chooseDrawingToSave();
                if (!target.isPresent() || !saveDrawingTo(target.get())) {
                    return; // the user must not lose the drawing because saving failed or was cancelled
                }
            }
        }

        Optional<File> file = FileDialogs.chooseDrawingToOpen();
        if (!file.isPresent()) {
            return;
        }
        boolean loaded = runFileCommand(new LoadCommand(new CanvasImporter(contextDTO), file.get()),
                "Load Canvas Error", "Could not load the canvas. Maybe you tried to load a library file.");
        if (loaded) {
            // Commands recorded so far refer to shapes that are no longer on the canvas
            invoker.clearHistory();
            attachEventHandlersToAllShapes();
        }
    }

    private boolean hasShapesOnCanvas() {
        return drawingPane.getChildren().stream().anyMatch(node -> node.getUserData() instanceof ShapeInterface);
    }

    /**
     * Attaches event handlers and enables drag functionality for all shapes currently on the canvas.
     */
    private void attachEventHandlersToAllShapes() {
        for (Node node : drawingPane.getChildren()) {
            if (node.getUserData() instanceof ShapeInterface) {
                attachEventHandlersToShape(node);
                makeDraggable((ShapeInterface) node.getUserData());
            }
        }
    }

    /**
     * Handles the undo action by delegating to the {@code Invoker}.
     * Reattaches necessary event handlers to shapes after undoing.
     *
     * @param actionEvent the action event triggered by the undo button
     */
    @FXML
    public void undoClicked(ActionEvent actionEvent) {
        invoker.undo();
        attachEventHandlersToAllShapes();
    }

    /**
     * Toggles the visibility of the grid on the canvas based on the checkbox state.
     *
     * @param actionEvent the action event triggered by the grid toggle checkbox
     */
    public void toggleGrid(ActionEvent actionEvent) {
        gridPane.setVisible(checkBoxGrid.isSelected());
    }

    /**
     * Exports the favorite shapes to a library file.
     *
     * @param actionEvent the action event triggered by the save library button
     */
    public void saveLibrary(ActionEvent actionEvent) {
        FileDialogs.chooseLibraryToSave().ifPresent(file ->
                runFileCommand(new SaveLibraryCommand(new LibraryExporterReceiver(favoriteManagerReceiver), file),
                        "Save Library Error", "Could not save the library."));
    }

    /**
     * Imports a shape library into the favorites list.
     * <p>If there already are favorites the user is asked to confirm, because they are replaced.
     * Existing favorites are kept unless a valid library file is actually loaded.</p>
     *
     * @param actionEvent the action event triggered by the import library button
     */
    public void importLibrary(ActionEvent actionEvent) {
        if (!favoritesShapeComboBox.getItems().isEmpty()) {
            ButtonType overwrite = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            Optional<ButtonType> answer = confirm("Load Favorite Library", "Library is not empty.",
                    "Do you want to override the current library?", overwrite, ButtonType.NO);
            if (!answer.isPresent() || answer.get() != overwrite) {
                return;
            }
        }

        FileDialogs.chooseLibraryToOpen().ifPresent(file ->
                runFileCommand(new LoadLibraryCommand(new LibraryImporterReceiver(favoriteManagerReceiver), file),
                        "Load Favorite Library", "Could not load the library. Maybe you tried to load a canvas file."));
    }

    /**
     * Runs a command that may fail with an I/O error, showing an error alert in that case.
     *
     * @return {@code true} if the command completed successfully
     */
    private boolean runFileCommand(CommandInterface command, String title, String message) {
        try {
            invoker.executePossibleExceptionCommand(command);
            return true;
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(e.getMessage());
            alert.setContentText(message);
            alert.showAndWait();
            return false;
        }
    }

    private Optional<ButtonType> confirm(String title, String header, String content, ButtonType... buttons) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.getButtonTypes().setAll(buttons);
        return alert.showAndWait();
    }
}
