package com.asdt.yahtzee.game.score;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asdt.yahtzee.game.Die;

public class OfaKindScoreStrategy implements ScoreStrategy {
    int numOfAKind;

    public OfaKindScoreStrategy(int i) {
        numOfAKind = i;
    }

    @Override
    public int calculate(List<Die> kept, boolean isJoker) {
        Map<Integer, Integer> counts = new HashMap<>();
        for (Die d : kept) {
            int num = d.getNumber();
            if (counts.containsKey(num)) {
                counts.put(num, counts.get(num) + 1);
            } else {
                counts.put(num, 1);
            }
        }
        for (int c : counts.values()) {
            if (c >= numOfAKind) {
                if (numOfAKind == 5)
                    return 50;
                return sum(kept);
            }
        }
        return 0;
    }

    private int sum(List<Die> kept) {
        return kept.stream().map(d -> d.getNumber()).reduce(0, (a, b) -> a + b);
    }

}
