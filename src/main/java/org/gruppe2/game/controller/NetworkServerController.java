package org.gruppe2.game.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.gruppe2.game.Action;
import org.gruppe2.game.Player;
import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.event.ChatEvent;
import org.gruppe2.game.event.Event;
import org.gruppe2.game.event.PlayerActionQuery;
import org.gruppe2.game.event.PlayerLeaveEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.helper.GameHelper;
import org.gruppe2.game.helper.RoundHelper;
import org.gruppe2.game.session.Handler;
import org.gruppe2.game.session.Helper;
import org.gruppe2.game.session.Message;
import org.gruppe2.game.session.Query;
import org.gruppe2.network.ConnectedClient;
import org.gruppe2.network.NetworkIO;

public class NetworkServerController extends AbstractController {
	private ServerSocketChannel serverSocket;
	private ArrayList<ConnectedClient> clients = new ArrayList<>();

	@Helper
	private GameHelper gameHelper;
	@Helper
	private RoundHelper roundHelper;

	private Query<Action> action = null;

	@Handler
	private void handleEvent(Event e) {
		if (e instanceof RoundStartEvent) {
			broadcastObject(gameHelper.getModel());
			broadcastObject(roundHelper.getModel());
			broadcastObject(e);
		} else if (!(e instanceof PlayerActionQuery)) {
			broadcastObject(e);
		} else {
			PlayerActionQuery query = (PlayerActionQuery) e;
			clients.stream()
					.filter(c -> query.getPlayer().getUUID()
							.equals(c.getPlayerUUID()))
					.findFirst()
					.ifPresent(
							c -> {
								try {
									c.getConnection().sendObject(gameHelper.getModel());
									c.getConnection().sendObject(roundHelper.getModel());
									c.getConnection().sendObject(e);
									action = ((PlayerActionQuery) e)
											.getPlayer().getAction();
								} catch (IOException e1) {
								}
							});
		}
	}

	@Override
	public void update() {
		if (serverSocket != null) {
			try {
				SocketChannel client = serverSocket.accept();
				if (client != null) {
					NetworkIO connection = new NetworkIO(client);
					client.configureBlocking(false);
					addClient(connection);
				}

			} catch (IOException e) {

			}
		}

		for (int i = 0; i < clients.size(); i++) {
			try {
				String[] args = clients.get(i).getConnection().readMessage();

				if (args == null)
					continue;

				UUID uuid;
				switch (args[0]) {
				case "SAY":
					uuid = clients.get(i).getPlayerUUID();
					if (uuid == null)
						continue;

					broadcastObject(new ChatEvent(args[1], uuid));
					break;
				case "JOIN":
					clients.get(i).setPlayerUUID(UUID.fromString(args[1]));
					getContext().message("addPlayer",
							clients.get(i).getPlayerUUID(), args[3], args[2]);
					break;
				case "KICK":
					getContext()
							.message("kickPlayer", UUID.fromString(args[1]));
				case "DISCONNECT":
					uuid = clients.get(i).getPlayerUUID();
					Optional<Player> player = gameHelper.findPlayerByUUID(uuid);

					if (player.isPresent()) {
						getContext().message("kickPlayer",
								player.get().getUUID());
						Optional<RoundPlayer> roundPlayer = roundHelper
								.findPlayerByUUID(uuid);

						if (roundPlayer.isPresent())
							roundHelper.getActivePlayers().remove(
									roundPlayer.get());

						addEvent(new PlayerLeaveEvent(player.get()));
					}

					clients.remove(i--);
					break;
				case "ACTION":

					uuid = clients.get(i).getPlayerUUID();

					setPlayerActionFromMessage(uuid, args);
				}
			} catch (IOException e) {
				if (clients.get(i).getPlayerUUID() != null) {
					getContext().message("kickPlayer",
							clients.get(i).getPlayerUUID());
				}

				clients.remove(i--);
			}
		}
	}

	private void setPlayerActionFromMessage(UUID uuid, String[] args) {
		if (action != null) {
			switch (args[1]) {
			case "Call":
				action.set(new Action.Call());
				break;

			case "Check":
				action.set(new Action.Check());
				break;

			case "Fold":
				action.set(new Action.Fold());
				break;

			case "Raise":
				action.set(new Action.Raise(Integer.valueOf(args[2])));
				break;
			}

			action = null;
		}
	}

	@Message
	public void listen() {
		try {
			serverSocket = ServerSocketChannel.open();
			serverSocket.socket().bind(new InetSocketAddress(8888));
			serverSocket.configureBlocking(false);
		} catch (IOException ignore) {
		}
	}

	@Message
	public void addClient(NetworkIO connection) {
		clients.add(new ConnectedClient(connection));

		try {
			connection.setInputFormat(NetworkIO.Format.STRING);
			connection.setOutputFormat(NetworkIO.Format.OBJECT);
			connection.setPing(true);

			connection.sendObject(gameHelper.getModel());
			connection.sendObject(roundHelper.getModel());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	private void broadcastObject(Object object) {
		for (int i = 0; i < clients.size(); i++) {
			try {
				clients.get(i).getConnection().sendObject(object);
			} catch (IOException e) {
				if (clients.get(i).getPlayerUUID() != null) {
					getContext().message("kickPlayer",
							clients.get(i).getPlayerUUID());
				}
				clients.remove(i--);
			}
		}
	}
}
