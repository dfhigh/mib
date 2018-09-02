package org.mib.rest.pipe.io;

/**
 * Created by dufei on 18/5/10.
 */
public interface PipeOutputConsumer<I, O> {

    O consume(I payload);

    long consumed();
}
