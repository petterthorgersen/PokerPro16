package org.gruppe2.ui.javafx.ingame;

import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.SoundPlayer;

public class ProgressBarCountDown extends HBox {

	ProgressBar progressBar = new ProgressBar(0);
	Label time = new Label("");
	Timeline preTimer;

	private final int totalTime = 30;
	private Integer ropeTime = totalTime / 3;
	private IntegerProperty timeSeconds = new SimpleIntegerProperty(ropeTime);
	private DoubleProperty progress = new SimpleDoubleProperty(ropeTime / 10);
	private IntegerProperty countDown = new SimpleIntegerProperty();
	private IntegerProperty waitTime = new SimpleIntegerProperty(totalTime * 2);

	private boolean progressBarRunning = false;

	public ProgressBarCountDown() {
		progressBar.prefWidthProperty().bind(prefWidthProperty());
		countDown.bind(progress.multiply(10).add(1));
		time.setVisible(false);
		time.textProperty().bind(countDown.asString());
		time.fontProperty().bind(
				PokerApplication.getApplication().bigFontProperty());
		spacingProperty().setValue(5);
		alignmentProperty().setValue(Pos.CENTER);
		setUpProgressBar();
		try {
			progressBar.progressProperty().bind(progress.negate().add(1));
		} catch (Exception e) {

		}
		getChildren().add(time);
	}

	private void setUpProgressBar() {
		progressBar.setVisible(false);
		getChildren().add(progressBar);
	}

	public void startProgressBarTimer() {
		progressBarRunning = true;
		timeSeconds.set(ropeTime);

		Timeline rope = new Timeline(new KeyFrame(Duration.seconds(ropeTime),
				new KeyValue(progress, 0)));

		preTimer = new Timeline(new KeyFrame(
				Duration.seconds(2 * totalTime / 3), new KeyValue(waitTime, 0)));
		preTimer.setOnFinished(event -> {
			rope.playFromStart();
			initialize();
		});
		preTimer.play();
	}

	public void initialize() {
		progressBar.setVisible(true);
		time.setVisible(true);
		SoundPlayer.playCountDownTimerMusic();
	}

	public void stopProgressBar() {
		if (progressBarRunning) {
			SoundPlayer.stopCountDownTimerMusic();
			progressBar.setVisible(false);
			time.setVisible(false);
			progress.setValue(ropeTime / 10);

			if (preTimer.getStatus().equals(Status.RUNNING))
				preTimer.stop();
		}
	}
}
