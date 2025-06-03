package org.example.model.contextMenu;

import javafx.scene.paint.Color;

public interface ContextMenuActions {
    /**
     * Called when the fill color of a graphical element is changed.
     *
     * @param newColor the new fill color to be applied
     */
    void onFillColorChanged(Color newColor);

    /**
     * Called when the stroke color of a graphical element is changed.
     *
     * @param newColor the new stroke color to be applied
     */
    void onStrokeColorChanged(Color newColor);

    /**
     * Triggered when a graphical element is sent to the back of the stacking order.
     * This method reorders the graphical elements so that the selected element appears
     * behind all other elements within the same context.
     */
    void onSendToBack();

    /**
     * Triggered when a graphical element is brought to the front of the stacking order.
     * This method reorders the graphical elements so that the selected element appears
     * in front of all other elements within the same context.
     */
    void onBringToFront();

    /**
     * Triggered to handle the copy operation within the context menu.
     * This method is called when content is selected to be duplicated
     * and prepared to be placed in a clipboard or similar temporary storage,
     * without removing it from its original position.
     */
    void onCopy();

    /**
     * Triggered to handle the cut operation within the context menu.
     * This method is called when content is removed from the current context
     * and prepared to be placed elsewhere, typically transferring it to a clipboard.
     */
    void onCut();

    /**
     * Triggered to handle the paste operation within the context menu.
     * This method is called when content is pasted into the application,
     * performing the necessary actions to incorporate the pasted data into the current context.
     */
    void onPaste();

    /**
     * Called to handle the deletion of a graphical element.
     * This method is triggered when the delete action is initiated,
     * removing the selected element from the context.
     */
    void onDelete();


    /**
     * Called when the size of a graphical element is resized. This method updates the element's
     * dimensions based on the provided old and new size values.
     */
    void onResize(double oldWidth, double oldHeight, double newSize);

    /**
     * Triggered when a graphical element is stretched, changing its dimensions from the old width and height
     * to the new width and height.
     *
     * @param oldWidth  the previous width of the graphical element before stretching
     * @param oldHeight the previous height of the graphical element before stretching
     * @param newWidth  the new width of the graphical element after stretching
     * @param newHeight the new height of the graphical element after stretching
     */
    void onStretch(double oldWidth, double oldHeight, double newWidth, double newHeight);

    /**
     *
     */
    void onMirrorUp();

    /**
     * Triggered when a graphical element is mirrored downwards along its horizontal axis.
     * This operation flips the element vertically, resulting in an inverted orientation
     * below its original position.
     */
    void onMirrorDown();

    /**
     * Triggered when a graphical element is mirrored to the left.
     * This operation reflects the current graphical element along its vertical axis,
     * resulting in a reversed orientation on the left side.
     */
    void onMirrorLeft();

    /**
     * Triggered when a graphical element is mirrored to the right.
     * This operation reflects the current graphical element along its vertical axis,
     * resulting in a reversed orientation on the right side.
     */
    void onMirrorRight();

    /**
     * Triggered to perform a horizontal mirroring operation of a graphical element
     * around its center. This operation flips the element horizontally
     * without altering its vertical position or overall dimensions.
     */
    void onMirrorHorizontalAroundCenter();


    /**
     * Triggered when a graphical element is mirrored vertically around its center axis.
     * This operation reflects the element over its vertical axis, maintaining
     * its position while flipping its orientation.
     */
    void onMirrorVerticalAroundCenter();


    /**
     * Triggered when an item is added to the favorites list with a specific name.
     *
     * @param name the name of the item to be added to the favorites
     */
    void onAddToFavoritesWithName(String name);

    /**
     * Called when a graphical element is rotated, updating its angle of rotation.
     *
     * @param oldAngle the previous angle of the graphical element before rotation
     * @param newAngle the new angle of the graphical element after rotation
     */
    void onRotate(double oldAngle, double newAngle);
}
