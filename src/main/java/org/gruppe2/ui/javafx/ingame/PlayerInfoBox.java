package org.gruppe2.ui.javafx.ingame;


import java.util.Optional;
import java.util.UUID;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
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
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.ToolTip;
/**
 * InfoBoxes used for players that are not you.
 * @author htj063
 *
 */
public class PlayerInfoBox extends VBox {
    private UUID playerUUID = null;

    Player player;

    @FXML
    private ProgressBarCountDown progressBar;
    private ObjectProperty<Font> font = new SimpleObjectProperty<>();

    @Helper
    private RoundHelper roundHelper;
    @Helper
    private GameHelper gameHelper;

    @FXML
    private Label name;
    @FXML
    private Label bank;
    @FXML
    private Label bet;
    @FXML
    private ImageView avatar;
    @FXML
    private ImageView fold;
    @FXML
    private Label lastAction;
    private boolean isActive;

    PlayerInfoBox() {
        UIResources.loadFXML(this);
        Game.setAnnotated(this);
        
    }

    public void setPlayerUUID(UUID uuid) {
        playerUUID = uuid;

        if (uuid == null) {
            setVisible(false);
            player = null;
            return;
        }

        Optional<Player> player = gameHelper.findPlayerByUUID(uuid);
        Optional<RoundPlayer> roundPlayer = roundHelper.findPlayerByUUID(uuid);

        setVisible(player.isPresent());

        if (!player.isPresent())
            return;

        this.player = player.get();

        name.setText(player.get().getName());
        bank.setText(String.valueOf(player.get().getBank()));
        avatar.setImage(UIResources.getAvatar(player.get().getAvatar()));

        fold.setVisible(roundPlayer.isPresent());

        if (roundPlayer.isPresent()) {
            bet.setText(String.valueOf(roundPlayer.get().getBet()));
        }
        
        
    }
    

    private void setActive(boolean active) {
        getStyleClass().clear();
        getStyleClass().add(active ? "paneActive" : "pane");
    }

    @Handler
    public void onRoundStart(RoundStartEvent event){
        if (player != null) resetBox();
    }

    private void resetBox() {
        fold.setVisible(false);
        lastAction.setVisible(false);
        bet.setText("0");
        opacityProperty().setValue(1);
    }

    @FXML
    public void hover(){
        Color color = UIResources.getAvatarColor(player.getAvatar());
        setBackground(new Background(new BackgroundFill(color.darker(), new CornerRadii(5), null)));
        
    }

    @FXML
    public void noHover(){
        setActive(isActive);
    }


    @FXML
    public void viewStatistic(MouseEvent event) {
        SceneController.setTooltip(new ToolTip(new Statistic(playerUUID)), event.getSceneX(), event.getSceneY());
    }

    @Handler
    public void onPreAction(PlayerPreActionEvent event) {
        if (event.getPlayer().getUUID().equals(playerUUID)) {
        	progressBar.startProgressBarTimer();
            fold.setVisible(false);
            lastAction.setVisible(false);
            isActive = true;
            setActive(isActive);
        } else {
            isActive = false;
            setActive(isActive);
        }
    }

    @Handler
    public void onPostAction(PlayerPostActionEvent event) {
        if (!event.getPlayer().getUUID().equals(playerUUID))
            return;
        progressBar.stopProgressBar();
        bank.setText(String.valueOf(event.getPlayer().getBank()));
        bet.setText(String.valueOf(event.getRoundPlayer().getBet()));

        if (event.getAction() instanceof Action.Fold) {
            fold.setVisible(true);
            opacityProperty().setValue(0.3);
        } else {
            lastAction.setText(event.getAction().toString());
            lastAction.setVisible(true);
        }
    }
    
    @Handler
    public void onPlayerWon(PlayerWonEvent playerWonEvent){
    	bet.setText("0");
    	if(player != null)
    		bank.setText(String.valueOf(player.getBank()));
    	progressBar.stopProgressBar();
    }
    
    @Handler
    public void onPlayerLeave(PlayerLeaveEvent e) {
        if (!e.getPlayer().getUUID().equals(playerUUID))
            return;

        setVisible(false);
        progressBar.stopProgressBar();
    }

    public boolean isPlayerActive() {
        for (RoundPlayer roundPlayer : roundHelper.getActivePlayers()) {
            if (roundPlayer.getUUID().equals(player.getUUID()))
                return true;
        }
        return false;
    }

    public Font getFont() {
        return font.get();
    }

    public ObjectProperty<Font> fontProperty() {
        return font;
    }

    public void setFont(Font font) {
        this.font.set(font);
    }
}
