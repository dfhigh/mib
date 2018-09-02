package org.mib.rest.client.callback;

import org.mib.rest.context.IterableHttpContext;
import org.apache.http.HttpResponse;
import org.apache.http.nio.protocol.BasicAsyncResponseConsumer;
import org.apache.http.protocol.HttpContext;

import static org.mib.rest.utils.ResponseInterceptor.REQUEST_KEY;
import static org.mib.rest.utils.ResponseInterceptor.REQUEST_TIME_KEY;

public class RequestAwareAsyncResponseConsumer extends BasicAsyncResponseConsumer {

    @Override
    protected HttpResponse buildResult(final HttpContext context) {
        HttpResponse response = super.buildResult(context);
        if (context instanceof IterableHttpContext) {
            IterableHttpContext ihc = (IterableHttpContext) context;
            ihc.forEach(entry -> response.setHeader(entry.getKey(), entry.getValue().toString()));
        } else {
            Object request = context.removeAttribute(REQUEST_KEY);
            if (request != null) response.setHeader(REQUEST_KEY, request.toString());
            Object requestTime = context.removeAttribute(REQUEST_TIME_KEY);
            if (requestTime != null) response.setHeader(REQUEST_TIME_KEY, requestTime.toString());
        }
        return response;
    }
}
