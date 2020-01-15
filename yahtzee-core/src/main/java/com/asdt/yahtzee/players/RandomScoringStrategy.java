package com.asdt.yahtzee.players;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.asdt.yahtzee.game.Player;
import com.asdt.yahtzee.game.score.ScoreFactory;

public class RandomScoringStrategy implements ScoringStrategy {
    private Random r = new Random();

    @Override
    public String selectCategory(Player player) {
        String randomCategory = "";
        Set<String> cat = ScoreFactory.getInstance().getCategories();
        Map<String, Integer> alreadyScored = player.getScored();
		for (Map.Entry<String, Integer> as : alreadyScored.entrySet()) {
            if (as.getValue() != null)
                cat.remove(as.getKey());
        }
        int size = cat.size();
        int index = r.nextInt(size);
        int i = 0;
        for (String c : cat) {
            if (i == index) {
                randomCategory = c;
                break;
            }
            i++;
        }
        // System.out.println(this.getClass().toString() + "  chooses category: " + randomCategory);
        return randomCategory;
    }
}
