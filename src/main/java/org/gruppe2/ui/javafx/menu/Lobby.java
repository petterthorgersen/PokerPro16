package org.gruppe2.ui.javafx.menu;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;

import org.gruppe2.game.session.SessionContext;
import org.gruppe2.network.MasterClient;
import org.gruppe2.network.TableEntry;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.ingame.Game;
import org.gruppe2.ui.javafx.ingame.GameScene;

/**
 * Created by Petter on 11/04/2016.
 */

public class Lobby extends BorderPane {
    MasterClient masterClient;
    @FXML
    private TextField search;
    @FXML
    private Button refresh;
    @FXML
    private CheckBox checkBoxFriends;
    @FXML
    private TilePane lobbyTiles;
    @FXML
    private BorderPane lobby = this;
    @FXML
    private HBox searchBar;
    @FXML
    private ImageView createGame;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private Pane tile;

    private Timer timer = new Timer();

    private static Modal modal = null;

    public Lobby() {
        UIResources.loadFXML(this);
        setSize();

        try {
            masterClient = new MasterClient();
            masterClient.setOnError(this::quit);
            masterClient.setOnJoinGame(this::joinGame);
            masterClient.setOnRefresh(this::updateTables);

            Lobby _this = this;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Platform.runLater(_this::update);
                }
            }, 50, 50);
        } catch (IOException e) {
            Modal.messageBox("Lobby", "Couldn't find any servers", true);
            quit();
        }
    }

    private void quit() {
        timer.cancel();
        modal.close();
        modal = null;
    }

    private void joinGame(SessionContext context) {
        quit();
        Game.getInstance().setContext(context);
        SceneController.setOnlyThisScene(new GameScene());
    }

    private void update() {
        try {
            masterClient.update();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void refresh() {
        while (lobbyTiles.getChildren().size() > 1)
            lobbyTiles.getChildren().remove(1);

        try {
            masterClient.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void friendBox() {
    	
    }

    @FXML
    private void requestCreateGame() {
        CreateGameSettings.show(masterClient::requestCreateGame);
    }

    public void requestJoinGame(UUID uuid) {
        try {
            masterClient.requestJoinTable(uuid);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setSize() {
        searchBar.spacingProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.03));
        lobby.maxWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.7));
        lobby.maxHeightProperty().bind(
                PokerApplication.getRoot().heightProperty().multiply(0.7));
        scrollPane.maxWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.7));
        scrollPane.maxHeightProperty().bind(
                PokerApplication.getRoot().heightProperty().multiply(0.7));
        search.prefWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.3));
        refresh.prefWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.1));
        lobbyTiles.hgapProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.02));
        lobbyTiles.vgapProperty().bind(
                PokerApplication.getRoot().heightProperty().multiply(0.02));
        createGame.fitWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.13));
        createGame.preserveRatioProperty().setValue(true);
    }

    public void keyListener(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER)
            refresh();
    }


    public void updateTables(List<TableEntry> tablesInLobby) {
        if (tablesInLobby.size() == 0)
            return;

        if (checkBoxFriends.selectedProperty().get()) {
            // check for tables with friends on
        }

        for (TableEntry table : tablesInLobby) {

            String players = table.getCurrentPlayers() + "/" + table.getMaxPlayers();
            String name = table.getName().isEmpty() ? table.getUUID().toString() : table.getName();

            lobbyTiles.getChildren().add(new LobbyTable(players, table.getUUID(), name, this));
        }
    }

    public static void show() {
        modal = new Modal(true);
        modal.setPercentSize(0.8, 0.8);
        modal.setContent(new Lobby());
        modal.setTitle("Online Lobby");
        modal.show();
    }
}
