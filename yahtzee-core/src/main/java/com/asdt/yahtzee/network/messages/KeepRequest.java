package com.asdt.yahtzee.network.messages;

import java.io.Serializable;

public class KeepRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    int[] keep;
    String name;

    public KeepRequest(String name, int[] keep) {
        this.name = name;
        this.keep = new int[keep.length];
        for (int i = 0; i < keep.length; i++) {
            this.keep[i] = keep[i];
        }
    }

    public void print() {
        for (int i = 0; i < keep.length; i++) {
            System.out.println(keep[i]);
        }
    }

    public int[] getKeep() {
        int[] keepCopy = new int[keep.length];
        for (int i = 0; i < keep.length; i++)
            keepCopy[i] = keep[i];
        return keepCopy;
    }
}
