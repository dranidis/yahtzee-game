package com.asdt.yahtzee.players;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.Player;
import com.asdt.yahtzee.game.score.ScoreFactory;

public class MaximumScoringStrategy implements ScoringStrategy {

    @Override
    public String selectCategory(Player player) {

        double sum = getSumOfUnusedCategories(player);
        // categories.remove("ch");

        double maxScore = -1;
        String maxCategory = "";
        for (String category : categories) {
            if (player.getScored().get(category) != null)
                continue;
            int score;
            try {
                score = player.getScoreForCategory(category);
            } catch (InvalidScoringCategory e) {
                score = -1;
            }
            if (score < 0)
                continue;
            score += sum - probValues.get(category);
            if (score > maxScore) {
                maxScore = score;
                maxCategory = category;
            }
        }

        // todo:
        // if score is 0 select categories according to difficulty
        // choose the easiest of the hardest or the one giving the less reward?
        //
        // is two categories are possible and one is harder prefer the harder
        //
        //
        // int chanceScore = player.getScoreForCategory("ch");
        // if (chanceScore > maxScore) {
        // maxCategory = "ch";
        // }

        if (maxCategory.equals("")) {
            throw new RuntimeException(this.getClass().toString() + " maxCategory empty");
        }
        // System.out.println(this.getClass().toString() + " chooses category: " +
        // maxCategory);
        return maxCategory;
    }

    private double getSumOfUnusedCategories(Player player) {
        double sum = 0;
        for(String category: categories) {
            if (player.getScored().get(category) != null) {
                sum += probValues.get(category);
            }
        }
        return sum;
    }

    static Set<String> categories = ScoreFactory.getInstance().getCategories();
    static {
        categories.remove("UB");
        categories.remove("YB");
    }

    static Map<String, Double> probValues;
    static {
        probValues = new HashMap<>();
        probValues.put("1s", 2.1);
        probValues.put("2s", 4.21);
        probValues.put("3s", 6.31);
        probValues.put("4s", 8.42);
        probValues.put("5s", 10.53);
        probValues.put("6s", 12.63);
        probValues.put("3k", 15.19);
        probValues.put("4k", 5.61);
        probValues.put("fh", 9.15);
        probValues.put("s4", 18.46);
        probValues.put("s5", 10.44);
        probValues.put("5k", 2.3);
        probValues.put("ch", 23.33);
    }

}

/*
 * Without prob values 100000 Yahtzee! avg 113 min 30 max 303
 */
