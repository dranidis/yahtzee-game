package com.asdt.yahtzee.network;

import java.util.HashMap;
import java.util.Map;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.network.messages.Response;

public class ServerGame {

    Map<String, Connection> gamePlayers;

    Game game;

    private int numPlayers;

    public ServerGame(int numPlayers) {
        this.numPlayers = numPlayers;
        game = new Game();
        gamePlayers = new HashMap<>();
    }

    public void addPlayer(String name, Connection connection) {
        System.out.println("User: " + name + " joined a Game");

        game.addPlayer(name);
        gamePlayers.put(name, connection);
    }

    public void start() {
        System.out.println("Starting a game with players: " + gamePlayers.keySet());

        game.startRound();
        game.getNextPlayer();
        broadCast(Response.GAME_STARTED);
    }

    public boolean isAccepting() {
        return gamePlayers.size() < numPlayers;
    }

    public boolean isReady() {
        return gamePlayers.size() == numPlayers;
    }

    public void broadCast(Object obj) {
        for(Connection connection: gamePlayers.values()) {
            connection.sendObject(obj);
        }
    }

}
