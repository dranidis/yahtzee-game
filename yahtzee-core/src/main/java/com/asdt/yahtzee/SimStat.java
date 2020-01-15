package com.asdt.yahtzee;

import com.asdt.yahtzee.game.Game;

public class SimStat {
    Game game;
    int games = 0;
	public SimStat(Game game, int i) {
        this.game = game;
        this.games = i;

        start();
    }

    private void start() {
        double totalScore = 0;
        int min = 10000;
        int max = 0;
        for(int i=0; i< games; i++) {
            Simulation s = new Simulation(game);
            int score = s.getScore();
            totalScore += score;
            if (score > max) max = score;
            if (score < min) min = score;
        }
        System.out.println(String.format("Avg: %6.2f", totalScore/games));
        System.out.println(String.format("Min: %4d", min));
        System.out.println(String.format("Max: %4d", max));
    }



}
