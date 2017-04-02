package org.gruppe2.network;

import java.util.UUID;

public class ConnectedClient {
    private final NetworkIO connection;
    private UUID playerUUID = null;

    public ConnectedClient(NetworkIO connection) {
        this.connection = connection;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public NetworkIO getConnection() {
        return connection;
    }
}
