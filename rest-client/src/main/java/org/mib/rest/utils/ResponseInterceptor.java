package org.mib.rest.utils;

import org.mib.rest.client.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;
import org.mib.rest.exception.BadRequestException;
import org.mib.rest.exception.ForbiddenException;
import org.mib.rest.exception.ResourceNotFoundException;
import org.mib.rest.exception.UnauthorizedException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mib.common.ser.Serdes.deserializeFromJson;

/**
 * Created by dufei on 18/5/9.
 */
@Slf4j
public class ResponseInterceptor {

    public static final String REQUEST_KEY = "request";
    public static final String REQUEST_TIME_KEY = "requestTime";
    public static final String REQUEST_LATENCY_KEY = "requestLatency";

    public static HttpResponse intercept(HttpUriRequest request, HttpResponse response) throws Exception {
        return intercept(request.toString(), response);
    }

    public static HttpResponse intercept(HttpResponse response) throws Exception {
        Header header = response.getFirstHeader(REQUEST_KEY);
        String request = header == null ? null : header.getValue();
        return intercept(request, response);
    }

    private static HttpResponse intercept(String request, HttpResponse response) throws Exception {
        StatusLine sl = response.getStatusLine();
        switch (sl.getStatusCode()) {
            case HttpStatus.SC_OK:
            case HttpStatus.SC_ACCEPTED:
            case HttpStatus.SC_CREATED:
                return response;
            case HttpStatus.SC_BAD_REQUEST:
                String msg = extractErrorMessage(EntityUtils.toByteArray(response.getEntity()));
                log.warn("bad request for request {} with msg {}", request, msg);
                throw new BadRequestException(msg);
            case HttpStatus.SC_UNAUTHORIZED:
                msg = extractErrorMessage(EntityUtils.toByteArray(response.getEntity()));
                log.warn("unauthorized for request {} with msg {}", request, msg);
                throw new UnauthorizedException(msg);
            case HttpStatus.SC_FORBIDDEN:
                msg = extractErrorMessage(EntityUtils.toByteArray(response.getEntity()));
                log.warn("permission denied for request {} with msg {}", request, msg);
                throw new ForbiddenException(msg);
            case HttpStatus.SC_NOT_FOUND:
                msg = extractErrorMessage(EntityUtils.toByteArray(response.getEntity()));
                log.warn("no resource found for {} with msg {}", request, msg);
                throw new ResourceNotFoundException(msg);
            case HttpStatus.SC_BAD_GATEWAY:
                EntityUtils.consumeQuietly(response.getEntity());
                log.error("bad gateway for request {}", request);
                throw new Exception("bad gateway for " + request);
            case HttpStatus.SC_GATEWAY_TIMEOUT:
                EntityUtils.consumeQuietly(response.getEntity());
                log.error("gateway timeout for {}", request);
                throw new Exception("gateway timeout for " + request);
            default:
                msg = extractErrorMessage(EntityUtils.toByteArray(response.getEntity()));
                log.error("internal server error for {} with msg {}", request, msg);
                throw new Exception(msg);
        }
    }

    private static String extractErrorMessage(byte[] bytes) {
        try {
            ErrorResponse er = deserializeFromJson(bytes, ErrorResponse.class);
            return er.getMsg() == null ? er.getError() : er.getMsg();
        } catch (IllegalArgumentException e) {
            return new String(bytes, UTF_8);
        }
    }
}
