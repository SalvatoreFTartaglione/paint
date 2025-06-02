package org.example.model.commands;

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
        // Debug print
        printStack("After EXECUTE");
    }

    public void executeLoadCommand(CommandInterface cmd) {
        cmd.executeCommand();
        history.clear();
    }

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
            //debug print
            printStack("After UNDO");
        }
    }

    // debug method
    private void printStack(String title) {
        System.out.println("==== " + title + " ====");
        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.println("[" + i + "] " + history.get(i).getClass().getSimpleName());
        }
        System.out.println("=====================");
    }
}
