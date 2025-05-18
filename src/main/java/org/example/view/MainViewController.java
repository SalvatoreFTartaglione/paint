package org.example.view;

import java.net.URL;
import java.util.List;
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
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import org.example.controller.ShapeOption;
import org.example.model.shape.*;

public class MainViewController implements Initializable {

    @FXML
    public CheckBox gridButton;
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
    private Canvas drawingCanvas;
    @FXML
    private Canvas gridCanvas;
    @FXML
    private Label gridOnLabel;
    @FXML
    private Label gridOffLabel;

    private boolean isDragging = false;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridCanvas.setDisable(true);
        gridCanvas.setVisible(false); // griglia nascosta inizialmente

        drawingCanvas.setDisable(false);
        drawingCanvas.setVisible(true); // canvas delle forme sempre visibile

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

        scrollPane.setOnMousePressed(event -> {
            if (event.isSecondaryButtonDown()) {
                contextMenu.show(scrollPane, event.getScreenX(), event.getScreenY());
            } else {
                contextMenu.hide();
            }
        });
    }

    private void drawGrid() {
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
    }


    private void initializeShapeChoiceBox() {
        List<ShapeOption> shapes = List.of(
                new ShapeOption("Ellipse", "/org/example/images/ellipse.png"),
                new ShapeOption("Line", "/org/example/images/segment.png"),
                new ShapeOption("Rectangle", "/org/example/images/rectangle.png")
        );

        shapeComboBox.getItems().addAll(shapes);

        // Imposto la cell factory per mostrare nome + immagine
        shapeComboBox.setCellFactory(lv -> new ListCell<ShapeOption>() {
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
                    imageView.setFitHeight(20);  // opzionale: scala l'immagine
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
                // coordinate locali del clic sul Canvas
                Point2D localCoords = mainCanvasContainer.sceneToLocal(mouseEvent.getSceneX(), mouseEvent.getSceneY());
                double x = localCoords.getX();
                double y = localCoords.getY();
                switch (selectedShape.getName()) {
                    case "Ellipse": {
                        AbstractShape abstractEllipse = createEllipse(borderColorPicker.getValue(), fillColorPicker.getValue(), 2, 40, 20);
                        ShapeInterface shapeInterface = abstractEllipse.getShape();
                        //controllo che la shape selezionata sia un'ellisse
                        if (shapeInterface instanceof EllipseShape) {
                            EllipseShape ellipseShape = (EllipseShape) shapeInterface;
                            ellipseShape.draw(x, y);
                            // Aggiungi l'ellisse al contenitore del Canvas
                            mainCanvasContainer.getChildren().add(ellipseShape);
                        }
                        break;
                    }
                    case "Line": {
                        AbstractShape abstractLine = createLine(borderColorPicker.getValue(), fillColorPicker.getValue(), 2, 40, 20);
                        ShapeInterface shapeInterface = abstractLine.getShape();
                        if (shapeInterface instanceof LineShape) {
                            LineShape lineShape = (LineShape) shapeInterface;
                            lineShape.draw(x, y);
                            mainCanvasContainer.getChildren().add(lineShape);
                        }
                        break;
                    }
                    case "Rectangle": {
                        AbstractShape abstractRectangle = createRectangle(borderColorPicker.getValue(), fillColorPicker.getValue(), 2, 40, 20);
                        ShapeInterface shapeInterface = abstractRectangle.getShape();
                        if (shapeInterface instanceof RectangleShape) {
                            RectangleShape rectangleShape = (RectangleShape) shapeInterface;
                            rectangleShape.draw(x, y);
                            mainCanvasContainer.getChildren().add(rectangleShape);
                        }
                        break;
                    }
                }
            }
        }
    }

    private AbstractShape createRectangle(Color borderColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape rectangle = new RectangleCreator();
        rectangle.creationOfShape(borderColor,fillColor,borderWidth, width, height);
        return rectangle;
    }

    private AbstractShape createLine(Color borderColor, Color fillColor, int borderWidth, double width, double height) {
        AbstractShape line = new LineCreator();
        line.creationOfShape(borderColor, fillColor, borderWidth, width, height);
        return line;
    }

    private AbstractShape createEllipse(Color borderColor, Color fillColor, int borderWidth, int width, int height) {
        AbstractShape ellipse = new EllipseCreator();
        ellipse.creationOfShape(borderColor, fillColor, borderWidth, width, height);
        return ellipse;
    }


    @FXML
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
    }



}

