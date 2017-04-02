package org.gruppe2.game.session;

import java.util.UUID;

import org.gruppe2.ai.Difficulty;
import org.gruppe2.game.controller.AIController;
import org.gruppe2.game.controller.ChatController;
import org.gruppe2.game.controller.GameController;
import org.gruppe2.game.controller.NetworkServerController;
import org.gruppe2.game.controller.RecordController;
import org.gruppe2.game.controller.RoundController;
import org.gruppe2.game.controller.StatisticsController;
import org.gruppe2.game.model.ChatModel;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.model.StatisticsModel;

public class HostSession extends Session {
    public HostSession(String name, Integer minPlayers, Integer maxPlayers, Integer buyIn, GameModel.BotPolicy botPolicy, Integer smallBlind, Integer bigBlind, Integer waitTime, Difficulty botDiff) {
        addModel(new GameModel(UUID.randomUUID(), name, minPlayers, maxPlayers, buyIn, botPolicy, smallBlind, bigBlind, waitTime, botDiff));
        addModel(new RoundModel());
        addModel(new ChatModel());
        addModel(new StatisticsModel());
    }

    @Override
    public void init() {
        addController(GameController.class);
        addController(RoundController.class);
        addController(AIController.class);
        addController(ChatController.class);
        addController(StatisticsController.class);
        addController(NetworkServerController.class);
        addController(RecordController.class);
    }

    @Override
    public void update() {

    }
}