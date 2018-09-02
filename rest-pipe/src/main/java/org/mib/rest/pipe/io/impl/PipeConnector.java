package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeInputProvider;
import org.mib.rest.pipe.io.PipeOutputConsumer;

import java.util.concurrent.atomic.AtomicLong;

import static org.mib.common.validator.Validator.validateObjectNotNull;

public class PipeConnector<I, O> implements PipeOutputConsumer<I, O> {

    private final PipeOutputConsumer<I, O> consumer;
    private final PipeInputProvider<O> provider;
    private final AtomicLong consumed;

    public PipeConnector(final PipeOutputConsumer<I, O> consumer, final PipeInputProvider<O> provider) {
        validateObjectNotNull(consumer, "previous pipe output consumer");
        validateObjectNotNull(provider, "next pipe input provider");
        this.consumer = consumer;
        this.provider = provider;
        this.consumed = new AtomicLong(0);
    }

    @Override
    public O consume(I payload) {
        O out = consumer.consume(payload);
        consumed.incrementAndGet();
        if (out != null) provider.offer(out);
        return out;
    }

    @Override
    public long consumed() {
        return consumed.get();
    }
}
