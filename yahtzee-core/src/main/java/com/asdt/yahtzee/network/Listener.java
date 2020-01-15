package com.asdt.yahtzee.network;

import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.UnknownScoringCategory;
import com.asdt.yahtzee.network.messages.KeepRequest;
import com.asdt.yahtzee.network.messages.Request;
import com.asdt.yahtzee.network.messages.ScoreRequest;
import com.asdt.yahtzee.network.messages.UserRequest;

/**
 * A separate Listener object is created for each Connection object created by
 * the server for each connecting client. The listener is responsible for
 * handling the messages sent by the client associated to the connection. The
 * listener can send messages to the client by using the connection attribute.
 */
public class Listener {

    private Connection connection;
    String name;

    ServerGame serverGame;

    public Listener(Connection connection) {
        this.connection = connection;
    }

    /**
     * The on method listens and handles objects sent by clients.
     *
     * @param object the object sent by clients
     */
    public void on(Object object) {
        System.out.println("RECEIVED from " + connection.id + " " + object.toString());
        if (object instanceof UserRequest) {
            GameFactory gameFactory = GameFactory.getInstance();
            synchronized (gameFactory) {
                // a new user has given a name and wants to play a numPlayer game
                UserRequest user = (UserRequest) object;
                serverGame = gameFactory.getGame(user.numPlayers);

                if (serverGame.isAccepting()) {
                    this.name = user.name;
                    serverGame.addPlayer(user.name, connection);

                    if (serverGame.isReady()) {
                        serverGame.start();
                        gameFactory.removeGame(user.numPlayers);
                    }
                }
            }
        } else if (object instanceof KeepRequest) {
            /* the client has sent the dice to keep */
            KeepRequest keepRequest = (KeepRequest) object;
            serverGame.game.rollKeeping(name, keepRequest.getKeep());

            // clients are waiting
            serverGame.broadCast(serverGame.game.toString());

        } else if (object instanceof ScoreRequest) {
            /* the client has sent the category to score */
            ScoreRequest scoreRequest = (ScoreRequest) object;
            int score;
            try {
                score = serverGame.game.scoreACategory(scoreRequest.name, scoreRequest.categoryName);
            } catch (UnknownScoringCategory | InvalidScoringCategory e) {
                score = -1;
            }

            connection.sendObject(score);

            /*
             * The client has scored a category. Change to the next player. If no next
             * player start a new round and get the next player.
             */
            if (score >= 0) {
                // clients are waiting
                serverGame.broadCast(serverGame.game.toString());

                String player = serverGame.game.getNextPlayer();
                if (player == null) {
                    serverGame.game.startRound();
                    serverGame.game.getNextPlayer();
                }
            }
        } else if (object instanceof String) {
            /* handes string messages sent by the client */
            String str = (String) object;
            if (str.equals(Request.CURRENTPLAYER)) {
                // client is waiting
                connection.sendObject(serverGame.game.getCurrentPlayersName());
            } else
                System.out.println(
                        "UNHANDLED String request from client:" + connection.id + " Request:" + object.toString());
        }
    }

}
