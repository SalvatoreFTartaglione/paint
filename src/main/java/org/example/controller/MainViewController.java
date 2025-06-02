package org.example.controller;


import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.example.model.DTO.DrawingContextDTO;
import org.example.model.commands.*;
import org.example.model.receivers.*;
import org.example.model.receivers.ChangesOnShapeReceiver;
import org.example.model.receivers.DraggingReceiver;
import org.example.model.receivers.ShapeReceiver;
import org.example.model.shape.*;
import javafx.scene.shape.Rectangle;
import org.example.model.registry.FavoriteShapeRegistry;
import org.example.model.contextMenu.ContextMenuActions;
import org.example.model.contextMenu.ContextMenuManager;
import org.example.model.shape.MirrorDirectionEnum;
import org.example.model.shape.ShapeInterface;
import org.example.model.ZoomableScrollPane;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.*;

import static java.lang.Math.round;

public class MainViewController implements Initializable {


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
    //private FavoriteManagerReceiver favoriteManagerReceiver;
    private boolean isDragging0 = false;                                                                                // State flag to understand if user is dragging and avoid accidentally adding new shapes to the canvas
    private boolean hasDragged = false;
    private ShapeReceiver shapeReceiver = new ShapeReceiver(null);
    private double currentZoom = 1.0;
    private Pane gridPane = new Pane();
    private ObservableList<ShapeInterface> favoritesShapeList = FXCollections.observableArrayList();
    //Usati per il rettangolo della selezione multipla
    private Rectangle selectionRect;
    private Point2D selectionStart;
    private final List<ShapeInterface> currentSelection = new ArrayList<>();
    private GroupReceiver grpReceiver;
    private Alert mirrorOutsideCanvasAlert = new Alert(Alert.AlertType.ERROR);

    private final Point2D[] dragStart = new Point2D[1];
    private final Point2D[] shapeStartPosition = new Point2D[1];

    private FavoriteManagerReceiver favoriteManagerReceiver = new FavoriteManagerReceiver();
    //private ObservableList<ShapeInterface> favoritesShapeList = FXCollections.observableArrayList();
    //@FXML     DA CAPIRE SE USARE STRING O SHAPE INTERFACE COME TIPO !!!
    //private ComboBox<ShapeInterface> favoritesShapeComboBox;

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

        initMultiSelectCommand();
        grpReceiver = new GroupReceiver(drawingPane);

        // --- NUOVO FILTER per deselezionare solo sul click sinistro in background ---
        drawingPane.addEventFilter(MouseEvent.MOUSE_PRESSED, evt -> {
            // 1) se sto disegnando il rettangolo di selezione, non toccare nulla
            if (selectionRect.isVisible()) return;

            // 2) solo sul click sinistro
            if (evt.getButton() != MouseButton.PRIMARY) return;

            // 2) se ho già selezionato delle shape e il click non è sopra una di esse, allora clear
            if (!currentSelection.isEmpty()) {
                boolean clickOnSelected = currentSelection.stream()
                        .map(ShapeInterface::getNode)
                        .anyMatch(node ->
                                node.contains(node.sceneToLocal(evt.getSceneX(), evt.getSceneY()))
                        );
                if (!clickOnSelected) {
                    clearSelection();
                }
            }
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
            // 1) se sto ancora disegnando il rettangolo di selezione, chiudo quella logica
            if (selectionRect.isVisible()) {
                onSelectionReleased(event);
                return;
            }

            // 2) altrimenti, se click sinistro senza drag, disegno la nuova forma
            if (!isDragging && event.getButton() == MouseButton.PRIMARY) {
                DrawOnCanvas(event);
            }
            // 3) se click destro in background, mostro il menu di “Paste”
            else if (event.getButton() == MouseButton.SECONDARY) {
                boolean clickedOnShape = drawingPane.getChildren().stream()
                        .anyMatch(node -> node.contains(node.sceneToLocal(event.getSceneX(), event.getSceneY())));
                if (!clickedOnShape) {
                    showPasteOnlyContextMenu(event);
                }
            }
        });

        scrollPane.addEventHandler(MouseEvent.MOUSE_PRESSED, evt -> {
            // Se c’è un contextMenu aperto, lo chiudo
            if (evt.getButton() == MouseButton.PRIMARY) {
                if (contextMenu != null && contextMenu.isShowing()) {
                    contextMenu.hide();
                }
            }
        });


        // stops dragging with mouse of the drawing pane
        scrollPane.setPannable(false);
        scrollPane.setTarget(mainCanvasContainer);
        //drawingPane.getChildren().clear();

        // favoriteShapesComboBox section
        FavoriteShapeRegistry.getInstance().addObserver(this::onFavoriteListChanged);

        favoritesShapeComboBox.setOnAction(e -> {
            String selected = favoritesShapeComboBox.getSelectionModel().getSelectedItem();
            //System.out.println("[DEBUG] Selezionato dal comboBox: " + selected);

            if (selected != null) {
                ShapeInterface shape = FavoriteShapeRegistry.getInstance().get(selected);
                //System.out.println("[DEBUG] Recuperata shape dal registry: " + shape.toString());

                if (shape != null) {
                    ShapeInterface copy = shape.cloneShape();
                    //System.out.println("[DEBUG] Clonata shape: " + copy);
                    contextDTO.setShape(copy.cloneShape());
                    contextDTO.setShapeOption(new ShapeOption("Favorite", ""));
                } else {
                    //System.out.println("[DEBUG] Nessuna shape trovata nel registry per: " + selected);
                }
            }
        });

    }

    // favoriteShapesComboBox section
    private void onFavoriteListChanged(Set<String> updatedNames) {
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

    private void initMultiSelectCommand() {
        contextDTO.setDrawingPane(drawingPane);
        selectionRect = new Rectangle();
        selectionRect.setStroke(Color.web("#0099FF"));
        selectionRect.getStrokeDashArray().addAll(5.0, 5.0);
        selectionRect.setFill(Color.web("#0099FF", 0.1));
        selectionRect.setVisible(false);
        drawingPane.getChildren().add(selectionRect);

        drawingPane.addEventFilter(MouseEvent.MOUSE_PRESSED, this::onSelectionPressed);
        drawingPane.addEventFilter(MouseEvent.MOUSE_DRAGGED, this::onSelectionDragged);
        drawingPane.addEventFilter(MouseEvent.MOUSE_RELEASED, this::onSelectionReleased);
    }

    private void onSelectionPressed(MouseEvent e) {
        // se clicco sullo sfondo (non sopra una shape)
        boolean clickedShape = drawingPane.getChildren().stream()
                .filter(n -> n.getUserData() instanceof ShapeInterface)
                .anyMatch(n -> n.contains(n.sceneToLocal(e.getSceneX(), e.getSceneY())));
        if (!clickedShape && e.getButton() == MouseButton.PRIMARY) {
            clearSelection();
            selectionStart = new Point2D(e.getX(), e.getY());
            selectionRect.setX(selectionStart.getX());
            selectionRect.setY(selectionStart.getY());
            selectionRect.setWidth(0);
            selectionRect.setHeight(0);
            selectionRect.setVisible(true);
        }
    }

    private void onSelectionDragged(MouseEvent e) {
        if (!selectionRect.isVisible()) return;
        double rawX = e.getX();
        double rawY = e.getY();
        double clampedX = Math.max(0, Math.min(rawX, drawingPane.getWidth()));
        double clampedY = Math.max(0, Math.min(rawY, drawingPane.getHeight()));

        double x = Math.min(selectionStart.getX(), clampedX);
        double y = Math.min(selectionStart.getY(), clampedY);
        double w = Math.abs(clampedX - selectionStart.getX());
        double h = Math.abs(clampedY - selectionStart.getY());

        selectionRect.setX(x);
        selectionRect.setY(y);
        selectionRect.setWidth(w);
        selectionRect.setHeight(h);
    }

    private void onSelectionReleased(MouseEvent e) {
        if (!selectionRect.isVisible()) return;

        double x1 = selectionRect.getX();
        double y1 = selectionRect.getY();
        double w  = selectionRect.getWidth();
        double h  = selectionRect.getHeight();

        currentSelection.clear();
        for (Node node : drawingPane.getChildren()) {
            if (node.getUserData() instanceof ShapeInterface) {
                Bounds b = node.getBoundsInParent();
                if (b.intersects(x1, y1, w, h)) {
                    node.setEffect(new DropShadow(10, Color.web("#0099FF")));
                    currentSelection.add((ShapeInterface)node.getUserData());
                }
            }
        }

        //warningLabel.setText(currentSelection.size() + " forme selezionate");
        selectionRect.setVisible(false);
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
                    try {
                        showContextMenu(shape, event);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            // Consuma l'evento per evitare che si propaghi al container
            event.consume();
        });
    }

    /**
     * Show context menu for selected shape
     */
    private void showContextMenu(ShapeInterface shape, MouseEvent event) throws IOException {
        // Create context menu actions
        ContextMenuActions actions = new ContextMenuActions() {

            @Override
            //Per evitare che se nel gruppo è presente una linea e si fa il fillColor, alla linea non succeda niente
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
                    // Controlla se il nome è già nel ComboBox
                    if (favoritesShapeComboBox.getItems().contains(name)) {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Name Already Exists");
                        alert.setHeaderText("This name is already in the favorites list");
                        alert.setContentText("Please choose a different name to save the shape as a favorite.");
                        alert.showAndWait();
                        return; // blocca il salvataggio
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
                    if (pastedShape instanceof CompositeShapeInterface) {
                        makeDraggableGroup((CompositeShapeInterface) pastedShape);
                    }
                    else {
                        makeDraggable(pastedShape);
                    }
                }
            }

            @Override
            public void onDelete() {
                CommandInterface cmd = new DeleteCommand(contextDTO, shape, shapeReceiver);
                invoker.execute(cmd);
            }

            @Override
            public void onUngroup() {
                CompositeShapeInterface groupShape = (CompositeShapeInterface) shape;
                UngroupCommand cmd = new UngroupCommand(grpReceiver, groupShape);
                invoker.execute(cmd);

                // 2) Ottengo le shape scomposte
                List<ShapeInterface> ungroupedChildren = cmd.getChildren();

                // 3) Ottengo le posizioni salvate in coordinate di scena
                Map<ShapeInterface, Point2D> savedScene = cmd.getOriginalScenePositions();

                // 4) Per ogni child: ripristino posizione e interattività
                for (ShapeInterface child : ungroupedChildren) {
                    Node childNode = child.getNode();

                    // a) Prendo il punto salvato in coordinate di scena
                    Point2D scenePt = savedScene.get(child);
                    if (scenePt != null) {
                        // b) Converto in coordinate locali del drawingPane
                        Point2D localOnPane = drawingPane.sceneToLocal(scenePt);

                        // c) Reset di eventuali traslazioni accumulate
                        childNode.setTranslateX(0);
                        childNode.setTranslateY(0);

                        // d) Imposto layoutX/Y basato sulla conversione
                        childNode.setLayoutX(localOnPane.getX());
                        childNode.setLayoutY(localOnPane.getY());
                    }

                    // e) Ripristino interattività
                    childNode.setMouseTransparent(false);
                    childNode.setPickOnBounds(true); // opzionale

                    attachEventHandlersToShape(childNode);
                    makeDraggable(child);
                }
            }
        };

        // Create context menu
        ContextMenuManager.MenuComponents menuComponents = ContextMenuManager.createContextMenu(shape, actions);
        contextMenu = menuComponents.contextMenu;

        // 3) Se siamo in multi-selezione e sto cliccando su una shape selezionata,
        //    aggiungi “Group” invece delle restanti opzioni
        if (currentSelection.size() >= 2 && currentSelection.contains(shape)) {
            MenuItem groupItem = new MenuItem("Group");
            groupItem.setOnAction(e -> {
                // Costruisci e esegui il GroupCommand come fai di consueto
                List<ShapeInterface> toGroup = new ArrayList<>(currentSelection);
                GroupCommand cmd = new GroupCommand(grpReceiver, toGroup);
                invoker.execute(cmd);
                CompositeShapeInterface composite = cmd.getCreatedGroup();
                // pick-on-bounds + figli mouse-transparent
                Node groupNode = composite.getNode();
                groupNode.setPickOnBounds(true);
                for (ShapeInterface s : composite.getChildren()) {
                    s.getNode().setMouseTransparent(true);
                }
                attachEventHandlersToShape(groupNode);
                makeDraggableGroup(composite);
                clearSelection();
            });
            contextMenu = new ContextMenu(groupItem);
            contextMenu.show(mainCanvasContainer, event.getScreenX(), event.getScreenY());
            return;
        }
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
                invoker.execute(cmd);

                // Recupera la shape incollata, perchè senza questo non posso aprire il contextMenu su una figura incollata
                ShapeInterface pastedShape = ((PasteCommand) cmd).getPastedShape();
                if (pastedShape != null && pastedShape.getNode() != null) {
                    attachEventHandlersToShape(pastedShape.getNode());
                    if(pastedShape instanceof CompositeShapeInterface) {
                        makeDraggableGroup((CompositeShapeInterface) pastedShape);
                    }
                    else {
                        makeDraggable(pastedShape);
                    }
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
                new ShapeOption("Text", "/org/example/images/text.png"),
                new ShapeOption("Polygon", "/org/example/images/polygon.png"),
                new ShapeOption("Favorite", "/org/example/images/favorites.png")
        );

        shapeComboBox.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isFavoriteSelected = newVal != null && "Favorite".equals(newVal.getName());
            boolean hasFavorites = !favoritesShapeComboBox.getItems().isEmpty();

            favoritesShapeComboBox.setDisable(!(isFavoriteSelected && hasFavorites));

            // opzionale: mostra messaggio d’errore se seleziona “Favorite” ma non ci sono shape salvate
            if (isFavoriteSelected && !hasFavorites) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("No Favorites");
                alert.setHeaderText("No saved favorite shapes.");
                alert.setContentText("Add at least one shape to favorites to use this option.");
                alert.showAndWait();

                // Rimuovi la shape memorizzata se non stai più usando una Favorite
                if (!"Favorite".equals(newVal.getName())) {
                    contextDTO.setShape(null);
                }
                // Rimuove la selezione
                shapeComboBox.getSelectionModel().clearSelection();
            }
        });


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

            double deltaX = event.getSceneX() - dragStart[0].getX();
            double deltaY = event.getSceneY() - dragStart[0].getY();

            Point2D deltaInParent = node.getParent().sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY()))
                    .subtract(node.getParent().sceneToLocal(dragStart[0]));

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
                    MoveShapeCommand cmd = new MoveShapeCommand(shape, -totalDeltaX, -totalDeltaY, draggingReceiver);
                    invoker.execute(cmd);
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

    private void makeDraggableGroup(CompositeShapeInterface compositeShape) {
        final Point2D[] dragStart = new Point2D[1];
        final Point2D[] shapeStartPosition = new Point2D[1];

        Node node = compositeShape.getNode();
        if (node == null) return;

        node.setOnMousePressed(event -> {
            dragStart[0] = new Point2D(event.getSceneX(), event.getSceneY());
            shapeStartPosition[0] = new Point2D(compositeShape.getX(), compositeShape.getY());
            event.consume();
        });

        node.setOnMouseDragged(event -> {
            if (!isDragging0) {
                isDragging0 = true;
            }
            hasDragged = true;

            // Usa coordinate scene invece di coordinate locali del nodo
            double deltaX = event.getSceneX() - dragStart[0].getX();
            double deltaY = event.getSceneY() - dragStart[0].getY();

            // Parametri semplificati per il controllo limiti
            double zoom = drawingPane.getScaleX();
            double paneWidth = drawingPane.getPrefWidth();
            double paneHeight = drawingPane.getPrefHeight();
            double extraMargin = 20;

            // Verifica se il movimento è consentito
            if (compositeShape.canMoveBy(deltaX, deltaY, paneWidth, paneHeight, zoom, extraMargin)) {
                compositeShape.moveBy(deltaX, deltaY);
                dragStart[0] = new Point2D(event.getSceneX(), event.getSceneY());
            }

            event.consume();
        });

        node.setOnMouseReleased(event -> {
            if (hasDragged) {
                Point2D currentPosition = new Point2D(compositeShape.getX(), compositeShape.getY());
                double totalDeltaX = currentPosition.getX() - shapeStartPosition[0].getX();
                double totalDeltaY = currentPosition.getY() - shapeStartPosition[0].getY();

                if (Math.abs(totalDeltaX) > 1 || Math.abs(totalDeltaY) > 1) {
                    compositeShape.moveBy(-totalDeltaX, -totalDeltaY); // rollback
                    MoveShapeCommand moveShapeCommand = new MoveShapeCommand(compositeShape, totalDeltaX, totalDeltaY, draggingReceiver);
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
        clearSelection();
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
    private void saveFile(ActionEvent event) throws IOException {
        canvasExporter = new CanvasExporter(drawingPane);
        SaveCommand saveCommandFile = new SaveCommand(canvasExporter);
        invoker.execute(saveCommandFile);
    }


    /**
     * @param event
     * Calls the function to load a file
     */
    @FXML
    private void loadFile(ActionEvent event) throws IOException {
        canvasImporter = new CanvasImporter(drawingPane);
        // Check if canvas is empty
        if (drawingPane.getChildren().stream().anyMatch(node -> node.getUserData() instanceof ShapeInterface)) {
            loadManager(canvasImporter);
            // Assign event handlers to all loaded shapes
            attachEventHandlersToAllShapes();
            initMultiSelectCommand();
        }
        else {
            // Clear workspace from older shapes
            drawingPane.getChildren().clear();
            initMultiSelectCommand();
            // Execute load
            LoadCommand load = new LoadCommand(canvasImporter);
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

    private void clearSelection() {
        // rimuove l’effetto DropShadow da tutte le shape selezionate
        for (ShapeInterface s : currentSelection) {
            s.getNode().setEffect(null);
        }
        currentSelection.clear();
        warningLabel.setText("");
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
                invoker.executeLoadCommand(load);

            } else if (result.get().getText().equals("No")) {
                // Clear workspace from older shapes
                drawingPane.getChildren().clear();
                // Execute load
                LoadCommand load = new LoadCommand(canvasImporter);
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
        attachEventHandlersToAllShapes();
        clearSelection();
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
            // L'utente ha confermato di voler sovrascrivere
            favoritesShapeComboBox.getItems().clear(); // Pulisce la libreria attuale
            LoadLibraryCommand loadLibraryCommand = new LoadLibraryCommand(libraryImporterReceiver);
            invoker.execute(loadLibraryCommand);
        }
    }
}

