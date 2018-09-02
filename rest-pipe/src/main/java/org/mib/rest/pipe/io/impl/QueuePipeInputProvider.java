package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeInputProvider;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.mib.rest.utils.Validator.validateIntPositive;
import static org.mib.rest.utils.Validator.validateObjectNotNull;

public class QueuePipeInputProvider<T> implements PipeInputProvider<T> {

    private final BlockingQueue<T> queue;
    private final AtomicLong provided;
    private final AtomicBoolean closed;

    public QueuePipeInputProvider(final int capacity) {
        validateIntPositive(capacity, "queue capacity");
        this.queue = new ArrayBlockingQueue<>(capacity);
        this.closed = new AtomicBoolean(false);
        this.provided = new AtomicLong(0);
    }

    public QueuePipeInputProvider(final BlockingQueue<T> queue) {
        validateObjectNotNull(queue, "queue");
        this.queue = queue;
        this.closed = new AtomicBoolean(false);
        this.provided = new AtomicLong(0);
    }

    @Override
    public void offer(T payload) {
        if (isClosed()) {
            throw new IllegalStateException("can't offer to a closed pipe");
        }
        while (!queue.offer(payload));
        provided.incrementAndGet();
    }

    @Override
    public T take() {
        return queue.poll();
    }

    @Override
    public boolean isClosed() {
        return closed.get() && queue.isEmpty();
    }

    @Override
    public long provided() {
        return provided.get();
    }

    @Override
    public void close() throws IOException {
        closed.compareAndSet(false, true);
    }
}
