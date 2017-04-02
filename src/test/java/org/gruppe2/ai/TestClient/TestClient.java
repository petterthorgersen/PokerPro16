package org.gruppe2.ai.TestClient;

import org.gruppe2.ai.AI;
import org.gruppe2.ai.AdvancedAI;
import org.gruppe2.ai.Difficulty;
import org.gruppe2.ai.GameInfo;
import org.gruppe2.game.GameBuilder;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.*;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.SessionContext;

import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Callable. Runs a game with the given parameters. AI vs AI.
 * Returns a GameResult object with the results from the games simulated.
 * Created by balto on 11.05.16.
 */
public class TestClient implements Callable<GameResult> {
    private SessionContext context;
    private UUID playerUUID;
    private AI ai;
    private GameResult gameResult = new GameResult();
    private boolean running = true;

    private int numberOfBots;
    private int buyIn;
    private int smallBlind;
    private int bigBlind;
    private Difficulty botDiff;
    private Difficulty testDiff;

    @Helper
    GameHelper gameHelper;

    @Helper
    RoundHelper roundHelper;

    /**
     *
     * @param numberOfBots number of bots
     * @param testDiff your difficulty
     * @param botDiff difficulty of the bots testing against
     * @param buyIn buyin for game/starting chips
     * @param smallBlind small blind
     * @param bigBlind big blind
     */
    public TestClient(int numberOfBots, Difficulty testDiff, Difficulty botDiff, int buyIn, int smallBlind, int bigBlind) {
        this.numberOfBots = numberOfBots;
        this.buyIn = buyIn;
        this.smallBlind = smallBlind;
        this.bigBlind = bigBlind;
        this.botDiff = botDiff;
        this.testDiff = testDiff;
    }

    private void init() {
        ai = new AdvancedAI();

        GameBuilder gameBuilder = new GameBuilder().botPolicy(GameModel.BotPolicy.FILL)
                .playerRange(this.numberOfBots, this.numberOfBots)
                .waitTime(1).botDiff(botDiff)
                .buyIn(this.buyIn)
                .blinds(this.smallBlind, this.bigBlind);

        playerUUID = UUID.randomUUID();

        context = gameBuilder.start();
        context.setAnnotated(this);
        context.waitReady();

        if (context.message("addPlayer", playerUUID, "Testrunner", "testrunner").get()) {
            System.out.println("Testrunner successfully added itself to the game.");
        } else {
            System.out.println("Testrunner failed to add itself to the game.");
        }
    }

    @Override
    public GameResult call() {
        System.out.println("GAME SIMULATION:(might use some time depending on number of players etc.)");

        init();

        while (running) {
            context.getEventQueue().process();
        }
        return this.gameResult;
    }

    @Handler
    public void onAction(PlayerActionQuery query) {
        if (!query.getPlayer().getUUID().equals(playerUUID))
            return; // Query isn't for us :(

        Player player = query.getPlayer();
        RoundPlayer roundPlayer = query.getRoundPlayer();

        //Create GameInfo POJO and fill with the info AI needs
        GameInfo gameInfo = new GameInfo();
        gameInfo.setBigBlind(gameHelper.getBigBlind());
        gameInfo.setCommunityCards(roundHelper.getCommunityCards());
        gameInfo.setPossibleActions(roundHelper.getPlayerOptions(query.getPlayer().getUUID()));
        gameInfo.setActivePlayers(roundHelper.getActivePlayers());
        gameInfo.setHighestBet(roundHelper.getHighestBet());
        gameInfo.setDifficulty(this.testDiff);


        ai.doAction(query.getPlayer(),
                query.getRoundPlayer(),
                gameInfo);
    }

    @Handler
    void onPlayerJoin(PlayerJoinEvent event) {
        System.out.println("AI: "+event.getPlayer().getName() + " has been added");
    }

    @Handler
    void onPlayerLeave(PlayerLeaveEvent event) {
        System.out.println("AI: "+event.getPlayer().getName() + " has left.");
    }

    @Handler
    void onRoundStart(RoundStartEvent event) {
        System.out.println("---------");
        System.out.println("A new round has started. The following players are in the game: ");

        String s = roundHelper.getActivePlayers().size() + "players left: ";
        for (RoundPlayer p : roundHelper.getActivePlayers()) {
            Player p2 = gameHelper.findPlayerByUUID(p.getUUID()).get();
            s += " " + p2.getName() +"(bank: "+p2.getBank()+")";
        }
        System.out.println(s);

    }

    @Handler
    void onRoundEnd(RoundEndEvent event) {
        gameResult.setRoundsPlayed(gameResult.getRoundsPlayed() + 1);
        System.out.println("Round has ended");
    }

    @Handler
    void onPlayerWon(PlayerWonEvent event) {
        for (int i = 0; i < event.getPlayers().size(); i++) {
            if (event.getPlayers().get(i).getUUID().equals(this.playerUUID))
                gameResult.setRoundsWon(gameResult.getRoundsWon() + 1);

            //At last round this will be the winner of the game
            gameResult.setWinnerOfGame(event.getPlayers().get(0));
            System.out.println(event.getPlayers().get(i).getName() + " has won the round");
        }
    }

    @Handler
    void onGameQuit(QuitEvent event) {
        System.out.println("Game is over: " + event.getReason());
        this.running = false;
        //System.exit(0);
    }

    public SessionContext getContext() {
        return context;
    }
}
