package org.example.model.utils;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * A custom implementation of a scroll pane that supports zoom functionality.
 * This class wraps a target node within a group and allows users to scale (zoom) its content.
 * The zoom level can be controlled programmatically.
 */
public class ZoomableScrollPane extends ScrollPane {
    private double scaleValue = 0.7;
    private Node target;
    private Node zoomNode;

    /**
     *
     */
    public ZoomableScrollPane() {
        super();
        this.target = new Group();
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));

        setPannable(false);
        setHbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        setFitToHeight(true);
        setFitToWidth(true);

        updateScale();
    }

    /**
     * Wraps the given node into a centered container and returns it as the outer node.
     * This method ensures that the provided node is centered within its parent container.
     *
     * @param node the Node to be wrapped and centered within a container
     * @return the outer Node which contains the given node centered in a VBox
     */
    private Node outerNode(Node node) {
        Node outerNode = centeredNode(node);
        return outerNode;
    }

    /**
     * Wraps the given node into a VBox container and centers it within that container.
     *
     * @param node the Node to be wrapped and centered in a VBox
     * @return a VBox containing the given node, with alignment set to center
     */
    private Node centeredNode(Node node) {
        VBox vBox = new VBox(node);
        vBox.setAlignment(Pos.CENTER);
        return vBox;
    }

    /**
     * Updates the scale of the target node using the current scale value.
     * The method adjusts the horizontal and vertical scaling transformations
     * of the target node based on the specified scale factor.
     */
    private void updateScale() {
        target.setScaleX(scaleValue);
        target.setScaleY(scaleValue);
    }

    /**
     * Sets the zoom level for the content of the scroll pane.
     * The method adjusts the scale value and updates the layout accordingly.
     *
     * @param scale the desired zoom level; should be a positive double value where 1.0 represents 100% scaling
     */
    public void setZoom(double scale) {
        this.scaleValue = scale;
        updateScale();
        this.layout();
    }

    /**
     * Sets the target node for the zoomable scroll pane.
     * The target node will be wrapped in a group for scaling purposes,
     * and the content of the scroll pane will be updated to display the new target.
     * The scale transformation of the target is updated to match the current zoom level.
     *
     * @param target the new Node to be displayed and managed within the zoomable scroll pane
     */
    public void setTarget(Node target) {
        this.target = target;
        this.zoomNode = new Group(target);
        setContent(outerNode(zoomNode));
        updateScale();
    }
}