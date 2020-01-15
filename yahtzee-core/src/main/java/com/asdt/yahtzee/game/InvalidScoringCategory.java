package com.asdt.yahtzee.game;

public class InvalidScoringCategory extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String categoryName;

    private int die;

    public InvalidScoringCategory(String categoryName, int die) {
        this.categoryName = categoryName;
        this.die = die;
    }

    @Override
    public String getMessage() {
        return categoryName + " cannot be scored as Joker before you score the matching upper section first: " + die
                + "s";
    }
}
