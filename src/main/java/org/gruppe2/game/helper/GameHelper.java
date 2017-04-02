package org.gruppe2.game.helper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.gruppe2.ai.Difficulty;
import org.gruppe2.game.Player;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.session.SessionContext;

public class GameHelper {
    private final GameModel model;

    public GameHelper(SessionContext context) {
        model = context.getModel(GameModel.class);
    }

    public int getButton() {
        return model.getButton();
    }

    public Player getButtonPlayer() {
        return model.getPlayers().get(model.getButton());
    }

    public boolean canStart() {
        return model.getPlayers().size() >= model.getMinPlayers();
    }

    public List<Player> getPlayers() {
        return model.getPlayers();
    }

    public Optional<Player> findPlayerByUUID(UUID uuid) {
        return findPlayer(p -> p.getUUID().equals(uuid));
    }

    public Optional<Player> findPlayerByName(String name) {
        return findPlayer(p -> p.getName().equals(name));
    }

    public Optional<Player> findPlayer(Predicate<Player> predicate) {
        return model.getPlayers().stream()
                .filter(predicate)
                .findFirst();
    }

    public GameModel getModel() {
        return model;
    }

    public int getBuyIn() {
        return model.getBuyIn();
    }

    public int getSmallBlind() {
        return model.getSmallBlind();
    }

    public int getBigBlind() {
        return model.getBigBlind();
    }

    public void setButton(int button) {
        model.setButton(button);
    }

    public int getRoundsCompleted() {
        return model.getRoundsCompleted();
    }

    public void setRoundsCompleted(int rounds) {
        model.setRoundsCompleted(rounds);
    }

    public int getWaitTime() {
        return model.getWaitTime();
    }

    public Difficulty getBotDiff() {
        return model.getBotDiff();
    }
}
