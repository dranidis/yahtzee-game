package com.asdt.yahtzee.game.score;

import java.util.Comparator;
import java.util.List;

import com.asdt.yahtzee.game.Die;

public class SequenceScoreStrategy implements ScoreStrategy {
    int length;

    public SequenceScoreStrategy(int i) {
        length = i;
    }

    @Override
    public int calculate(List<Die> kept, boolean isJoker) {
        if (isJoker && length == 4)
            return 30;
        if (isJoker && length == 5)
            return 40;

        kept.sort(Comparator.comparing(Die::getNumber));
        int countSeq = 1;
        for (int i = 0; i < kept.size() - 1; i++) {
            if (kept.get(i + 1).getNumber() - kept.get(i).getNumber() == 1)
                countSeq++;
        }
        if (countSeq < length) {
            return 0;
        } else {
            if (length == 4)
                return 30;
            if (length == 5)
                return 40;
        }
        return 0;
    }

}
