package org.gruppe2.ui.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import org.gruppe2.Main;
/**
 * SoundPlayer for all sounds in the game
 * @author htj063
 *
 */
public class SoundPlayer {

    private static final Duration FADE_DURATION = Duration.seconds(2.0);
    private static MediaPlayer introMediaPlayer;
    private static MediaPlayer countDownMediaPlayer;

    public static void playIntroMusic() {
        introMediaPlayer = playSound(SoundPlayer.class.getResource("/sound/jazzy_intro.mp3")
                .toExternalForm());
    }

    public static void playVictoryMusic() {
        playSound(SoundPlayer.class.getResource("/sound/victory.mp3")
                .toExternalForm());
    }

    public static void playCountDownTimerMusic() {
        countDownMediaPlayer = playSound(SoundPlayer.class.getResource(
                "/sound/countdown.mp3").toExternalForm());
        if (countDownMediaPlayer != null) {
            fadeOutOnEnd(countDownMediaPlayer, 10);
        }
    }


    public static void playYes() {
        playSound(SoundPlayer.class.getResource("/sound/yes.mp3")
                .toExternalForm());
    }

    public static void playNo() {
        playSound(SoundPlayer.class.getResource("/sound/no.mp3")
                .toExternalForm());
    }

    public static void playFuckOff() {
        playSound(SoundPlayer.class.getResource("/sound/fuckoff.mp3")
                .toExternalForm());
    }

    public static void playRaidingParty() {
        playSound(SoundPlayer.class.getResource("/sound/raidingparty.mp3")
                .toExternalForm());
    }

    public static void playFlush() {
        playSound(SoundPlayer.class.getResource("/sound/flush.mp3")
                .toExternalForm());
    }

    public static void playTrololo() {
        playSound(SoundPlayer.class.getResource("/sound/trollolo.mp3")
                .toExternalForm());
    }

    public static void play2Hours() {
        playSound(SoundPlayer.class.getResource("/sound/2hours.mp3")
                .toExternalForm());
    }


    public static void stopIntroMusic() {
        if (introMediaPlayer != null)
            fadeOutOnEnd(introMediaPlayer, 2);
    }

    public static void stopCountDownTimerMusic() {
        if (countDownMediaPlayer != null)
            countDownMediaPlayer.stop();
    }


    private static MediaPlayer playSound(String musicFile) {
        if (Main.isNoSound())
            return null;

        try {

            Media sound = new Media(musicFile);
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
            return mediaPlayer;

        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }

        return null;
    }

    private static void fadeOutOnEnd(MediaPlayer mediaPlayer, int delayTime) {
        final Timeline fadeOutTimeline = new Timeline(new KeyFrame(
                FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), 0.0)));
        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(delayTime), ae -> fadeOutTimeline.play()));
        timeline.play();

    }

    @SuppressWarnings("unused")
	private static void fadeInOnStart(MediaPlayer mediaPlayer) {
        final Timeline fadeInTimeline = new Timeline(new KeyFrame(
                FADE_DURATION, new KeyValue(mediaPlayer.volumeProperty(), 1.0)));
        fadeInTimeline.play();
    }


}
