package org.mib.rest.client.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

import static org.mib.rest.utils.ResponseInterceptor.intercept;

/**
 * Created by dufei on 18/5/9.
 */
@Slf4j
public abstract class HttpResponseHandler<T> implements FutureCallback<HttpResponse> {

    @Override
    public void completed(HttpResponse result) {
        if (result == null) return;
        try {
            result = intercept(result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        onSuccess(convert(result));
    }

    @Override
    public void failed(Exception ex) {
        log.error("", ex);
        throw new RuntimeException(ex);
    }

    @Override
    public void cancelled() {
        // we shouldn't get here
        throw new IllegalStateException("request cancelled");
    }

    protected abstract T convert(HttpResponse response);

    /**
     * handle 200 responses, implementations must consume the http entity, aka, stream must be closed.
     * @param payload converted payload
     */
    protected abstract void onSuccess(T payload);

}
