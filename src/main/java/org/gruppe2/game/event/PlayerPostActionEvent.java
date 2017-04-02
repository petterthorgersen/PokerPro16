package org.gruppe2.game.event;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;

public class PlayerPostActionEvent implements Event {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6633381682061970387L;
	private final Player player;
    private final RoundPlayer roundPlayer;
    private final Action action;

    public PlayerPostActionEvent(Player player, RoundPlayer roundPlayer, Action action) {
        this.player = player;
        this.roundPlayer = roundPlayer;
        this.action = action;
    }

    public Player getPlayer() {
        return player;
    }

    public RoundPlayer getRoundPlayer() {
        return roundPlayer;
    }

    public Action getAction() {
        return action;
    }
}
