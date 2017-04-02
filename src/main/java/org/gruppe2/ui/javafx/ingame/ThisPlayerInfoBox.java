package org.gruppe2.ui.javafx.ingame;

import java.util.Optional;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.event.PlayerPreActionEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.ui.UIResources;

/**
 * Creates a box to show information about you.
 * @author htj063
 *
 */
public class ThisPlayerInfoBox extends VBox {

	@Helper
    private GameHelper game;
    @Helper
    private RoundHelper round;

    @FXML
    private ProgressBarCountDown progressBar;
    @FXML
    private Label name;
    @FXML
    private ImageView avatar;
	@FXML 
	private ImageView fold;
    @FXML
    private Label bank;
    @FXML
    private Label bet;
    @FXML
    private Label lastAction;
    Player player;
    private boolean isActive;

    public ThisPlayerInfoBox() {
        UIResources.loadFXML(this);
        Game.setAnnotated(this);
    }

    @Handler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().getUUID().equals(Game.getPlayerUUID()))
            return;

        Optional<Player> player = game.findPlayerByUUID(Game.getPlayerUUID());

        setVisible(player.isPresent());

        if (!player.isPresent())
            return;
        else this.player = player.get();
        fold.setVisible(false);
        lastAction.setVisible(false);

        name.setText(player.get().getName());
        bank.setText(String.valueOf(player.get().getBank()));
        bet.setText("0");

        avatar.setImage(UIResources.getAvatar(player.get().getAvatar()));
    }

    @Handler
    public void onPlayerLeave(PlayerLeaveEvent e) {
        if (!e.getPlayer().getUUID().equals(Game.getPlayerUUID()))
            return;

        setVisible(false);
        progressBar.stopProgressBar();
    }

    @Handler
    public void onRoundStart(RoundStartEvent e) {
        fold.setVisible(false);
        progressBar.stopProgressBar();
    }

    @Handler
    public void onPreAction(PlayerPreActionEvent e){
        if (e.getPlayer().getUUID().equals(Game.getPlayerUUID())) {
        	
        	progressBar.startProgressBarTimer();
            fold.setVisible(false);
            lastAction.setVisible(false);
            isActive = true;
            bank.setText(String.valueOf(e.getPlayer().getBank()));
            bet.setText(String.valueOf(e.getRoundPlayer().getBet()));
            setActive(isActive);
        } else {
            isActive = false;
            setActive(isActive);
        }
    }

    @Handler
    public void onPostAction(PlayerPostActionEvent e){
        if (!e.getPlayer().getUUID().equals(Game.getPlayerUUID()))
            return;
        
        progressBar.stopProgressBar();
        bank.setText(String.valueOf(e.getPlayer().getBank()));
        bet.setText(String.valueOf(e.getRoundPlayer().getBet()));

        if (e.getAction() instanceof Action.Fold) {
            fold.setVisible(true);
        } else {
            lastAction.setText(e.getAction().toString());
            lastAction.setVisible(true);
        }
    }
    @Handler
    public void onPlayerWon(PlayerWonEvent playerWonEvent){
    	bet.setText("0");
    	bank.setText(String.valueOf(player.getBank()));
    	progressBar.stopProgressBar();
    }

    @FXML
    public void hover(){
        if (player == null)
            return;

        Color color = UIResources.getAvatarColor(player.getAvatar());
        setBackground(new Background(new BackgroundFill(color.darker(), new CornerRadii(5), null)));

    }

    @FXML
    public void noHover(){
        setActive(isActive);
    }

    private void setActive(boolean active) {
        getStyleClass().clear();
        getStyleClass().add(active ? "paneActive" : "pane");
    }
}
