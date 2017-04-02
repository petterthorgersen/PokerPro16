package org.gruppe2.game.session;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import org.gruppe2.game.controller.ClientRoundController;
import org.gruppe2.game.controller.ReplayController;
import org.gruppe2.game.controller.StatisticsController;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.ReplayModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.model.StatisticsModel;

public class ReplaySession extends Session {

    public ReplaySession(String file) {
        try {
            Object object;

            InputStream stream = new FileInputStream(file);
            addModel(new ReplayModel(stream));

            if (!((object = readObject(stream)) instanceof GameModel))
                throw new RuntimeException();

            addModel((GameModel) object);

            if (!((object = readObject(stream)) instanceof  RoundModel))
                throw new RuntimeException();

            addModel((RoundModel) object);

            addModel(new StatisticsModel());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void init() {
        addController(ReplayController.class);
        addController(ClientRoundController.class);
        addController(StatisticsController.class);
    }

    @Override
    public void update() {

    }

    public static Object readObject(InputStream stream) {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(stream);

            return objectInputStream.readObject();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
