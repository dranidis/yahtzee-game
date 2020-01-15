package com.asdt.yahtzee.websocket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.asdt.yahtzee.websocket.messages.PlayerListMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Remembers users connected by their session id
 */

@Component
public class PlayerCatalog {
    Logger logger = LoggerFactory.getLogger(PlayerCatalog.class);

    @Autowired
    private SimpMessagingTemplate messageSender;
    
    // private static PlayerCatalog instance = new PlayerCatalog();
    // public static PlayerCatalog getInstance() {
    //     return instance;
    // }

    public PlayerCatalog() {
    }



    // HTTP sessionId, name
    private Map<String, String> connectedPlayers = new HashMap<>();;

    public void playerConnected(String id) {
        // String player = connectedPlayers.get(id);
        // if (player == null) {
        //     logger.trace("New session id:" + id);
        //     connectedPlayers.put(id, "noname");
        // } else {
        //     logger.trace("User " + player + " connects again!");
        // }
    }

	public void updateName(String sessionId, String playerName) {
        AtomicInteger atomicInteger = new AtomicInteger();
        String uniquePlayerName = playerName;
        while (connectedPlayers.values().contains(uniquePlayerName)) {
            uniquePlayerName = playerName + "_" + atomicInteger.incrementAndGet();
        }
        connectedPlayers.put(sessionId, uniquePlayerName);
	}

	public void disconnected(String httpSessionId) {
        logger.trace("Disconnected: " + connectedPlayers.get(httpSessionId));
        connectedPlayers.remove(httpSessionId);
        logger.trace("Remaining players: " + getListofNames());
        messageSender.convertAndSend("/topic/players", new PlayerListMessage(getListofNames()));
	}

	public List<String> getListofNames() {
		return new ArrayList<String>(connectedPlayers.values());
	}

	public void print() {
        logger.trace(connectedPlayers.entrySet().toString());
	}

	public String getName(String sessionId) {
		return connectedPlayers.get(sessionId);
	}

}