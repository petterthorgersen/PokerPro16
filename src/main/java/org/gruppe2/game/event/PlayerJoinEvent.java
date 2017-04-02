package org.gruppe2.game.event;

import org.gruppe2.game.Player;

public class PlayerJoinEvent implements Event {
    /**
	 * 
	 */
	private static final long serialVersionUID = 452993610851375218L;
	private final Player player;

    public PlayerJoinEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
