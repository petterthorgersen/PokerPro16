package org.gruppe2.game.controller;

import java.util.UUID;

import org.gruppe2.game.Action;
import org.gruppe2.game.PlayerStatistics;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.model.StatisticsModel;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Message;
import org.gruppe2.game.session.Model;

public class StatisticsController extends AbstractController {
	@Model
	private StatisticsModel model;

	@Message
	public void addPlayerStatistics(UUID playerUUID, PlayerStatistics stats) {
		model.getPlayerStatistics().put(playerUUID, stats);
	}

	@Handler
	public void onAction(PlayerPostActionEvent e) {
		PlayerStatistics stats = model.getPlayerStatistics().get(
				e.getPlayer().getUUID());
		Action action = e.getAction();

		if (action instanceof Action.Fold) {
			stats.getTimesFolded().incrementAndGet();
		} else if (action instanceof Action.Call) {
			stats.getTimesCalled().incrementAndGet();
		} else if (action instanceof Action.Check) {
			stats.getTimesChecked().incrementAndGet();
		} else if (action instanceof Action.Raise) {
			Action.Raise raise = (Action.Raise) action;

			stats.getTotalBets().addAndGet(raise.getAmount());
		}
	}

	@Handler
	public void onPlayerJoin(PlayerJoinEvent e) {
		System.out.println(model);
		System.out.println(e.getPlayer());
		System.out.println(e.getPlayer().getUUID());

		if (!model.getPlayerStatistics().containsKey(e.getPlayer().getUUID())) {
			model.getPlayerStatistics().put(e.getPlayer().getUUID(),
					new PlayerStatistics());
		}
	}

	@Handler
	public void onPlayerLeave(PlayerLeaveEvent e) {
		model.getPlayerStatistics().remove(e.getPlayer().getUUID());
	}
}
