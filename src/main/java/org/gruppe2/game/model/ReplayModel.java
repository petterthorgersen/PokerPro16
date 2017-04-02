package org.gruppe2.game.model;

import java.io.InputStream;

public class ReplayModel {
    private final InputStream stream;

    public ReplayModel(InputStream stream) {
        this.stream = stream;
    }

    public InputStream getStream() {
        return stream;
    }
}
