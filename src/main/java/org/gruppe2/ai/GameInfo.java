package org.gruppe2.ai;

import java.util.List;

import org.gruppe2.game.Card;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;

/**
 * POJO that contains the information AI needs to make decisions.
 * This way we don't have to pass around references for every turn,
 * but instead GameInfo object
 */
public class GameInfo {
    PossibleActions possibleActions;
    List<Card> communityCards;
    private int bigBlind;
    int highestBet;
    int roundNumber;
    List<RoundPlayer> activePlayers;
    Difficulty difficulty;

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public int getHighestBet() {
        return highestBet;
    }

    public void setHighestBet(int highestBet) {
        this.highestBet = highestBet;
    }

    public List<RoundPlayer> getActivePlayers() {
        return activePlayers;
    }

    public void setActivePlayers(List<RoundPlayer> activePlayers) {
        this.activePlayers = activePlayers;
    }

    public PossibleActions getPossibleActions() {
        return possibleActions;
    }

    public void setPossibleActions(PossibleActions possibleActions) {
        this.possibleActions = possibleActions;
    }

    public List<Card> getCommunityCards() {
        return communityCards;
    }

    public void setCommunityCards(List<Card> communityCards) {
        this.communityCards = communityCards;
    }

    public int getBigBlind() {
        return bigBlind;
    }

    public void setBigBlind(int bigBlind) {
        this.bigBlind = bigBlind;
    }
}
