package org.gruppe2.game.controller;

import org.gruppe2.ai.AI;
import org.gruppe2.ai.AdvancedAI;
import org.gruppe2.ai.Difficulty;
import org.gruppe2.ai.GameInfo;
import org.gruppe2.ai.NewDumbAI;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;

public class AIController extends AbstractController {
	private AI ai;
	private Difficulty difficulty;

	@Helper
	private GameHelper gameHelper;

	@Helper
	private RoundHelper roundHelper;

	@Override
	public void init() {
		difficulty = gameHelper.getBotDiff();

		switch (difficulty) {
			case RANDOM :
				ai = new NewDumbAI();
			default:
				ai = new AdvancedAI();
				break;
		}
	}

	@Handler
	public void onAction(PlayerActionQuery query) {
		if (!query.getPlayer().isBot())
			return;

        //Create GameInfo POJO and fill with the info AI needs
        GameInfo gameInfo = new GameInfo();
        gameInfo.setBigBlind(gameHelper.getBigBlind());
        gameInfo.setCommunityCards(roundHelper.getCommunityCards());
        gameInfo.setPossibleActions(roundHelper.getPlayerOptions(query.getPlayer().getUUID()));
        gameInfo.setActivePlayers(roundHelper.getActivePlayers());
        gameInfo.setHighestBet(roundHelper.getHighestBet());
		gameInfo.setRoundNumber(roundHelper.getRoundNum());
		gameInfo.setDifficulty(this.difficulty);

		setTask(gameHelper.getWaitTime(), () -> {
			ai.doAction(query.getPlayer(),
                    query.getRoundPlayer(),
					gameInfo);
		});
	}
}
