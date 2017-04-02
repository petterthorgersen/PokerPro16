package org.gruppe2.ai;

import org.gruppe2.game.RoundPlayer;
import org.gruppe2.game.session.SessionContext;

public interface AIEvaluate {
    double evaluate(SessionContext context, RoundPlayer player);
}
