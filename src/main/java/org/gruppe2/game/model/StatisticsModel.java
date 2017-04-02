package org.gruppe2.game.model;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.gruppe2.game.PlayerStatistics;

public class StatisticsModel {
    private final Map<UUID, PlayerStatistics> playerStatistics = new ConcurrentHashMap<>();

    public Map<UUID, PlayerStatistics> getPlayerStatistics() {
        return playerStatistics;
    }
}
