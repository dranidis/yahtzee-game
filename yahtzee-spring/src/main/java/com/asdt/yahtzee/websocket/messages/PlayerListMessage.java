package com.asdt.yahtzee.websocket.messages;

import java.util.List;

public class PlayerListMessage {

    private List<String> players;

    public PlayerListMessage() {
    }

    public PlayerListMessage(List<String> players) {
        this.players = players;
    }

    public List<String> getPlayers() {
        return players;
    }

    public void setPlayers(List<String> players) {
        this.players = players;
    }

}
