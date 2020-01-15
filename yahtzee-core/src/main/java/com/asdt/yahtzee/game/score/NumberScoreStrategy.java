package com.asdt.yahtzee.game.score;

import java.util.List;

import com.asdt.yahtzee.game.Die;

public class NumberScoreStrategy implements ScoreStrategy {
    private int number;

    public NumberScoreStrategy(int i) {
        number = i;
	}

	@Override
    public int calculate(List<Die> kept, boolean isJoker) {
        int total = 0;
        for(Die d: kept) {
            if (d.getNumber() == number) {
                total += number;
            }
        }
        return total;
    }

}
