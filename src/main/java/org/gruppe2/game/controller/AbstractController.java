package org.gruppe2.game.controller;

import org.gruppe2.game.event.Event;
import org.gruppe2.game.session.Session;
import org.gruppe2.game.session.SessionContext;
import org.gruppe2.game.session.TimerTask;

abstract class AbstractController  implements Controller {
    private Session session;

    @Override
    public void init() {

    }

    @Override
    public void update() {

    }

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    Session getSession() {
        return session;
    }

    SessionContext getContext() {
        return session.getContext();
    }

    void addEvent(Event event) {
        getSession().addEvent(event);
    }

    TimerTask setTask(long ms, Runnable runnable) {
        return getSession().setTask(ms, runnable);
    }

    void cancelTask(TimerTask task) {
        getSession().cancelTask(task);
    }

    <M> M getModel(Class<M> klass) {
        return getContext().getModel(klass);
    }
}
