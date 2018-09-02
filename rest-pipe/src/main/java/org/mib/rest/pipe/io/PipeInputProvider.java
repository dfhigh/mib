package org.mib.rest.pipe.io;

import java.io.Closeable;

/**
 * Created by dufei on 18/5/10.
 */
public interface PipeInputProvider<T> extends Closeable {

    void offer(T payload);

    T take();

    boolean isClosed();

    long provided();
}
