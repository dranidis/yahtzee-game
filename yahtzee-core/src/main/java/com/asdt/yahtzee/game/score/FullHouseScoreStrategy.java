package com.asdt.yahtzee.game.score;

import java.util.List;

import com.asdt.yahtzee.game.Die;

public class FullHouseScoreStrategy implements ScoreStrategy {

    @Override
    public int calculate(List<Die> kept, boolean isJoker) {
        if (isJoker)
            return 25;

        int first = kept.get(0).getNumber();
        int second = 0;
        int countFirst = 0;
        int countSecond = 0;

        for (Die d : kept) {
            int num = d.getNumber();
            if (num == first) {
                countFirst++;
                continue;
            }
            // first occurence of second number
            if (second == 0) {
                second = num;
            }
            if (num == second) {
                countSecond++;
                continue;
            }
        }
        if (countFirst == 3 && countSecond == 2 || countFirst == 2 && countSecond == 3)
            return 25;
        return 0;
    }

}
