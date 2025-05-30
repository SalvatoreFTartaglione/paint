package org.example.model.shape;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;
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
        MenuItem numeric = new MenuItem("Resize Numerically");
        MenuItem visual  = new MenuItem("Resize Visually");

        // Resize panels (initially hidden)
        CustomMenuItem numericPanel = createResizeNumericItem(shape, actions::onResize);
        CustomMenuItem visualPanel  = createResizeVisualItem(shape, actions::onResize);
        numericPanel.setVisible(false);
        visualPanel.setVisible(false);

        // Fill Color (for line must be null)
        if (shape.getFillColor() != null) {
            fillPicker = createColorPickerItem(contextMenu, "Change Fill Color:", shape.getFillColor(), actions::onFillColorChanged);
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

        // Toggle visibility of respective panels
        Label numericLabel = new Label("Numerical Resize");
        CustomMenuItem numericBtn = new CustomMenuItem(numericLabel);
        numericBtn.setHideOnClick(false);
        numericBtn.setOnAction(e -> {
            numericPanel.setVisible(true);
            visualPanel.setVisible(false);
        });

        Label visualLabel = new Label("Visual Resize");
        CustomMenuItem visualBtn = new CustomMenuItem(visualLabel);
        visualBtn.setHideOnClick(false);
        visualBtn.setOnAction(e -> {
            numericPanel.setVisible(false);
            visualPanel.setVisible(true);
        });

        // Vertical mirroring up
        Label verticalMirroringUpLabel = new Label("Vertical Mirroring Up");
        CustomMenuItem verticalMirroringUp = new CustomMenuItem(verticalMirroringUpLabel);
        verticalMirroringUp.setHideOnClick(false);
        verticalMirroringUp.setOnAction(e -> {actions.onMirrorUp();});

        // Vertical mirroring down
        Label verticalMirroringLabel = new Label("Vertical Mirroring Down");
        CustomMenuItem verticalMirroringDown = new CustomMenuItem(verticalMirroringLabel);
        verticalMirroringDown.setHideOnClick(false);
        verticalMirroringDown.setOnAction(e -> {actions.onMirrorDown();});

        // Horizontal mirroring left
        Label horizontalMirroringLeftLabel = new Label("Horizontal Mirroring Left");
        CustomMenuItem horizontalMirroringLeft = new CustomMenuItem(horizontalMirroringLeftLabel);
        horizontalMirroringLeft.setHideOnClick(false);
        horizontalMirroringLeft.setOnAction(e -> {actions.onMirrorLeft();});

        // Horizontal mirroring right
        Label horizontalMirroringRightLabel = new Label("Horizontal Mirroring Right");
        CustomMenuItem horizontalMirroringRight = new CustomMenuItem(horizontalMirroringRightLabel);
        horizontalMirroringRight.setHideOnClick(false);
        horizontalMirroringRight.setOnAction(e -> {actions.onMirrorRight();});

        // Add items
        //contextMenu.getItems().addAll(numericBtn, visualBtn, numericPanel, visualPanel);
        contextMenu.getItems().addAll(sendToBackItem, bringToFrontItem, cutItem, copyItem, pasteItem, deleteItem, verticalMirroringUp,verticalMirroringDown, horizontalMirroringLeft, horizontalMirroringRight);

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

    @FunctionalInterface
    private interface ColorChangeHandler {
        void onColorChanged(Color color);
    }

    @FunctionalInterface
    public interface ResizeHandler {
        void onResize(double newWidth, double newHeight);
    }

    private static CustomMenuItem createResizeNumericItem(ShapeInterface shape, ResizeHandler resizeHandler) {
        TextField w = new TextField(String.format("%.1f", shape.getWidth()));
        TextField h = new TextField(String.format("%.1f", shape.getHeight()));
        w.setPrefWidth(60); h.setPrefWidth(60);

        Button applyBtn = new Button("Apply");
        applyBtn.setPrefWidth(60);

        HBox box = new HBox(10, new Label("W:"), w, new Label("H:"), h, applyBtn);
        CustomMenuItem item = new CustomMenuItem(box);
        item.setHideOnClick(false);

        Runnable apply = () -> {
            try {
                double nw = Double.parseDouble(w.getText());
                double nh = Double.parseDouble(h.getText());

                // Validation: checks for element > 300
                boolean widthValid = nw > 0 && nw < 300;
                boolean heightValid = nh > 0 && nh < 300;

                if (widthValid && heightValid) {
                    // Reset border for bad input
                    w.setStyle("");
                    h.setStyle("");
                    resizeHandler.onResize(nw, nh);
                } else {
                    // Higlight field with bad values
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
                }

            } catch (NumberFormatException e) {
                // Red border
                w.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
                h.setStyle("-fx-border-color: red; -fx-border-width: 3px;");
            }
        };

        // Applica quando viene premuto il pulsante
        applyBtn.setOnAction(e -> apply.run());

        // Gestisci l'evento Enter sui text field
        w.setOnAction(e -> apply.run());
        h.setOnAction(e -> apply.run());

        return item;
    }

    private static CustomMenuItem createResizeVisualItem(ShapeInterface shape, ResizeHandler resizeHandler) {
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
            double finalSize = slider.getValue();
            shape.scaleToSize(finalSize);
            resizeHandler.onResize(shape.getWidth(), shape.getHeight());
        });

        HBox box = new HBox(10, slider, valLabel, applyBtn);
        CustomMenuItem item = new CustomMenuItem(box);
        item.setHideOnClick(false);
        return item;
    }
}