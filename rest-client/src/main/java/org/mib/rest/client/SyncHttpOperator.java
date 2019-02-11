package org.mib.rest.client;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

import static org.mib.common.validator.Validator.validateIntPositive;

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
    protected HttpResponse executeHttp(HttpUriRequest request) throws Exception {
        log.debug("executing {}...", request);
        HttpResponse response = http.execute(request);
        log.debug("executed {} with response {}", request, response);
        return response;
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
