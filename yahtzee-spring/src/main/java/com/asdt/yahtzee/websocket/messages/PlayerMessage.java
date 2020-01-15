package com.asdt.yahtzee.websocket.messages;

public class PlayerMessage {
    private String name;

    public PlayerMessage() {

    }

    public PlayerMessage(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "PlayerMessage [name=" + name + "]";
    }
}
