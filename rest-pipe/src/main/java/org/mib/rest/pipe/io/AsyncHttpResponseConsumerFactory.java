package org.mib.rest.pipe.io;

import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

public interface AsyncHttpResponseConsumerFactory<I, O> {

    HttpAsyncResponseConsumer<O> createConsumer(I input);
}
