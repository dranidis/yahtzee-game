package com.asdt.yahtzee.game;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PlayerTest {

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void getScore0() {
        Player p = new Player("p1");
        assertEquals("Empty score is 0", 0, p.getScore());
    }

    @Mock
    Map<String, Integer> scored;

    @InjectMocks
    Player p = new Player("p");

    @Test
    public void bonusUpper() {
        Mockito.when(scored.get("1s")).thenReturn(3);
        Mockito.when(scored.get("2s")).thenReturn(6);
        Mockito.when(scored.get("3s")).thenReturn(9);
        Mockito.when(scored.get("4s")).thenReturn(12);
        Mockito.when(scored.get("5s")).thenReturn(15);
        Mockito.when(scored.get("6s")).thenReturn(18);

        assertEquals("Get bonus 35 when at least 63", 63 + 35, p.getScore());

        Mockito.when(scored.get("3k")).thenReturn(10);
        Mockito.when(scored.get("4k")).thenReturn(20);
        Mockito.when(scored.get("fh")).thenReturn(25);
        Mockito.when(scored.get("s4")).thenReturn(30);
        Mockito.when(scored.get("s5")).thenReturn(40);
        Mockito.when(scored.get("5k")).thenReturn(50);
        Mockito.when(scored.get("ch")).thenReturn(10);

        assertEquals("Total ", 63 + 35 + 185, p.getScore());
    }

    @Test
    public void noBonusUpper() {
        Mockito.when(scored.get("1s")).thenReturn(2);
        Mockito.when(scored.get("2s")).thenReturn(6);
        Mockito.when(scored.get("3s")).thenReturn(9);
        Mockito.when(scored.get("4s")).thenReturn(12);
        Mockito.when(scored.get("5s")).thenReturn(15);
        Mockito.when(scored.get("6s")).thenReturn(18);

        assertEquals("No bonus 35 when 62", 62, p.getScore());

        Mockito.when(scored.get("3k")).thenReturn(10);
        Mockito.when(scored.get("4k")).thenReturn(20);
        Mockito.when(scored.get("fh")).thenReturn(25);
        Mockito.when(scored.get("s4")).thenReturn(30);
        Mockito.when(scored.get("s5")).thenReturn(40);
        Mockito.when(scored.get("5k")).thenReturn(50);
        Mockito.when(scored.get("ch")).thenReturn(10);

        assertEquals("Total ", 62 + 185, p.getScore());
    }

    @Test
    public void yahtzeeBonus() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();
        mockScored.put("1s", 2);
        mockScored.put("2s", 6);
        mockScored.put("3s", 9);
        mockScored.put("4s", 12);
        mockScored.put("5s", 15);
        mockScored.put("6s", 18);
        mockScored.put("s4", 30);
        mockScored.put("s5", 40);
        mockScored.put("5k", 50);
        mockScored.put("ch", 10);

        Player p = new Player("p");
        p.setScored(mockScored);
        p.setDice(new Die[] { new Die(2), new Die(2), new Die(2), new Die(2), new Die(2) });

        assertEquals("score fh as joker", 25, p.score("fh"));
        assertEquals("bonus yahtzee", 100, p.scored.get("YB").intValue());
        assertEquals("Total ", 62 + 130 + 100 + 25, p.getScore());
    }

    @Test
    public void thereIsNoYahtzeeBonusIfYahtzeeIs0() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();

        mockScored.put("5k", 0);

        Player p = new Player("p");
        p.setScored(mockScored);
        p.setDice(new Die[] { new Die(2), new Die(2), new Die(2), new Die(2), new Die(2) });

        assertEquals("score 2s", 10, p.score("2s"));
        assertNull("bonus yahtzee", p.scored.get("YB"));
        assertEquals("Total ", 10, p.getScore());
    }

    @Test
    public void yahtzeeJokerHasToBeFilledFirstInTheUpperSectionWhenYahtzeeIs0() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();

        mockScored.put("5k", 0);

        Player p = new Player("p");
        p.setScored(mockScored);

        int die = 2;
        p.setDice(new Die[] { new Die(die), new Die(die), new Die(die), new Die(die), new Die(die) });

        String categoryName = "fh";

        exceptionRule.expect(InvalidScoringCategory.class);
        exceptionRule.expectMessage(categoryName
                + " cannot be scored as Joker before you score the matching upper section first: " + die + "s");
        p.score(categoryName);
    }

    @Test
    public void yahtzeeJokerWhenYahtzeeIs0() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();

        mockScored.put("2s", 6);
        mockScored.put("5k", 0);

        Player p = new Player("p");
        p.setScored(mockScored);
        p.setDice(new Die[] { new Die(2), new Die(2), new Die(2), new Die(2), new Die(2) });

        assertEquals("score fh", 25, p.score("fh"));
        assertNull("bonus yahtzee", p.scored.get("YB"));
        assertEquals("Total ", 31, p.getScore());
    }

    @Test
    public void yahtzeeBonusOnlyInUpper() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();
        mockScored.put("1s", 2);

        mockScored.put("3s", 9);
        mockScored.put("4s", 12);
        mockScored.put("5s", 15);
        mockScored.put("6s", 18);
        mockScored.put("s4", 30);
        mockScored.put("s5", 40);
        mockScored.put("5k", 50);
        mockScored.put("ch", 10);

        Player p = new Player("p");
        p.setScored(mockScored);
        p.setDice(new Die[] { new Die(2), new Die(2), new Die(2), new Die(2), new Die(2) });

        exceptionRule.expect(InvalidScoringCategory.class);
        exceptionRule.expectMessage(
                "fh" + " cannot be scored as Joker before you score the matching upper section first: " + 2 + "s");
        p.score("fh");

        assertEquals("score 2s as joker is possible", 10, p.score("2s"));
        assertEquals("bonus yahtzee", 100, p.scored.get("YB").intValue());
        assertEquals("Total (bonus)", 56 + 130 + 100 + 10 + 35, p.getScore());
    }

    @Test
    public void secondYahtzeeBonus() throws InvalidScoringCategory {
        Map<String, Integer> mockScored = new HashMap<>();
        mockScored.put("1s", 2);

        mockScored.put("3s", 9);
        mockScored.put("4s", 12);
        mockScored.put("5s", 15);
        mockScored.put("6s", 18);
        mockScored.put("s4", 30);
        mockScored.put("s5", 40);
        mockScored.put("5k", 50);
        mockScored.put("ch", 10);

        Player p = new Player("p");
        p.setScored(mockScored);
        p.setDice(new Die[] { new Die(2), new Die(2), new Die(2), new Die(2), new Die(2) });

        assertEquals("score 2s as joker is possible", 10, p.score("2s"));
        assertEquals("score fh as joker is possible", 25, p.score("fh"));
        assertEquals("bonus yahtzee", 200, p.scored.get("YB").intValue());
        assertEquals("Total 56+10 upper plus 35 bonus, 130+25 plus 200 YBonus", 56 + 130 + 200 + 10 + 35 + 25,
                p.getScore());
    }

    @Test
    public void maxScore() throws InvalidScoringCategory {
        Player p = new Player("p");
        int totalScore = 0;

        p.setDice(6, 6, 6, 6, 6);
        p.score("5k");
        totalScore += 50;
        assertEquals("5k", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("6s");
        totalScore += 100 + 5 * 6;
        assertEquals("6s", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("3k");
        totalScore += 100 + 5 * 6;
        assertEquals("3k", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("4k");
        totalScore += 100 + 5 * 6;
        assertEquals("4k", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("ch");
        totalScore += 100 + 5 * 6;
        assertEquals("ch", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("fh");
        totalScore += 100 + 25;
        assertEquals("fh", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("s4");
        totalScore += 100 + 30;
        assertEquals("s4", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("s5");
        totalScore += 100 + 40;
        assertEquals("s5", totalScore, p.getScore());

        for (int i = 1; i <= 5; i++) {
            p.setDice(i, i, i, i, i);
            p.score("" + i + "s");
            totalScore += 100 + 5 * i;
            if (i == 4) {
                totalScore += 35; // Uppersection bonus
            }
            assertEquals("" + i + "s", totalScore, p.getScore());
        }

        assertEquals("Max score", 1575, p.getScore());

    }

    @Test
    public void jokerYajhtzeeAtDifferentUpperSectionShouldBeZero() throws InvalidScoringCategory {
        Player p = new Player("p");
        int totalScore = 0;

        p.setDice(6, 6, 6, 6, 6);
        p.score("5k");
        totalScore += 50;
        assertEquals("5k", totalScore, p.getScore());

        p.setDice(6, 6, 6, 6, 6);
        p.score("5s");
        totalScore += 100; // + 5 * 0
        assertEquals("6s", totalScore, p.getScore());

    }

    @Test
    public void updateUpperSectionBonusAfterScoring() throws InvalidScoringCategory {
        Player p = new Player("p");
        String cat = "";

        for(int i = 1; i <= 5; i++ ) {
            p.setDice(i, i, i, 6, 6);
            cat = "" + i + "s";
            p.score(cat);
            assertEquals(0, p.getFullScoreSheet().get("UB").intValue());
        }
        p.setDice(1, 1, 6, 6, 6);
        cat = "" + 6 + "s";
        p.score(cat);
        assertEquals(35, p.getFullScoreSheet().get("UB").intValue());
    }

}
