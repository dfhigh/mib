package org.mib.rest.client.callback;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

import static org.mib.rest.utils.Serdes.deserializeFromJson;
import static org.mib.rest.utils.Validator.validateObjectNotNull;

@Slf4j
public abstract class JsonHttpResponseHandler<T> extends HttpResponseHandler<T> {

    private final JavaType type;

    public JsonHttpResponseHandler(final Class<T> clazz) {
        validateObjectNotNull(clazz, "type class");
        this.type = TypeFactory.defaultInstance().constructType(clazz);
    }

    public JsonHttpResponseHandler(final TypeReference<T> tr) {
        validateObjectNotNull(tr, "type reference");
        this.type = TypeFactory.defaultInstance().constructType(tr);
    }

    @Override
    protected T convert(HttpResponse response) {
        try {
            return deserializeFromJson(EntityUtils.toByteArray(response.getEntity()), type);
        } catch (IOException e) {
            EntityUtils.consumeQuietly(response.getEntity());
            log.error("failed to deserialize response as {}", type, e);
            throw new RuntimeException(e);
        }
    }

}
