package com.asdt.yahtzee.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomKeepingStrategy implements KeepingStrategy {
    private Random r = new Random();

    @Override
    public int[] rollKeeping() {
        List<Integer> list = new ArrayList<>();
        double prob;
        for (int i = 0; i < 5; i++) {
            prob = r.nextDouble();
            if (prob > 0.5) {
                list.add(i + 1);
            }
        }
        // System.out.println(this.getClass().toString() + " keeps " + list);
        return list.stream().mapToInt(i -> i).toArray();
    }
}
