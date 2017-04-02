package org.gruppe2.ui.javafx.ingame;

import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.PossibleActions;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerPostActionEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Query;
import org.gruppe2.ui.UIResources;

/**
 * This is the bottom choicebar used to press an action (fold or bet)
 *
 * @author htj063
 */
public class ChoiceBar extends StackPane {
    @Helper
    private GameHelper gameHelper;
    @Helper
    private RoundHelper roundHelper;

    @FXML
    private HBox spectatorBar;
    @FXML
    private HBox playerBar;
    @FXML
    private Button btnFold;
    @FXML
    private Slider slider;
    @FXML
    private Label sliderValue;
    @FXML
    private Button btnBet;

    private Query<Action> actionQuery = null;

    public ChoiceBar() {
        UIResources.loadFXML(this);
        Game.setAnnotated(this);
        setEvents();
    }

    @FXML
    public void onJoinAction(ActionEvent event) {
        Game.getInstance().join();
    }

    @FXML
    public void setEvents() {
        slider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    updateSliderValue();
                });
    }

    public void updateSliderValue() {
        sliderValue.textProperty().setValue(checkMaxBid());
    }

    @FXML
    public void onFoldAction() {
        if (actionQuery != null) {
            actionQuery.set(new Action.Fold());
            actionQuery = null;
        }
    }

    @FXML
    public void onBetAction() {
        if (actionQuery != null) {
            UUID uuid = Game.getPlayerUUID();
            Player player = gameHelper.findPlayerByUUID(uuid).get();
            int myBank = player.getBank();
            @SuppressWarnings("unused")
            int heighestBet = roundHelper.getModel().getHighestBet();

            PossibleActions possibleActions = roundHelper
                    .getPlayerOptions(uuid);

            int amount = ((int) slider.getValue()) - possibleActions.getCallAmount();

            if (possibleActions.canAllIn()) {
                actionQuery.set(new Action.AllIn());
            } else if (amount == 0) {
                if (possibleActions.canCall()) {
                    actionQuery.set(new Action.Call());
                } else if (possibleActions.canCheck()) {
                    actionQuery.set(new Action.Check());
                } else {
                    System.out.println("call amount was: " + possibleActions.getCallAmount() + "highest bet was: " + roundHelper.getModel().getHighestBet() + "\nmy bank was" + myBank);
                    throw new RuntimeException();
                }
            } else if (possibleActions.canRaise()) {
                actionQuery.set(new Action.Raise(amount));
            }

            actionQuery = null;
        }

    }

    public void increaseSlider() {
        slider.setValue(slider.getValue() * 2);
    }

    public void decreaseSlider() {
        slider.setValue(slider.getValue() / 2);
    }

    /**
     * Decide what the bet buttons text each round should be.
     *
     * @param slider
     * @return
     */
    private String checkMaxBid() {
        PossibleActions pa = roundHelper.getPlayerOptions(Game.getPlayerUUID());
        @SuppressWarnings("unused")
        int heighestBet = roundHelper.getModel().getHighestBet();
        @SuppressWarnings("unused")
        int myBank = gameHelper.findPlayerByUUID(Game.getPlayerUUID()).get().getBank();

        if (slider.getValue() == slider.getMax() || pa.canAllIn())
            btnBet.setText("All in");
        else if (slider.getValue() > pa.getCallAmount())
            btnBet.setText("Raise");
        else if (pa.canCall())
            btnBet.setText("Call");
        else
            btnBet.setText("Check");
        return (int) slider.getValue() + " CHIPS";
    }

    @Handler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (event.getPlayer().getUUID().equals(Game.getPlayerUUID())) {
            spectatorBar.setVisible(false);
            playerBar.setVisible(true);
        }
    }

    @Handler
    public void onPlayerPostAction(PlayerPostActionEvent event) {
        btnFold.setDisable(true);
        btnBet.setDisable(true);
        slider.setDisable(true);
        sliderValue.setDisable(true);
    }

    @Handler
    public void onActionQuery(PlayerActionQuery query) {
        if (!query.getPlayer().getUUID().equals(Game.getPlayerUUID()))
            return;

        PossibleActions actions = roundHelper.getPlayerOptions(Game.getPlayerUUID());

        btnFold.setDisable(false);
        btnBet.setDisable(false);

        if (actions.canRaise()) {
            slider.setDisable(false);
            sliderValue.setDisable(false);
            slider.setMin(actions.getCallAmount());
            slider.setMax(actions.getMaxRaise() + actions.getCallAmount());
        }

        if (actions.canCheck())
            slider.setValue(0);
        else if (actions.canCall())
            slider.setValue(actions.getCallAmount());
        else if (actions.canAllIn())
            slider.setValue(query.getPlayer().getBank());

        actionQuery = query.getPlayer().getAction();

        updateSliderValue();
    }
}