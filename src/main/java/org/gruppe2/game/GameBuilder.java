package org.gruppe2.game;

import org.gruppe2.ai.Difficulty;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.session.HostSession;
import org.gruppe2.game.session.Session;
import org.gruppe2.game.session.SessionContext;

public class GameBuilder {
    private String name = "A Fine Table";
    private int min = 2;
    private int max = 4;
    private int buyIn = 100;
    private int smallBlind = 10;
    private int bigBlind = 20;
    private int waitTime = 1000;
    private Difficulty botDiff = Difficulty.NORMAL;
    private GameModel.BotPolicy botPolicy = GameModel.BotPolicy.FILL;

    public GameBuilder name(String name) {
        this.name = name;

        return this;
    }

    public GameBuilder playerRange(int min, int max) {
        this.min = min;
        this.max = max;

        return this;
    }

    public GameBuilder buyIn(int buyIn) {
        this.buyIn = buyIn;

        return this;
    }

    
    public GameBuilder blinds(int smallBlind, int bigBlind){
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;

        return this;
    }
    public GameBuilder botDiff(Difficulty botDiff){
        this.botDiff = botDiff;

        return this;
    }

    public GameBuilder botPolicy (GameModel.BotPolicy policy) {
        botPolicy = policy;

        return this;
    }

    public GameBuilder waitTime (int waitTime) {
        this.waitTime = waitTime;

        return this;
    }

    public SessionContext start() {
        return Session.start(HostSession.class, name, min, max, buyIn, botPolicy, smallBlind, bigBlind, waitTime, botDiff);
    }
}
