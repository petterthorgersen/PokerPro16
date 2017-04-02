package org.gruppe2.game.event;

import org.gruppe2.game.Player;

public class PlayerLeaveEvent implements Event {
    /**
	 * 
	 */
	private static final long serialVersionUID = 2540777702147787581L;
	private final Player player;

    public PlayerLeaveEvent(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
