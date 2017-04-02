package org.gruppe2.ui.javafx.menu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import org.gruppe2.Resources;
import org.gruppe2.game.session.ReplaySession;
import org.gruppe2.game.session.Session;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;
import org.gruppe2.ui.javafx.SceneController;
import org.gruppe2.ui.javafx.ingame.Game;
import org.gruppe2.ui.javafx.ingame.GameScene;

import java.io.File;
import java.util.UUID;

public class Replay extends GridPane {

    @FXML
    private ComboBox<String> replays;
    @FXML
    private Button start;


    public Replay() {
        UIResources.loadFXML(this);

        File replayDir = new File(Resources.getUserDir("replays"));

        for (File file : replayDir.listFiles()) {
            replays.getItems().add(0, file.getName());
        }
    }

    @FXML
    private void startReplay() {
        if (replays.getSelectionModel().getSelectedIndex() < 0)
            return;

        String selected = replays.getItems().get(replays.getSelectionModel().getSelectedIndex());

        System.out.println(selected);

        Game.setPlayerUUID(UUID.randomUUID());
        Game.getInstance().setContext(Session.start(ReplaySession.class, Resources.getUserDir("replays") + selected));
        SceneController.setOnlyThisScene(new GameScene());
    }

    public static void show() {
        Modal modal = new Modal(true);
        modal.setPercentSize(0.4, 0.4);
        modal.setContent(new Replay());
        modal.setTitle("Replay");
        modal.show();
    }

}
