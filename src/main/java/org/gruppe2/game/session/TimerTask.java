package org.gruppe2.game.session;

public class TimerTask {
    private final long delay;
    private final Runnable task;

    public TimerTask(long delay, Runnable task) {
        this.delay = delay;
        this.task = task;
    }

    public long getExpireTimeMs() {
        return delay;
    }

    public void run() {
        task.run();
    }
}
