package org.example.model.shape;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class ContextMenuManager {

    public static class MenuComponents  {

        public final ContextMenu contextMenu;
        public final ColorPicker fillColorPicker;
        public final ColorPicker strokeColorPicker;

        public MenuComponents(ContextMenu contextMenu, ColorPicker fillColorPicker, ColorPicker strokeColorPicker) {
            this.contextMenu = contextMenu;
            this.fillColorPicker = fillColorPicker;
            this.strokeColorPicker = strokeColorPicker;
        }
    }

    /*
     * Creates a complitely configured context menu with event handlers
     */
    public static MenuComponents createContextMenu(ShapeInterface shape, ContextMenuActions actions) {

        ContextMenu contextMenu = new ContextMenu();

        // Color Management
        ColorPicker fillPicker = null;
        ColorPicker strokePicker = new ColorPicker(shape.getStrokeColor());

        // Resize mode buttons
        MenuItem stretch = new MenuItem("Stretch");
        MenuItem resize  = new MenuItem("Resize");

        // Resize panels (initially hidden)
        CustomMenuItem stretchPanel = createStretchItem(shape, actions::onStretch);
        CustomMenuItem resizePanel  = createResizeItem(shape, actions::onResize);
        stretchPanel.setVisible(false);
        resizePanel.setVisible(false);

        MenuItem ungroupItem = null;

        //UngroupMenu
        if(shape.isGroup()){
            ungroupItem = new MenuItem("Ungroup");
            ungroupItem.setOnAction(e->actions.onUngroup());
        }

        // Fill Color (for line must be null)
        if (shape.getFillColor() != null) {
            fillPicker = createColorPickerItem(
                    contextMenu,
                    "Change Fill Color:",
                    shape.getFillColor() != null ? shape.getFillColor() : shape.getStrokeColor(),
                    newColor -> actions.onFillColorChanged(newColor)
            );
        }
        // Stroke Color
        createColorPickerItem(contextMenu, "Change Border Color:", shape.getStrokeColor(), actions::onStrokeColorChanged);

        // Send to Back
        CustomMenuItem sendToBackItem = new CustomMenuItem(new Label("Send to Back"));
        sendToBackItem.setOnAction(e -> actions.onSendToBack());

        // Bring to Front
        CustomMenuItem bringToFrontItem = new CustomMenuItem(new Label("Bring to Front"));
        bringToFrontItem.setOnAction(e -> actions.onBringToFront());

        //Copy shape
        MenuItem copyItem = new MenuItem("Copy");
        copyItem.setOnAction(e -> actions.onCopy());

        //Cut shape
        MenuItem cutItem = new MenuItem("Cut");
        cutItem.setOnAction(e -> actions.onCut());

        //Paste shape
        MenuItem pasteItem = new MenuItem("Paste");
        pasteItem.setOnAction(e -> actions.onPaste());

        //Delete shape
        MenuItem deleteItem = new MenuItem("Delete");
        deleteItem.setOnAction(e -> actions.onDelete());

        // Resize shape
        Label visualLabel = new Label("Resize");
        CustomMenuItem visualBtn = new CustomMenuItem(visualLabel);
        visualBtn.setHideOnClick(false);
        visualBtn.setOnAction(e -> {
            resizePanel.setVisible(true);
        });

        // stretch shape
        Label numericLabel = new Label("Stretch");
        CustomMenuItem numericBtn = new CustomMenuItem(numericLabel);
        numericBtn.setHideOnClick(false);
        numericBtn.setOnAction(e -> {
            stretchPanel.setVisible(true);
        });

        Menu mirroringMenu = new Menu("Flip");

        // Voci del sottomenù
        MenuItem verticalUp = new MenuItem("Vertical Up");
        verticalUp.setOnAction(e -> actions.onMirrorUp());

        MenuItem verticalDown = new MenuItem("Vertical Down");
        verticalDown.setOnAction(e -> actions.onMirrorDown());

        MenuItem verticalCenter = new MenuItem("Vertical Around Center");
        verticalCenter.setOnAction(e -> actions.onMirrorVerticalAroundCenter());

        MenuItem horizontalLeft = new MenuItem("Horizontal Left");
        horizontalLeft.setOnAction(e -> actions.onMirrorLeft());

        MenuItem horizontalRight = new MenuItem("Horizontal Right");
        horizontalRight.setOnAction(e -> actions.onMirrorRight());

        MenuItem horizontalCenter = new MenuItem("Horizontal Around Center");
        horizontalCenter.setOnAction(e -> actions.onMirrorHorizontalAroundCenter());



        // Aggiunta voci al sottomenù
        mirroringMenu.getItems().addAll(
                verticalUp, verticalDown,
                verticalCenter, horizontalLeft,
                horizontalRight,
                horizontalCenter
        );


        // Save shape as favorite
        Label addToFavoritesLabel = new Label("Add to Favorites");
        CustomMenuItem addToFavorites = new CustomMenuItem(addToFavoritesLabel);
        addToFavorites.setHideOnClick(false);
        addToFavorites.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("MyFavorite");
            dialog.setHeaderText("Enter a name for this favorite shape:");
            dialog.showAndWait().ifPresent(name -> {
                actions.onAddToFavoritesWithName(name);  // da definire nel controller
            });
        });

        // Add items
        contextMenu.getItems().addAll(numericBtn, visualBtn, resizePanel, stretchPanel);
        if(ungroupItem != null) {
            contextMenu.getItems().addAll(sendToBackItem, bringToFrontItem, cutItem, copyItem, pasteItem, deleteItem, mirroringMenu, ungroupItem, addToFavorites);
        }
        else{
            contextMenu.getItems().addAll(sendToBackItem, bringToFrontItem, cutItem, copyItem, pasteItem, deleteItem, mirroringMenu, addToFavorites);
        }
        //contextMenu.getItems().addAll(numericBtn, visualBtn, numericPanel, visualPanel);
        //contextMenu.getItems().addAll(sendToBackItem, bringToFrontItem, cutItem, copyItem, pasteItem, deleteItem, mirroringMenu, addToFavorites);

        return new MenuComponents(contextMenu, fillPicker, strokePicker);
    }

    private static ColorPicker createColorPickerItem(ContextMenu contextMenu, String labelText, Color initialColor, ColorChangeHandler handler) {
        ColorPicker colorPicker = new ColorPicker(initialColor);
        Label label = new Label(labelText);
        HBox box = new HBox(10, label, colorPicker);
        CustomMenuItem item = new CustomMenuItem(box);
        item.setHideOnClick(false);

        colorPicker.setOnAction(e -> handler.onColorChanged(colorPicker.getValue()));

        contextMenu.getItems().add(item);
        return colorPicker;
    }

    private static CustomMenuItem createStretchItem(ShapeInterface shape, StretchHandler stretchHandler) {

        // Check if it's a text or a line
        boolean isTextShape = shape.getText() != null;
        boolean isLineShape = shape.getFillColor() == null;

        // get old shape dimensions (WxH) do display in the textfield
        TextField w = (isTextShape || isLineShape) ? new TextField("") : new TextField(String.format("%.1f",shape.getWidth()));
        TextField h = (isTextShape || isLineShape) ? new TextField("") : new TextField(String.format("%.1f", shape.getHeight()));
        w.setPrefWidth(60); h.setPrefWidth(60);

        Button applyBtn = new Button("Apply");
        applyBtn.setPrefWidth(60);

        // invalid input warning
        Label warningLabel = new Label();
        warningLabel.setStyle("-fx-text-fill: red;");

        HBox inputsBox = new HBox(10, new Label("W:"), w, new Label("H:"), h, applyBtn);
        VBox wrapper = new VBox(5, inputsBox, warningLabel);
        CustomMenuItem item = new CustomMenuItem(wrapper);
        item.setHideOnClick(false);

        Runnable apply = () -> {
            try {
                // get newWidth and newHeight value from the textfield
                double newWidth = Double.parseDouble(w.getText());
                double newHeight = Double.parseDouble(h.getText());



                double maxLimit = isTextShape ? 10.0 : 300.0;

                // Validation: checks for element
                boolean widthValid = newWidth > 0 && newWidth <= maxLimit;
                boolean heightValid = newHeight > 0 && newHeight <= maxLimit;

                // if newWidth and newHeight are too big
                if (widthValid && heightValid) {
                    Double oldWidth = shape.getStretchWidth();
                    Double oldHeight = shape.getStretchHeight();
                    // Reset border for bad input
                    w.setStyle("");
                    h.setStyle("");
                    // Clear warning
                    warningLabel.setText("");
                    stretchHandler.onStretch(oldWidth, oldHeight, newWidth, newHeight);
                } else {
                    // Highlights only invalid fields
                    if (!widthValid) {
                        w.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                    } else {
                        w.setStyle("");
                    }
                    if (!heightValid) {
                        h.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                    } else {
                        h.setStyle("");
                    }
                    // Shows warning
                    if (isTextShape) {
                        warningLabel.setText("Max stretch value must be positive and can't be over 10x10");
                    } else {
                        warningLabel.setText("Max stretch value must be positive and can't be over 300x300");
                    }
                }
            } catch (NumberFormatException e) {
                w.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                h.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                warningLabel.setText("Invalid number format.");
            }
        };
        // Apply changes when button gets pressed
        applyBtn.setOnAction(e -> apply.run());
        // Handle enter event on textfields
        w.setOnAction(e -> apply.run());
        h.setOnAction(e -> apply.run());

        return item;
    }

    private static CustomMenuItem createResizeItem(ShapeInterface shape, ResizeHandler resizeHandler) {
        double currentSize = shape.getEffectiveSize();
        double minSize = shape.getMinSliderSize();
        double maxSize = shape.getMaxSliderSize();

        Slider slider = new Slider(minSize, maxSize, currentSize);
        Label valLabel = new Label(shape.getSliderLabel(slider.getValue()));

        Button applyBtn = new Button("Apply");
        applyBtn.setPrefWidth(60);

        slider.setPrefWidth(150);
        slider.setShowTickLabels(true);

        // Only update label, no visual changes to shape
        slider.valueProperty().addListener((obs, oldV, newV) -> {
            double newSize = newV.doubleValue();
            valLabel.setText(shape.getSliderLabel(newSize));
        });

        // Apply changes only when Apply button is clicked
        applyBtn.setOnAction(e -> {
            //get previous shape dimensions (WxH)
            double oldWidth = shape.getWidth();
            double oldHeight = shape.getHeight();
            double newSize = slider.getValue();
            resizeHandler.onResize(oldWidth, oldHeight, newSize);
        });

        HBox box = new HBox(10, slider, valLabel, applyBtn);
        CustomMenuItem item = new CustomMenuItem(box);
        item.setHideOnClick(false);
        return item;
    }

    @FunctionalInterface
    private interface ColorChangeHandler {
        void onColorChanged(Color color);
    }

    @FunctionalInterface
    public interface StretchHandler {
        void onStretch(double oldWidth, double oldHeight, double newWidth, double newHeight);
    }

    @FunctionalInterface
    public interface ResizeHandler {
        void onResize(double oldWidth, double oldHeight, double newSize);
    }
}