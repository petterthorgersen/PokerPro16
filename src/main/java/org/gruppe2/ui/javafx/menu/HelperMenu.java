package org.gruppe2.ui.javafx.menu;

import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import org.gruppe2.game.Cards;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.ingame.DifferentHandView;

public class HelperMenu extends VBox {
    @FXML
    VBox innerBox;
    @FXML
    ScrollPane entirePane;
    @FXML
    Label title;

    public HelperMenu() {
        UIResources.loadFXML(this);
        setSizes();
        innerBox.setLayoutX(-innerBox.getWidth());
        addAllHands();
    }

    //Create all hands possible
	private void addAllHands() {
        innerBox.getChildren().addAll(new DifferentHandView("High Card", Cards.asList("AS"), Cards.asList("7H 4C 2S JD")));
        innerBox.getChildren().addAll(new DifferentHandView("One Pair", Cards.asList("AD AC"), Cards.asList("7H 4C 2S")));
        innerBox.getChildren().addAll(new DifferentHandView("Two Pair", Cards.asList("QD QC 9H 9S"), Cards.asList("3S")));
        innerBox.getChildren().addAll(new DifferentHandView("Three of a Kind", Cards.asList("JD JC JS"), Cards.asList("4D 8S")));
        innerBox.getChildren().addAll(new DifferentHandView("Straight", Cards.asList("7D 8S 9H 1D JC"), null));
        innerBox.getChildren().addAll(new DifferentHandView("Flush", Cards.asList("3D 5D 7D 8D QD"), null));
        innerBox.getChildren().addAll(new DifferentHandView("Full House", Cards.asList("AS AH JD JC JH"), null));
        innerBox.getChildren().addAll(new DifferentHandView("Four of a Kind", Cards.asList("KS KD KC KH"), Cards.asList("QD")));
        innerBox.getChildren().addAll(new DifferentHandView("Straight Flush", Cards.asList("5S 6S 7S 8S 9S"), null));
        innerBox.getChildren().addAll(new DifferentHandView("Royal Flush", Cards.asList("1S JS QS KS AS"), null));

    }

    private void setSizes() {
        entirePane.prefWidthProperty().bind(this.prefWidthProperty().multiply(0.9));
        entirePane.maxWidthProperty().bind(this.maxWidthProperty().multiply(0.9));
        entirePane.setVisible(false);
        entirePane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        innerBox.maxWidthProperty().bind(entirePane.maxWidthProperty());

        this.maxWidthProperty().bind(
                PokerApplication.getRoot().widthProperty().multiply(0.22));
        this.maxHeightProperty().bind(
                PokerApplication.getRoot().heightProperty().multiply(0.6));

        pickOnBoundsProperty().setValue(false);
    }

    public void showMenu() {
        menuAnimation();
    }

    private void menuAnimation() {
        if (!entirePane.isVisible()) {
            entirePane.setVisible(true);
            entirePane.setTranslateX(-entirePane.getWidth());
        }
        TranslateTransition openMenu = new TranslateTransition(new Duration(300), entirePane);
        openMenu.setToX(0);
        TranslateTransition closeMenu = new TranslateTransition(new Duration(300), entirePane);
        if (entirePane.getTranslateX() != 0) {
            entirePane.setVisible(true);
            openMenu.play();
        } else {
            closeMenu.setToX(-entirePane.getWidth());
            closeMenu.play();
            closeMenu.setOnFinished(hide -> entirePane.setVisible(false));
        }
    }
}
