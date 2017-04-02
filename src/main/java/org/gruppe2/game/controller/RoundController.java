package org.gruppe2.game.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Stack;
import java.util.UUID;

import org.gruppe2.game.Action;
import org.gruppe2.game.Card;
import org.gruppe2.game.Player;
import org.gruppe2.game.PokerLog;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.SidePot;
import org.gruppe2.game.calculation.Showdown;
import org.gruppe2.game.event.CommunityCardsEvent;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPaysBlind;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.event.PlayerPreActionEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.event.RoundEndEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Message;
import org.gruppe2.game.session.TimerTask;

public class RoundController extends AbstractController {
    @Helper
    private RoundHelper round;
    @Helper
    private GameHelper game;

    private final Showdown showdown = new Showdown();
    private LocalDateTime timeToStart = null;
    private Player player = null;
    private RoundPlayer roundPlayer = null;
    private UUID lastPlayerInRound = null;
    private Stack<Card> deck = new Stack<>();
    private PokerLog logger = null;
    private ArrayList<UUID> raiseStory = new ArrayList<>();
    private TimerTask autoFold = null;
    private boolean endRound = false;
    private boolean waitForNewRound = false;

    @Override
    public void update() {
        if (round.isPlaying() && timeToStart != null) {
            if (LocalDateTime.now().isAfter(timeToStart)) {
                timeToStart = null;
                resetRound();

                if (!round.isPlaying())
                    return;

                payBlinds();
                addEvent(new RoundStartEvent());
            } else {
                return;
            }
        }

        if (round.isPlaying() && !waitForNewRound) {
            if (round.getActivePlayers().size() == 1) {
                roundEnd();
                return;
            }

            // Go to next player and do actions
            if (player == null) {
                Optional<Player> op = game.findPlayerByUUID(round.getCurrentUUID());
                Optional<RoundPlayer> opr = round.findPlayerByUUID(round.getCurrentUUID());

                if (!op.isPresent() || !opr.isPresent())
                    return;

                player = op.get();
                roundPlayer = opr.get();

                addEvent(new PlayerPreActionEvent(player, roundPlayer));

                if (player.getBank() > 0) {
                    addEvent(new PlayerActionQuery(player, roundPlayer));
                    autoFold = setTask(30000, () -> player.getAction().set(new Action.Fold()));
                } else if (player.getBank() == 0)
                    player.getAction().set(new Action.Pass());
                else
                    throw new IllegalStateException("Player: " + player.getName() + " has less than 0 chips");
            }

            if (player.getAction().isDone()) {
                if (autoFold != null) {
                    cancelTask(autoFold);
                    autoFold = null;
                }

                if (!(player.getAction().get() instanceof Action.Pass))
                    handleAction(player, roundPlayer, player.getAction().get());

                if (endRound || !(player.getAction().get() instanceof Action.Raise) &&
                        ((round.getLastRaiserID() == null && player.getUUID().equals(lastPlayerInRound)) || player.getUUID().equals(round.getLastRaiserID()))) {
                    player.getAction().reset();
                    player = null;
                    roundPlayer = null;
                    nextRound();
                } else {
                    round.setCurrent((round.getCurrent() + 1) % round.getActivePlayers().size());
                    endRound = round.getPlayersWithChipsLeft() <= 1;
                    player.getAction().reset();
                    player = null;
                    roundPlayer = null;
                }
            }
        }
    }

    @Message
    public boolean roundStart() {
        if (!round.isPlaying()) {
            round.setPlaying(true);
            logger = new PokerLog();
            timeToStart = LocalDateTime.now().plusSeconds(3);
            return true;
        }

        return false;
    }

    @Handler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        Optional<RoundPlayer> opr = round.findPlayerByUUID(event.getPlayer().getUUID());

        if (!opr.isPresent())
            return;

        RoundPlayer player = opr.get();

        if (player == null)
            return;

        if (player.getUUID().equals(this.player.getUUID())) {
            this.player = null;
            roundPlayer = null;
        }

        if (raiseStory.removeIf(uuid -> player.getUUID().equals(uuid))) {
            if (raiseStory.size() > 0)
                round.setLastRaiserID(raiseStory.get(raiseStory.size()-1));
            else
                round.setLastRaiserID(null);
        }

        if (round.getActivePlayers().indexOf(player) <= round.getCurrent()) {
            round.setCurrent(round.getCurrent()-1);
        }

        round.getActivePlayers().remove(player);
        game.getPlayers().remove(event.getPlayer());
    }

    private void resetRound() {
        List<Player> sortedPlayers = new ArrayList<>(game.getPlayers());
        sortedPlayers.sort((p1, p2) -> Integer.compare(p1.getTablePosition(), p2.getTablePosition()));

        List<RoundPlayer> active = round.getActivePlayers();
        active.clear();
        resetDeck();
        logger = new PokerLog();

        boolean done = false;
        for (int i = game.getButton(); !done; i++) {
            int j = (i + 1) % sortedPlayers.size();
            Player p = sortedPlayers.get(j);
            if (p.getBank() > 0)
                active.add(new RoundPlayer(p.getUUID(), deck.pop(), deck.pop()));
            else addEvent(new PlayerLeaveEvent(p));

            done = j == game.getButton();
        }

        if (active.size() <= 1) {
            getContext().message("quit", "Not enough players");
            round.setPlaying(false);
            return;
        }

        round.addPlayersToMap(active);

        round.setPot(0);
        round.setHighestBet(0);
        round.setCurrent(0);
        lastPlayerInRound = round.getLastActivePlayerID();
        round.resetRound();
        round.getCommunityCards().clear();
        round.setLastRaiserID(null);
        round.setPlayersWithChipsLeft(active.size());
    }

    private void payBlinds() {
        int currentBigBlind = game.getBigBlind() + ((int) (game.getRoundsCompleted() * game.getBigBlind() * 0.1));
        int currentSmallBlind = game.getSmallBlind() + ((int) (game.getRoundsCompleted() * game.getSmallBlind() * 0.1));

        RoundPlayer roundPlayer = round.getActivePlayers().get(1);
        Optional<Player> op = game.findPlayerByUUID(roundPlayer.getUUID());

        if (!op.isPresent())
            throw new NoSuchElementException("Player with id: " + roundPlayer.getUUID() + " not found in the game player list");

        Player player = op.get();

        if (player.getBank() < currentBigBlind) {
            currentBigBlind = player.getBank();
            currentSmallBlind = currentBigBlind / 2;
            if (currentSmallBlind <= 0)
                currentSmallBlind = 1;
        }
        handleAction(player, roundPlayer, new Action.Blind(currentBigBlind));
        addEvent(new PlayerPaysBlind(player, roundPlayer, currentBigBlind));

        roundPlayer = round.getActivePlayers().get(0);
        op = game.findPlayerByUUID(roundPlayer.getUUID());

        if (!op.isPresent())
            throw new NoSuchElementException("Player with id: " + roundPlayer.getUUID() + " not found in the game player list");

        player = op.get();

        if (player.getBank() < currentSmallBlind)
            currentSmallBlind = player.getBank();

        handleAction(player, roundPlayer, new Action.Blind(currentSmallBlind));
        addEvent(new PlayerPaysBlind(player, roundPlayer, currentSmallBlind));
    }

    private void handleAction(Player player, RoundPlayer roundPlayer, Action action) {
        if (!legalAction(player, roundPlayer, action)) {
            System.err.println(player.getName() + " can't do action: " + action);
            player.getAction().set(new Action.Fold());
        }

        logger.recordPlayerAction(player, roundPlayer, action);

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
            round.getActivePlayers().remove(round.getCurrent());
            round.setCurrent(round.getCurrent() - 1);
            round.setPlayersWithChipsLeft(round.getPlayersWithChipsLeft() - 1);
        }

        if (action instanceof Action.Raise) {
            raise = ((Action.Raise) action).getAmount();
            int chipsToMove = (round.getHighestBet() - roundPlayer.getBet()) + raise;
            moveChips(player, roundPlayer, round.getHighestBet() + raise, player.getBank() - chipsToMove, chipsToMove);
            round.setLastRaiserID(player.getUUID());
            round.playerRaise(player.getUUID());
            raiseStory.add(player.getUUID());

            if (raise == player.getBank() + roundPlayer.getBet() - round.getHighestBet())
                round.setPlayersWithChipsLeft(round.getPlayersWithChipsLeft() - 1);
        }

        if (action instanceof Action.Blind) {
            int amount = ((Action.Blind) action).getAmount();
            moveChips(player, roundPlayer, amount, player.getBank() - amount, amount);
        }

        if (roundPlayer.getBet() > round.getHighestBet())
            round.setHighestBet(roundPlayer.getBet());

        if (!(action instanceof Action.Blind))
            addEvent(new PlayerPostActionEvent(player, roundPlayer, action));
    }

    private void moveChips(Player player, RoundPlayer roundPlayer, int playerSetBet, int playerSetBank, int addToTablePot) {
        roundPlayer.setBet(playerSetBet);
        player.setBank(playerSetBank);
        round.addToPot(addToTablePot);
    }

    private boolean legalAction(Player player, RoundPlayer roundPlayer, Action action) {
        if (!round.getActivePlayers().contains(roundPlayer))
            return false;

        PossibleActions pa = round.getPlayerOptions(player.getUUID());
        if (action instanceof Action.Check)
            return pa.canCheck();
        else if (action instanceof Action.Raise) {
            int raise = ((Action.Raise) action).getAmount();
            if (raise < 1 || raise > player.getBank() + roundPlayer.getBet() - round.getHighestBet()) {
                System.err.println(player.getName() + " cant raise with " + ((Action.Raise) action).getAmount());
                player.getAction().set(new Action.Fold());
            }
            return pa.canRaise();
        } else if (action instanceof Action.Call)
            return pa.canCall();
        else if (action instanceof Action.Fold || action instanceof Action.Blind || action instanceof Action.AllIn || action instanceof Action.Pass)
            return true;
        else
            throw new IllegalArgumentException("Not an action");
    }

    private void nextRound() {
        if (round.getRoundNum() == 3) {
            roundEnd();
        } else {
            round.nextRound();
            round.setLastRaiserID(null);
            lastPlayerInRound = round.getLastActivePlayerID();
            round.resetRaiseMap();
            round.setCurrent(0);
            endRound = false;

            if (round.getRoundNum() == 1) {
                for (int i = 0; i < 3; i++)
                    round.getCommunityCards().add(deck.pop());
            } else if (round.getRoundNum() == 2 || round.getRoundNum() == 3)
                round.getCommunityCards().add(deck.pop());

            addEvent(new CommunityCardsEvent(new ArrayList<>(round.getCommunityCards())));

            logger.incrementRound(round.getCommunityCards());

            if (round.getCommunityCards().size() > 0) {
                waitForNewRound = true;
                setTask(500, () -> waitForNewRound = false);
            }
        }
    }

    private void roundEnd() {
        round.setPlaying(false);
        addEvent(new RoundEndEvent());

        if (round.getActivePlayers().size() == 1) {
            Optional<Player> op = game.findPlayerByUUID(round.getActivePlayers().get(0).getUUID());

            if (!op.isPresent())
                throw new NoSuchElementException("Can't find winning player");

            Player winner = op.get();
            winner.setBank(winner.getBank() + round.getPot());
            addEvent(new PlayerWonEvent(new ArrayList<Player>(Collections.singletonList(winner)), new ArrayList<Integer>(Collections.singletonList(round.getPot()))));
            round.setPot(0);
        }
        else {
            List<SidePot> sidePots = round.calculateSidePots();
            SidePot highestBetPot = sidePots.get(sidePots.size()-1);

            //Give back chips to highest bidder if he is the only one with the bet
            if (sidePots.size() > 1 && highestBetPot.getPlayers().size() == 1) {
                Optional<Player> op = game.findPlayerByUUID(highestBetPot.getPlayers().get(0));

                if (op.isPresent()) {
                    Player p = op.get();
                    p.setBank(p.getBank() + highestBetPot.getPot());
                }

                sidePots.remove(highestBetPot);
            }
          
            List<RoundPlayer> winners = showdown.getWinnersOfRound(round.getActivePlayers(), round.getCommunityCards());
            List<List<UUID>> potWinners = new ArrayList<>();
            Map<UUID, Integer> winnerTotals = new HashMap<>();

            int potsDone = 0;

            while (potsDone < sidePots.size()) {
                for (int i = potsDone; i < sidePots.size(); i++) {
                    List<UUID> canWin = new ArrayList<>();
                    for (RoundPlayer rp : winners) {
                        if (sidePots.get(i).getPlayers().contains(rp.getUUID())) {
                            canWin.add(rp.getUUID());
                        }
                    }

                    if (canWin.size() > 0) {
                        potsDone++;
                        potWinners.add(canWin);
                    }
                    else break;
                }

                if (potsDone < sidePots.size()) {
                    List<RoundPlayer> players = new ArrayList<>();
                    for (UUID id : sidePots.get(potsDone).getPlayers()) {
                        players.add(round.findPlayerByUUID(id).get());
                    }
                    winners = showdown.getWinnersOfRound(players, round.getCommunityCards());
                }
            }

            if (potWinners.size() != sidePots.size())
                throw new IllegalStateException("Not all sidepots have winners. Tell Mikal to fix");

            for (int i = 0; i < potWinners.size(); i++) {
                for (UUID id : potWinners.get(i)) {
                    if (!winnerTotals.keySet().contains(id))
                        winnerTotals.put(id, 0);
                    winnerTotals.put(id, winnerTotals.get(id) + sidePots.get(i).getPot() / potWinners.get(i).size());
                }
            }


            List<Player> allWinners = new ArrayList<>();
            List<Integer> chips = new ArrayList<>();
            round.setPot(0);
            for (UUID id : winnerTotals.keySet()) {
                Optional<Player> op = game.findPlayerByUUID(id);

                if (!op.isPresent())
                    throw new NoSuchElementException("Can't find winning player");

                Player winner = op.get();
                allWinners.add(winner);
                chips.add(winnerTotals.get(id));
                winner.setBank(winner.getBank() + winnerTotals.get(id));
            }

            addEvent(new PlayerWonEvent(allWinners, chips));
        }

        logger.writeToFile();
        game.setButton((game.getButton() + 1) % game.getPlayers().size());
        game.setRoundsCompleted(game.getRoundsCompleted() + 1);

        roundStart();
        timeToStart = LocalDateTime.now().plusSeconds(10);
    }

    private void resetDeck() {
        deck.clear();

        for (Card.Suit suit : Card.Suit.values()) {
            for (int face = 2; face <= 14; face++) {
                deck.push(new Card(face, suit));
            }
        }

        Collections.shuffle(deck);
    }
}