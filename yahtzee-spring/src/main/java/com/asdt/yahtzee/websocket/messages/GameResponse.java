package com.asdt.yahtzee.websocket.messages;

import java.util.Arrays;

public class GameResponse {
    int round;
    private String currentPlayer;
    private int roll;
    int[] dice;
    private String categoryName;
    private int score;
    private SheetSubResponse sheet;

    public GameResponse() {
    }

    public GameResponse(int round, String currentPlayer, SheetSubResponse sheet, int[] dice, int roll, String categoryName, int score) {
        this.round = round;
        this.currentPlayer = currentPlayer;
        this.dice = new int[dice.length];
        for (int i = 0; i < dice.length; i++) {
            this.dice[i] = dice[i];
        }
        this.roll = roll;
        this.categoryName = categoryName;
        this.sheet = sheet;
        this.score = score;
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(String currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    @Override
    public String toString() {
        return "GameMessage [categoryName=" + categoryName + ", currentPlayer=" + currentPlayer + ", dice="
                + Arrays.toString(dice) + ", roll=" + roll + ", score=" + score + "]";
    }

    public int[] getDice() {
        return dice;
    }

    public void setDice(int[] dice) {
        this.dice = dice;
    }

    public int getRoll() {
        return roll;
    }

    public void setRoll(int roll) {
        this.roll = roll;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public SheetSubResponse getSheet() {
        return sheet;
    }

    public void setSheet(SheetSubResponse sheet) {
        this.sheet = sheet;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

}
