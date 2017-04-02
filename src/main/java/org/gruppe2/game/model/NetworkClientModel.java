package org.gruppe2.game.model;

import org.gruppe2.network.NetworkIO;

public class NetworkClientModel {
    private final NetworkIO connection;

    public NetworkClientModel(NetworkIO connection) {
        this.connection = connection;
    }

    public NetworkIO getConnection() {
        return connection;
    }
}
