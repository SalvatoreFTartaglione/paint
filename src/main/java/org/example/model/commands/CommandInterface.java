package org.example.model.commands;

import javafx.geometry.Point2D;
import org.example.model.shape.CompositeShapeInterface;
import org.example.model.shape.ShapeInterface;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CommandInterface {

    void executeCommand();
    void undoCommand();
    default boolean isExecutable(){
        return true;
    }
    default void executePossibleExceptionCommand() throws IOException {
    }
    default boolean isUndoable(){
        return true;
    }

    default List<ShapeInterface> getChildren() {
        return null;
    }

    default Map<ShapeInterface, Point2D> getOriginalScenePositions() {
        return null;
    }

    default CompositeShapeInterface getCreatedGroup() {
        return null;
    }

}
