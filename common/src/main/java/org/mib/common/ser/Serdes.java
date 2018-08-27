package org.mib.common.ser;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.mib.common.validator.Validator.validateObjectNotNull;

@Slf4j
public class Serdes {

    private static final ObjectMapper OM = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
            .configure(JsonParser.Feature.ALLOW_NON_NUMERIC_NUMBERS, true);

    private static final ObjectWriter OW = OM.writerWithDefaultPrettyPrinter();

    public static String serializeAsJsonString(Object object) {
        validateObjectNotNull(object, "object");
        try {
            return OM.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to serialize object to json", e);
        }
    }

    public static String serializeAsPrettyJsonString(Object object) {
        validateObjectNotNull(object, "object");
        try {
            return OW.writeValueAsString(object);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to serialize object to json", e);
        }
    }

    public static byte[] serializeAsJsonBytes(Object object) {
        validateObjectNotNull(object, "object");
        try {
            return OM.writeValueAsBytes(object);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to serialize object to json", e);
        }
    }

    public static <T> T deserializeFromJson(String json, Class<T> clazz) {
        if (json == null) return null;
        try {
            return OM.readValue(json, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json " + json, e);
        }
    }

    public static <T> T deserializeFromJson(byte[] bytes, Class<T> clazz) {
        if (bytes == null) return null;
        try {
            return OM.readValue(bytes, clazz);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json bytes " + new String(bytes, UTF_8), e);
        }
    }

    public static <T> T deserializeFromJson(String json, TypeReference<T> tr) {
        if (json == null) return null;
        try {
            return OM.readValue(json, tr);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json " + json, e);
        }
    }

    public static <T> T deserializeFromJson(byte[] bytes, TypeReference<T> tr) {
        if (bytes == null) return null;
        try {
            return OM.readValue(bytes, tr);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json bytes " + new String(bytes, UTF_8), e);
        }
    }

    public static <T> T deserializeFromJson(String json, JavaType type) {
        if (json == null) return null;
        try {
            return OM.readValue(json, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json " + json, e);
        }
    }

    public static <T> T deserializeFromJson(byte[] bytes, JavaType type) {
        if (bytes == null) return null;
        try {
            return OM.readValue(bytes, type);
        } catch (IOException e) {
            throw new IllegalArgumentException("failed to deserialize from json bytes " + new String(bytes, UTF_8), e);
        }
    }
}
