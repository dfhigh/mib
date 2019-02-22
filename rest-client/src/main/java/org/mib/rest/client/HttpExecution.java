package org.mib.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static org.mib.common.ser.Serdes.deserializeFromJson;
import static org.mib.common.ser.Serdes.serializeAsJsonString;
import static org.mib.common.validator.Validator.validateCollectionNotEmptyContainsNoNull;
import static org.mib.common.validator.Validator.validateIntNotNegative;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

@NotThreadSafe
public class HttpExecution {

    private static volatile HttpOperator HTTP;

    private final RequestBuilder rb;

    private HttpExecution(final RequestBuilder rb) {
        validateObjectNotNull(rb, "request builder");
        this.rb = rb;
    }

    private static HttpOperator getHttp() {
        if (HTTP == null) {
            synchronized (HttpExecution.class) {
                if (HTTP == null) {
                    HTTP = new SyncHttpOperator();
                }
            }
        }
        return HTTP;
    }

    public static HttpExecution method(String method, String url) {
        validateStringNotBlank(method, "http method");
        validateStringNotBlank(url, "url");
        return new HttpExecution(RequestBuilder.create(method).setUri(url));
    }

    public static HttpExecution get(String url) {
        return method(HttpGet.METHOD_NAME, url);
    }

    public static HttpExecution post(String url) {
        return method(HttpPost.METHOD_NAME, url);
    }

    public static HttpExecution put(String url) {
        return method(HttpPut.METHOD_NAME, url);
    }

    public static HttpExecution patch(String url) {
        return method(HttpPatch.METHOD_NAME, url);
    }

    public static HttpExecution delete(String url) {
        return method(HttpDelete.METHOD_NAME, url);
    }

    public HttpExecution param(String key, String value) {
        return param(new BasicNameValuePair(key, value));
    }

    public HttpExecution param(NameValuePair nvp) {
        validateObjectNotNull(nvp, "name value pair");
        validateStringNotBlank(nvp.getName(), "param key");
        rb.addParameter(nvp);
        return this;
    }

    public HttpExecution params(Collection<NameValuePair> params) {
        validateCollectionNotEmptyContainsNoNull(params, "parameters");
        params.forEach(this::param);
        return this;
    }

    public HttpExecution header(String key, String value) {
        return header(new BasicHeader(key, value));
    }

    public HttpExecution header(Header header) {
        validateObjectNotNull(header, "header");
        validateStringNotBlank(header.getName(), "header key");
        rb.addHeader(header);
        return this;
    }

    public HttpExecution headers(Collection<Header> headers) {
        validateCollectionNotEmptyContainsNoNull(headers, "headers");
        headers.forEach(this::header);
        return this;
    }

    public HttpExecution timeoutMillis(int timeoutMillis) {
        validateIntNotNegative(timeoutMillis, "timeout in milliseconds");
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        return this;
    }

    public HttpExecution jsonBody(Object payload) {
        validateObjectNotNull(payload, "json payload");
        rb.setEntity(new StringEntity(serializeAsJsonString(payload), ContentType.APPLICATION_JSON));
        return this;
    }

    public HttpExecution textBody(String text) {
        return textBody(text, StandardCharsets.UTF_8);
    }

    public HttpExecution textBody(String text, Charset charset) {
        validateStringNotBlank(text, "text payload");
        validateObjectNotNull(charset, "charset");
        rb.setEntity(new StringEntity(text, charset));
        return this;
    }

    public HttpExecution textBody(String text, ContentType contentType) {
        validateStringNotBlank(text, "text payload");
        validateObjectNotNull(contentType, "content type");
        rb.setEntity(new StringEntity(text, contentType));
        return this;
    }

    public HttpExecution rawBody(byte[] payload) {
        validateObjectNotNull(payload, "byte payload");
        rb.setEntity(new ByteArrayEntity(payload));
        return this;
    }

    public HttpExecution rawBody(byte[] payload, ContentType contentType) {
        validateObjectNotNull(payload, "byte payload");
        validateObjectNotNull(contentType, "content type");
        rb.setEntity(new ByteArrayEntity(payload, contentType));
        return this;
    }

    public HttpExecution streamBody(String key, InputStream is) {
        validateStringNotBlank(key, "stream key");
        validateObjectNotNull(is, "input stream");
        rb.setEntity(MultipartEntityBuilder.create().setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                .addBinaryBody(key, is, ContentType.APPLICATION_FORM_URLENCODED, key).build());
        return this;
    }

    public HttpResponse execute(HttpOperator http) throws Exception {
        validateObjectNotNull(http, "http operator");
        http.contextInjection(rb);
        return http.executeHttpWithRetry(rb.build());
    }

    public HttpResponse execute() throws Exception {
        return execute(getHttp());
    }

    public HttpEntity executeForEntity(HttpOperator http) throws Exception {
        return execute(http).getEntity();
    }

    public HttpEntity executeForEntity() throws Exception {
        return executeForEntity(getHttp());
    }

    public InputStream executeForStream(HttpOperator http) throws Exception {
        return executeForEntity(http).getContent();
    }

    public InputStream executeForStream() throws Exception {
        return executeForStream(getHttp());
    }

    public byte[] executeForRaw(HttpOperator http) throws Exception {
        return EntityUtils.toByteArray(executeForEntity(http));
    }

    public byte[] executeForRaw() throws Exception {
        return executeForRaw(getHttp());
    }

    public <T> T executeForJson(HttpOperator http, Class<T> clazz) throws Exception {
        return deserializeFromJson(executeForRaw(http), clazz);
    }

    public <T> T executeForJson(Class<T> clazz) throws Exception {
        return executeForJson(getHttp(), clazz);
    }

    public <T> T executeForJson(HttpOperator http, TypeReference<T> tr) throws Exception {
        return deserializeFromJson(executeForRaw(http), tr);
    }

    public <T> T executeForJson(TypeReference<T> tr) throws Exception {
        return executeForJson(getHttp(), tr);
    }

    public void executeVoid(HttpOperator http) throws Exception {
        EntityUtils.consumeQuietly(executeForEntity(http));
    }

    public void executeVoid() throws Exception {
        executeVoid(getHttp());
    }
}
