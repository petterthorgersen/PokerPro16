package org.gruppe2.ui.javafx.ingame;

import java.util.List;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import org.gruppe2.game.Card;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.PokerApplication;
/**
 * Used to get either name or the image view of a card
 *
 */
public class DifferentHandView extends VBox {
    @FXML private Label handName;
    @FXML private HBox cards;

    public DifferentHandView(String name, List<Card> hand, List<Card> notHand){
        UIResources.loadFXML(this);
        handName.setText(name);
        if (notHand != null) for (Card card : notHand) this.cards.getChildren().add(createCardImage(card, false));
        for (Card card : hand) this.cards.getChildren().add(createCardImage(card, true));
    }

    public ImageView createCardImage(Card card, boolean hand) {
        String name = "/images/cards/" + getCardName(card) + ".png";

        Image image = new Image(getClass().getResourceAsStream(name), 200, 0, true, true);

        ImageView cardPic = new ImageView(image);
        cardPic.fitWidthProperty().bind(PokerApplication.getRoot().widthProperty().multiply(0.025));
        cardPic.preserveRatioProperty().setValue(true);

        if (!hand) cardPic.opacityProperty().setValue(0.5);
        return cardPic;
    }

    public String getCardName(Card card) {
        String finalName = String.valueOf(card.getSuit().toString()
                .toLowerCase().charAt(0));

        if (card.getFaceValue() > 9)
            finalName += card.getFaceValue();
        else
            finalName += "0" + card.getFaceValue();

        return finalName;
    }
}
