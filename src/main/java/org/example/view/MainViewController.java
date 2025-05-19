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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.commands.Load;
import org.example.model.commands.Save;
import org.example.model.shape.*;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    public CheckBox gridButton;
    public Pane drawingPane;
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
    private ShapeInterface selectedShapeForContextMenu = null;
    private ContextMenu contextMenu = new ContextMenu();

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
                selectedShapeForContextMenu.setFillColor(changeFillColorPicker.getValue());
            }
        });

        changeStrokeColorPicker.setOnAction(event -> {
            if (selectedShapeForContextMenu != null) {
                selectedShapeForContextMenu.setStrokeColor(changeStrokeColorPicker.getValue());
            }
        });

        // Mostra il menu su clic destro dentro `drawingPane`
        drawingPane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                Node clickedShape = findShapeAt(event.getX(), event.getY());
                if (clickedShape instanceof ShapeInterface) {
                    selectedShapeForContextMenu = (ShapeInterface) clickedShape;

                    // Imposta i color picker con i colori attuali della shape selezionata
                    changeFillColorPicker.setValue(selectedShapeForContextMenu.getFillColor());
                    changeStrokeColorPicker.setValue(selectedShapeForContextMenu.getStrokeColor());

                    contextMenu.show(drawingPane, event.getScreenX(), event.getScreenY());
                } else {
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

        changeFillColorPicker.setOnAction(event -> {
            if (selectedShapeForContextMenu != null) {
                // Rimuove la shape selezionata
                /*((Shape) selectedShapeForContextMenu).setFill(changeFillColorPicker.getValue());*/
                drawingPane.getChildren().remove(selectedShapeForContextMenu);

                Color newFillColor = changeFillColorPicker.getValue();
                selectedShapeForContextMenu.setFillColor(newFillColor);

                String shapeName = selectedShapeForContextMenu.getClass().getSimpleName().replace("Shape", "");

                AbstractShape abstractShape = null;

                switch (shapeName) {
                    case "Ellipse":
                        abstractShape = createEllipse(
                                selectedShapeForContextMenu.getStrokeColor(),
                                newFillColor,
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    case "Line":
                        abstractShape = createLine(
                                selectedShapeForContextMenu.getStrokeColor(),
                                newFillColor,
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    case "Rectangle":
                        abstractShape = createRectangle(
                                selectedShapeForContextMenu.getStrokeColor(),
                                newFillColor,
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    default:
                        System.out.println("Forma non riconosciuta: " + shapeName);
                }

                if (abstractShape != null) {
                    ShapeInterface shapeInterface = abstractShape.getShape();

                    if (shapeInterface instanceof EllipseShape) {
                        EllipseShape ellipse = (EllipseShape) shapeInterface;
                        ellipse.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = ellipse;
                        drawingPane.getChildren().add(ellipse);
                    } else if (shapeInterface instanceof LineShape) {
                        LineShape line = (LineShape) shapeInterface;
                        line.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = line;
                        drawingPane.getChildren().add(line);
                    } else if (shapeInterface instanceof RectangleShape) {
                        RectangleShape rectangle = (RectangleShape) shapeInterface;
                        rectangle.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = rectangle;
                        drawingPane.getChildren().add(rectangle);
                    }
                }
            }
        });


        changeStrokeColorPicker.setOnAction(event -> {
            /*((Ellipse) selectedShapeForContextMenu).setStroke(changeStrokeColorPicker.getValue());*/
            if (selectedShapeForContextMenu != null) {
                drawingPane.getChildren().remove(selectedShapeForContextMenu);

                Color newStrokeColor = changeStrokeColorPicker.getValue();
                selectedShapeForContextMenu.setStrokeColor(newStrokeColor);

                String shapeName = selectedShapeForContextMenu.getClass().getSimpleName().replace("Shape", "");

                AbstractShape abstractShape = null;

                switch (shapeName) {
                    case "Ellipse":
                        abstractShape = createEllipse(
                                newStrokeColor,
                                selectedShapeForContextMenu.getFillColor(),
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    case "Line":
                        abstractShape = createLine(
                                newStrokeColor,
                                selectedShapeForContextMenu.getFillColor(),
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    case "Rectangle":
                        abstractShape = createRectangle(
                                newStrokeColor,
                                selectedShapeForContextMenu.getFillColor(),
                                selectedShapeForContextMenu.getCustomStrokeWidth(),
                                selectedShapeForContextMenu.getWidth(),
                                selectedShapeForContextMenu.getHeight()
                        );
                        break;
                    default:
                        System.out.println("Forma non riconosciuta: " + shapeName);
                }

                if (abstractShape != null) {
                    ShapeInterface shapeInterface = abstractShape.getShape();

                    if (shapeInterface instanceof EllipseShape) {
                        EllipseShape ellipse = (EllipseShape) shapeInterface;
                        ellipse.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = ellipse;
                        drawingPane.getChildren().add(ellipse);
                    } else if (shapeInterface instanceof LineShape) {
                        LineShape line = (LineShape) shapeInterface;
                        line.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = line;
                        drawingPane.getChildren().add(line);
                    } else if (shapeInterface instanceof RectangleShape) {
                        RectangleShape rectangle = (RectangleShape) shapeInterface;
                        rectangle.draw(selectedShapeForContextMenu.getX(), selectedShapeForContextMenu.getY());
                        selectedShapeForContextMenu = rectangle;
                        drawingPane.getChildren().add(rectangle);
                    }
                }
            }
        });

    }

    private Node findShapeAt(double x, double y) {
        for (Node node : drawingPane.getChildren()) {
            if (node instanceof ShapeInterface) {
                ShapeInterface shape = (ShapeInterface) node;
                // Verifica approssimata della presenza del click nella shape
                if (Math.abs(shape.getX() - x) <= 40 && Math.abs(shape.getY() - y) <= 80) {
                    return node;
                }
            }
        }
        return null;
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
            if(!contextMenu.isShowing()) {
                ShapeOption selectedShape = shapeComboBox.getValue();
                if (selectedShape != null) {
                    // coordinate locali del clic sul Canvas
                    Point2D localCoords = mainCanvasContainer.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                    double x = localCoords.getX();
                    double y = localCoords.getY();
                    switch (selectedShape.getName()) {
                        case "Ellipse": {
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
                            AbstractShape abstractLine = createLine(borderColorPicker.getValue(), fillColorPicker.getValue(), 4, 80, 40);
                            ShapeInterface shapeInterface = abstractLine.getShape();
                            if (shapeInterface instanceof LineShape) {
                                LineShape lineShape = (LineShape) shapeInterface;
                                lineShape.draw(x, y);
                                drawingPane.getChildren().add(lineShape);
                            }
                            break;
                        }
                        case "Rectangle": {
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

    /**
     * @param event
     * Calls the function to save the file
     */
    @FXML
    private void saveFile(ActionEvent event) {
        Save saveFile = new Save(drawingPane);
        System.out.println(drawingPane.getChildren().size());
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

