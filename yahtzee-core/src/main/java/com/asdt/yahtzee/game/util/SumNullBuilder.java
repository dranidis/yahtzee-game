package com.asdt.yahtzee.game.util;

public class SumNullBuilder {
    int sum = 0;
    public SumNullBuilder add(Integer i) {
        if (i != null) {
            sum += i.intValue();
        }
        return this;
    }
    public int getSum() {
        return sum;
    }
}
