package org.gruppe2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import org.gruppe2.game.PlayerStatistics;
import org.gruppe2.network.MasterServer;
import org.gruppe2.ui.console.ConsoleApplication;
import org.gruppe2.ui.javafx.PokerApplication;

/**
 * Entry point of the program.
 *
 * Responsible for reading the program arguments and parsing properties.
 */
public class Main {
    public static void setFirstStart(boolean firstStart) {
        Main.firstStart = firstStart;
    }

    private enum EntryPoint {
        CONSOLE, JAVAFX, SERVER, MASTER
    }

    private static Properties properties = new Properties();
    private static EntryPoint entryPoint = EntryPoint.JAVAFX;
    private static boolean autostart = false;
    private static boolean noSound = false;
    private static boolean firstStart = false;

    public static void main(String[] args) {
        parseArgs(args);
        loadProperties();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                saveProperties();
            }
        });

        switch (entryPoint) {
            case CONSOLE:
                new ConsoleApplication().run();
                break;
            case SERVER:
                break;
            case MASTER:
                System.out.println("starting master server");
                new MasterServer();
                break;
            default:
                PokerApplication.launch(args);
                break;
        }
    }

    /**
     * Parse console arguments from main
     * @param args console arguments
     */
    private static void parseArgs(String[] args) {
        for (String arg : args) {
            switch (arg.toLowerCase()) {
                case "--console":
                case "--nogui":
                case "-c":
                    entryPoint = EntryPoint.CONSOLE;
                    break;

                case "-a":
                case "--autostart":
                    autostart = true;
                    break;

                case "-s":
                case "--server":
                    entryPoint = EntryPoint.SERVER;
                    break;
                case "-m":
                case "--master":
                    entryPoint = EntryPoint.MASTER;
                    break;
                case "--nosound":
                    noSound = true;
                    break;

                default:
                    System.out.println("Unknown argument: " + arg);
                    break;
            }
        }
    }

    /**
     * Load the properties.
     *
     * First from the included default.properties and then the one on disk.
     */
    private static void loadProperties() {
        try {
            FileInputStream stream = new FileInputStream(Resources.getProperties());

            properties.load(Resources.getDefaultProperties());
            properties.load(stream);

            stream.close();
        } catch (IOException e) {
            System.err.println("Couldn't load properties: " + e.getMessage());
        }
    }

    /**
     * Save properties to disk.
     */
    private static void saveProperties() {
        try {
            FileOutputStream stream = new FileOutputStream(Resources.getProperties());

            properties.store(stream, "Poker Pro 16 Best Poker");

            stream.close();
        } catch (IOException e) {
            System.err.println("Couldn't save properties: " + e.getMessage());
        }
    }

    /**
     * Get a property from the properties map
     * @param key key
     * @return string value or null if properties doesn't have the key
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    /**
     * Set a property from the properties map
     * @param key key
     * @param value value
     */
    public static void setProperty(String key, String value) {
        properties.setProperty(key, value);
    }

    /**
     * Load the player's statistics.
     * @return a PlayerStatistics object
     */
    public static PlayerStatistics loadPlayerStatistics() {
        PlayerStatistics stats = new PlayerStatistics();

        for (Field f : PlayerStatistics.class.getDeclaredFields()) {
            try {
                int val = Integer.parseInt(properties.getProperty(f.getName(), "0"));

                // The fields are private, so make them accessible.
                f.setAccessible(true);

                ((AtomicInteger) f.get(stats)).set(val);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return stats;
    }

    /**
     * Save the player's statistic to disk
     * @param stats a PlayerStatistics object
     */
    public static void savePlayerStatistics(PlayerStatistics stats) {
        for (Field f : PlayerStatistics.class.getDeclaredFields()) {
            try {
                f.setAccessible(true);

                int val = ((AtomicInteger) f.get(stats)).get();

                setProperty(f.getName(), String.valueOf(val));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * If the game was launched with the --autostart parameter,
     * start a singleplayer game with default configuration.
     * @return true if launched with --autostart, false else
     */
    public static boolean isAutostart() {
        return autostart;
    }

    /**
     * If the game was launched with the --nosound parameter,
     * don't play any sounds.
     * @return true if launched with --nosound, false else
     */
    public static boolean isNoSound() {
        return noSound;
    }

    /**
     * If no properties file was found on disk, we assume that it's the
     * first time the user started the game.
     * @return true if it's the first start, false else
     */
    public static boolean isFirstStart() {
        return firstStart;
    }
}
