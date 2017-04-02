package org.gruppe2.game.session;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.gruppe2.game.event.Event;

/**
 * The context to interface with {@link Session}.
 * This is created for every thread connected to a Session instance.
 */
public class SessionContext {
    private final Session session;

    private final ConcurrentEventQueue eventQueue = new ConcurrentEventQueue();

    /**
     * Connect self to a session and add our queue to its {@link SessionEventQueue}
     * @param session a reference to a running {@link Session} instance
     */
    SessionContext(Session session) {
        this.session = session;

        session.getEventQueue().addQueue(eventQueue);
    }

    /**
     * Get this context's event queue
     * @return event queue
     */
    public ConcurrentEventQueue getEventQueue() {
        return eventQueue;
    }

    @SuppressWarnings("unchecked")
	public <M> M getModel(Class<M> klass) {
        return (M) session.getModel(klass);
    }

    public Query<Boolean> sendMessage(String name, Object... args) {
        return session.sendMessage(name, args);
    }

    public Query<Boolean> message(String name, Object... args) {
        return session.sendMessage(name, args);
    }

    /**
     * Send a message to Session to quit
     */
    public void quit() {
        message("quit");
    }

    /**
     * Wait for the session to start.
     */
    public void waitReady() {
        while (!session.isReady()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAnnotated(Object obj) {
        setAnnotatedModels(obj);
        setAnnotatedHelpers(obj);
        setAnnotatedHandlers(obj);
    }

    public void setAnnotatedModels(Object obj) {
        for (Field f : obj.getClass().getDeclaredFields()) {
            if (f.getAnnotation(Model.class) == null)
                continue;

            try {

                f.setAccessible(true);
                f.set(obj, getModel(f.getType()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAnnotatedHelpers(Object obj) {
        for (Field f : obj.getClass().getDeclaredFields()) {
            if (f.getAnnotation(Helper.class) == null)
                continue;

            try {
                Class<?> type = f.getType();
                Constructor<?> ctor = type.getConstructor(SessionContext.class);

                f.setAccessible(true);
                f.set(obj, ctor.newInstance(this));
            } catch (NoSuchMethodException e) {
                System.err.printf("Field %s in %s: must be a helper\n", f.getName(), obj.getClass().getSimpleName());
                e.printStackTrace();
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public void setAnnotatedHandlers(Object obj) {
        for (Method m : obj.getClass().getDeclaredMethods()) {
            if (m.getAnnotation(Handler.class) == null)
                continue;

            if (m.getParameterCount() != 1)
                throw new RuntimeException("Handler " + m.getName() + " can only take one argument");

            if (!m.getReturnType().equals(Void.TYPE))
                throw new RuntimeException(String.format("Handler %s return type: expected void, got %s", m.getName(), m.getReturnType()));

            Class<?> eventClass = m.getParameterTypes()[0];

            if (eventClass.isInstance(Event.class))
                throw new RuntimeException("Handler " + m.getName() + " must handle an Event");

            m.setAccessible(true);

            getEventQueue().registerHandler(eventClass, (Event event) -> m.invoke(obj, event));
        }
    }

    public Thread getThread() {
        return session.getThread();
    }

    /**
     * Create a brand new context for use in new threads
     *
     * @return A
     */
    public SessionContext createContext() {
        return new SessionContext(session);
    }
}
