package org.example.model.commands;

import java.util.Stack;

public class UndoManager {
    private final Stack<CommandInterface> history = new Stack<>();

    /**
     * Executes a command and stores it for potentia.
     *
     * @param cmd the command to execute
     */
    public void executeCommand(CommandInterface cmd) {
        cmd.executeCommand();
        history.push(cmd);
        printStack("After EXECUTE");
    }

    public void executeCopyCommand(CommandInterface cmd){
        cmd.executeCommand();
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
            printStack("After UNDO");
        }
    }

    private void printStack(String title) {
        System.out.println("==== " + title + " ====");
        for (int i = history.size() - 1; i >= 0; i--) {
            System.out.println("[" + i + "] " + history.get(i).getClass().getSimpleName());
        }
        System.out.println("=====================");
    }
}
