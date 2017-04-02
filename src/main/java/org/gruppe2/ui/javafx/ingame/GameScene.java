package org.gruppe2.ui.javafx.ingame;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import org.gruppe2.Main;
import org.gruppe2.game.PlayerStatistics;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.event.QuitEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.model.StatisticsModel;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Model;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.ConfettiOrMoney;
import org.gruppe2.ui.javafx.Modal;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.SoundPlayer;

public class GameScene extends Pane {
    
	@Helper
    private GameHelper gameHelper;
    @Helper
    private RoundHelper roundHelper;
    @Model
    private StatisticsModel statisticsModel;

    @FXML
    private Table table;
    @FXML
    private ChoiceBar choiceBar;
    @FXML
    private ChatBox chatBox;
    @FXML
    private PlayerCards playerCards;

    public GameScene() {
    	SoundPlayer.stopIntroMusic();
    	
        UIResources.loadFXML(this);
        Game.setAnnotated(this);
        
        choiceBar.toFront();
        chatBox.toFront();
        playerCards.toFront();
        playerCards.setPlayerUUID(Game.getPlayerUUID());
    }

    
    @SuppressWarnings("incomplete-switch")
	public void gameKeyOptions(KeyEvent event){
    	switch (event.getCode()) {
        case UP:    choiceBar.onBetAction(); break;
        case DOWN:  choiceBar.onFoldAction(); break;
        case LEFT:  choiceBar.decreaseSlider(); break;
        case RIGHT: choiceBar.increaseSlider(); break;    
    }
    }

	@Handler
    public void onQuit(QuitEvent event) {
        PlayerStatistics stats = statisticsModel.getPlayerStatistics().get(Game.getPlayerUUID());

        if (stats != null) {
            Main.savePlayerStatistics(stats);
        }

        Modal.messageBox("Left table", event.getReason(),true);
    }

    @FXML
    public void onMouseClicked(MouseEvent e) {
        requestFocus();
    }

    @Handler
    public void onPlayerWon(PlayerWonEvent event){
        getChildren().add(new ConfettiOrMoney(100, true));
        String text = "";
        for (int i = 0; i < event.getPlayers().size(); i ++)
            text += event.getPlayers().get(i).getName() + " has won " + event.getChips().get(i) + " chips! \n";
        Label label = new Label(text);
        SceneController.setFadingModal(label);
        SoundPlayer.playVictoryMusic();
    }

    @Handler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        if (event.getPlayer().getUUID().equals(Game.getPlayerUUID()))
            Game.getContext().message("quit", "You've been kicked");
    }
}
