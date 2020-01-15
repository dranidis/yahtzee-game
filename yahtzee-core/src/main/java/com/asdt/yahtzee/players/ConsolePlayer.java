package com.asdt.yahtzee.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ConsolePlayer implements GamePlayer {

    Scanner s;

    public ConsolePlayer() {
        s = new Scanner(System.in, "UTF-8");
    }

    @Override
    public int[] rollKeeping() {

        List<Integer> list = new ArrayList<>();
        int keep = 7;
        do {
            if (s.hasNextInt()) {
                keep = s.nextInt();

                if (keep > 0) {
                    if (keep < 6)
                        list.add(keep);
                    else
                        print("1 to 5!");
                } else { // -1 will keep all
                    break;
                }
            } else {
                s.next();
                print("1 to 5!");
            }
        } while (keep != 0);
        if (keep < 0) {
            for (int i = 1; i < 6; i++)
                list.add(i);
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    protected void print(String string) {
        System.out.println(string);
    }

    @Override
    public String selectCategory() {
        return s.next();
    }
}
