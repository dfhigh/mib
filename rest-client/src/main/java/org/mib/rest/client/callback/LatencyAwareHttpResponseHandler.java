package org.mib.rest.client.callback;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.util.function.Consumer;

import static org.mib.rest.utils.ResponseInterceptor.REQUEST_LATENCY_KEY;
import static org.mib.rest.utils.Validator.validateObjectNotNull;

public class LatencyAwareHttpResponseHandler<T> extends HttpResponseHandler<T> {

    private final HttpResponseHandler<T> handler;
    private final Consumer<Long> latencyConsumer;

    public LatencyAwareHttpResponseHandler(HttpResponseHandler<T> handler, Consumer<Long> latencyConsumer) {
        validateObjectNotNull(handler, "inner handler");
        validateObjectNotNull(latencyConsumer, "latency consumer");
        this.handler = handler;
        this.latencyConsumer = latencyConsumer;
    }

    @Override
    protected T convert(HttpResponse response) {
        Header latency = response.getFirstHeader(REQUEST_LATENCY_KEY);
        if (latency != null) latencyConsumer.accept(Long.parseLong(latency.getValue()));
        return handler.convert(response);
    }

    @Override
    protected void onSuccess(T payload) {
        handler.onSuccess(payload);
    }
}
