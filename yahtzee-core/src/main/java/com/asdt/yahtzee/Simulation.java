package com.asdt.yahtzee;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.UnknownScoringCategory;
import com.asdt.yahtzee.players.Bot;
import com.asdt.yahtzee.players.GamePlayer;
import com.asdt.yahtzee.players.MaximumScoringStrategy;
import com.asdt.yahtzee.players.RandomKeepingStrategy;

public class Simulation {
    Game game;
    GamePlayer gp;
    int score = 0;

    public Simulation(Game game) {
        this.game = game;
        String name = "random";
        game.addPlayer(name);
        gp = new Bot(game, name, new RandomKeepingStrategy(), new MaximumScoringStrategy());

        for (int i = 0; i < 13; i++) {
            round();
        }
        score = game.getPlayerScore(name);
    }

    public int getScore() {
        return score;
    }

    public void round() {
        game.startRound();
        String player = game.getNextPlayer();
        while (player != null) {
            play(player);
            player = game.getNextPlayer();
        }
    }

    public void play(String name) {
        game.rollKeeping(name);
        for (int r = 0; r < 2; r++) {
            // interact with the player
            int[] array = gp.rollKeeping();

            if (array.length == 5 || array.length == 1 && array[0] == -1) {
                break;
            }
            game.rollKeeping(name, array);
        }
        // interact with the player
        String categoryName = gp.selectCategory();

        int score;
        try {
            score = game.scoreACategory(name, categoryName);
        } catch (UnknownScoringCategory | InvalidScoringCategory e) {
            score = -1;
        }
        while (score < 0) {
            // System.out.println("Invalid choice! " + "(score=" + score + ")");

            // interact with the player
            categoryName = gp.selectCategory();
            try {
                score = game.scoreACategory(name, categoryName);
            } catch (UnknownScoringCategory | InvalidScoringCategory e) {
                score = -1;
            }
        }
    }
}
