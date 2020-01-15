package com.asdt.yahtzee.players;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.game.Player;

/**
 * Bot plays according to provided strategies.
 */
public class Bot implements GamePlayer {
    private Player player;
    KeepingStrategy keepingStrategy;
    ScoringStrategy scoringStrategy;

    public Bot(Game game, String name, KeepingStrategy keepingStrategy, ScoringStrategy scoringStrategy) {
        player = game.getPlayer(name);
        this.keepingStrategy = keepingStrategy;
        this.scoringStrategy = scoringStrategy;
    }

    @Override
    public int[] rollKeeping() {
        return keepingStrategy.rollKeeping();
    }

    @Override
    public String selectCategory() {
        return scoringStrategy.selectCategory(player);
    }
}
