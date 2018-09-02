package org.mib.rest.pipe.io.impl;

import org.mib.rest.client.HttpOperator;
import org.mib.rest.client.SyncHttpOperator;
import org.mib.rest.client.callback.HttpResponseHandler;
import org.mib.rest.pipe.io.PipeOutputConsumer;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.util.concurrent.atomic.AtomicLong;

import static org.mib.rest.utils.ResponseInterceptor.REQUEST_LATENCY_KEY;
import static org.mib.rest.utils.Validator.validateIntPositive;
import static org.mib.rest.utils.Validator.validateObjectNotNull;

@Slf4j
public class SyncRestExecutor<R extends HttpUriRequest, T> implements PipeOutputConsumer<R, Void> {

    private final HttpOperator http;
    private final HttpResponseHandler<T> handler;
    private final AtomicLong consumed;

    public SyncRestExecutor(final HttpOperator http, final HttpResponseHandler<T> handler) {
        validateObjectNotNull(http, "http operator");
        validateObjectNotNull(handler, "response handler");
        this.http = http;
        this.handler = handler;
        this.consumed = new AtomicLong(0);
    }

    public SyncRestExecutor(final int maxConn, final int maxConnPerRoute, final HttpResponseHandler<T> handler) {
        validateIntPositive(maxConn, "max connection");
        validateIntPositive(maxConnPerRoute, "max connection per route");
        validateObjectNotNull(handler, "response handler");
        this.http = new SyncHttpOperator(maxConn, maxConnPerRoute);
        this.handler = handler;
        this.consumed = new AtomicLong(0);
    }

    @Override
    public Void consume(R payload) {
        HttpResponse response = null;
        try {
            long start = System.currentTimeMillis();
            response = http.executeHttp(payload);
            response.setHeader(REQUEST_LATENCY_KEY, String.valueOf(System.currentTimeMillis()-start));
            handler.completed(response);
        } catch (Exception e) {
            handler.failed(e);
        } finally {
            consumed.incrementAndGet();
            if (response != null) EntityUtils.consumeQuietly(response.getEntity());
        }
        return null;
    }

    @Override
    public long consumed() {
        return consumed.get();
    }
}
