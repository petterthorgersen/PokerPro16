package org.gruppe2.game.session;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.gruppe2.game.Player;
import org.gruppe2.game.controller.ClientRoundController;
import org.gruppe2.game.controller.NetworkClientController;
import org.gruppe2.game.controller.RecordController;
import org.gruppe2.game.controller.StatisticsController;
import org.gruppe2.game.model.ChatModel;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.NetworkClientModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.model.StatisticsModel;
import org.gruppe2.network.NetworkIO;

public class ClientSession extends Session {

    public ClientSession(String ip) {
        connect(ip);

        addModel(new ChatModel());
        addModel(new StatisticsModel());
    }
    
    public ClientSession(NetworkIO connection) {
    	try {
			waitForSync(connection);
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        addModel(new ChatModel());
        addModel(new StatisticsModel());
    }

    @Override
    public void init() {
        addController(NetworkClientController.class);
        addController(ClientRoundController.class);
        addController(StatisticsController.class);
        addController(RecordController.class);
    }

    @Override
    public void update() {

    }

    private void connect(String ip) {
        try {
            SocketChannel channel = SocketChannel.open(new InetSocketAddress(ip, 8888));
            NetworkIO connection = new NetworkIO(channel);
            channel.configureBlocking(false);
            
            waitForSync(connection);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    private void waitForSync(NetworkIO connection) throws IOException, ClassNotFoundException {
        connection.setInputFormat(NetworkIO.Format.OBJECT);
        connection.setOutputFormat(NetworkIO.Format.STRING);

        int numSyncs = 0;
        while (numSyncs < 2) {
            Object object = connection.readObject();

            if (object == null)
                continue;

            if (object instanceof GameModel || object instanceof RoundModel) {
                addModel(object);
                numSyncs++;
            }
        }

        addModel(new NetworkClientModel(connection));

        GameModel gameModel = (GameModel) getModel(GameModel.class);

        for (Player p : gameModel.getPlayers()) {
            System.out.printf("Player: %s (%d)\n", p.getName(), p.getTablePosition());
        }
    }
}
