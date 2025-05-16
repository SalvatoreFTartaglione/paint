package org.example.view;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.util.StringConverter;
import org.example.controller.ShapeOption;

public class MainViewController implements Initializable {

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
    private StackPane mainCanvasContainer;
    @FXML
    private Canvas noGridCanvas;
    @FXML
    private Canvas gridCanvas;
    @FXML
    private Label gridOnLabel;
    @FXML
    private Label gridOffLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gridCanvas.setDisable(true);
        noGridCanvas.setDisable(false);
        initializeSplitPane();
        initializeShapeChoiceBox();
        initializeContextMenu();
    }

    private void initializeContextMenu() {
        ContextMenu contextMenu = new ContextMenu();

        CheckMenuItem gridItem = getCheckMenuItem();

        contextMenu.getItems().add(gridItem);

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


    private CheckMenuItem getCheckMenuItem() {
        CheckMenuItem gridItem = new CheckMenuItem("Grid");

        // Inizializza lo stato coerente all’avvio
        gridItem.setSelected(gridCanvas.isVisible());

        gridItem.setOnAction(e -> {
            boolean selected = gridItem.isSelected();

            gridCanvas.setVisible(selected);
            noGridCanvas.setVisible(!selected);

            gridOnLabel.setVisible(selected);
            gridOffLabel.setVisible(!selected);

            if (selected) {
                drawGrid();
            }
        });
        return gridItem;
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


}

