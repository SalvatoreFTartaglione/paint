package org.example.model;

public class MessageModel {
    private String message = "Hello, MVC with JavaFX!";

    /**
     * Returns the current message.
     *
     * @return the message string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Updates the message with a new value.
     *
     * @param message the new message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
