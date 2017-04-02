package org.gruppe2.game.event;

public class QuitEvent implements Event {
    private static final long serialVersionUID = 6702154227211023489L;

    private final String reason;

    public QuitEvent(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }
}
