package org.gruppe2.game.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.Event;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.ReplayModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Model;


public class ReplayController extends AbstractController {
    @Model
    private ReplayModel model;

    @Helper
    private GameHelper game;

    @Helper
    private RoundHelper round;

    private boolean waiting = false;

    @Override
    public void update() {
        if (waiting)
            return;

        Object object = readObject(model.getStream());

        if (object == null) {
            getContext().message("quit", "End of recording");
            return;
        }

        if (object instanceof RecordController.Wait) {
            RecordController.Wait wait = (RecordController.Wait) object;

            System.out.println(wait.getTime());

            waiting = true;
            setTask(wait.getTime(), () -> waiting = false);
        } else if (object instanceof GameModel) {
            game.getModel().apply((GameModel) object);
        } else if (object instanceof RoundModel) {
            round.getModel().apply((RoundModel) object);
        } else if (object instanceof Event) {
            checkEvent((Event) object);

            addEvent((Event) object);
        }
    }

    private void checkEvent(Event event) {
        System.out.println(">> " + event.getClass());

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

    private static Object readObject(InputStream stream) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(stream);

            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            return null;
        }
    }
}
