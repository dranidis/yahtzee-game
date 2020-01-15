package com.asdt.yahtzee.game;

import java.util.Random;

public class Die {

    int number;

    public Die(int i) {
        number = i;
	}

	public Die() {
	}

	public void reroll() {
        Random r = new Random();
        number = r.nextInt(6) + 1;
    }

    public int getNumber() {
        return number;
    }

    @Override
    public String toString() {
        return "" + number;
    }
}
