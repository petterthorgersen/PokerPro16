package org.gruppe2.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

import org.gruppe2.game.Card;
import org.gruppe2.ui.javafx.PokerApplication;

/**
 * A helper class with static methods for finding various resources.
 *
 * This differs from {@link org.gruppe2.Resources} in that it is used only by JavaFX stuff,
 * so that the master server can run on headless servers.
 */
public class UIResources {
    private final static String uiPackageString = PokerApplication.class.getPackage().getName();

    private static List<Image> cards = null;
    private static Image cardBack = null;

    private static Map<String, Image> avatars = null;
    private static Map<String, Color> avatarColors = null;
    private static Image defaultAvatar = null;

    private static Map<String, Image> emotes = null;

    /**
     * Get the correct FXML file in /views/ based on the class name
     *
     * If the class is org.gruppe2.ui.javafx.menu.Intro, then this will attempt to load /menu/Intro.fxml
     *
     * @param node a JavaFX node declared in org.gruppe2.ui.**
     */
    public static void loadFXML(Node node) {
        try {
            String path = node.getClass().getName().substring(uiPackageString.length()).replace('.', '/');

            FXMLLoader fxmlLoader = new FXMLLoader(UIResources.class.getResource("/views" + path + ".fxml"));

            fxmlLoader.setRoot(node);
            fxmlLoader.setController(node);

            fxmlLoader.load();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a cached avatar image
     * @param name the name of the avatar
     * @return Image instance
     */
    public static Image getAvatar(String name) {
        loadAvatars();

        return avatars.getOrDefault(name, defaultAvatar);
    }

    /**
     * Get the cached colour associated with the avatar image
     * @param name the name of the avatar
     * @return Color instance
     */
    public static Color getAvatarColor(String name) {
        loadAvatars();

        return avatarColors.getOrDefault(name, Color.WHITE);
    }

    /**
     * Get the name array of cached avatars
     * @return array with names of avatars
     */
    public static String[] listAvatars() {
        loadAvatars();

        return avatars.keySet().toArray(new String[avatars.size()]);
    }

    /**
     * Load the avatars from .jar if it hasn't been done yet
     */
    private static void loadAvatars() {
        if (avatars != null)
            return;

        avatars = new HashMap<>();
        avatarColors = new HashMap<>();

        try {
            Scanner dir = new Scanner(UIResources.class.getResourceAsStream("/images/avatars/avatars.txt"));

            while (dir.hasNext()) {
                String name = dir.next();
                Image image = new Image(UIResources.class.getResourceAsStream("/images/avatars/" + name + ".png"));
                defaultAvatar = image;

                avatars.put(name, image);

                PixelReader pixelReader = image.getPixelReader();
                Color color = pixelReader.getColor(2, 2);
                avatarColors.put(name, color);
            }
            dir.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a cached card image
     * @param card a {@link Card} to get
     * @return cached card Image
     */
    public static Image getCard(Card card) {
        loadCards();

        return cards.get(card.getSuit().ordinal() * 13 + (card.getFaceValue() - 2));
    }

    /**
     * Get the cached card back Image (ie. the back of the card)
     * @return cached Image
     */
    public static Image getCardBack() {
        if (cardBack == null) {
            cardBack = new Image(UIResources.class.getResourceAsStream("/images/cards/card_back.png"));
        }

        return cardBack;
    }

    /**
     * Load the cards from .jar if it hasn't been done yet
     */
    private static void loadCards() {
        if (cards != null)
            return;

        cards = new ArrayList<>();

        char[] suitChars = {'c', 'd', 'h', 's'};

        for (int suit = 0; suit < 4; suit++) {
            for (int face = 2; face <= 14; face++) {
                String path = String.format("/images/cards/%c%02d.png", suitChars[suit], face);

                cards.add(new Image(UIResources.class.getResourceAsStream(path)));
            }
        }
    }

    /**
     * Get the name array of cached emotes
     * @return array with names of emotes
     */
    public static String[] listEmotes() {
        loadEmotes();

        return emotes.keySet().toArray(new String[emotes.size()]);
    }

    /**
     * Get a cached emote
     * @param emote the name of the emote to get
     * @return cached emote Image
     */
    public static Image getEmote(String emote) {
        loadEmotes();

        return emotes.get(emote);
    }

    /**
     * Load the emotes from .jar if it hasn't been done yet
     */
    private static void loadEmotes() {
        if (emotes != null)
            return;

        try {
            String dir = "/images/emotes/";

            emotes = new HashMap<>();

            Properties emoteMap = new Properties();
            emoteMap.load(UIResources.class.getResourceAsStream("/images/emotes/emotes.properties"));

            for (Map.Entry<Object, Object> entry : emoteMap.entrySet()) {
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                String path = dir + value;

                emotes.put(key, new Image(UIResources.class.getResourceAsStream(path)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
