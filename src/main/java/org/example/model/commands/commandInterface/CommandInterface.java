package org.example.model.commands.commandInterface;

import java.io.IOException;

/**
 * Represents a generic command in the Command design pattern.
 * Each command encapsulates an action and can be executed and undone.
 * <p>
 * This interface is commonly used to decouple UI actions from their effects.
 */
public interface CommandInterface {


    /**
     * Executes the command.
     * Should perform the action encapsulated by the command.
     */
    void executeCommand();
    /**
     * Undoes the command.
     * Should reverse the action previously executed by {@link #executeCommand()}.
     */
    void undoCommand();
    /**
     * Indicates whether the command is currently executable.
     * Can be overridden by specific implementations to disable certain actions at runtime.
     *
     * @return {@code true} if the command can be executed; {@code false} otherwise
     */
    default boolean isExecutable(){
        return true;
    }
    /**
     * Executes the command and allows for throwing an {@link IOException}.
     * Default implementation does nothing; override only if the command may throw I/O exceptions.
     *
     * @throws IOException if an I/O error occurs during execution
     */
    default void executePossibleExceptionCommand() throws IOException {
    }
    /**
     * Indicates whether the command supports undo.
     * Can be overridden by commands that are not reversible.
     *
     * @return {@code true} if the command can be undone; {@code false} otherwise
     */
    default boolean isUndoable(){
        return true;
    }

}
