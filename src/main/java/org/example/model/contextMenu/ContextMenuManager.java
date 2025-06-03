package org.example.model.contextMenu;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.example.model.factory.product.ShapeInterface;

/**
 * The ContextMenuManager is responsible for creating and configuring context menus
 * tailored to interact with shapes and actions provided. It provides various utilities
 * to add sections, menu items, and configurable components such as color pickers.
 */
public class ContextMenuManager {

    /**
     * The MenuComponents class is a static inner class designed to encapsulate
     * the core elements required for creating and configuring a context menu.
     * It is primarily used for managing the menu components, such as the
     * context menu itself and the associated color pickers.
     */
    public static class MenuComponents  {

        public final ContextMenu contextMenu;
        public final ColorPicker fillColorPicker;
        public final ColorPicker strokeColorPicker;

        /**
         * Constructs a MenuComponents instance with the provided context menu and color pickers.
         *
         * @param contextMenu        the ContextMenu instance to be used for managing menu options
         * @param fillColorPicker    the ColorPicker instance for selecting fill colors
         * @param strokeColorPicker  the ColorPicker instance for selecting stroke colors
         */
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


        addTransformationButton(contextMenu, "Stretch", () -> {
            stretchPanel.setVisible(true);
            resizePanel.setVisible(false);
            rotatePanel.setVisible(false);
            flipPanel.setVisible(false);
        });
        contextMenu.getItems().add(stretchPanel);

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

        addFavoritesItem(contextMenu, actions);
        addMenuItem(contextMenu, "Delete", actions::onDelete);

        return new MenuComponents(contextMenu, fillPicker, strokePicker);
    }

    /**
     * Adds a section header to the given context menu. If the context
     * menu already contains items, a separator is added before the header.
     * The header is displayed with a custom style and is non-clickable.
     *
     * @param*/
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

    /**
     * Adds a menu item to the specified {@link ContextMenu} with the given text and action.
     * The menu item is styled with a specific CSS class and executes the provided action when selected.
     *
     * @param contextMenu the context menu to which the menu item will be added
     * @param text the text to be displayed on the menu item
     * @param action the action to be executed when the menu item is selected
     */
    private static void addMenuItem(ContextMenu contextMenu, String text, Runnable action) {
        Label label = new Label(text);
        label.getStyleClass().add("menu-item-label");
        CustomMenuItem item = new CustomMenuItem(label);
        item.setOnAction(e -> action.run());
        contextMenu.getItems().add(item);
    }

    /**
     * Adds a transformation button to the specified {@link ContextMenu}. The button is styled
     * with a specific indicator and executes the provided action when triggered.
     *
     * @param contextMenu the context menu to which the transformation button will be added
     * @param text the text to be displayed on the transformation button
     * @param action the action to be executed when the transformation button is selected
     */
    private static void addTransformationButton(ContextMenu contextMenu, String text, Runnable action) {
        Label label = new Label("▶ " + text);
        label.getStyleClass().add("menu-item-label");
        CustomMenuItem item = new CustomMenuItem(label);
        item.setHideOnClick(false);
        item.setOnAction(e -> action.run());
        contextMenu.getItems().add(item);
    }

    /**
     * Creates and adds a color picker item to the specified context menu. The color*/
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

    /**
     * Creates a flip panel represented by a {@link CustomMenuItem}, which contains buttons
     * for various mirroring operations. The buttons trigger specific actions defined in
     * the provided {@link ContextMenuActions} interface.
     *
     * @param actions the {@link ContextMenuActions}*/
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


    /**
     * Creates a stretch configuration item as a {@link CustomMenuItem} for adjusting the width and height
     * of the given {@link ShapeInterface}. The item includes input fields for entering dimensions,
     * an apply button, and validation logic with warnings for invalid input.
     *
     * @param shape the {@link ShapeInterface} to be stretched, which provides the current dimensions and properties
     * @param stretchHandler the {@link StretchHandler} to handle the stretch action, receiving the old and new dimensions
     * @return the fully configured {@link CustomMenuItem} that allows the user to adjust the dimensions of the shape
     */
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

    /**
     * Creates a resize configuration item as a {@link CustomMenuItem} for adjusting the size of
     * the given {@link ShapeInterface}. The item includes a slider for selecting the desired size,
     * a label to display the current value, and an apply button to confirm the new size. Size changes
     * are handled through the provided {@link ResizeHandler}.
     *
     * @param shape the {@link ShapeInterface} whose size can be adjusted, providing the current size,
     *              minimum/maximum constraints, and slider label generation
     * @param resizeHandler the {@link ResizeHandler} to handle the resize action, receiving*/
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

    /**
     * Creates a rotate configuration item as a {@link CustomMenuItem} for adjusting the rotation angle
     * of the given {@link ShapeInterface}. The item includes an input field for entering the desired
     * angle, a button to apply the change, and validation logic with warnings for invalid input.
     *
     * @param shape the {@link ShapeInterface} to be rotated, which provides the current angle and
     *              properties for rotation adjustments
     * @param rotateHandler the {@link RotateHandler} to handle the rotation action, receiving the
     *                      old and new angles of the shape
     * @return the fully configured {@link CustomMenuItem} that allows the user to adjust
     *         the rotation angle of the shape
     */
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


    /**
     * Adds a "Add to Favorites" menu item to the specified context menu. When selected,
     * this menu item opens a dialog prompting the user to enter a name for the favorite item
     * and triggers the associated action defined in the {@link ContextMenuActions}.
     *
     * @param contextMenu the {@link ContextMenu} to which the "Add to Favorites" item will be added
     * @param actions the {@link ContextMenuActions} interface containing the action to*/
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

    /**
     * Functional interface representing a handler for color change events.
     *
     * This interface is used to define a callback method that is called
     * whenever a color change event occurs. The handler is invoked with
     * the new color as its parameter, allowing custom logic to be executed
     * in response to the color change.
     *
     * Methods that involve this handler typically allow users to select
     * or modify colors in the application's context menu or UI components.
     */
    @FunctionalInterface
    private interface ColorChangeHandler {
        void onColorChanged(Color color);
    }

    /**
     * Functional interface for handling stretch events on graphical elements.
     * This interface defines a*/
    @FunctionalInterface
    public interface StretchHandler {
        void onStretch(double oldWidth, double oldHeight, double newWidth, double newHeight);
    }

    /**
     * The ResizeHandler functional interface is used to handle resize operations for a given component or object.
     * It provides a single abstract method, `onResize`, which is invoked when the dimensions of an object change.
     * The method is supplied with*/
    @FunctionalInterface
    public interface ResizeHandler {
        void onResize(double oldWidth, double oldHeight, double newSize);
    }

    /**
     * Functional interface for handling rotation events within a context menu or related user interactions.
     * This handler is designed to manage actions associated with rotating a shape or object, such as updating
     * its rotation angle or triggering specific behaviors tied to rotation changes.
     *
     * The interface contains a single method, `onRotate`, which accepts the old angle and the new angle
     * as parameters, allowing implementers to define custom logic for handling rotation events. The purpose
     * of this interface is to provide a clear contract for rotation-related transformations without imposing
     * implementation details, aligning with the principles of functional programming.
     */
    @FunctionalInterface
    public interface RotateHandler {
        void onRotate(double oldAngle, double newAngle);
    }
}