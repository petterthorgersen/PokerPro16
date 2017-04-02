package org.gruppe2.ui.javafx.menu;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import org.gruppe2.Main;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.Modal;

class PersonalStatistics extends GridPane {
	

    @FXML
    private Label gamesPlayed;
    @FXML
    private Label gamesWon;
    @FXML
    private Label gamesLost;
    @FXML
    private Label gamesFolded;
    @FXML
    private Label timesCalled;
    @FXML
    private Label timesChecked;
    @FXML
    private Label timesRaised;
    @FXML
    private Label totalBets;
    @FXML
    private Label totalWinnings;
    

    PersonalStatistics() {
    	
        UIResources.loadFXML(this);
        
        
        gamesPlayed.setText(Main.getProperty("gamesPlayed"));
        gamesWon.setText(Main.getProperty("gamesWon"));
        gamesLost.setText(Main.getProperty("gamesLost"));
        timesCalled.setText(Main.getProperty("timesCalled"));
        timesChecked.setText(Main.getProperty("timesChecked"));
        timesRaised.setText(Main.getProperty("timesRaised"));
        gamesFolded.setText(Main.getProperty("timesFolded"));
        totalBets.setText(Main.getProperty("totalBets"));
        totalWinnings.setText(Main.getProperty("totalWinnings"));
        
    }
    public static void show() {
		Modal modal = new Modal(true);
		modal.setPercentSize(0.5, 0.5);
		modal.setContent(new PersonalStatistics());
		modal.setTitle(Main.getProperty("name")+"'s"+" Statistics");
		modal.show();
	}
}
