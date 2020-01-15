package com.asdt.yahtzee.game.score;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ScoreFactory {

    private Map<String,ScoreStrategy> scoreStrategies;

    private List<String> categories = new ArrayList<>(Arrays.asList(new String[] {
        "1s", "2s","3s","4s","5s","6s","UB", "3k","4k","fh","s4","s5","5k","ch", "YB"
    }));

    private ScoreFactory() {
        scoreStrategies = new HashMap<>();
        scoreStrategies.put("1s", new NumberScoreStrategy(1));
        scoreStrategies.put("2s", new NumberScoreStrategy(2));
        scoreStrategies.put("3s", new NumberScoreStrategy(3));
        scoreStrategies.put("4s", new NumberScoreStrategy(4));
        scoreStrategies.put("5s", new NumberScoreStrategy(5));
        scoreStrategies.put("6s", new NumberScoreStrategy(6));
        scoreStrategies.put("3k", new OfaKindScoreStrategy(3));
        scoreStrategies.put("4k", new OfaKindScoreStrategy(4));
        scoreStrategies.put("s4", new SequenceScoreStrategy(4));
        scoreStrategies.put("s5", new SequenceScoreStrategy(5));
        scoreStrategies.put("5k", new OfaKindScoreStrategy(5));
        scoreStrategies.put("fh", new FullHouseScoreStrategy());
        scoreStrategies.put("ch", new ChanceScoreStrategy());
    }

    private static ScoreFactory instance = new ScoreFactory();

	public static ScoreFactory getInstance() {
		return instance;
	}

	public ScoreStrategy getScoreStrategy(String categoryName) {
		return scoreStrategies.get(categoryName);
	}

	public Map<String, Integer> getScoringSheet() {
        Map<String, Integer> scoringSheet = new HashMap<String, Integer>();
        for(String cat: categories)
            scoringSheet.put(cat, null);
		return scoringSheet;
	}

    public Set<String> getCategories() {
        // clone the keys so that original Map is not changed!
        return  new HashSet<>(scoreStrategies.keySet());
    }

	public boolean isValidCategory(String categoryName) {
        return scoreStrategies.get(categoryName) != null;
	}
}
