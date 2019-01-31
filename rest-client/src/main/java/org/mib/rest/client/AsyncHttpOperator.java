package org.mib.rest.client;

import org.mib.rest.client.callback.HttpResponseHandler;
import org.mib.rest.client.callback.RequestAwareAsyncResponseConsumer;
import org.mib.rest.context.IterableHttpContext;
import org.mib.rest.context.RestContext;
import org.mib.rest.context.RestScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.impl.nio.reactor.IOReactorConfig;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.nio.protocol.HttpAsyncResponseConsumer;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import static org.mib.common.ser.Serdes.serializeAsJsonString;
import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.rest.utils.ResponseInterceptor.REQUEST_KEY;
import static org.mib.rest.utils.ResponseInterceptor.REQUEST_TIME_KEY;
import static org.mib.rest.utils.ResponseInterceptor.intercept;

/**
 * Created by dufei on 18/5/8.
 */
@Slf4j
@ThreadSafe
public class AsyncHttpOperator extends HttpOperator {

    private static final int TWO_MINS_IN_MILLI = 120000;

    private static final FutureCallback<HttpResponse> SYNC_CB = new FutureCallback<HttpResponse>() {
        @Override
        public void completed(HttpResponse result) {
            // do nothing
        }

        @Override
        public void failed(Exception ex) {
            log.error("failed to execute http", ex);
            throw new RuntimeException(ex);
        }

        @Override
        public void cancelled() {
            // we shouldn't get here
            throw new IllegalStateException("request cancelled");
        }
    };

    private final CloseableHttpAsyncClient http;

    public AsyncHttpOperator() {
        this(DEFAULT_CONN, DEFAULT_CONN_PER_ROUTE);
    }

    public AsyncHttpOperator(final int maxConn, final int maxConnPerRoute) {
        validateIntPositive(maxConn, "maxConn");
        validateIntPositive(maxConnPerRoute, "maxConnPerRoute");
        IOReactorConfig reactorConfig = IOReactorConfig.custom().setConnectTimeout(TWO_MINS_IN_MILLI).setSoTimeout(TWO_MINS_IN_MILLI).build();
        this.http = HttpAsyncClients.custom().setMaxConnTotal(maxConn).setMaxConnPerRoute(maxConnPerRoute).setDefaultIOReactorConfig(reactorConfig).build();
        http.start();
    }

    public <T> void getAsync(String url, Map<String, String> parameters, Map<String, String> headers, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpGet.METHOD_NAME, url, parameters, headers, null, timeoutMillis, handler);
    }

    public <T> void postAsync(String url, Map<String, String> parameters, Map<String, String> headers, Object payload, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, handler);
    }

    public <T> void postAsync(String url, Map<String, String> parameters, Map<String, String> headers, InputStream is, String key, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, handler);
    }

    public <T> void putAsync(String url, Map<String, String> parameters, Map<String, String> headers, Object payload, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, handler);
    }

    public <T> void putAsync(String url, Map<String, String> parameters, Map<String, String> headers, InputStream is, String key, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, handler);
    }

    public <T> void patchAsync(String url, Map<String, String> parameters, Map<String, String> headers, Object payload, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, handler);
    }

    public <T> void patchAsync(String url, Map<String, String> parameters, Map<String, String> headers, InputStream is, String key, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, handler);
    }

    public <T> void deleteAsync(String url, Map<String, String> parameters, Map<String, String> headers, Object payload, int timeoutMillis, HttpResponseHandler<T> handler) {
        executeHttp(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, handler);
    }

    private void executeHttp(String method, String url, Map<String, String> parameters, Map<String, String> headers, Object payload, int timeoutMillis, FutureCallback<HttpResponse> callback) {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);
        if (parameters != null) parameters.forEach(rb::addParameter);
        if (headers != null) headers.forEach(rb::setHeader);
        if (payload != null) rb.setEntity(new StringEntity(serializeAsJsonString(payload), ContentType.APPLICATION_JSON));
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        executeHttp(rb.build(), callback);
    }

    private void executeHttp(String method, String url, Map<String, String> parameters, Map<String, String> headers, InputStream is, String key, int timeoutMillis, FutureCallback<HttpResponse> callback) {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);
        if (parameters != null) parameters.forEach(rb::addParameter);
        if (headers != null) headers.forEach(rb::setHeader);
        if (StringUtils.isNotBlank(key) && is != null) {
            rb.setEntity(
                    MultipartEntityBuilder.create()
                            .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                            .addBinaryBody(key, is, ContentType.APPLICATION_FORM_URLENCODED, key)
                            .build()
            );
        }
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        executeHttp(rb.build(), callback);
    }

    public void executeHttp(HttpUriRequest request, FutureCallback<HttpResponse> callback) {
        executeHttp(request, new IterableHttpContext(), callback);
    }

    public void executeHttp(HttpUriRequest request, IterableHttpContext context, FutureCallback<HttpResponse> callback) {
        executeHttp(request, new RequestAwareAsyncResponseConsumer(), context, callback);
    }

    public void executeHttp(HttpUriRequest request, HttpAsyncResponseConsumer<HttpResponse> consumer, IterableHttpContext context, FutureCallback<HttpResponse> callback) {
        log.debug("executing {}...", request);
        validateObjectNotNull(consumer, "async response consumer");
        RestContext rc = RestScope.getRestContext();
        if (rc.getContextHeaders() != null && !rc.getContextHeaders().isEmpty()) {
            rc.getContextHeaders().forEach(request::addHeader);
        }
        if (context != null) {
            context.setAttribute(REQUEST_KEY, request);
            context.setAttribute(REQUEST_TIME_KEY, System.currentTimeMillis());
        }
        http.execute(HttpAsyncMethods.create(request), consumer, context, callback);
    }

    public void executeHttp(HttpAsyncRequestProducer producer, HttpAsyncResponseConsumer<HttpResponse> consumer, IterableHttpContext context, FutureCallback<HttpResponse> callback) {
        log.debug("executing {}...", producer);
        validateObjectNotNull(producer, "request producer");
        validateObjectNotNull(consumer, "async response consumer");
        http.execute(producer, consumer, context, callback);
    }

    @Override
    public HttpResponse executeHttp(HttpUriRequest request) throws Exception {
        log.debug("executing {}...", request);
        RestContext rc = RestScope.getRestContext();
        if (rc.getContextHeaders() != null && !rc.getContextHeaders().isEmpty()) {
            rc.getContextHeaders().forEach(request::addHeader);
        }
        HttpResponse response = http.execute(request, SYNC_CB).get();
        return intercept(request, response);
    }

    @Override
    public void finalize() {
        if (http != null) {
            try {
                http.close();
            } catch (IOException e) {
                log.error("failed to close http client", e);
            }
        }
    }

}
