package org.example.model.concreteCommands;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.factory.product.ShapeInterface;
import org.example.model.receivers.FavoriteManagerReceiver;

/**
 * SaveFavoriteCommand is a concrete implementation of the Command design pattern.
 * This command is responsible for saving a shape as a favorite in the associated
 * FavoriteManagerReceiver. Upon execution, it stores a deep copy of the provided shape
 * under a specified name in the favorite registry managed by the receiver.
 *
 * SaveFavoriteCommand is not undoable and attempting to perform an undo operation
 * will result in an {@link UnsupportedOperationException}.
 *
 * The stored shape is cloned to ensure immutability and prevent external modifications
 * to the original shape from affecting the saved favorite.
 *
 * Implements the {@link CommandInterface} contract.
 */
public class SaveFavoriteCommand implements CommandInterface {

    private final FavoriteManagerReceiver receiver;
    private final ShapeInterface shape;
    private final String name;

    /**
     * Constructs a new SaveFavoriteCommand instance to save the provided shape
     * as a favorite using the specified name in the FavoriteManagerReceiver.
     * A deep copy of the shape is stored to ensure immutability.
     *
     * @param receiver the FavoriteManagerReceiver responsible for managing favorites
     * @param shape the ShapeInterface instance to be saved as a favorite
     * @param name the unique name under which the shape will be saved as a favorite
     */
    public SaveFavoriteCommand(FavoriteManagerReceiver receiver, ShapeInterface shape, String name) {
        this.receiver = receiver;
        this.shape = shape.cloneShape();  // salvo una copia
        this.name = name;
    }

    /**
     * Executes the save as favorite command.
     * This method delegates the action to the receiver, instructing it to save a deep copy
     * of the provided shape instance under the specified name in the favorite registry.
     *
     * This operation enables users to store specific shapes with unique names for future retrieval.
     * The receiver handles the*/
    @Override
    public void executeCommand() {
        receiver.saveAsFavorite(name, shape);
    }

    /**
     * Reverts the action of the command. In the context of the SaveFavoriteCommand,
     * the undo operation is not supported and will throw an {@link UnsupportedOperationException}.
     *
     * This is because the SaveFavoriteCommand performs an irreversible
     * operation of saving a shape as a favorite, which does not have an associated undo logic.
     *
     * @throws UnsupportedOperationException if this method is invoked.
     */
    @Override
    public void undoCommand() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Determines whether the command is undoable.
     * This method provides information about whether the action
     * performed by this command can be reversed.
     *
     * @return false as this command is not undoable.
     */
    @Override
    public boolean isUndoable() {
        return false;
    }
}
