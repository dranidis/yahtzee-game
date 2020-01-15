package com.asdt.yahtzee.players;

import static org.junit.Assert.assertEquals;

import com.asdt.yahtzee.game.Die;
import com.asdt.yahtzee.game.Player;

import org.junit.Test;

public class MaxScoringTest {

    @Test
    public void testMaxFh() {
        Die[] kept = new Die[5];
        kept[0] = new Die(1);
        kept[1] = new Die(1);
        kept[2] = new Die(1);
        kept[3] = new Die(6);
        kept[4] = new Die(6);

        Player p = new Player("p");
        p.setDice(kept);

        MaximumScoringStrategy strategy = new MaximumScoringStrategy();
        assertEquals("fh gives 25", "fh", strategy.selectCategory(p));
    }

    @Test
    public void testMax3k() {
        Die[] kept = new Die[5];
        kept[0] = new Die(5);
        kept[1] = new Die(5);
        kept[2] = new Die(5);
        kept[3] = new Die(6);
        kept[4] = new Die(6);

        Player p = new Player("p");
        p.setDice(kept);

        MaximumScoringStrategy strategy = new MaximumScoringStrategy();
        assertEquals("3k gives 27, ch gives 27, fh gives 25 select fh", "fh", strategy.selectCategory(p));
    }

    @Test
    public void testMaxCh() {
        Die[] kept = new Die[5];
        kept[0] = new Die(5);
        kept[1] = new Die(5);
        kept[2] = new Die(5);
        kept[3] = new Die(6);
        kept[4] = new Die(6);

        Player p = new Player("p");
        p.setDice(kept);
        p.getScored().put("3k", 27); // make it not available;

        MaximumScoringStrategy strategy = new MaximumScoringStrategy();
        assertEquals("3k not avail, ch gives 27, fh gives 25, select fh due to prob values", "fh", strategy.selectCategory(p));
    }

}
