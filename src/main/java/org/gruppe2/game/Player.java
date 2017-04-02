package org.gruppe2.game;

import java.io.Serializable;
import java.util.UUID;

import org.gruppe2.game.session.Query;

public class Player implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6346624233994406345L;
	private final UUID uuid;
    private final String name;
    private final String avatar;
    private final Query<Action> action = new Query<>();
    private final boolean bot;
    private final int tablePosition;

    private volatile int bank;

    public Player(UUID uuid, String name, String avatar, boolean bot, int tablePosition) {
        this.uuid = uuid;
        this.name = name;
        this.avatar = avatar;
        this.bot = bot;
        this.tablePosition = tablePosition;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public boolean isBot() {
        return bot;
    }

    public int getTablePosition() {
        return tablePosition;
    }

    public int getBank() {
        return bank;
    }

    public void setBank(int bank) {
        this.bank = bank;
    }

    public Query<Action> getAction() {
        return action;
    }
}
