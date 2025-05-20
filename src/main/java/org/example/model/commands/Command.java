package org.example.model.commands;


public interface Command {
    void execute();
    void undo();

}
