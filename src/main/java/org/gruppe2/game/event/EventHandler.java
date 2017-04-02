package org.gruppe2.game.event;

import java.lang.reflect.InvocationTargetException;

public interface EventHandler<T extends Event> {
    void handle(T event) throws IllegalAccessException, InvocationTargetException;
}
