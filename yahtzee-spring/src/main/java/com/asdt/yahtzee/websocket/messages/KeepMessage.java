package com.asdt.yahtzee.websocket.messages;

import java.util.Arrays;

public class KeepMessage {
    private String name;
    private int[] keep;
    private int msDelay;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int[] getKeep() {
        return keep;
    }

    public void setKeep(int[] keep) {
        this.keep = keep;
    }

    public KeepMessage(String name, int[] keep, int msDelay) {
        this.name = name;
        this.keep = keep;
        this.msDelay =msDelay;
    }

    public KeepMessage() {
    }

    @Override
    public String toString() {
        return "KeepMessage [keep=" + Arrays.toString(keep) + ", msDelay=" + msDelay + ", name=" + name + "]";
    }

    public int getMsDelay() {
        return msDelay;
    }

    public void setMsDelay(int msDelay) {
        this.msDelay = msDelay;
    }

}
