package org.example.model.commands;

import java.util.Stack;

public class UndoManager {
    private final Stack<CommandInterface> history = new Stack<>();

    /**
     * Executes a command and stores it for potentia.
     * @param cmd the command to execute
     */
    public void executeCommand(CommandInterface cmd) {
        cmd.executeCommand();
        history.push(cmd);
    }

    public void executeLoadCommand(CommandInterface cmd) {
        cmd.executeCommand();
        history.clear();
    }

    public void executeSaveCommand(CommandInterface cmd) {
        cmd.executeCommand();
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
