package com.asdt.yahtzee.game.score;

import java.util.List;

import com.asdt.yahtzee.game.Die;

public class ChanceScoreStrategy implements ScoreStrategy {

    @Override
    public int calculate(List<Die> kept, boolean isJoker) {
        return kept.stream().map(d -> d.getNumber()).reduce(0, (a, b) -> a + b);
    }

}
