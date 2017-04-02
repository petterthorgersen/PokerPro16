package org.gruppe2.game.controller;

import java.io.IOException;
import java.util.UUID;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.Event;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.event.QuitEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.NetworkClientModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Message;
import org.gruppe2.game.session.Model;
import org.gruppe2.game.session.Query;

public class NetworkClientController extends AbstractController {

    @Model
    private NetworkClientModel model;

    @Helper
    private GameHelper game;
    @Helper
    private RoundHelper round;

    Query<Action> actionQuery;

    //	player.getAction().isDone()
    @Override
    public void update() {
        try {
            Object object;

            while ((object = model.getConnection().readObject()) != null) {
                if (object instanceof Event) {
                    checkEvent((Event) object);
                    addEvent((Event) object);
                } else if (object instanceof RoundModel) {
                    round.getModel().apply((RoundModel) object);
                } else if (object instanceof GameModel) {
                    game.getModel().apply((GameModel) object);
                }
            }

            checkForAction();

        } catch (IOException | ClassNotFoundException e) {
            getContext().quit();
        }
    }

    private void checkEvent(Event event) {
        if (event instanceof PlayerJoinEvent) {
            onPlayerJoin((PlayerJoinEvent) event);
        } else if (event instanceof PlayerLeaveEvent) {
            onPlayerLeave((PlayerLeaveEvent) event);
        } else if (event instanceof PlayerPostActionEvent) {
            onPostAction((PlayerPostActionEvent) event);
        }
    }

    private void onPlayerJoin(PlayerJoinEvent e) {
        game.getPlayers().add(e.getPlayer());
    }

    private void onPlayerLeave(PlayerLeaveEvent e) {
        game.getPlayers().remove(e.getPlayer());
    }

    private void onPostAction(PlayerPostActionEvent event) {
        Player player = game.findPlayerByUUID(event.getPlayer().getUUID()).get();
        player.setBank(event.getPlayer().getBank());

        RoundPlayer roundPlayer = round.findPlayerByUUID(event.getRoundPlayer().getUUID()).get();
        roundPlayer.setBet(event.getRoundPlayer().getBet());

        round.setHighestBet(Math.max(round.getHighestBet(), roundPlayer.getBet()));
    }

    private void checkForAction() {

        if (actionQuery != null && actionQuery.isDone()) {
            Action action = actionQuery.get();

            
            sendMessage(String.format("ACTION;%s\r\n", action.toNetworkString()));
            actionQuery = null;
        }
    }

    @Handler
    public void setActionQuery(PlayerActionQuery query) {
        actionQuery = query.getPlayer().getAction();
    }

    @Message
    public void addPlayer(UUID uuid, String name, String avatar) {
        try {
            model.getConnection().sendMessage(
                    "JOIN;" + uuid + ";" + avatar + ":" + name + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Message
    public void chat(String message, UUID playerUUID) {
        sendMessage(String.format("SAY:%s\r\n", message));
    }

    @Message
    public void kickPlayer(UUID playerUUID) {
        sendMessage(String.format("KICK;%s\r\n", playerUUID));
    }

    @Handler
    public void onQuit(QuitEvent quitEvent) {
        sendMessage("DISCONNECT\r\n");
    }

    private void sendMessage(String message) {
        try {
            model.getConnection().sendMessage(message);
        } catch (IOException e) {
            e.printStackTrace();
            getContext().quit();
        }
    }
}
