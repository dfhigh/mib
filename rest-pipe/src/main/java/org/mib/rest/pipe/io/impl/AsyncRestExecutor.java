package org.mib.rest.pipe.io.impl;

import org.apache.commons.lang3.StringUtils;
import org.mib.rest.client.AsyncHttpOperator;
import org.mib.rest.client.callback.HttpResponseHandler;
import org.mib.rest.context.IterableHttpContext;
import org.mib.rest.pipe.io.AsyncHttpResponseConsumerFactory;
import org.mib.rest.pipe.io.PipeOutputConsumer;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.nio.protocol.BasicAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import java.util.concurrent.atomic.AtomicLong;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.rest.utils.ResponseInterceptor.REQUEST_TIME_KEY;

public class AsyncRestExecutor<R extends HttpUriRequest, T> implements PipeOutputConsumer<R, Void> {

    private final AsyncHttpOperator http;
    private final HttpResponseHandler<T> handler;
    private final AsyncHttpResponseConsumerFactory<R, HttpResponse> factory;
    private final AtomicLong consumed;
    private final String cookie;

    public AsyncRestExecutor(final AsyncHttpOperator http, final HttpResponseHandler<T> handler) {
        this(http, handler, t -> null);
    }

    public AsyncRestExecutor(final AsyncHttpOperator http, final HttpResponseHandler<T> handler, final String cookie) {
        this(http, handler, t -> null, cookie);
    }

    public AsyncRestExecutor(final AsyncHttpOperator http, final HttpResponseHandler<T> handler,
                             final AsyncHttpResponseConsumerFactory<R, HttpResponse> factory) {
        this(http, handler, factory, null);
    }

    public AsyncRestExecutor(final AsyncHttpOperator http, final HttpResponseHandler<T> handler,
                             final AsyncHttpResponseConsumerFactory<R, HttpResponse> factory, final String cookie) {
        validateObjectNotNull(http, "http operator");
        validateObjectNotNull(handler, "response handler");
        validateObjectNotNull(factory, "consumer factory");
        this.http = http;
        this.handler = handler;
        this.factory = factory;
        this.consumed = new AtomicLong(0);
        this.cookie = cookie;
    }

    public AsyncRestExecutor(final int maxConn, final int maxConnPerRoute, final HttpResponseHandler<T> handler) {
        this(maxConn, maxConnPerRoute, handler, t -> null);
    }

    public AsyncRestExecutor(final int maxConn, final int maxConnPerRoute, final HttpResponseHandler<T> handler, final String cookie) {
        this(maxConn, maxConnPerRoute, handler, t -> null, cookie);
    }

    public AsyncRestExecutor(final int maxConn, final int maxConnPerRoute, final HttpResponseHandler<T> handler,
                             final AsyncHttpResponseConsumerFactory<R, HttpResponse> factory) {
        this(maxConn, maxConnPerRoute, handler, factory, null);
    }

    public AsyncRestExecutor(final int maxConn, final int maxConnPerRoute, final HttpResponseHandler<T> handler,
                             final AsyncHttpResponseConsumerFactory<R, HttpResponse> factory, final String cookie) {
        validateIntPositive(maxConn, "max connection");
        validateIntPositive(maxConnPerRoute, "max connection per route");
        validateObjectNotNull(handler, "response handler");
        validateObjectNotNull(factory, "consumer supplier");
        this.http = new AsyncHttpOperator(maxConn, maxConnPerRoute);
        this.handler = handler;
        this.factory = factory;
        this.consumed = new AtomicLong(0);
        this.cookie = cookie;
    }

    @Override
    public Void consume(R payload) {
        if (StringUtils.isNotBlank(cookie)) payload.addHeader("Cookie", cookie);
        HttpAsyncResponseConsumer<HttpResponse> consumer = factory.createConsumer(payload);
        if (consumer == null) {
            http.executeHttp(payload, handler);
        } else {
            HttpHost host = URIUtils.extractHost(payload.getURI());
            http.executeHttp(new BasicAsyncRequestProducer(host, payload) {
                @Override
                public void requestCompleted(final HttpContext context) {
                    if (context != null) context.setAttribute(REQUEST_TIME_KEY, System.currentTimeMillis());
                }
            }, consumer, new IterableHttpContext(), handler);
        }
        consumed.incrementAndGet();
        return null;
    }

    @Override
    public long consumed() {
        return consumed.get();
    }
}
