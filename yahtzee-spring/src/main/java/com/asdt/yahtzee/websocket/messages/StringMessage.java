package com.asdt.yahtzee.websocket.messages;

public class StringMessage {
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public StringMessage() {
    }

    public StringMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GameEndMessage [message=" + message + "]";
    }

}
