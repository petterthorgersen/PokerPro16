package org.gruppe2.game.event;

import java.util.List;

import org.gruppe2.game.Player;

public class PlayerWonEvent implements Event{
    /**
	 * 
	 */
	private static final long serialVersionUID = -372752616850376543L;

    private final List<Player> players;
    private final List<Integer> chips;

    public PlayerWonEvent(List<Player> players, List<Integer> chips) {
        this.players = players;
        this.chips = chips;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Integer> getChips() {
        return chips;
    }
}
