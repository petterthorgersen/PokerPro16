package org.gruppe2.game.session;

import java.io.Serializable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Query<T> implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2750291921582809866L;

	private enum State { WAIT, DONE, CANCELLED }

    private volatile State state = State.WAIT;
    private volatile T obj = null;

    public boolean cancel() {
        if (state != State.WAIT)
            return false;

        state = State.CANCELLED;
        return true;
    }

    public boolean isCancelled() {
        return state == State.CANCELLED;
    }

    public boolean isDone() {
        return state == State.DONE;
    }

    public T get() {
        while (state == State.WAIT) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (state == State.DONE)
            return obj;

        if (state == State.CANCELLED)
            throw new CancellationException();

        return null;
    }

    public T get(long timeout, TimeUnit unit) throws TimeoutException {
        long msUnit = unit.toMillis(timeout);

        for (int i = 0; state == State.WAIT && i < msUnit; i += 10) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }
        }

        if (state == State.WAIT)
            throw new TimeoutException();

        return get();
    }

    public synchronized void set(T obj) {
        switch (state) {
            case WAIT:
                this.obj = obj;
                state = State.DONE;
                break;

            case CANCELLED:
            case DONE:
                break;
        }
    }

    public synchronized void reset() {
        state = State.WAIT;
        obj = null;
    }
}
