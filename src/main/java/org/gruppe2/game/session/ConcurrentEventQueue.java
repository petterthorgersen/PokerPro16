package org.gruppe2.game.session;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.gruppe2.game.event.Event;
import org.gruppe2.game.event.EventHandler;
import org.gruppe2.game.event.QuitEvent;

/**
 *
 */
public class ConcurrentEventQueue {
    private final ConcurrentLinkedQueue<Event> eventQueue = new ConcurrentLinkedQueue<>();

    /* Adding handlers only happens on a single thread, so we don't care about thread safety here */
    private final Map<Class<?>, List<EventHandler<Event>>> handlerMap = new HashMap<>();

    /**
     * Register a new EventHandler to handle certain events
     *
     * @param klass The Class object of a class that implements Event. ex: PlayerJoinEvent.class
     * @param handler handler which will be called when the event occurs
     */
    public void registerHandler(Class<?> klass, EventHandler<Event> handler) {
        List<EventHandler<Event>> list = handlerMap.get(klass);

        if (list == null) {
            list = new ArrayList<>();
            handlerMap.put(klass, list);
        }

        list.add(handler);
    }

    /**
     * Add an event to be processed later
     * @param event event to be added
     */
    void addEvent(Event event) {
        eventQueue.add(event);
    }

    /**
     * Iterate over new events and send them out to their respective handlers
     */
    public void process() {
        List<EventHandler<Event>> genericList = handlerMap.get(Event.class);
        Event event;

        while ((event = eventQueue.poll()) != null) {
            List<EventHandler<Event>> list = handlerMap.get(event.getClass());

            if (event instanceof QuitEvent)
                eventQueue.clear();

            if (genericList != null) {
                for (EventHandler<Event> handler : genericList)
                    handle(handler, event);
            }

            if (list != null) {
                for (EventHandler<Event> handler : list)
                    handle(handler, event);
            }
        }
    }

    /**
     * @return number of pending events
     */
    int size() {
        return eventQueue.size();
    }

    private void handle(EventHandler<Event> handler, Event event) {
        try {
            handler.handle(event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            System.out.println(e.getTargetException().toString());
            e.printStackTrace();
        }
    }
}