package com.asdt.yahtzee.network.messages;

import java.io.Serializable;

public class ScoreRequest implements Serializable {

	/**
     *
     */
    private static final long serialVersionUID = 1L;
    public String name;
    public String categoryName;

    public ScoreRequest(String name, String categoryName) {
        this.name = name;
        this.categoryName = categoryName;
	}

}
