package com.asdt.yahtzee.websocket;

import java.util.concurrent.TimeUnit;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.UnknownScoringCategory;
import com.asdt.yahtzee.websocket.messages.GameResponse;
import com.asdt.yahtzee.websocket.messages.KeepMessage;
import com.asdt.yahtzee.websocket.messages.SheetSubResponse;
import com.asdt.yahtzee.websocket.messages.StringMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class SingleGame {
    Logger logger = LoggerFactory.getLogger(PlayerCatalog.class);

    @Autowired
    private PlayerCatalog playerCatalog; 
    
    private Game game;
    int msDelay = 200; // final value will be set by client

    // Set<String> players = new HashSet<>();

    // public List<String> addPlayer(String name) {
    // players.add(name);
    // return new ArrayList<>(players);
    // }

    private void rolling(String currentPlayer, int[] keep, int msDelay) {
        messageSender.convertAndSend("/topic/rolling", new KeepMessage(currentPlayer, keep, msDelay));
        try {
            TimeUnit.MILLISECONDS.sleep(msDelay); // for the animation in the UI
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        game.rollKeeping(currentPlayer, keep);
    }

    public GameResponse start() {
        game = new Game();

        for (String player : playerCatalog.getListofNames())
            game.addPlayer(player);

        game.startRound();
        logger.trace("Game started with " + game.getPlayers().keySet());

        String currentPlayer = game.getNextPlayer();
        SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());

        messageSender.convertAndSend("/topic/game", new GameResponse(game.getRound(), currentPlayer, sheet,
                new int[] {}, game.getCurrentPlayer().getRoll(), "", 0));

        rolling(currentPlayer, new int[] {}, msDelay);

        int[] dice = game.getDice();
        sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
        return new GameResponse(game.getRound(), currentPlayer, sheet, dice, 1, "", 0);
    }

    public GameResponse rollKeeping(KeepMessage keepMsg) {
        msDelay = keepMsg.getMsDelay();
        int roll = game.getCurrentPlayer().getRoll();
        if (roll <= 3) {

            rolling(keepMsg.getName(), keepMsg.getKeep(), msDelay);

            int[] dice = game.getDice();
            SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
            return new GameResponse(game.getRound(), keepMsg.getName(), sheet, dice, roll, "", 0);
        }
        return null;
    }

    @Autowired
    private SimpMessagingTemplate messageSender;

    public GameResponse scoreCategory(String playerName, String categoryName)
            throws UnknownScoringCategory, InvalidScoringCategory {
        int score = 0;
        score = game.scoreACategory(playerName, categoryName);

        SheetSubResponse sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());

        GameResponse msg = new GameResponse(game.getRound(), playerName, sheet, game.getDice(),
                game.getCurrentPlayer().getRoll(), categoryName, score);

        messageSender.convertAndSend("/topic/game", msg);

        String currentPlayer = game.getNextPlayer();
        if (currentPlayer == null) { // round has ended
            if (game.getRound() < 13) {
                game.startRound();
                currentPlayer = game.getNextPlayer();
            } else { // game has ended
                messageSender.convertAndSend("/topic/end", new StringMessage("END"));
                return null; // will be ignored
            }
        }

        rolling(currentPlayer, new int[] {}, msDelay);

        int[] dice = game.getDice();
        sheet = new SheetSubResponse(game.getCurrentPlayer().getFullScoreSheet());
        return new GameResponse(game.getRound(), currentPlayer, sheet, dice, 1, "", 0);
    }

}
