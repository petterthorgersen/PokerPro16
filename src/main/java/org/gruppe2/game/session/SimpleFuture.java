package org.gruppe2.game.session;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SimpleFuture<T> implements Future<T> {
    private enum State { WAIT, DONE, CANCELLED }

    private volatile State state = State.WAIT;
    private volatile T obj = null;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        if (state != State.WAIT)
            return false;

        state = State.CANCELLED;
        return true;
    }

    @Override
    public boolean isCancelled() {
        return state == State.CANCELLED;
    }

    @Override
    public boolean isDone() {
        return state == State.DONE;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        while (state == State.WAIT)
            Thread.sleep(10);

        if (state == State.DONE)
            return obj;

        if (state == State.CANCELLED)
            throw new CancellationException();

        return null;
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        long msUnit = unit.toMillis(timeout);

        for (int i = 0; state == State.WAIT && i < msUnit; i += 10) {
            Thread.sleep(10);
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

            case DONE:
                break;

            case CANCELLED:
                throw new CancellationException();
        }
    }
}
