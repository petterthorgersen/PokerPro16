package org.gruppe2.ui.javafx.menu;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import org.gruppe2.Main;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;

public class CreateGameSettings extends GridPane {
    @FXML
    private TextField tableName;
    @FXML
    private TextField smallBlind;
    @FXML
    private TextField bigBlind;
    @FXML
    private TextField startMoney;
    @FXML
    private TextField maxPlayers;
    @FXML
    private TextField minPlayers;
    @FXML
    private ComboBox<String> botDiff;

    private Consumer<List<String>> start;

    public CreateGameSettings(Consumer<List<String>> start) {
        UIResources.loadFXML(this);
        setDefaultSettings();

        this.start = start;
    }

    @FXML
    private void ok() {
        if (valuesAreValid()) {
            saveSettings();
            start.accept(Arrays.asList(tableName.getText(),
                    smallBlind.getText(), bigBlind.getText(),
                    startMoney.getText(), maxPlayers.getText(),
                    minPlayers.getText(), botDiff.getSelectionModel()
                            .getSelectedItem()));
        }
    }

    private void saveSettings() {
        Main.setProperty("tableName", tableName.getText());
        Main.setProperty("smallBlind", smallBlind.getText());
        Main.setProperty("bigBlind", bigBlind.getText());
        Main.setProperty("startMoney", startMoney.getText());
        Main.setProperty("minPlayers", minPlayers.getText());
        Main.setProperty("maxPlayers", maxPlayers.getText());
        Main.setProperty("botDiff", botDiff.getSelectionModel()
                .getSelectedItem());
    }

    @FXML
    private void cancel() {
    }

    private boolean valuesAreValid() {
        if (!tableName.getText().equals("") && !smallBlind.getText().equals("")
                && !bigBlind.getText().equals("")
                && !startMoney.getText().equals("")
                && !maxPlayers.getText().equals("")) {
            return true;
        } else
            return false;
    }

    public static void show(Consumer<List<String>> start) {
        Modal modal = new Modal(true);
        modal.setPercentSize(0.5, 0.5);
        modal.setContent(new CreateGameSettings(start));
        modal.setTitle("Create Game");
        modal.show();
    }

    public void setDefaultSettings() {
        tableName.setText(Main.getProperty("tableName"));
        smallBlind.setText(Main.getProperty("smallBlind"));
        bigBlind.setText(Main.getProperty("bigBlind"));
        startMoney.setText(Main.getProperty("startMoney"));
        maxPlayers.setText(Main.getProperty("maxPlayers"));
        minPlayers.setText(Main.getProperty("minPlayers"));
        setBotDiff();

    }

    private void setBotDiff() {
    	botDiff.getItems().add("Random");
    	botDiff.getItems().add("None");
        botDiff.getItems().add("Easy");
        botDiff.getItems().add("Normal");
        botDiff.getItems().add("Hard");
        

        String botDiffFromFile = Main.getProperty("botDiff");

        botDiff.getSelectionModel().select(botDiffFromFile);
        
    }
}
