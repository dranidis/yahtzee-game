package com.asdt.yahtzee.network.messages;

import java.io.Serializable;

public class UserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    public String name;

    public int numPlayers;

    public UserRequest(String name, int numPlayers) {
        this.name = name;
        this.numPlayers = numPlayers;
	}

}
