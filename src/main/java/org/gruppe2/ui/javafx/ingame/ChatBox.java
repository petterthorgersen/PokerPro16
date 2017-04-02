package org.gruppe2.ui.javafx.ingame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.VPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import org.gruppe2.game.Card;
import org.gruppe2.game.Hand;
import org.gruppe2.game.Player;
import org.gruppe2.game.calculation.Generic;
import org.gruppe2.game.event.ChatEvent;
import org.gruppe2.game.event.PlayerJoinEvent;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.PlayerWonEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.ui.UIResources;
import org.gruppe2.ui.javafx.PokerApplication;
import org.gruppe2.ui.javafx.SoundPlayer;
/**
 * This is the chatbox used for messages between players and commands.
 * @author htj063
 *
 */
public class ChatBox extends VBox {
    private static final double openedHeight = 400.0;
    private static final double closedHeight = 100.0;

    private final List<String> emotes = Arrays.asList(UIResources.listEmotes());

    @Helper
    private GameHelper gameHelper;
    @Helper
    private RoundHelper roundHelper;

    @FXML
    private GridPane chatArea;
    @FXML
    private TextField chatField;
    @FXML
    private ScrollPane scrollPane;

    private DoubleProperty nameWidth = new SimpleDoubleProperty();
    private DoubleProperty messageWidth = new SimpleDoubleProperty();
    private DoubleProperty chatHeight = new SimpleDoubleProperty(closedHeight);

    private int numLines = 1;

    public ChatBox() {
        UIResources.loadFXML(this);
        Game.setAnnotated(this);

        nameWidth.bind(widthProperty().multiply(0.2));
        messageWidth.bind(widthProperty().subtract(nameWidth.multiply(1.2)));

        chatField.focusedProperty().addListener((o, oldVal, hasFocus) -> {
            if (hasFocus) {
                chatHeight.set(openedHeight);
                scrollPane.setStyle("-fx-background-color: #0d0d0d");
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            } else {
                chatHeight.set(closedHeight);
                scrollPane.setStyle("-fx-background-color: transparent");
                scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            }
        });
    }

    @FXML
    public void onChatAction(ActionEvent event) {
        if (gameHelper.findPlayerByUUID(Game.getPlayerUUID()).isPresent() && !checkForCommands(chatField.getText())) {
            Game.message("chat", chatField.getText(), Game.getPlayerUUID());
        }

        chatField.setText("");
    }

    private boolean checkForCommands(String text) {
        String command = text.toLowerCase();

        switch (command) {
            case "/besthand":

                if(roundHelper.findPlayerByUUID(Game.getPlayerUUID()).isPresent()) {
                    List<Card> cards = new ArrayList<Card>();
                    cards.addAll(roundHelper.getCommunityCards());
                    Card[] playerCards = roundHelper.findPlayerByUUID(Game.getPlayerUUID()).get().getCards();

                    for (int i = 0; i < playerCards.length; i++) {
                        cards.add(playerCards[i]);
                    }

                    Hand hand = Generic.getBestHandForPlayer(cards);
                    String answer = hand.toString();
                    addLine("Possible best hand is: " + answer);
                } else {
                    addLine("Player has no hand.");
                }
                return true;
            case "/log":
                addLine(command + "is epic");
                // Print logs--->
                return true;
            case "/sounds":

            	addLine("Possible sounds:\n  yes\n  no\n  raiding party\n  fuck off\n  flush\n  trol\n  2 hours");
            	return true;
            case "/help":
            	addLine("Possible commands: \n  /besthand\n  /log\n  /sounds\n  /help");

            	return true;
            default:
                return false;
        }
    }

    @Handler
    public void chatHandler(ChatEvent chatEvent) {
        if (chatEvent.getMessage().isEmpty())
            return;

        checkForSound(chatEvent.getMessage());

        Player player = gameHelper.findPlayerByUUID(chatEvent.getPlayerUUID()).get();

        addPlayerMessage(player, chatEvent.getMessage());
    }

    private boolean checkForSound(String message) {
    	String command = message.toLowerCase();

        switch (command) {
          
            case "fuck off":
            	
            	SoundPlayer.playFuckOff();
                return true;
            case "raiding party":
            	
            	SoundPlayer.playRaidingParty();
                return true;
            case "flush":
            	
        		SoundPlayer.playFlush();
        		return true;

            case "2 hours":
            	
        		SoundPlayer.play2Hours();
        		return true;
            case "trol":
            	
        		SoundPlayer.playTrololo();
        		return true;

            case "yes":
            	
        		SoundPlayer.playYes();
        		return true;
            case "no":
            	
        		SoundPlayer.playNo();
        		return true;
            
            default:
                return false;
        }
		
	}

	@Handler
    public void onRoundStart(RoundStartEvent event) {
        addLine("A new round has started");
    }

    @Handler
    public void onPlayerJoin(PlayerJoinEvent event) {
        addLine(String.format("%s has joined the game", event.getPlayer().getName()));
    }

    @Handler
    public void onPlayerLeave(PlayerLeaveEvent event) {
        addLine(String.format("%s has left the game", event.getPlayer().getName()));
    }

    @Handler
    public void onPlayerWon(PlayerWonEvent event) {
        for (int i = 0; i < event.getPlayers().size(); i ++)
            addLine(event.getPlayers().get(i).getName() + " has won " + event.getChips().get(i) + " chips! ");
    }

    private void addPlayerMessage(Player player, String message) {
        Text playerName = new Text(player.getName());

        playerName.setFill(getPlayerColor(player));
        playerName.fontProperty().bind(PokerApplication.getApplication().smallFontProperty());

        TextFlow messageFlow = parseEmotes(message);
        messageFlow.maxWidthProperty().bind(messageWidth);

        chatArea.add(playerName, 0, numLines);
        chatArea.add(messageFlow, 1, numLines);

        GridPane.setValignment(playerName, VPos.TOP);

        numLines++;

        scrollPane.setVvalue(1.0);
    }

    private void addLine(String text) {
        Text textNode = new Text(text);
        TextFlow textFlow = new TextFlow(textNode);

        textNode.setFill(Color.GRAY);
        textNode.fontProperty().bind(PokerApplication.getApplication().smallFontProperty());
        textFlow.maxWidthProperty().bind(nameWidth.add(messageWidth));

        chatArea.add(textFlow, 0, numLines, 2, 1);

        numLines++;

        scrollPane.setVvalue(1.0);
    }

    private TextFlow parseEmotes(String message) {
        TextFlow flow = new TextFlow();
        String[] words = message.split("\\s+");
        String buffer = "";

        for (String word : words) {
            

            if ((emotes.indexOf(word)) >= 0) {
                Text text = new Text(buffer);
                text.setFill(Color.GRAY);
                text.fontProperty().bind(PokerApplication.getApplication().smallFontProperty());
                flow.getChildren().add(text);
                buffer = " ";

                ImageView imageView = new ImageView(UIResources.getEmote(word));
                imageView.setPreserveRatio(true);
                imageView.fitHeightProperty().bind(PokerApplication.getApplication().scaleProperty().multiply(14));
                flow.getChildren().add(imageView);

                continue;
            }

            buffer += word + " ";
        }

        if (!buffer.isEmpty()) {
            Text text = new Text(buffer);
            text.setFill(Color.LIGHTGRAY);
            text.fontProperty().bind(PokerApplication.getApplication().smallFontProperty());
            flow.getChildren().add(text);
        }

        return flow;
    }

    private Color getPlayerColor(Player player) {
        return UIResources.getAvatarColor(player.getAvatar());
    }

    public double getChatHeight() {
        return chatHeight.get();
    }

    public DoubleProperty chatHeightProperty() {
        return chatHeight;
    }
}
