package org.gruppe2.ui.javafx.menu;

/*
 * Created by Petter on 04/04/2016.
 */

import java.io.IOException;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.ConfettiOrMoney;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.SoundPlayer;

public class Intro extends StackPane {

	@FXML
	private ImageView logo;
	@FXML
	private Label clickToContinue;
	private FadeTransition fadeTransition;

	public Intro() {
		UIResources.loadFXML(this);
	    SoundPlayer.playIntroMusic(); //IF YOU WANT MUSIC PUT THIS ON
		logo.fitWidthProperty().bind(
				PokerApplication.getRoot().widthProperty().multiply(0.8));
		clickToContinueFading(clickToContinue);
		setKeyListener();
		this.setFocused(true);
	}

	public void goToMenu() throws IOException {
		fadeTransition.stop();
		ConfettiOrMoney.stopAllAnimations();
		SceneController.setScene(new MainMenu());
	}
	private void setKeyListener() {
    	this.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
            public void handle(KeyEvent event) {
            	System.out.println("key pressed");
            	proceed(event);
            }
        });
	}

	@FXML
	public void proceed(KeyEvent event) {
		SceneController.setScene(new MainMenu());
	}

	private void clickToContinueFading(Node node) {
		fadeTransition = new FadeTransition(Duration.millis(700), node);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.1);
		fadeTransition.setCycleCount(100);
		fadeTransition.setAutoReverse(true);
		fadeTransition.play();
	}
}
