package org.example.model.commands;

public interface CommandInterface {

    void executeCommand();
    void undoCommand();
    default boolean isExecutable(){
        return true;
    }

}
