package org.mib.rest.client;

import org.mib.rest.context.RestContext;
import org.mib.rest.context.RestScope;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.annotation.concurrent.ThreadSafe;
import java.io.IOException;

import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.rest.context.RestContext.REQUEST_ID_HEADER;
import static org.mib.rest.context.RestContext.TOKEN_KEY;
import static org.mib.rest.context.RestContext.PERMANENT_KEY;
import static org.mib.rest.utils.ResponseInterceptor.intercept;

/**
 * Created by dufei on 18/5/8.
 */
@Slf4j
@ThreadSafe
public class SyncHttpOperator extends HttpOperator {

    private final CloseableHttpClient http;

    public SyncHttpOperator(int maxConn, int maxConnPerRoute) {
        validateIntPositive(maxConn, "maxConn");
        validateIntPositive(maxConnPerRoute, "maxConnPerRoute");
        this.http = HttpClients.custom().setMaxConnTotal(maxConn).setMaxConnPerRoute(maxConnPerRoute).build();
    }

    @Override
    public HttpResponse executeHttp(HttpUriRequest request) throws Exception {
        log.debug("executing {}...", request);
        RestContext pc = RestScope.getProphetContext();
        if (StringUtils.isNotBlank(pc.getToken()))
            request.addHeader(TOKEN_KEY, pc.getToken());
        if (StringUtils.isNotBlank(pc.getPermanentKey()))
            request.addHeader(PERMANENT_KEY, pc.getPermanentKey());
        if (StringUtils.isNotBlank(pc.getRequestId())) request.addHeader(REQUEST_ID_HEADER, pc.getRequestId());
        HttpResponse response = http.execute(request);
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
