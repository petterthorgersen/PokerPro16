package org.gruppe2.game.event;

import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;

/**
 * Created by Mikal on 28.04.2016.
 */
public class PlayerPaysBlind implements Event {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1030928698715497092L;
	private final Player player;
    private final RoundPlayer roundPlayer;
    private final int blindAmount;

    public PlayerPaysBlind(Player player, RoundPlayer roundPlayer, int blindAmount) {
        this.player = player;
        this.roundPlayer = roundPlayer;
        this.blindAmount = blindAmount;
    }

    public Player getPlayer() {
        return player;
    }

    public RoundPlayer getRoundPlayer() {
        return roundPlayer;
    }

    public int getBlindAmount() {
        return blindAmount;
    }
}
