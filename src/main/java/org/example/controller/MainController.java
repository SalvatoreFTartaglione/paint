package org.example.controller;

import org.example.model.MessageModel;

public class MainController {
    private final MessageModel model;

    public MainController(MessageModel model) {
        this.model = model;
    }

    public String getMessage() {
        return model.getMessage();
    }

    public void setMessage(String message) {
        model.setMessage(message);
    }
}
