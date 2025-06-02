package org.example.model.commands;

import org.example.model.shape.ShapeInterface;
import org.example.model.receivers.FavoriteManagerReceiver;

public class SaveFavoriteCommand implements CommandInterface {

    private final FavoriteManagerReceiver receiver;
    private final ShapeInterface shape;
    private final String name;

    public SaveFavoriteCommand(FavoriteManagerReceiver receiver, ShapeInterface shape, String name) {
        this.receiver = receiver;
        this.shape = shape.cloneShape();  // salvo una copia
        this.name = name;
    }

    @Override
    public void executeCommand() {
        receiver.saveAsFavorite(name, shape);
    }

    @Override
    public void undoCommand() {
        //receiver.removeFavorite(name);
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isUndoable() {
        return false;
    }
}
