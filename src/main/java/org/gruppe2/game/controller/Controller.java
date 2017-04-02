package org.gruppe2.game.controller;

import org.gruppe2.game.session.Session;

public interface Controller {
    void setSession(Session session);

    void init();
    void update();
}