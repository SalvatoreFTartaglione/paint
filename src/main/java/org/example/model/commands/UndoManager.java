package org.example.model.commands;

import java.util.Stack;

public class UndoManager {
    private final Stack<Command> history = new Stack<>();

    /**
     * Executes a command and stores it for potential undo.
     *
     * @param cmd the command to execute
     */
    public void executeCommand(Command cmd) {
        cmd.execute();
        history.push(cmd);
    }

    /**
     * Undoes the last executed command, if available.
     */
    public void undo() {
        if (!history.isEmpty()) {
            history.pop().undo();
        }
    }
}
