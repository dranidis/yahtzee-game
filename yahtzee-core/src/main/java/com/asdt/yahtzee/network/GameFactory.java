package com.asdt.yahtzee.network;

import java.util.HashMap;
import java.util.Map;

public class GameFactory {
    private static GameFactory instance = new GameFactory();
    private Map<Integer, ServerGame> serverGames = new HashMap<>();

    private GameFactory() {
    }

    public static GameFactory getInstance() {
        return instance;
    }

	public ServerGame getGame(int numPlayers) {
        ServerGame serverGame = serverGames.get(numPlayers);
        if (serverGame == null) {
            serverGame = new ServerGame(numPlayers);
            serverGames.put(numPlayers, serverGame);
        }
		return serverGame;
	}

	public void removeGame(int numPlayers) {
        serverGames.put(numPlayers, null);
	}

}
