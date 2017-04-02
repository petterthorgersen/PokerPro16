package org.gruppe2.ui.javafx.ingame;

import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.gruppe2.game.PlayerStatistics;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.model.StatisticsModel;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Model;
import org.gruppe2.ui.UIResources;

class Statistic extends GridPane {
	UUID uuid;
    @Model
    private StatisticsModel model;
    @Helper
    private GameHelper gameHelper;

    @FXML
    private Label name;
    @FXML
    private Label gamesPlayed;
    @FXML
    private Label gamesWon;
    @FXML
    private Label gamesLost;
    @FXML
    private Label timesCalled;
    @FXML
    private Label timesChecked;
    @FXML
    private Label timesRaised;
    @FXML
    private Label totalBets;
    @FXML
    private Label totalWinnings;
    @FXML
    private Button kick;

    Statistic(UUID playerUUID) {
    	uuid = playerUUID;
        UIResources.loadFXML(this);
        Game.setAnnotated(this);

        PlayerStatistics stats = model.getPlayerStatistics().get(playerUUID);

        name.setText(gameHelper.findPlayerByUUID(playerUUID).get().getName());

        gamesPlayed.setText(String.valueOf(stats.getGamesPlayed()));
        gamesWon.setText(String.valueOf(stats.getGamesWon()));
        gamesLost.setText(String.valueOf(stats.getGamesLost()));
        timesCalled.setText(String.valueOf(stats.getTimesCalled()));
        timesChecked.setText(String.valueOf(stats.getTimesChecked()));
        timesRaised.setText(String.valueOf(stats.getTimesRaised()));
        totalBets.setText(String.valueOf(stats.getGamesPlayed()));
        totalWinnings.setText(String.valueOf(stats.getGamesPlayed()));
    }
    @FXML
    private void kickPlayer(){
    	System.out.println("kicking player");
    	Game.getContext().message("kickPlayer", uuid);
    }
}
