package com.asdt.yahtzee.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.asdt.yahtzee.game.score.ScoreFactory;
import com.asdt.yahtzee.game.util.SumNullBuilder;

public class Player {

    private String name;
    private Die[] dice = new Die[5];
    private boolean[] kept = new boolean[5];
    private int roll = 1;
    Map<String, Integer> scored;

    public Player(String name) {
        this.name = name;
        for (int d = 0; d < dice.length; d++) {
            dice[d] = new Die(6);
        }
        scored = ScoreFactory.getInstance().getScoringSheet();
    }

    public String getName() {
        return name;
    }

    public void rollKeeping(boolean[] keep) {
        if (roll > 3) {
            throw new RuntimeException("You cannot roll anymore");
        }
        if (roll != 1) { // ignore if first roll
            for (int d = 0; d < dice.length; d++) {
                kept[d] = keep[d];
            }
        }
        for (int d = 0; d < dice.length; d++) {
            if (!kept[d])
                dice[d].reroll();
        }
        roll++;
    }

    // for testing
    public void setDice(Die[] dice) {
        this.dice = new Die[dice.length];
        for (int i = 0; i < dice.length; i++)
            this.dice[i] = dice[i];
    }

    public void setDice(int... dice) {
        this.dice = new Die[dice.length];
        for (int i = 0; i < dice.length; i++)
            this.dice[i] = new Die(dice[i]);
    }

    // for testing
    void setScored(Map<String, Integer> scored) {
        this.scored = scored;
    }

    public Map<String, Integer> getScored() {
        return scored;
    }

    public boolean[] getKept() {
        boolean[] copy = new boolean[kept.length];
        for (int i = 0; i < copy.length; i++)
            copy[i] = kept[i];
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name + "\n");
        sb.append(getScoreSheet());
        sb.append("\nDice: " + new ArrayList<Die>(Arrays.asList(dice)) + "\nKept: " + keptToString());
        return sb.toString();
    }

    public Map<String, Integer> getFullScoreSheet() {
        // call this first in order to update the score sheet
        // UB etc.
        //
        int totalScore = getScore();

        // then copy the score sheet
        //
        Map<String, Integer> fullScoreSheet = new HashMap<>(scored);
        fullScoreSheet.put("US", getUSScore());
        fullScoreSheet.put("TS", totalScore);
        if (fullScoreSheet.get("UB") == null) {
            fullScoreSheet.put("UB", 0);
        }
        if (fullScoreSheet.get("YB") == null) {
            fullScoreSheet.put("YB", 0);
        }
        return fullScoreSheet;
    }

    private String getScoreSheet() {
        int upperScore = getUSScore();
        int totalScore = getScore();
        // "1s", "2s","3s","4s","5s","6s","UB", "3k","4k","fh","s4","s5","5k","ch", "YB"
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("    Aces   (1s)  %s      3 of a Kind (3k)  %s%n", ifNullSpace(scored.get("1s")),
                ifNullSpace(scored.get("3k"))));
        sb.append(String.format("    Twos   (2s)  %s      4 of a Kind (4k)  %s%n", ifNullSpace(scored.get("2s")),
                ifNullSpace(scored.get("4k"))));
        sb.append(String.format("    Threes (3s)  %s      Full House  (fh)  %s%n", ifNullSpace(scored.get("3s")),
                ifNullSpace(scored.get("fh"))));
        sb.append(String.format("    Fours  (4s)  %s      S. Straight (s4)  %s%n", ifNullSpace(scored.get("4s")),
                ifNullSpace(scored.get("s4"))));
        sb.append(String.format("    Fives  (5s)  %s      L. Straight (s5)  %s%n", ifNullSpace(scored.get("5s")),
                ifNullSpace(scored.get("s5"))));
        sb.append(String.format("    Sixes  (6s)  %s      Yahtzee     (5k)  %s%n", ifNullSpace(scored.get("6s")),
                ifNullSpace(scored.get("5k"))));
        sb.append(String.format("    Sect. Bonus  %s      Chance      (ch)  %s%n", ifNullSpace(scored.get("UB")),
                ifNullSpace(scored.get("ch"))));
        sb.append(String.format("    Sect. Total  %s      Yahtzee Bonus     %s       TOTAL   %s%n",
                ifNullSpace(upperScore), ifNullSpace(scored.get("YB")), ifNullSpace(totalScore)));
        return sb.toString();
    }

    private String ifNullSpace(Integer i) {
        if (i == null)
            return "   ";
        else
            return String.format("%3d", i);
    }

    private String keptToString() {
        StringBuffer s = new StringBuffer();
        s.append(" ");
        for (int i = 0; i < 5; i++) {
            if (kept[i]) {
                s.append("K  ");
            } else {
                s.append("-  ");
            }
        }
        return s.toString();
    }

    public int score(String categoryName) throws InvalidScoringCategory {
        int scoreForCategory = getScoreForCategory(categoryName);
        if (scoreForCategory < 0) {
            return scoreForCategory;
        }
        // update sheet
        if (isBonusYahtzee()) {
            SumNullBuilder sb = new SumNullBuilder();
            sb.add(scored.get("YB")).add(100);
            scored.put("YB", sb.getSum());
        }

        scored.put(categoryName, scoreForCategory);

        roll = 1;
        for (int d = 0; d < kept.length; d++)
            kept[d] = false;

        return scoreForCategory;
    }

    public int getScoreForCategory(String categoryName) throws InvalidScoringCategory {
        // already scored category
        if (scored.get(categoryName) != null)
            return -1;

        // bonus categories are not selected for scoring by user
        if (categoryName.equals("UB") || categoryName.equals("YB"))
            return -3;

        boolean isJoker = false;

        if (isJokerYahtzee()) {
            int num = dice[0].getNumber();
            String catUpperSection = "" + num + "s";
            if (isJokerCategory(categoryName) && scored.get(catUpperSection) == null) {
                // if (!categoryName.equals(catUpperSection) && scored.get(catUpperSection) == null) {
                    // return -5;
                throw new InvalidScoringCategory(categoryName, num);
            }
            isJoker = true;
        }

        int s = ScoreFactory.getInstance().getScoreStrategy(categoryName)
                .calculate(new ArrayList<Die>(Arrays.asList(dice)), isJoker);
        return s;
    }

    private boolean isJokerCategory(String categoryName) {
        return categoryName.equals("fh") || categoryName.equals("s4") || categoryName.equals("s5");
    }

    private boolean isJokerYahtzee() {
        return scored.get("5k") != null && dice[0].getNumber() == dice[1].getNumber()
                && dice[1].getNumber() == dice[2].getNumber() && dice[2].getNumber() == dice[3].getNumber()
                && dice[3].getNumber() == dice[4].getNumber();
    }

    private boolean isBonusYahtzee() {
        return scored.get("5k") != null && scored.get("5k") == 50 && dice[0].getNumber() == dice[1].getNumber()
                && dice[1].getNumber() == dice[2].getNumber() && dice[2].getNumber() == dice[3].getNumber()
                && dice[3].getNumber() == dice[4].getNumber();
    }

    private int getUSScore() {
        SumNullBuilder sb = new SumNullBuilder();
        sb.add(scored.get("1s")).add(scored.get("2s")).add(scored.get("3s")).add(scored.get("4s")).add(scored.get("5s"))
                .add(scored.get("6s"));

        return sb.getSum();
    }

    public int getScore() {
        SumNullBuilder sb = new SumNullBuilder();
        sb.add(getUSScore());

        if (sb.getSum() >= 63) {
            scored.put("UB", 35);
            sb.add(35);
        }

        sb.add(scored.get("3k")).add(scored.get("4k")).add(scored.get("s4")).add(scored.get("s5")).add(scored.get("fh"))
                .add(scored.get("5k")).add(scored.get("ch")).add(scored.get("YB"));

        return sb.getSum();
    }

    public int getDice(int i) {
        return dice[i].getNumber();
    }

    public int getRoll() {
        return roll;
    }

}
