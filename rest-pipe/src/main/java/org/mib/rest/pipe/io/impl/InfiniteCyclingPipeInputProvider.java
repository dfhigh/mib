package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeInputProvider;

import java.io.IOException;

import static org.mib.rest.utils.Validator.validateObjectNotNull;

public class InfiniteCyclingPipeInputProvider<T> implements PipeInputProvider<T> {

    private final PipeInputProvider<T> internal;

    public InfiniteCyclingPipeInputProvider(final PipeInputProvider<T> internal) {
        validateObjectNotNull(internal, "internal pip");
        this.internal = internal;
    }

    @Override
    public void offer(T payload) {
        internal.offer(payload);
    }

    @Override
    public T take() {
        T payload = internal.take();
        if (payload != null) internal.offer(payload);
        return payload;
    }

    @Override
    public boolean isClosed() {
        return false;
    }

    @Override
    public long provided() {
        return internal.provided();
    }

    @Override
    public void close() throws IOException {
        // it can't be closed, so do nothing
    }
}
