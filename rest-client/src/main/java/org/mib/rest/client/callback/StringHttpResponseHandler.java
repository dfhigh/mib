package org.mib.rest.client.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static org.mib.rest.utils.Validator.validateObjectNotNull;

@Slf4j
public abstract class StringHttpResponseHandler extends HttpResponseHandler<String> {

    private final Charset charset;

    public StringHttpResponseHandler() {
        this(StandardCharsets.UTF_8);
    }

    public StringHttpResponseHandler(final Charset charset) {
        validateObjectNotNull(charset, "charset");
        this.charset = charset;
    }

    @Override
    protected String convert(HttpResponse response) {
        try {
            return EntityUtils.toString(response.getEntity(), charset);
        } catch (IOException e) {
            EntityUtils.consumeQuietly(response.getEntity());
            log.error("failed to convert http response to string", e);
            throw new RuntimeException(e);
        }
    }

}
