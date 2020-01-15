package com.asdt.yahtzee;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.asdt.yahtzee.game.Game;
import com.asdt.yahtzee.game.InvalidScoringCategory;
import com.asdt.yahtzee.game.UnknownScoringCategory;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Unit test for simple App.
 */
public class AppTest {
    /**
     * Rigorous Test :-)
     */

    private Game game;

    @Before
    public void setUp() {
        game = new Game();
        game.addPlayer("p1");
        game.addPlayer("p2");
        game.startRound();
    }

    @Test
    public void oneRoll() {
        game.rollKeeping("p1");
        int[] dice1 = game.getDice();
        boolean[] kept1 = game.getKept();

        assertEquals("5 numbers are rolled", 5, dice1.length);
        for (int i : dice1) {
            assertTrue("Each dice is 1-6", i >= 1 && i <= 6);
        }
        assertArrayEquals("Arrays are equal", new boolean[] { false, false, false, false, false }, kept1);
    }

    @Test
    public void oneRoll2() {
        game.rollKeeping("p1");
        int[] dice1 = game.getDice();

        game.rollKeeping("p1", 1, 2);
        int[] dice2 = game.getDice();
        boolean[] kept2 = game.getKept();

        assertArrayEquals("Arrays are equal", new boolean[] { true, true, false, false, false }, kept2);
        assertEquals("Kept dice 1 is same", dice1[0], dice2[0]);
        assertEquals("Kept dice 2 is same", dice1[1], dice2[1]);
    }

    @Test
    public void oneRoll3() {
        game.rollKeeping("p1");

        int[] dice1 = game.getDice();

        game.rollKeeping("p1", 1, 2);
        int[] dice2 = game.getDice();

        // final reroll
        game.rollKeeping("p1", 1, 2, 3);
        int[] dice3 = game.getDice();
        boolean[] kept3 = game.getKept();

        assertArrayEquals("Arrays are equal", new boolean[] { true, true, true, false, false }, kept3);
        assertEquals("Kept dice 1 is same", dice1[0], dice3[0]);
        assertEquals("Kept dice 2 is same", dice1[1], dice3[1]);
        assertEquals("Kept dice 2 is same", dice2[2], dice3[2]);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void notYourTurn() {
        exceptionRule.expect(RuntimeException.class);
        exceptionRule.expectMessage("Not your turn");
        game.rollKeeping("p2");
    }

    @Test
    public void scoredCategory() throws UnknownScoringCategory, InvalidScoringCategory {
        game.getNextPlayer();

        game.rollKeeping("p1");
        int score1 = game.scoreACategory("p1", "1s");
        assertTrue("Category 1 not used by p1 positive score", score1 >= 0);

        /* scores */
        int totalScore1 = game.getPlayerScore("p1");
        assertEquals("Total score of p1 is first score", score1, totalScore1);
        int totalScore2 = game.getPlayerScore("p2");
        assertEquals("Total score of p2 (before playing) 0", 0, totalScore2);

        game.getNextPlayer();

        game.rollKeeping("p2");

        int score2 = game.scoreACategory("p2", "1s");
        assertTrue("Category 1 not used by p2 positive score", score2 >= 0);

        /* scores */
        totalScore1 = game.getPlayerScore("p1");
        assertEquals("Total score of p1 is first score", score1, totalScore1);
        totalScore2 = game.getPlayerScore("p2");
        assertEquals("Total score of p2 (after playing) score2", score2, totalScore2);

        game.startRound();

        game.rollKeeping("p1");
        int score11 = game.scoreACategory("p1", "2s");
        assertTrue("Category 2s not used by p1 positive score", score11 >= 0);

        /* scores */
        totalScore1 = game.getPlayerScore("p1");
        assertEquals("Total score of p1 is first and secod score", score1 + score11, totalScore1);
        totalScore2 = game.getPlayerScore("p2");
        assertEquals("Total score of p2 (after playing) score2", score2, totalScore2);

    }

    @Test
    public void alreadyScoredCategory() throws UnknownScoringCategory, InvalidScoringCategory {
        game.getNextPlayer();

        game.rollKeeping("p1");
        game.rollKeeping("p1", 1, 4);
        int score = game.scoreACategory("p1", "1s");
        assertTrue("Category 1 not used by p1 positive score", score >= 0);

        game.getNextPlayer();

        game.rollKeeping("p2");
        score = game.scoreACategory("p2", "1s");
        assertTrue("Category 1 not used by p2 positive score", score >= 0);

        game.startRound();

        game.rollKeeping("p1");
        boolean[] kept1 = game.getKept();
        // make sure kept are not remember from previous round
        assertArrayEquals("Arrays are equal", new boolean[] { false, false, false, false, false }, kept1);

        score = game.scoreACategory("p1", "1s");
        assertEquals("Category 1 already used by p1", -1, score);
    }

    @Test
    public void unknownCategoryThrowsException() throws UnknownScoringCategory, InvalidScoringCategory {
        game.getNextPlayer();

        game.rollKeeping("p1");
        game.rollKeeping("p1", 1, 4);

        String categoryName = "11s";
        exceptionRule.expect(UnknownScoringCategory.class);
        exceptionRule.expectMessage("Unknown " + categoryName + " for scoring");
        game.scoreACategory("p1", categoryName);
    }
}
