package org.example.controller;

import org.example.model.MessageModel;

public class MainController {
    private final MessageModel model;

    /**
     * Creates a new controller for the given model.
     *
     * @param model the message model to control
     */
    public MainController(MessageModel model) {
        this.model = model;
    }

    /**
     * Retrieves the current message from the model.
     *
     * @return the current message
     */
    public String getMessage() {
        return model.getMessage();
    }

    /**
     * Updates the model's message with a new value.
     *
     * @param message the new message to set
     */
    public void setMessage(String message) {
        model.setMessage(message);
    }
}
