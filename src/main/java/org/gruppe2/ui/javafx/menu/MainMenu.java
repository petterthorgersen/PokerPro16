package org.gruppe2.ui.javafx.menu;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.gruppe2.Main;
import org.gruppe2.ai.Difficulty;
import org.gruppe2.game.GameBuilder;
import org.gruppe2.game.session.ClientSession;
import org.gruppe2.game.session.Session;
import org.gruppe2.game.session.SessionContext;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.ingame.Game;
import org.gruppe2.ui.javafx.ingame.GameScene;

/**
 *
 */
public class MainMenu extends BorderPane {
    @FXML
    private ImageView logo;
    @FXML
    private Button online;
    @FXML
    private Button singlePlayer;
    @FXML
    private Button viewStatistics;
    @FXML
    private Button settings;
    @FXML
    private Button replay;
    @FXML
    private VBox vBox;

    public MainMenu() {
        UIResources.loadFXML(this);
        logo.fitWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.8));
        vBox.maxWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.3));

        setButtonSize();
        fadeIn();

        if (Main.isFirstStart()) {
            Main.setFirstStart(false);
            Settings.show(false);
        }
    }

    public void goToSinglePlayer() throws InterruptedException {
        CreateGameSettings.show(list -> {

            Modal.messageBox("Loading", "Game is starting...", false);
            Platform.runLater(() -> {

                SessionContext context = new GameBuilder()
                        .blinds(Integer.valueOf(list.get(1)), Integer.valueOf(list.get(2)))
                        .buyIn(Integer.valueOf(list.get(3)))
                        .playerRange(Integer.valueOf(list.get(5)), Integer.valueOf(list.get(4)))
                        .botDiff(Difficulty.valueOf(list.get(6).toUpperCase()))
                        .start();

                context.waitReady();
                Game.getInstance().setContext(context);
                Game.getInstance().join();
                SceneController.setOnlyThisScene(new GameScene());
            });
        });
    }

    public void goToTestServer() {
        Game.autostart();
        Game.getContext().waitReady();
        Game.message("listen");
        SceneController.setScene(new GameScene());
    }

    public void goToTestClient() {
        Game.getInstance().setContext(Session.start(ClientSession.class, "localhost"));
        Game.getContext().waitReady();
        SceneController.setScene(new GameScene());
    }

    public void createNetWorkTable() {
        SceneController.setScene(new GameScene());
    }

    public void goToLobby() {
        Lobby.show();
    }

    public void goToStatistics() {
        PersonalStatistics.show();
    }

    public void goToSettings() {
        Settings.show(true);
    }

    public void goToReplay() {
        Replay.show();
    }

    private void fadeIn() {
        FadeTransition fadeTransition = new FadeTransition(
                Duration.millis(800), vBox);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        fadeTransition.play();
    }

    private void setButtonSize() {
        online.setMaxWidth(Double.MAX_VALUE);
        singlePlayer.setMaxWidth(Double.MAX_VALUE);
        viewStatistics.setMaxWidth(Double.MAX_VALUE);
        settings.setMaxWidth(Double.MAX_VALUE);
        replay.setMaxWidth(Double.MAX_VALUE);
    }
}
