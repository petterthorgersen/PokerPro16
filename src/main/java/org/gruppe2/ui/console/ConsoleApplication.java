package org.gruppe2.ui.console;

import java.util.Scanner;
import java.util.UUID;

import org.gruppe2.Main;
import org.gruppe2.game.Action;
import org.gruppe2.game.Card;
import org.gruppe2.game.GameBuilder;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.CommunityCardsEvent;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPaysBlind;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.event.QuitEvent;
import org.gruppe2.game.event.RoundEndEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.SessionContext;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class ConsoleApplication implements Runnable {
    private SessionContext context;
    private UUID playerUUID;

    @Helper
    GameHelper gameHelper;

    @Helper
    RoundHelper roundHelper;

    private void init() {
        GameBuilder gameBuilder = new GameBuilder().botPolicy(GameModel.BotPolicy.FILL).playerRange(2, 8).waitTime(5);

        System.out.println("Welcome to PokerPro16 Console Edition");

        playerUUID = UUID.randomUUID();

        context = gameBuilder.start();
        context.setAnnotated(this);
        context.waitReady();

        if (context.message("addPlayer", playerUUID, "Zohar", "zohar").get()) {
            System.out.println("Successfully added the player");
        } else {
            System.out.println("Unsuccessfully");
        }
    }

    @Override
    public void run() {
        init();

        while (true) {
            context.getEventQueue().process();

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Handler
    public void onAction(PlayerActionQuery query) {
        if (!query.getPlayer().getUUID().equals(playerUUID))
            return; // Query isn't for us :(

        Player player = query.getPlayer();
        RoundPlayer roundPlayer = query.getRoundPlayer();

        System.out.println("Highest bet: " + roundHelper.getHighestBet());
        System.out.println("Table pot: " + roundHelper.getPot());
        System.out.printf("Your cards: %s %s \n", roundPlayer.getCards()[0].toPrettyString(), roundPlayer.getCards()[1].toPrettyString());
        System.out.printf("Your chips: %d \n", player.getBank());
        System.out.printf("Current bet: %d \n", roundPlayer.getBet());
        System.out.println("> Your turn, you can: ");
        System.out.println(roundHelper.getPlayerOptions(player.getUUID()));

        System.out.println("> ");

        Scanner in = new Scanner(System.in);
        Scanner ls = new Scanner(in.nextLine());
        String cmd = ls.next().toLowerCase();

        switch (cmd) {
            case "fold":
                query.getPlayer().getAction().set(new Action.Fold());
                break;

            case "check":
                query.getPlayer().getAction().set(new Action.Check());
                break;

            case "call":
                query.getPlayer().getAction().set(new Action.Call());
                break;

            case "raise":
                query.getPlayer().getAction().set(new Action.Raise(ls.nextInt()));
                break;

            case "allin":
                query.getPlayer().getAction().set(new Action.AllIn());
                break;

            default:
                System.out.println("Unknown command");
                break;
        }
        in.close();
        ls.close();
    }

    @Handler
    public void onPostAction(PlayerPostActionEvent event) {
        Player player = event.getPlayer();
        RoundPlayer roundPlayer = event.getRoundPlayer();
        Action action = event.getAction();

        System.out.printf("  %s (%d : %d) ", player.getName(), player.getBank(), roundPlayer.getBet());

        if (action instanceof Action.Fold) {
            System.out.println("folded");
        } else if (action instanceof Action.Call) {
            System.out.println("called");
        } else if (action instanceof Action.Check) {
            System.out.println("checked");
        } else if (action instanceof Action.Raise) {
            System.out.println("raised by " + ((Action.Raise) action).getAmount());
        } else if (action instanceof Action.Pass) {
            System.out.println("passed");
        } else {
            System.out.println("!!! " + action);
        }
    }

    @Handler
    void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println(event.getPlayer().getName() + " has connected");
    }

    @Handler
    void onPlayerLeave(PlayerLeaveEvent event) {
        System.out.println(event.getPlayer().getName() + " has disconnected");
    }

    @Handler
    void onRoundStart(RoundStartEvent event) {
        System.out.println("A new round has started");
    }

    @Handler
    void onRoundEnd(RoundEndEvent event) {
        System.out.println("Rounds ended");
    }

    @Handler
    void onCommunialCards(CommunityCardsEvent event) {
        String s = "Active players left: ";
        for (RoundPlayer p : roundHelper.getActivePlayers()) {
            Player p2 = gameHelper.findPlayerByUUID(p.getUUID()).get();
            s += " " + p2.getName();
        }
        System.out.println(s);

        String cards = "Community cards: ";
        for (Card c : event.getCards())
            cards += " " + c.toPrettyString();
        System.out.println(cards);
    }

    @Handler
    void onPlayerWon(PlayerWonEvent event) {
        for (int i = 0; i < event.getPlayers().size(); i ++)
            System.out.println(event.getPlayers().get(i).getName() + " has won " + event.getChips().get(i) + " chips!");
    }

    @Handler
    void onGameQuit(QuitEvent event) {
        System.exit(0);
    }

    @Handler
    void onBlind(PlayerPaysBlind event) {
        System.out.println(event.getPlayer().getName() + " payed " + event.getBlindAmount());
    }

    public SessionContext getContext() {
        return context;
    }
}
