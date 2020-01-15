package com.asdt.yahtzee.game;

public class UnknownScoringCategory  extends Exception {

	/**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnknownScoringCategory(String categoryName) {
        super("Unknown " + categoryName + " for scoring");
	}

}
