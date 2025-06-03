package org.example.model.invoker;

import org.example.model.commandInterface.CommandInterface;
import org.example.model.concreteCommands.AddShapeCommand;

import java.io.IOException;
import java.util.Stack;

public class Invoker {
    private final Stack<CommandInterface> history = new Stack<>();

    /**
     * Executes a command and stores it for potential.
     *
     * @param cmd the command to execute
     */
    public void execute(CommandInterface cmd) {
        cmd.executeCommand();
        if (cmd.isUndoable()) {
            if (cmd instanceof AddShapeCommand) {
                if (((AddShapeCommand) cmd).wasExecuted()) {
                    history.push(cmd);
                }
            } else {
                history.push(cmd);
            }
        }
    }

    /**
     * Executes a given command and clears the command history stack.
     *
     * @param cmd the command to execute
     */
    public void executeLoadCommand(CommandInterface cmd) {
        cmd.executeCommand();
        history.clear();
    }

    /**
     * Executes a command that may throw an {@link IOException} and potentially stores it in the history stack if it is undoable.
     *
     * @param cmd the command to execute, must implement {@link CommandInterface}. The command's {@code executePossibleExceptionCommand()} method
     *            is called, and if the command is undoable (determined by {@code isUndoable()}), it is added to the history stack.
     * @throws IOException if an I/O error occurs during the execution of the command
     */
    public void executePossibleExceptionCommand(CommandInterface cmd) throws IOException {
        cmd.executePossibleExceptionCommand();
        if (cmd.isUndoable()) {
            history.push(cmd);
        }
    }

    /**
     * Undoes the last executed command, if available.
     */
    public void undo() {
        if (!history.isEmpty()) {
            history.pop().undoCommand();
        }
    }


}
