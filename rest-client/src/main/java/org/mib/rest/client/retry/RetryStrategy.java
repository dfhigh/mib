package org.mib.rest.client.retry;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

@FunctionalInterface
public interface RetryStrategy {

    /**
     * @return base retry interval in milliseconds.
     * Negative result indicates we should NOT retry this request.
     */
    int getRetryIntervalMillis(final HttpRequest req, final HttpResponse res, final Exception e, final int attempts);
}
