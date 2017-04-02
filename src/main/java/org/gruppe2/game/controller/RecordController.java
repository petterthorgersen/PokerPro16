package org.gruppe2.game.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;

import org.gruppe2.Resources;
import org.gruppe2.game.event.Event;
import org.gruppe2.game.event.QuitEvent;
import org.gruppe2.game.event.RoundStartEvent;
import org.gruppe2.game.model.GameModel;
import org.gruppe2.game.model.RoundModel;
import org.gruppe2.game.session.Handler;

public class RecordController extends AbstractController {
    private OutputStream stream = null;

    public static class Wait implements Serializable {
        private static final long serialVersionUID = -8263959860922386464L;

        private final long time;

        private Wait(long time) {
            this.time = time;
        }

        public long getTime() {
            return time;
        }
    }

    private long lastEventTime = -1L;

    @Override
    public void init() {
        try {
            stream = new FileOutputStream(Resources.getUserDir("replays") + LocalDateTime.now().toString() + ".pp16");

            writeObject(getModel(GameModel.class));
            writeObject(getModel(RoundModel.class));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Handler
    private void onEvent(Event event) {
        if (stream == null)
            return;

        if (lastEventTime < 0) {
            lastEventTime = System.currentTimeMillis();
        } else if (System.currentTimeMillis() - lastEventTime > 100) {
            writeObject(new Wait(System.currentTimeMillis() - lastEventTime));
            lastEventTime = System.currentTimeMillis();
        }

        if (event instanceof RoundStartEvent) {
            writeObject(getModel(GameModel.class));
            writeObject(getModel(RoundModel.class));
        }
        writeObject(event);
    }

    @Handler
    private void onQuit(QuitEvent event) {
        if (stream == null)
            return;

        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeObject(Object object) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(object);

            byte[] bytes = byteArrayOutputStream.toByteArray();

            stream.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
