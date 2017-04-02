package org.gruppe2.ui.javafx.ingame;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javafx.application.Platform;

import org.gruppe2.Main;
import org.gruppe2.ai.NewDumbAI;
import org.gruppe2.game.GameBuilder;
import org.gruppe2.game.event.QuitEvent;
import org.gruppe2.game.session.Query;
import org.gruppe2.game.session.SessionContext;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.menu.MainMenu;
/**
 * Creates an instance of a game, with a context and other variables as UUID for player.
 * @author htj063
 *
 */
public class Game {
    private final static Game instance = new Game();

    private UUID playerUUID = UUID.randomUUID();
    private SessionContext context = null;
    private Timer sessionTimer = null;

    private Game() {

    }

    public static UUID getPlayerUUID() {
        return instance.playerUUID;
    }

    public static void setPlayerUUID(UUID uuid) {
        instance.playerUUID = uuid;
    }

    public static SessionContext getContext() {
        return instance.context;
    }

    public static Game getInstance() {
        return instance;
    }

    public void setContext(SessionContext context) {
        quit();

        this.context = context;
        this.context.getEventQueue().registerHandler(QuitEvent.class, event -> quit());

        startTimer();
    }

    public void join() {
        if (Main.getProperty("name").isEmpty()) {
            Main.setProperty("name", NewDumbAI.randomName());
        }

        if (Main.getProperty("avatar").isEmpty()) {
            String[] avatars = UIResources.listAvatars();
            Random random = new Random();

            Main.setProperty("avatar", avatars[random.nextInt(avatars.length)]);
        }

        Game.message("addPlayer", Game.getPlayerUUID(), Main.getProperty("name"), Main.getProperty("avatar"));
        Game.message("addPlayerStatistics", Game.getPlayerUUID(), Main.loadPlayerStatistics());
    }

    public static void autostart() {
        instance.setContext(new GameBuilder().waitTime(100).start());
    }

    public static Query<Boolean> message(String name, Object... args) {
        return getContext().message(name, args);
    }

    public static void setAnnotated(Object object) {
        getContext().setAnnotated(object);
    }

    private static void startTimer() {
    	instance.sessionTimer = new Timer();
        instance.sessionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> instance.context.getEventQueue().process());
            }
        }, 0, 50);
    }

    private static void quit() {
        if (instance.context == null)
            return;

        instance.context.quit();

        if (instance.sessionTimer != null) {
            instance.sessionTimer.cancel();
            instance.sessionTimer = null;
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ignored) {
        }

        // Process the queue one last time
        instance.context.getEventQueue().process();
        instance.context = null;

        SceneController.setScene(new MainMenu());
    }
}
