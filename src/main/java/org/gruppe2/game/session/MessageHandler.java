package org.gruppe2.game.session;

import java.lang.reflect.InvocationTargetException;

public interface MessageHandler {
    boolean call(Object... args) throws IllegalAccessException, InvocationTargetException;
}
