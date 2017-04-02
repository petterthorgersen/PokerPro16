package org.gruppe2.game.controller;

import java.util.Optional;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.CommunityCardsEvent;
import org.gruppe2.game.event.PlayerPaysBlind;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.event.PlayerPreActionEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;

public class ClientRoundController extends AbstractController{

    @Helper
    private RoundHelper round;

    @Helper
    private GameHelper game;

    @Handler
    public void onBlinds(PlayerPaysBlind event) {
        handleAction(event.getPlayer(), event.getRoundPlayer(), new Action.Blind(event.getBlindAmount()));
    }

    @Handler
    public void onPrePlayerAction(PlayerPreActionEvent event) {
        updatePlayer(event.getPlayer(), event.getRoundPlayer());
    }

    @Handler
    public void onPostAction(PlayerPostActionEvent event) {
        handleAction(event.getPlayer(), event.getRoundPlayer(), event.getAction());
    }

    @Handler
    public void onCommunityCards(CommunityCardsEvent event) {
        round.getCommunityCards().clear();
        round.getCommunityCards().addAll(event.getCards());
    }

    @Handler
    public void onPlayerWin(PlayerWonEvent event) {
        for (int i = 0; i < event.getPlayers().size(); i++) {
            Optional<Player> op = game.findPlayerByUUID(event.getPlayers().get(i).getUUID());

            if (op.isPresent())
                op.get().setBank(op.get().getBank() + event.getChips().get(i));
        }
    }

    private void handleAction(Player player, RoundPlayer roundPlayer, Action action) {
        int raise;
        if (action instanceof Action.Call) {
            raise = round.getHighestBet() - roundPlayer.getBet();
            moveChips(player, roundPlayer, roundPlayer.getBet() + raise, player.getBank() - raise, raise);
        }

        if (action instanceof Action.AllIn) {
            raise = player.getBank();
            moveChips(player, roundPlayer, roundPlayer.getBet() + raise, 0, raise);
            round.setPlayersWithChipsLeft(round.getPlayersWithChipsLeft() - 1);
        }

        if (action instanceof Action.Fold) {
            round.getActivePlayers().remove(roundPlayer);
            round.setPlayersWithChipsLeft(round.getPlayersWithChipsLeft() - 1);
        }

        if (action instanceof Action.Raise) {
            raise = ((Action.Raise) action).getAmount();
            int chipsToMove = (round.getHighestBet() - roundPlayer.getBet()) + raise;
            moveChips(player, roundPlayer, round.getHighestBet() + raise, player.getBank() - chipsToMove, chipsToMove);
            round.setLastRaiserID(player.getUUID());
            round.playerRaise(player.getUUID());

            if (raise == player.getBank() + roundPlayer.getBet() - round.getHighestBet())
                round.setPlayersWithChipsLeft(round.getPlayersWithChipsLeft() - 1);
        }

        if (action instanceof Action.Blind) {
            int amount = ((Action.Blind) action).getAmount();
            moveChips(player, roundPlayer, amount, player.getBank() - amount, amount);
        }

        if (roundPlayer.getBet() > round.getHighestBet())
            round.setHighestBet(roundPlayer.getBet());
    }

    private void moveChips(Player player, RoundPlayer roundPlayer, int playerSetBet, int playerSetBank, int addToTablePot) {
        roundPlayer.setBet(playerSetBet);
        player.setBank(playerSetBank);
        round.addToPot(addToTablePot);
    }

    private void updatePlayer(Player p, RoundPlayer rp) {
        Optional<Player> op = game.findPlayerByUUID(p.getUUID());
        Optional<RoundPlayer> opr = round.findPlayerByUUID(p.getUUID());

        if (!op.isPresent() || !opr.isPresent())
            return;

        op.get().setBank(p.getBank());
        opr.get().setBet(rp.getBet());
    }


}
