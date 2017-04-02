package org.gruppe2.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.gruppe2.Main;
import org.gruppe2.game.session.ClientSession;
import org.gruppe2.game.session.Session;
import org.gruppe2.game.session.SessionContext;

/**
 * Class for client handling in lobby, controll is given to ClientController when the game starts
 *
 * @author htj063
 */
public class MasterClient {
    private NetworkIO connection;

    private Consumer<SessionContext> onJoinGame = null;
    private Consumer<List<TableEntry>> onRefresh = null;
    private Runnable onError = null;

    public MasterClient() throws IOException {
        try {
            connect("localhost");
        } catch (IOException e) {
            connect(Main.getProperty("master"));
        }

        connection.sendMessage("HELLO\r\n");
    }

    private void connect(String ip) throws IOException {
        SocketChannel channel = SocketChannel.open(new InetSocketAddress(
                ip, 8888));
        connection = new NetworkIO(channel);

        channel.configureBlocking(false);

        connection.setInputFormat(NetworkIO.Format.STRING);
        connection.setOutputFormat(NetworkIO.Format.STRING);
    }

    public void update() throws IOException {
        String[] message = connection.readMessage();

        if (message == null)
            return;

        switch (message[0]) {
            case "HELLO":
                if (message[1].equals("MASTER")) {

                } else {
                    if (onError != null)
                        onError.run();
                }
                break;

            case "TABLE":
                if (onRefresh == null)
                    break;

                onRefresh.accept(parseTables(message));
                break;

            case "CREATED":
            case "JOINED":
                if (onJoinGame == null)
                    break;

                onJoinGame.accept(Session.start(ClientSession.class, connection));
                break;

            case "NO":
                if (onError == null)
                    break;

                onError.run();
                break;
        }
    }

    private List<TableEntry> parseTables(String[] message) {
        int i = 0;
        UUID uuid = null;
        String name = null;
        int currentPlayers = -1;
        int maxPlayers = -1;

        List<TableEntry> entries = new ArrayList<>();

        for (String messagePart : message) {

            if (i % 5 == 1) {
                uuid = UUID.fromString(messagePart);
            } else if (i % 5 == 2) {
                name = messagePart;
            } else if (i % 5 == 3) {
                currentPlayers = Integer.valueOf(messagePart);
            } else if (i % 5 == 4) {
                maxPlayers = Integer.valueOf(messagePart);
                entries.add(new TableEntry(uuid, name, currentPlayers, maxPlayers));
            }

            i++;
        }

        return entries;
    }

    
    public void requestCreateGame(List<String> args) {
        try {
            final String[] message = {"CREATE"};
            args.forEach(arg -> message[0] += ";" + arg);
            message[0] += "\r\n";

            connection.sendMessage(message[0]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Asks server if you can join table with specific uuid
     *
     * @param uuid
     */
    public void requestJoinTable(UUID uuid) throws IOException {
        connection.sendMessage("JOIN TABLE;" + uuid + "\r\n");
    }

    public void refresh() throws IOException {
        connection.sendMessage("SEARCH" + "\r\n");
    }

    public Consumer<SessionContext> getOnJoinGame() {
        return onJoinGame;
    }

    public void setOnJoinGame(Consumer<SessionContext> onJoinGame) {
        this.onJoinGame = onJoinGame;
    }

    public Consumer<List<TableEntry>> getOnRefresh() {
        return onRefresh;
    }

    public void setOnRefresh(Consumer<List<TableEntry>> onRefresh) {
        this.onRefresh = onRefresh;
    }

    public Runnable getOnError() {
        return onError;
    }

    public void setOnError(Runnable onError) {
        this.onError = onError;
    }
}
