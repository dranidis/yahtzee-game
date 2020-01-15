package com.asdt.yahtzee.players;

import com.asdt.yahtzee.game.Player;

public interface ScoringStrategy {

	String selectCategory(Player player);

}
