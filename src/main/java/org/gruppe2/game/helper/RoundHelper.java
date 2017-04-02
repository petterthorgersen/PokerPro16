package org.gruppe2.game.helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

import org.gruppe2.game.Card;
import org.gruppe2.game.Player;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.SidePot;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.session.SessionContext;

public class RoundHelper {
    private RoundModel model;
    private GameHelper helper;

    public RoundHelper(SessionContext context) {
        model = context.getModel(RoundModel.class);
        helper = new GameHelper(context);
    }

    public boolean isPlaying() {
        return model.isPlaying();
    }

    public void setPlaying(boolean playing) {
        model.setPlaying(playing);
    }

    public RoundModel getModel() {
        return model;
    }

    public int getCurrent() {
        return model.getCurrent();
    }

    public List<RoundPlayer> getActivePlayers() {
        return model.getActivePlayers();
    }

    public void setCurrent(int current) {
        model.setCurrent(current);
    }

    public UUID getCurrentUUID() {
        return getActivePlayers().get(getCurrent()).getUUID();
    }

    public RoundPlayer getCurrentPlayer() {
        return getActivePlayers().get(getCurrent());
    }

    public void setPot(int pot) {
        model.setPot(pot);
    }

    public void setHighestBet(int highestBet) {
        model.setHighestBet(highestBet);
    }

    public Optional<RoundPlayer> findPlayerByUUID(UUID uuid) {
        return findPlayer(p -> p.getUUID().equals(uuid));
    }

    public Optional<RoundPlayer> findPlayer(Predicate<RoundPlayer> predicate) {
        return model.getActivePlayers().stream()
                .filter(predicate)
                .findFirst();
    }

    public int getHighestBet() {
        return model.getHighestBet();
    }

    public void addToPot(int addToTablePot) {
        model.setPot(model.getPot() + addToTablePot);
    }

    public UUID getLastActivePlayerID() {
        return model.getActivePlayers().get(model.getNumberOfActivePlayers()-1).getUUID();
    }

    public void nextRound() {
        model.setRoundNumber(model.getRoundNumber()+1);
    }

    public void resetRound() {
        model.setRoundNumber(0);
    }

    public int getRoundNum() {
        return model.getRoundNumber();
    }

    public PossibleActions getPlayerOptions (UUID id) {
        PossibleActions options = new PossibleActions();
        Optional<Player> player = helper.findPlayerByUUID(id);
        Optional<RoundPlayer> roundPlayer = findPlayerByUUID(id);
        if(!player.isPresent() || !roundPlayer.isPresent()){
        	System.out.println("options did not work");
        	return options;
        }

        if (roundPlayer.get().getBet() == getHighestBet())
            options.setCheck();

        if (player.get().getBank() >= getHighestBet() - roundPlayer.get().getBet())
            if (getHighestBet() - roundPlayer.get().getBet() != 0)
                options.setCall(getHighestBet() - roundPlayer.get().getBet());

        if (!player.get().getUUID().equals(getLastRaiserID()) && model.getRaiseMap().get(id) < 3 && model.getPlayersWithChipsLeft() > 1) {
            int maxRaise = player.get().getBank() + roundPlayer.get().getBet() - getHighestBet();
            if (maxRaise > 0)
                options.setRaise(1, maxRaise);
        }

        if (!options.canCall() && !options.canCheck() && !options.canRaise())
            options.setAllIn();

        return options;
    }

    public void setLastRaiserID(UUID lastRaiserID) {
        model.setLastRaiserID(lastRaiserID);
    }

    public UUID getLastRaiserID() {
        return model.getLastRaiserID();
    }

    public List<Card> getCommunityCards() {
        return model.getCommunityCards();
    }

    public int getPot() {
        return model.getPot();
    }

    public Map<UUID, Integer> getRaiseMap() {
        return model.getRaiseMap();
    }

    public void addPlayersToMap(List<RoundPlayer> players) {
        Map<UUID, Integer> map = model.getRaiseMap();
        map.clear();
        for (RoundPlayer p : players) {
            map.put(p.getUUID(), 0);
        }
    }

    public void playerRaise(UUID id) {
        model.getRaiseMap().put(id, model.getRaiseMap().get(id) + 1);
    }

    public void resetRaiseMap() {
        Map<UUID, Integer> map = model.getRaiseMap();
        for (UUID id : map.keySet()){
            map.put(id, 0);
        }
    }

    public List<SidePot> calculateSidePots() {
        List<SidePot> pots = new ArrayList<>();
        List<RoundPlayer> active = model.getActivePlayers();

        ArrayList<Integer> betList = new ArrayList<>();
        boolean done = false;
        int previous = 0;
        while (!done){
            int lowestBet = Integer.MAX_VALUE;
            for (RoundPlayer p : active) {
                if (p.getBet() < lowestBet && p.getBet() > previous)
                    lowestBet = p.getBet();
            }

            betList.add(lowestBet);
            previous = lowestBet;

            done = lowestBet == model.getHighestBet();
        }

        for (int i = 0; i < betList.size(); i++) {
            int bet = betList.get(i);
            int pot;

            List<UUID> players = new ArrayList<>();

            for (RoundPlayer p : active) {
                if (p.getBet() >=  bet)
                    players.add(p.getUUID());
            }

            if (i > 0)
                bet -= betList.get(i-1);
            pot = bet * players.size();

            pots.add(new SidePot(players, pot));
        }

        int total = 0;
        for (SidePot sp : pots)
            total += sp.getPot();

        if (total < model.getPot())
            pots.get(0).setPot(pots.get(0).getPot() + model.getPot() - total);

        return pots;
    }

    public void setPlayersWithChipsLeft(int num) {
        model.setPlayersWithChipsLeft(num);
    }

    public int getPlayersWithChipsLeft() {
        return model.getPlayersWithChipsLeft();
    }
}
