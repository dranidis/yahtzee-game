package com.asdt.yahtzee.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.asdt.yahtzee.game.score.ScoreFactory;

/**
 * Game class:
 *
 * startRound should be called first to set the order of players getNextPlayer
 * should be called next to set the currentPlayer
 *
 * before calling any other methods
 */
public class Game {

    private int currentPlayerIndex = 0;
    private Player currentPlayer;
    private Map<String, Player> players = new HashMap<>();
    private ArrayList<Player> roundPlayers;

    int round = 0;

    public Game() {
        roundPlayers = new ArrayList<Player>(players.values());
    }

    /**
     * Roll the dice. Optionally, provide any dice index to keep from previous roll.
     *
     * @param playerName Player calling
     * @param keep       vararg which dice indices to keep
     */
    public void rollKeeping(String playerName, int... keep) {
        Player called = players.get(playerName);
        if (called != currentPlayer) {
            throw new RuntimeException(playerName + " Not your turn");
        }
        boolean[] kept = new boolean[5];
        for (int i = 0; i < keep.length; i++) {
            kept[keep[i] - 1] = true;
        }
        called.rollKeeping(kept);
    }

    /**
     * Choose category to score for all kept dice. Throws a RuntimeException if it
     * is not the turn of the player.
     *
     * @param playerName   the name of the player calling
     * @param categoryName the category to score
     * @return the score
     * @throws UnknownScoringCategory
     * @throws InvalidScoringCategory
     */
    public int scoreACategory(String playerName, String categoryName)
            throws UnknownScoringCategory, InvalidScoringCategory {
        Player called = players.get(playerName);
        if (called != currentPlayer) {
            throw new RuntimeException("Not your turn");
        }

        if(!ScoreFactory.getInstance().isValidCategory(categoryName)) {
            throw new UnknownScoringCategory(categoryName);
        }

        return called.score(categoryName);
    }

    public int[] getDice() {
        int diceNum[] = new int[5];
        for (int i = 0; i < 5; i++) {
            diceNum[i] = currentPlayer.getDice(i);
        }
        return diceNum;
    }

    public boolean[] getKept() {
        return currentPlayer.getKept();
    }

    @Override
    public String toString() {
        return currentPlayer.toString();
    }

    public String getNextPlayer() {
        if (currentPlayerIndex < roundPlayers.size()) {
            currentPlayer = roundPlayers.get(currentPlayerIndex++);
            return currentPlayer.getName();
        } else
            return null;
    }

    public void addPlayer(String name) {
        players.put(name, new Player(name));
    }

    public void startRound() {
        round++;
        roundPlayers = new ArrayList<Player>(players.values());
        currentPlayerIndex = 0;
        if (roundPlayers.size() > 0)
            currentPlayer = roundPlayers.get(currentPlayerIndex);
        else
            throw new RuntimeException("Game: no players in the list");
    }

    public String getCurrentPlayersName() {
        return currentPlayer.getName();
    }

    public int getPlayerScore(String name) {
        return players.get(name).getScore();
    }

    public Player getPlayer(String name) {
        return players.get(name);
    }

    public Map<String, Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getRound() {
        return round;
    }

}
