package org.gruppe2.ai;

import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;

public interface AI {
    void doAction(Player player, RoundPlayer roundPlayer, GameInfo gameInfo);


}
