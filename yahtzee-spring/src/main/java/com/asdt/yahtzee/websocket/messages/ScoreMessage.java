package com.asdt.yahtzee.websocket.messages;

public class ScoreMessage {

    private String name;
    private String category;



    public ScoreMessage(String playerName, String categoryName) {
        this.name = playerName;
        this.category = categoryName;
    }

    public ScoreMessage() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "ScoreMessage [category=" + category + ", name=" + name + "]";
    }

}
