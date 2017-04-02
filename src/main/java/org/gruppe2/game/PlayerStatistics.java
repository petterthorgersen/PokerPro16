package org.gruppe2.game;

import java.util.concurrent.atomic.AtomicInteger;

public class PlayerStatistics {
    private final AtomicInteger gamesPlayed = new AtomicInteger();
    private final AtomicInteger gamesWon = new AtomicInteger();
    private final AtomicInteger gamesLost = new AtomicInteger();
    private final AtomicInteger timesFolded = new AtomicInteger();
    private final AtomicInteger timesCalled = new AtomicInteger();
    private final AtomicInteger timesChecked = new AtomicInteger();
    private final AtomicInteger timesRaised = new AtomicInteger();
    private final AtomicInteger totalBets = new AtomicInteger();
    private final AtomicInteger totalWinnings = new AtomicInteger();

    public AtomicInteger getGamesPlayed() {
        return gamesPlayed;
    }

    public AtomicInteger getGamesWon() {
        return gamesWon;
    }

    public AtomicInteger getGamesLost() {
        return gamesLost;
    }

    public AtomicInteger getTimesFolded() {
        return timesFolded;
    }

    public AtomicInteger getTimesCalled() {
        return timesCalled;
    }

    public AtomicInteger getTimesChecked() {
        return timesChecked;
    }

    public AtomicInteger getTimesRaised() {
        return timesRaised;
    }

    public AtomicInteger getTotalBets() {
        return totalBets;
    }

    public AtomicInteger getTotalWinnings() {
        return totalWinnings;
    }
}
