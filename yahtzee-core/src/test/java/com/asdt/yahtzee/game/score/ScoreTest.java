package com.asdt.yahtzee.game.score;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.asdt.yahtzee.game.Die;
import com.asdt.yahtzee.game.score.ScoreFactory;

import org.junit.Test;

public class ScoreTest {

    @Test
    public void someScores() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(1));
        kept.add(new Die(2));
        kept.add(new Die(1));
        kept.add(new Die(6));
        kept.add(new Die(1));

        int score1 = ScoreFactory.getInstance().getScoreStrategy("1s").calculate(kept, false);
        assertEquals("1s category: 1 2 1 6 1 scores 3", 3, score1);

        int score2 = ScoreFactory.getInstance().getScoreStrategy("2s").calculate(kept, false);
        assertEquals("2s category: 1 2 1 6 1 scores 2", 2, score2);

        int score3 = ScoreFactory.getInstance().getScoreStrategy("3s").calculate(kept, false);
        assertEquals("3s category: 1 2 1 6 1 scores 0", 0, score3);

        int score4 = ScoreFactory.getInstance().getScoreStrategy("4s").calculate(kept, false);
        assertEquals("4s category: 1 2 1 6 1 scores 0", 0, score4);

        int score5 = ScoreFactory.getInstance().getScoreStrategy("5s").calculate(kept, false);
        assertEquals("5s category: 1 2 1 6 1 scores 0", 0, score5);

        int score6 = ScoreFactory.getInstance().getScoreStrategy("6s").calculate(kept, false);
        assertEquals("6s category: 1 2 1 6 1 scores 6", 6, score6);

        int score3k = ScoreFactory.getInstance().getScoreStrategy("3k").calculate(kept, false);
        assertEquals("3K category: 1 2 1 6 1 scores total:11", 11, score3k);

        int score4k = ScoreFactory.getInstance().getScoreStrategy("4k").calculate(kept, false);
        assertEquals("4K category: 1 2 1 6 1 scores total:0", 0, score4k);

        int scorefh = ScoreFactory.getInstance().getScoreStrategy("fh").calculate(kept, false);
        assertEquals("FH category: 1 2 1 6 1 scores 0", 0, scorefh);

        int scorech = ScoreFactory.getInstance().getScoreStrategy("ch").calculate(kept, false);
        assertEquals("change category: 1 2 1 6 1 scores 11", 11, scorech);

        int scoreS4 = ScoreFactory.getInstance().getScoreStrategy("ch").calculate(kept, false);
        assertEquals("change category: 1 2 1 6 1 scores 11", 11, scoreS4);
    }

    @Test
    public void fhScore() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(5));
        kept.add(new Die(5));
        kept.add(new Die(6));
        kept.add(new Die(6));
        kept.add(new Die(5));

        int score1 = ScoreFactory.getInstance().getScoreStrategy("1s").calculate(kept, false);
        assertEquals("1s category: 5 5 6 6 5 scores 0", 0, score1);

        int score2 = ScoreFactory.getInstance().getScoreStrategy("2s").calculate(kept, false);
        assertEquals("2s category: 5 5 6 6 5 scores 0", 0, score2);

        int score3 = ScoreFactory.getInstance().getScoreStrategy("3s").calculate(kept, false);
        assertEquals("3s category: 5 5 6 6 5 scores 0", 0, score3);

        int score4 = ScoreFactory.getInstance().getScoreStrategy("4s").calculate(kept, false);
        assertEquals("4s category: 5 5 6 6 5 scores 0", 0, score4);

        int score5 = ScoreFactory.getInstance().getScoreStrategy("5s").calculate(kept, false);
        assertEquals("5s category: 5 5 6 6 5 scores 15", 15, score5);

        int score6 = ScoreFactory.getInstance().getScoreStrategy("6s").calculate(kept, false);
        assertEquals("6s category: 5 5 6 6 5 scores 12", 12, score6);

        int score3k = ScoreFactory.getInstance().getScoreStrategy("3k").calculate(kept, false);
        assertEquals("3K category: 5 5 6 6 5 scores total: 27", 27, score3k);

        int score4k = ScoreFactory.getInstance().getScoreStrategy("4k").calculate(kept, false);
        assertEquals("4K category: 5 5 6 6 5 scores total:0", 0, score4k);

        int scorefh = ScoreFactory.getInstance().getScoreStrategy("fh").calculate(kept, false);
        assertEquals("FH category: 5 5 6 6 5 scores 25", 25, scorefh);

        int scorech = ScoreFactory.getInstance().getScoreStrategy("ch").calculate(kept, false);
        assertEquals("change category: 5 5 6 6 5 scores 27", 27, scorech);
    }

    @Test
    public void FourkScore() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(5));
        kept.add(new Die(5));
        kept.add(new Die(5));
        kept.add(new Die(6));
        kept.add(new Die(5));

        int score3k = ScoreFactory.getInstance().getScoreStrategy("3k").calculate(kept, false);
        assertEquals("3K category: 5 5 5 6 5 scores total: 26", 26, score3k);

        int score4k = ScoreFactory.getInstance().getScoreStrategy("4k").calculate(kept, false);
        assertEquals("4K category: 5 5 5 6 5 scores total:26", 26, score4k);

        int score5k = ScoreFactory.getInstance().getScoreStrategy("5k").calculate(kept, false);
        assertEquals("5K category: 5 5 5 6 5 scores total:0", 0, score5k);

        int scorefh = ScoreFactory.getInstance().getScoreStrategy("fh").calculate(kept, false);
        assertEquals("FH category: 5 5 5 6 5 scores 0", 0, scorefh);
    }

    @Test
    public void FivekScore() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(1));
        kept.add(new Die(1));
        kept.add(new Die(1));
        kept.add(new Die(1));
        kept.add(new Die(1));

        int score3k = ScoreFactory.getInstance().getScoreStrategy("3k").calculate(kept, false);
        assertEquals("3K category: 1 1 1 1 1 scores total: 5", 5, score3k);

        int score4k = ScoreFactory.getInstance().getScoreStrategy("4k").calculate(kept, false);
        assertEquals("4K category: 1 1 1 1 1 scores total: 5", 5, score4k);

        int score5k = ScoreFactory.getInstance().getScoreStrategy("5k").calculate(kept, false);
        assertEquals("5K category: 1 1 1 1 1 scores total: 50", 50, score5k);

        int scorefh = ScoreFactory.getInstance().getScoreStrategy("fh").calculate(kept, false);
        assertEquals("FH category: 5 5 5 6 5 scores 0", 0, scorefh);
    }

    @Test
    public void SSequenceScore() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(2));
        kept.add(new Die(2));
        kept.add(new Die(3));
        kept.add(new Die(4));
        kept.add(new Die(5));

        int scoreS4 = ScoreFactory.getInstance().getScoreStrategy("s4").calculate(kept, false);
        assertEquals("s3 category: 2 2 3 4 5 scores total: 30", 30, scoreS4);

        int scoreS5 = ScoreFactory.getInstance().getScoreStrategy("s5").calculate(kept, false);
        assertEquals("s4 category: 2 2 3 4 5 scores total: 0", 0, scoreS5);
    }

    @Test
    public void LSequenceScore() {
        List<Die> kept = new ArrayList<>();
        kept.add(new Die(1));
        kept.add(new Die(2));
        kept.add(new Die(3));
        kept.add(new Die(4));
        kept.add(new Die(5));

        int scoreS4 = ScoreFactory.getInstance().getScoreStrategy("s4").calculate(kept, false);
        assertEquals("s3 category: 1 2 3 4 5 scores total: 30", 30, scoreS4);

        int scoreS5 = ScoreFactory.getInstance().getScoreStrategy("s5").calculate(kept, false);
        assertEquals("s4 category: 1 2 3 4 5 scores total: 40", 40, scoreS5);
    }

    @Test
    public void categories13() {
        Map<String,Integer> sheet = ScoreFactory.getInstance().getScoringSheet();
        assertEquals("sheet has 13 + 2 (UB)+(YB) categories", 15, sheet.size());

    }

}
