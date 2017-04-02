package org.gruppe2.ai.TestClient;

import org.gruppe2.game.Player;

/**
 * Helper class for returning the result of a testclient session
 */
public class GameResult {
    int roundsPlayed = 0;
    int roundsWon = 0;
    Player winnerOfGame;

    public Player getWinnerOfGame() {
        return winnerOfGame;
    }

    public void setWinnerOfGame(Player winnerOfGame) {
        this.winnerOfGame = winnerOfGame;
    }

    public int getRoundsPlayed() {
        return roundsPlayed;
    }

    public void setRoundsPlayed(int roundsPlayed) {
        this.roundsPlayed = roundsPlayed;
    }

    public int getRoundsWon() {
        return roundsWon;
    }

    public void setRoundsWon(int roundsWon) {
        this.roundsWon = roundsWon;
    }
}
