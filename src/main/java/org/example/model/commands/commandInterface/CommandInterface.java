package org.example.model.commands.commandInterface;

import java.io.IOException;

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

}
