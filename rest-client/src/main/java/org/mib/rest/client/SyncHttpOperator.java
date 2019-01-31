package org.mib.rest.client;

import org.mib.rest.context.RestContext;
import org.mib.rest.context.RestScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.rest.utils.ResponseInterceptor.intercept;

/**
 * Created by dufei on 18/5/8.
 */
@Slf4j
@ThreadSafe
public class SyncHttpOperator extends HttpOperator {

    private final CloseableHttpClient http;

    public SyncHttpOperator() {
        this(DEFAULT_CONN, DEFAULT_CONN_PER_ROUTE);
    }

    public SyncHttpOperator(int maxConn, int maxConnPerRoute) {
        validateIntPositive(maxConn, "maxConn");
        validateIntPositive(maxConnPerRoute, "maxConnPerRoute");
        this.http = HttpClients.custom().setMaxConnTotal(maxConn).setMaxConnPerRoute(maxConnPerRoute).build();
    }

    @Override
    public HttpResponse executeHttp(HttpUriRequest request) throws Exception {
        log.debug("executing {}...", request);
        RestContext rc = RestScope.getRestContext();
        if (rc.getContextHeaders() != null && !rc.getContextHeaders().isEmpty()) {
            rc.getContextHeaders().forEach(request::addHeader);
        }
        HttpResponse response = http.execute(request);
        log.debug("executed {} with response {}", request, response);
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
