package org.mib.rest.pipe.io.impl;

import org.mib.rest.pipe.io.PipeInputProvider;
import com.conversantmedia.util.concurrent.DisruptorBlockingQueue;

import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.BlockingQueue;

import static org.mib.rest.utils.Validator.validateCollectionNotEmptyContainsNoNull;

public class StaticCollectionInputProvider<T> implements PipeInputProvider<T> {

    private final BlockingQueue<T> queue;
    private final long provided;

    public StaticCollectionInputProvider(final Collection<T> collection) {
        validateCollectionNotEmptyContainsNoNull(collection, "collection");
        this.queue = new DisruptorBlockingQueue<>(collection.size(), collection);
        this.provided = collection.size();
    }

    @Override
    public void offer(T payload) {
        throw new UnsupportedOperationException("could not offer to a static collection input provider");
    }

    @Override
    public T take() {
        return queue.poll();
    }

    @Override
    public boolean isClosed() {
        return queue.isEmpty();
    }

    @Override
    public long provided() {
        return provided;
    }

    @Override
    public void close() throws IOException {
        queue.clear();
    }
}
