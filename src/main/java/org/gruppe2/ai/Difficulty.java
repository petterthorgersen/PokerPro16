package org.gruppe2.ai;

public enum Difficulty {
    EASY("Easy"),
    NORMAL("Normal"),
    HARD("Hard"), 
    RANDOM("Random"),
    NONE("None");

    private final String name;

    Difficulty(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
