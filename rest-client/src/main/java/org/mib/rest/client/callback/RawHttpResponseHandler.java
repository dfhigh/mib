package org.mib.rest.client.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public abstract class RawHttpResponseHandler extends HttpResponseHandler<byte[]> {

    @Override
    protected byte[] convert(HttpResponse response) {
        try {
            return EntityUtils.toByteArray(response.getEntity());
        } catch (IOException e) {
            EntityUtils.consumeQuietly(response.getEntity());
            log.error("failed to convert response to byte array", e);
            throw new RuntimeException(e);
        }
    }

}
