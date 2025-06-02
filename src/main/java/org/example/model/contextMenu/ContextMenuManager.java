package org.example.model.contextMenu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.model.shape.ShapeInterface;

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
    public static MenuComponents createContextMenu(ShapeInterface shape, ContextMenuActions actions){

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.getStyleClass().add("custom-context-menu");

        ColorPicker fillPicker = null;
        ColorPicker strokePicker = new ColorPicker(shape.getStrokeColor());

        // === COLORS ===
        addSectionHeader(contextMenu, "COLORS");
        if (shape.getFillColor() != null) {
            fillPicker = createColorPickerItem(contextMenu, "Fill Color", shape.getFillColor(), actions::onFillColorChanged);
        }
        createColorPickerItem(contextMenu, "Border Color", shape.getStrokeColor(), actions::onStrokeColorChanged);

        // === TRANSFORMATIONS ===
        addSectionHeader(contextMenu, "TRANSFORMATIONS");
        // Control Panels
        CustomMenuItem resizePanel = createResizeItem(shape, actions::onResize);
        CustomMenuItem stretchPanel = createStretchItem(shape, actions::onStretch);
        CustomMenuItem rotatePanel = createRotateItem(shape, actions::onRotate);
        CustomMenuItem flipPanel = createFlipPanel(actions);
        resizePanel.setVisible(false);
        stretchPanel.setVisible(false);
        rotatePanel.setVisible(false);
        flipPanel.setVisible(false);

        // Buttons to show panels
        addTransformationButton(contextMenu, "Resize", () -> {
            resizePanel.setVisible(true);
            stretchPanel.setVisible(false);
            rotatePanel.setVisible(false);
            flipPanel.setVisible(false);
        });
        contextMenu.getItems().add(resizePanel);

        if(!shape.isGroup()) {
            addTransformationButton(contextMenu, "Stretch", () -> {
                stretchPanel.setVisible(true);
                resizePanel.setVisible(false);
                rotatePanel.setVisible(false);
                flipPanel.setVisible(false);
            });
            contextMenu.getItems().add(stretchPanel);
        }
        addTransformationButton(contextMenu, "Rotate", () -> {
            rotatePanel.setVisible(true);
            resizePanel.setVisible(false);
            stretchPanel.setVisible(false);
            flipPanel.setVisible(false);
        });
        contextMenu.getItems().add(rotatePanel);


        addTransformationButton(contextMenu, "Flip", () -> {
            flipPanel.setVisible(true);
            resizePanel.setVisible(false);
            stretchPanel.setVisible(false);
            rotatePanel.setVisible(false);
        });
        contextMenu.getItems().add(flipPanel);

        // === LAYER SELECTION ===
        addSectionHeader(contextMenu, "LAYERS");
        addMenuItem(contextMenu, "Send to Back", actions::onSendToBack);
        addMenuItem(contextMenu, "Bring to Front", actions::onBringToFront);

        // === CLIPBOARD SELECTION ===
        addSectionHeader(contextMenu, "CLIPBOARD");
        addMenuItem(contextMenu, "Copy", actions::onCopy);
        addMenuItem(contextMenu, "Cut", actions::onCut);
        addMenuItem(contextMenu, "Paste", actions::onPaste);

        // === ACTIONS SELECTION ===
        addSectionHeader(contextMenu, "ACTIONS");
        // Save shape as favorite
        Label addToFavoritesLabel = new Label("Add to Favorites");
        CustomMenuItem addToFavorites = new CustomMenuItem(addToFavoritesLabel);
        addToFavorites.setHideOnClick(false);
        addToFavorites.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("MyFavorite");
            dialog.setHeaderText("Enter a name for this favorite shape:");
            dialog.showAndWait().ifPresent(name -> {
                actions.onAddToFavoritesWithName(name);
            });
        });

        if (shape.isGroup()) {
            addMenuItem(contextMenu, "Ungroup", actions::onUngroup);
        }

        addFavoritesItem(contextMenu, actions);
        addMenuItem(contextMenu, "Delete", actions::onDelete);

        return new MenuComponents(contextMenu, fillPicker, strokePicker);
    }

    private static void addSectionHeader(ContextMenu contextMenu, String text) {
        if (!contextMenu.getItems().isEmpty()) {
            contextMenu.getItems().add(new SeparatorMenuItem());
        }
        Label header = new Label(text);
        header.getStyleClass().add("menu-section-header");
        CustomMenuItem headerItem = new CustomMenuItem(header);
        headerItem.setHideOnClick(false);
        contextMenu.getItems().add(headerItem);
    }

    private static void addMenuItem(ContextMenu contextMenu, String text, Runnable action) {
        Label label = new Label(text);
        label.getStyleClass().add("menu-item-label");
        CustomMenuItem item = new CustomMenuItem(label);
        item.setOnAction(e -> action.run());
        contextMenu.getItems().add(item);
    }

    private static void addTransformationButton(ContextMenu contextMenu, String text, Runnable action) {
        Label label = new Label("▶ " + text);
        label.getStyleClass().add("menu-item-label");
        CustomMenuItem item = new CustomMenuItem(label);
        item.setHideOnClick(false);
        item.setOnAction(e -> action.run());
        contextMenu.getItems().add(item);
    }

    private static ColorPicker createColorPickerItem(ContextMenu contextMenu, String labelText, Color initialColor, ColorChangeHandler handler) {
        ColorPicker colorPicker = new ColorPicker(initialColor);
        colorPicker.setPrefWidth(100);

        Label label = new Label(labelText);
        label.setPrefWidth(80);

        HBox container = new HBox(label, colorPicker);
        container.getStyleClass().add("color-picker-container");
        container.setAlignment(Pos.CENTER_LEFT);
        container.setSpacing(10);

        CustomMenuItem item = new CustomMenuItem(container);
        item.setHideOnClick(false);

        colorPicker.setOnAction(e -> handler.onColorChanged(colorPicker.getValue()));
        contextMenu.getItems().add(item);

        return colorPicker;
    }

    private static CustomMenuItem createFlipPanel(ContextMenuActions actions) {
        VBox buttons = new VBox(5);
        buttons.getStyleClass().add("control-panel");

        Button up = new Button("↑ Vertical Up");
        Button down = new Button("↓ Vertical Down");
        Button vCenter = new Button("↕ Vertical Center");
        Button left = new Button("← Horizontal Left");
        Button right = new Button("→ Horizontal Right");
        Button hCenter = new Button("↔ Horizontal Center");

        up.setPrefWidth(180);
        down.setPrefWidth(180);
        vCenter.setPrefWidth(180);
        left.setPrefWidth(180);
        right.setPrefWidth(180);
        hCenter.setPrefWidth(180);

        up.setOnAction(e -> actions.onMirrorUp());
        down.setOnAction(e -> actions.onMirrorDown());
        vCenter.setOnAction(e -> actions.onMirrorVerticalAroundCenter());
        left.setOnAction(e -> actions.onMirrorLeft());
        right.setOnAction(e -> actions.onMirrorRight());
        hCenter.setOnAction(e -> actions.onMirrorHorizontalAroundCenter());

        buttons.getChildren().addAll(up, down, vCenter, left, right, hCenter);

        CustomMenuItem flipPanel = new CustomMenuItem(buttons);
        flipPanel.setHideOnClick(false);

        return flipPanel;
    }


    private static CustomMenuItem createStretchItem(ShapeInterface shape, StretchHandler stretchHandler) {
        boolean isTextShape = shape.getText() != null;
        boolean isLineShape = shape.getFillColor() == null;

        TextField widthField = new TextField(isTextShape || isLineShape ? "" : String.format("%.1f", shape.getWidth()));
        TextField heightField = new TextField(isTextShape || isLineShape ? "" : String.format("%.1f", shape.getHeight()));

        widthField.getStyleClass().add("control-input");
        heightField.getStyleClass().add("control-input");
        widthField.setPrefWidth(70);
        heightField.setPrefWidth(70);
        widthField.setPromptText("Width");
        heightField.setPromptText("Height");

        Button applyBtn = new Button("Apply");
        applyBtn.getStyleClass().add("control-button");
        applyBtn.setPrefWidth(70);

        Label warningLabel = new Label();
        warningLabel.getStyleClass().add("warning-label");

        HBox inputsBox = new HBox(8);
        inputsBox.getChildren().addAll(
                new Label("W:"), widthField,
                new Label("H:"), heightField,
                applyBtn
        );
        inputsBox.setAlignment(Pos.CENTER_LEFT);

        VBox container = new VBox(8, inputsBox, warningLabel);
        container.getStyleClass().add("control-panel");

        CustomMenuItem item = new CustomMenuItem(container);
        item.setHideOnClick(false);

        Runnable apply = () -> {
            try {
                double newWidth = Double.parseDouble(widthField.getText());
                double newHeight = Double.parseDouble(heightField.getText());
                double maxLimit = isTextShape ? 10.0 : 300.0;

                boolean widthValid = newWidth > 0 && newWidth <= maxLimit;
                boolean heightValid = newHeight > 0 && newHeight <= maxLimit;

                if (widthValid && heightValid) {
                    widthField.getStyleClass().remove("input-error");
                    heightField.getStyleClass().remove("input-error");
                    warningLabel.setText("");

                    Double oldWidth = shape.getStretchWidth();
                    Double oldHeight = shape.getStretchHeight();
                    stretchHandler.onStretch(oldWidth, oldHeight, newWidth, newHeight);
                } else {
                    if (!widthValid) {
                        widthField.getStyleClass().add("input-error");
                    } else {
                        widthField.getStyleClass().remove("input-error");
                    }
                    if (!heightValid) {
                        heightField.getStyleClass().add("input-error");
                    } else {
                        heightField.getStyleClass().remove("input-error");
                    }

                    String limit = isTextShape ? "10x10" : "300x300";
                    warningLabel.setText("Values must be positive and ≤ " + limit);
                }
            } catch (NumberFormatException e) {
                widthField.getStyleClass().add("input-error");
                heightField.getStyleClass().add("input-error");
                warningLabel.setText("Please enter valid numbers");
            }
        };

        applyBtn.setOnAction(e -> apply.run());
        widthField.setOnAction(e -> apply.run());
        heightField.setOnAction(e -> apply.run());

        return item;
    }

    private static CustomMenuItem createResizeItem(ShapeInterface shape, ResizeHandler resizeHandler) {
        double currentSize = shape.getEffectiveSize();
        double minSize = shape.getMinSliderSize();
        double maxSize = shape.getMaxSliderSize();

        Slider slider = new Slider(minSize, maxSize, currentSize);
        slider.setPrefWidth(180);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);

        Label valueLabel = new Label(shape.getSliderLabel(slider.getValue()));
        valueLabel.setPrefWidth(80);
        valueLabel.setAlignment(Pos.CENTER_RIGHT);

        Button applyBtn = new Button("Apply");
        applyBtn.getStyleClass().add("control-button");
        applyBtn.setPrefWidth(70);

        slider.valueProperty().addListener((obs, oldV, newV) -> {
            valueLabel.setText(shape.getSliderLabel(newV.doubleValue()));
        });

        applyBtn.setOnAction(e -> {
            double oldWidth = shape.getWidth();
            double oldHeight = shape.getHeight();
            double newSize = slider.getValue();
            resizeHandler.onResize(oldWidth, oldHeight, newSize);
        });

        HBox container = new HBox(10, slider, valueLabel, applyBtn);
        container.getStyleClass().addAll("control-panel", "slider-container");
        container.setAlignment(Pos.CENTER_LEFT);

        CustomMenuItem item = new CustomMenuItem(container);
        item.setHideOnClick(false);
        return item;
    }

    private static CustomMenuItem createRotateItem(ShapeInterface shape, RotateHandler rotateHandler) {
        TextField angleField = new TextField(String.format("%.1f", shape.getAngle()));
        angleField.getStyleClass().add("control-input");
        angleField.setPrefWidth(80);
        angleField.setPromptText("0-360°");

        Button applyBtn = new Button("Apply");
        applyBtn.getStyleClass().add("control-button");
        applyBtn.setPrefWidth(70);

        Label warningLabel = new Label();
        warningLabel.getStyleClass().add("warning-label");

        HBox inputsBox = new HBox(8, new Label("Angle:"), angleField, applyBtn);
        inputsBox.setAlignment(Pos.CENTER_LEFT);

        VBox container = new VBox(8, inputsBox, warningLabel);
        container.getStyleClass().add("control-panel");

        CustomMenuItem item = new CustomMenuItem(container);
        item.setHideOnClick(false);

        Runnable apply = () -> {
            try {
                double newAngle = Double.parseDouble(angleField.getText());
                if (newAngle >= 0 && newAngle <= 360) {
                    angleField.getStyleClass().remove("input-error");
                    warningLabel.setText("");
                    rotateHandler.onRotate(shape.getAngle(), newAngle);
                } else {
                    angleField.getStyleClass().add("input-error");
                    warningLabel.setText("Angle must be between 0° and 360°");
                }
            } catch (NumberFormatException e) {
                angleField.getStyleClass().add("input-error");
                warningLabel.setText("Please enter a valid number");
            }
        };

        applyBtn.setOnAction(e -> apply.run());
        angleField.setOnAction(e -> apply.run());

        return item;
    }



    private static void addFavoritesItem(ContextMenu contextMenu, ContextMenuActions actions) {
        Label label = new Label("★ Add to Favorites");
        label.getStyleClass().add("menu-item-label");
        CustomMenuItem item = new CustomMenuItem(label);
        item.setHideOnClick(false);
        item.setOnAction(e -> {
            TextInputDialog dialog = new TextInputDialog("MyFavorite");
            dialog.setTitle("Add to Favorites");
            dialog.setHeaderText("Enter a name for this favorite shape:");
            dialog.setContentText("Name:");
            dialog.showAndWait().ifPresent(actions::onAddToFavoritesWithName);
        });
        contextMenu.getItems().add(item);
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

    @FunctionalInterface
    public interface RotateHandler {
        void onRotate(double oldAngle, double newAngle);
    }
}