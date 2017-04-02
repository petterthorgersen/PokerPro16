package org.gruppe2.game;

import java.io.Serializable;
import java.util.UUID;

public class RoundPlayer implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5367497435950538244L;
	private final UUID uuid;
    private Card[] cards = new Card[2];

    private volatile int bet = 0;

    public RoundPlayer(UUID uuid, Card card1, Card card2) {
        this.uuid = uuid;
        this.cards[0] = card1;
        this.cards[1] = card2;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        this.bet = bet;
    }

    public Card[] getCards() {
        return cards;
    }
    public void setCards(Card[] cards){
    	this.cards = cards;
    }
}
