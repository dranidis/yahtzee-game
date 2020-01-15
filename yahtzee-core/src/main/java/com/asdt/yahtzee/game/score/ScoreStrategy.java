package com.asdt.yahtzee.game.score;

import java.util.List;

import com.asdt.yahtzee.game.Die;

public interface ScoreStrategy {

	int calculate(List<Die> kept, boolean isJoker);

}
