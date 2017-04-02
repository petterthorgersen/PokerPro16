package org.gruppe2.game;

public enum Hand {
    ROYALFLUSH("Royal flush"),
    STRAIGHTFLUSH("Straight flush"),
    FOUROFAKIND("Four of a kind"),
    FULLHOUSE("Full house"),
    FLUSH("Flush"),
    STRAIGHT("Straight"),
    THREEOFAKIND("Three of a kind"),
    TWOPAIRS("Two pairs"),
    PAIR("Pair"),
    HIGHCARD("High card");

    private final String name;

    Hand(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
