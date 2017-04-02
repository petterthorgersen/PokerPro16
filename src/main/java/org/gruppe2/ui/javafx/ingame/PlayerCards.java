package org.gruppe2.ui.javafx.ingame;

import java.util.Optional;
import java.util.UUID;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import org.gruppe2.game.Card;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.RoundEndEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.ui.UIResources;

public class PlayerCards extends Pane {
    private UUID playerUUID;

    @Helper
    private RoundHelper roundHelper;
    @Helper
    private GameHelper gameHelper;

    @FXML
    private ImageView playerCard1;
    @FXML
    private ImageView playerCard2;

    public PlayerCards() {
        UIResources.loadFXML(this);
        Game.setAnnotated(this);
    }

    @Handler
    public void onRoundStart(RoundStartEvent event) {
        if (playerUUID == null)
            return;

        Optional<RoundPlayer> roundPlayer = roundHelper.findPlayerByUUID(playerUUID);

        setVisible(roundPlayer.isPresent());

        if (!roundPlayer.isPresent())
            return;

        if (!Game.getPlayerUUID().equals(playerUUID) && gameHelper.findPlayerByUUID(Game.getPlayerUUID()).isPresent()) {
            playerCard1.setImage(UIResources.getCardBack());
            playerCard2.setImage(UIResources.getCardBack());
        } else {
            Card[] cards = roundPlayer.get().getCards();

            playerCard1.setImage(UIResources.getCard(cards[0]));
            playerCard2.setImage(UIResources.getCard(cards[1]));
        }
    }

    /**
     * Show cards at the end of the round, aka showdown.
     * @param event
     */
    @Handler
    public void onRoundEnd(RoundEndEvent event) {
        if (playerUUID == null)
            return;

        Optional<RoundPlayer> roundPlayer = roundHelper.findPlayerByUUID(playerUUID);

        setVisible(roundPlayer.isPresent());

        if (!roundPlayer.isPresent())
            return;

        Card[] cards = roundPlayer.get().getCards();

        playerCard1.setImage(UIResources.getCard(cards[0]));
        playerCard2.setImage(UIResources.getCard(cards[1]));
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;

        if (playerUUID == null)
            setVisible(false);
    }
}
