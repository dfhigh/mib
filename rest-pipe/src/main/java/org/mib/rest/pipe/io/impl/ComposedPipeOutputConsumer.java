package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeOutputConsumer;
import com.google.common.collect.ImmutableList;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.mib.rest.utils.Validator.validateCollectionNotEmptyContainsNoNull;

public class ComposedPipeOutputConsumer<I, O> implements PipeOutputConsumer<I, O> {

    private final List<PipeOutputConsumer<I, O>> consumers;
    private final AtomicLong consumed;

    public ComposedPipeOutputConsumer(final List<PipeOutputConsumer<I, O>> consumers) {
        validateCollectionNotEmptyContainsNoNull(consumers, "consumer list");
        this.consumers = ImmutableList.copyOf(consumers);
        this.consumed = new AtomicLong(0);
    }

    @Override
    public O consume(I payload) {
        O out = null;
        for (PipeOutputConsumer<I, O> consumer : consumers) out = consumer.consume(payload);
        consumed.incrementAndGet();
        return out;
    }

    @Override
    public long consumed() {
        return consumed.get();
    }
}
