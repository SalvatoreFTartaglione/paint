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
import org.example.model.commands.Load;
import org.example.model.commands.Save;
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

    /**
     * Initializes the controller class.
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



    private void initializeContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        // Fill Color Menu Item
        Label fillLabel = new Label("Change Fill Color:");
        ColorPicker changeFillColorPickerContexMenu = new ColorPicker();
        HBox fillBox = new HBox(10, fillLabel, changeFillColorPickerContexMenu);
        CustomMenuItem fillColorItem = new CustomMenuItem(fillBox);
        fillColorItem.setHideOnClick(false); // evita che si chiuda subito

        // Border Color Menu Item
        Label borderLabel = new Label("Change Border Color:");
        ColorPicker changeStrokeColorPickerContexMenu = new ColorPicker();
        HBox borderBox = new HBox(10, borderLabel, changeStrokeColorPickerContexMenu);
        CustomMenuItem strokeColorItem = new CustomMenuItem(borderBox);
        strokeColorItem.setHideOnClick(false);

        // Aggiungi gli item al context menu
        contextMenu.getItems().addAll(fillColorItem, strokeColorItem);

        // Mostra il menu su click destro
        scrollPane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(scrollPane, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
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


    public void DrawOnCanvas(MouseEvent mouseEvent) {
        if (mouseEvent.getButton() == MouseButton.PRIMARY) {
            ShapeOption selectedShape = shapeComboBox.getValue();
            if (selectedShape != null) {

                switch (selectedShape.getName()) {
                    case "Ellipse": {
                        // coordinate locali del clic sul Canvas
                        Point2D localCoords = mainCanvasContainer.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        AbstractShape abstractEllipse = createEllipse(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                        ShapeInterface shapeInterface = abstractEllipse.getShape();
                        //controllo che la shape selezionata sia un'ellisse
                        if (shapeInterface instanceof EllipseShape) {
                            EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                            ellipseShape.draw(x, y);
                            // Aggiungi l'ellisse al contenitore del Canvas
                            drawingPane.getChildren().add(ellipseShape);
                        }
                        break;
                    }
                    case "Line": {
                        Point2D localCoords = mainCanvasContainer.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        if (startPoint == null) {
                            // first click: save starting point
                            startPoint = new Point2D(x, y);
                        } else {
                            // second click: save end point
                            LineCreator lineCreator = new LineCreator();
                            lineCreator.creationOfShape(borderColorPicker.getValue(), null, 10, 0, 0);
                            // Add line to container
                            ShapeInterface shapeInterface = lineCreator.getShape();
                            Double[] points = {x, y, startPoint.getX(), startPoint.getY()};
                            if (shapeInterface instanceof LineShape) {
                                LineShape lineShape = (LineShape) shapeInterface;
                                lineShape.draw(points);
                                mainCanvasContainer.getChildren().add(lineShape);
                            }
                            // Reset state variable
                            startPoint = null;
                        }
                        break;
                    }
                    case "Rectangle": {
                        // coordinate locali del clic sul Canvas
                        Point2D localCoords = mainCanvasContainer.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                        double x = localCoords.getX();
                        double y = localCoords.getY();
                        AbstractShape abstractRectangle = createRectangle(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                        ShapeInterface shapeInterface = abstractRectangle.getShape();
                        if (shapeInterface instanceof RectangleShape) {
                            RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                            rectangleShape.draw(x, y);
                            drawingPane.getChildren().add(rectangleShape);
                        }
                        break;
                    }
                }
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

    private AbstractShape createEllipse(Color strokeColor, Color fillColor, int borderWidth, int width, int height) {
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

