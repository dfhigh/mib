package org.mib.rest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.util.EntityUtils;

import java.io.InputStream;
import java.util.Collection;

import static org.mib.common.ser.Serdes.deserializeFromJson;
import static org.mib.common.ser.Serdes.serializeAsJsonString;

/**
 * Created by dufei on 17/12/1.
 */
@Slf4j
public abstract class HttpOperator {

    private static final TypeFactory TF = TypeFactory.defaultInstance();

    /*******************************************************************************************************************
     HTTP GET
     ******************************************************************************************************************/

    public <T> T getFor(String url, Class<T> clazz) throws Exception {
        return getFor(url, null, clazz);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, Class<T> clazz) throws Exception {
        return getFor(url, parameters, null, clazz);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, int timeoutMillis, Class<T> clazz) throws Exception {
        return getFor(url, parameters, null, timeoutMillis, clazz);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Class<T> clazz) throws Exception {
        return getFor(url, parameters, headers, 0, clazz);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpGet.METHOD_NAME, url, parameters, headers, null, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T getFor(String url, TypeReference<T> tr) throws Exception {
        return getFor(url, null, tr);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, TypeReference<T> tr) throws Exception {
        return getFor(url, parameters, null, tr);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return getFor(url, parameters, null, timeoutMillis, tr);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, TypeReference<T> tr) throws Exception {
        return getFor(url, parameters, headers, 0, tr);
    }

    public <T> T getFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpGet.METHOD_NAME, url, parameters, headers, null, timeoutMillis, TF.constructType(tr));
    }

    public byte[] getForRaw(String url) throws Exception {
        return getForRaw(url, null);
    }

    public byte[] getForRaw(String url, Collection<NameValuePair> parameters) throws Exception {
        return getForRaw(url, parameters, null);
    }

    public byte[] getForRaw(String url, Collection<NameValuePair> parameters, int timeoutMillis) throws Exception {
        return getForRaw(url, parameters, null, timeoutMillis);
    }

    public byte[] getForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return getForRaw(url, parameters, headers, 0);
    }

    public byte[] getForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpGet.METHOD_NAME, url, parameters, headers, null, timeoutMillis);
    }

    public InputStream getForStream(String url) throws Exception {
        return getForStream(url, null);
    }

    public InputStream getForStream(String url, Collection<NameValuePair> parameters) throws Exception {
        return getForStream(url, parameters, null);
    }

    public InputStream getForStream(String url, Collection<NameValuePair> parameters, int timeoutMillis) throws Exception {
        return getForStream(url, parameters, null, timeoutMillis);
    }

    public InputStream getForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return getForStream(url, parameters, headers, 0);
    }

    public InputStream getForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpGet.METHOD_NAME, url, parameters, headers, null, timeoutMillis);
    }

    /*******************************************************************************************************************
     HTTP POST
     ******************************************************************************************************************/

    public <T> T postFor(String url, Class<T> clazz) throws Exception {
        return postFor(url, null, clazz);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Class<T> clazz) throws Exception {
        return postFor(url, parameters, null, clazz);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Class<T> clazz) throws Exception {
        return postFor(url, parameters, headers, null, 0, clazz);
    }

    public <T> T postFor(String url, Object payload, Class<T> clazz) throws Exception {
        return postFor(url, null, null, payload, 0, clazz);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Object payload, Class<T> clazz) throws Exception {
        return postFor(url, parameters, null, payload, 0, clazz);
    }

    public <T> T postFor(String url, InputStream is, String key, Class<T> clazz) throws Exception {
        return postFor(url, null, null, is, key, 0, clazz);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, Class<T> clazz) throws Exception {
        return postFor(url, parameters, null, is, key, 0, clazz);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T postFor(String url, TypeReference<T> tr) throws Exception {
        return postFor(url, null, tr);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, TypeReference<T> tr) throws Exception {
        return postFor(url, parameters, null, tr);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, TypeReference<T> tr) throws Exception {
        return postFor(url, parameters, headers, null, 0, tr);
    }

    public <T> T postFor(String url, Object payload, TypeReference<T> tr) throws Exception {
        return postFor(url, null, null, payload, 0, tr);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Object payload, TypeReference<T> tr) throws Exception {
        return postFor(url, parameters, null, payload, 0, tr);
    }

    public <T> T postFor(String url, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return postFor(url, null, null, is, key, 0, tr);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return postFor(url, parameters, null, is, key, 0, tr);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(tr));
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(tr));
    }

    public byte[] postForRaw(String url) throws Exception {
        return postForRaw(url, null);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters) throws Exception {
        return postForRaw(url, parameters, null);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return postForRaw(url, parameters, headers, null, 0);
    }

    public byte[] postForRaw(String url, Object payload) throws Exception {
        return postForRaw(url, null, null, payload, 0);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return postForRaw(url, parameters, null, payload, 0);
    }

    public byte[] postForRaw(String url, InputStream is, String key) throws Exception {
        return postForRaw(url, null, null, is, key, 0);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return postForRaw(url, parameters, null, is, key, 0);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public InputStream postForStream(String url) throws Exception {
        return postForStream(url, null);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters) throws Exception {
        return postForStream(url, parameters, null);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return postForStream(url, parameters, headers, null, 0);
    }

    public InputStream postForStream(String url, Object payload) throws Exception {
        return postForStream(url, null, null, payload, 0);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return postForStream(url, parameters, null, payload, 0);
    }

    public InputStream postForStream(String url, InputStream is, String key) throws Exception {
        return postForStream(url, null, null, is, key, 0);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return postForStream(url, parameters, null, is, key, 0);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public void postVoid(String url) throws Exception {
        postVoid(url, null);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters) throws Exception {
        postVoid(url, parameters, null);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        postVoid(url, parameters, headers, null, 0);
    }

    public void postVoid(String url, Object payload) throws Exception {
        postVoid(url, null, null, payload, 0);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        postVoid(url, parameters, null, payload, 0);
    }

    public void postVoid(String url, InputStream is, String key) throws Exception {
        postVoid(url, null, null, is, key, 0);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        postVoid(url, parameters, null, is, key, 0);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T postFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(tr));
    }

    public byte[] postForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public InputStream postForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public void postVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    /*******************************************************************************************************************
     HTTP PUT
     ******************************************************************************************************************/

    public <T> T putFor(String url, Class<T> clazz) throws Exception {
        return putFor(url, null, clazz);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Class<T> clazz) throws Exception {
        return putFor(url, parameters, null, clazz);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Class<T> clazz) throws Exception {
        return putFor(url, parameters, headers, null, 0, clazz);
    }

    public <T> T putFor(String url, Object payload, Class<T> clazz) throws Exception {
        return putFor(url, null, null, payload, 0, clazz);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Object payload, Class<T> clazz) throws Exception {
        return putFor(url, parameters, null, payload, 0, clazz);
    }

    public <T> T putFor(String url, InputStream is, String key, Class<T> clazz) throws Exception {
        return putFor(url, null, null, is, key, 0, clazz);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, Class<T> clazz) throws Exception {
        return putFor(url, parameters, null, is, key, 0, clazz);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T putFor(String url, TypeReference<T> tr) throws Exception {
        return putFor(url, null, tr);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, TypeReference<T> tr) throws Exception {
        return putFor(url, parameters, null, tr);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, TypeReference<T> tr) throws Exception {
        return putFor(url, parameters, headers, null, 0, tr);
    }

    public <T> T putFor(String url, Object payload, TypeReference<T> tr) throws Exception {
        return putFor(url, null, null, payload, 0, tr);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Object payload, TypeReference<T> tr) throws Exception {
        return putFor(url, parameters, null, payload, 0, tr);
    }

    public <T> T putFor(String url, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return putFor(url, null, null, is, key, 0, tr);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return putFor(url, parameters, null, is, key, 0, tr);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(tr));
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(tr));
    }

    public byte[] putForRaw(String url) throws Exception {
        return putForRaw(url, null);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters) throws Exception {
        return putForRaw(url, parameters, null);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return putForRaw(url, parameters, headers, null, 0);
    }

    public byte[] putForRaw(String url, Object payload) throws Exception {
        return putForRaw(url, null, null, payload, 0);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return putForRaw(url, parameters, null, payload, 0);
    }

    public byte[] putForRaw(String url, InputStream is, String key) throws Exception {
        return putForRaw(url, null, null, is, key, 0);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return putForRaw(url, parameters, null, is, key, 0);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public byte[] putForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public InputStream putForStream(String url) throws Exception {
        return putForStream(url, null);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters) throws Exception {
        return putForStream(url, parameters, null);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return putForStream(url, parameters, headers, null, 0);
    }

    public InputStream putForStream(String url, Object payload) throws Exception {
        return putForStream(url, null, null, payload, 0);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return putForStream(url, parameters, null, payload, 0);
    }

    public InputStream putForStream(String url, InputStream is, String key) throws Exception {
        return putForStream(url, null, null, is, key, 0);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return putForStream(url, parameters, null, is, key, 0);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public void putVoid(String url) throws Exception {
        putVoid(url, null);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters) throws Exception {
        putVoid(url, parameters, null);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        putVoid(url, parameters, headers, null, 0);
    }

    public void putVoid(String url, Object payload) throws Exception {
        putVoid(url, null, null, payload, 0);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        putVoid(url, parameters, null, payload, 0);
    }

    public void putVoid(String url, InputStream is, String key) throws Exception {
        putVoid(url, null, null, is, key, 0);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        putVoid(url, parameters, null, is, key, 0);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPut.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPut.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(tr));
    }

    public byte[] putFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public InputStream putForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public void putVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    /*******************************************************************************************************************
     HTTP PATCH
     ******************************************************************************************************************/

    public <T> T patchFor(String url, Class<T> clazz) throws Exception {
        return patchFor(url, null, clazz);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Class<T> clazz) throws Exception {
        return patchFor(url, parameters, null, clazz);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Class<T> clazz) throws Exception {
        return patchFor(url, parameters, headers, null, 0, clazz);
    }

    public <T> T patchFor(String url, Object payload, Class<T> clazz) throws Exception {
        return patchFor(url, null, null, payload, 0, clazz);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Object payload, Class<T> clazz) throws Exception {
        return patchFor(url, parameters, null, payload, 0, clazz);
    }

    public <T> T patchFor(String url, InputStream is, String key, Class<T> clazz) throws Exception {
        return patchFor(url, null, null, is, key, 0, clazz);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, Class<T> clazz) throws Exception {
        return patchFor(url, parameters, null, is, key, 0, clazz);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T patchFor(String url, TypeReference<T> tr) throws Exception {
        return patchFor(url, null, tr);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, TypeReference<T> tr) throws Exception {
        return patchFor(url, parameters, null, tr);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, TypeReference<T> tr) throws Exception {
        return patchFor(url, parameters, headers, null, 0, tr);
    }

    public <T> T patchFor(String url, Object payload, TypeReference<T> tr) throws Exception {
        return patchFor(url, null, null, payload, 0, tr);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Object payload, TypeReference<T> tr) throws Exception {
        return patchFor(url, parameters, null, payload, 0, tr);
    }

    public <T> T patchFor(String url, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return patchFor(url, null, null, is, key, 0, tr);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, InputStream is, String key, TypeReference<T> tr) throws Exception {
        return patchFor(url, parameters, null, is, key, 0, tr);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(tr));
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis, TF.constructType(tr));
    }

    public byte[] patchForRaw(String url) throws Exception {
        return patchForRaw(url, null);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters) throws Exception {
        return patchForRaw(url, parameters, null);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return patchForRaw(url, parameters, headers, null, 0);
    }

    public byte[] patchForRaw(String url, Object payload) throws Exception {
        return patchForRaw(url, null, null, payload, 0);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return patchForRaw(url, parameters, null, payload, 0);
    }

    public byte[] patchForRaw(String url, InputStream is, String key) throws Exception {
        return patchForRaw(url, null, null, is, key, 0);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return patchForRaw(url, parameters, null, is, key, 0);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public byte[] patchForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public InputStream patchForStream(String url) throws Exception {
        return patchForStream(url, null);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters) throws Exception {
        return patchForStream(url, parameters, null);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return patchForStream(url, parameters, headers, null, 0);
    }

    public InputStream patchForStream(String url, Object payload) throws Exception {
        return patchForStream(url, null, null, payload, 0);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return patchForStream(url, parameters, null, payload, 0);
    }

    public InputStream patchForStream(String url, InputStream is, String key) throws Exception {
        return patchForStream(url, null, null, is, key, 0);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        return patchForStream(url, parameters, null, is, key, 0);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public void patchVoid(String url) throws Exception {
        patchVoid(url, null);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters) throws Exception {
        patchVoid(url, parameters, null);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        patchVoid(url, parameters, headers, null, 0);
    }

    public void patchVoid(String url, Object payload) throws Exception {
        patchVoid(url, null, null, payload, 0);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        patchVoid(url, parameters, null, payload, 0);
    }

    public void patchVoid(String url, InputStream is, String key) throws Exception {
        patchVoid(url, null, null, is, key, 0);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, InputStream is, String key) throws Exception {
        patchVoid(url, parameters, null, is, key, 0);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPatch.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPatch.METHOD_NAME, url, parameters, headers, is, key, timeoutMillis);
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(tr));
    }

    public byte[] patchFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public InputStream patchForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public void patchVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    /*******************************************************************************************************************
     HTTP DELETE
     ******************************************************************************************************************/

    public <T> T deleteFor(String url, Class<T> clazz) throws Exception {
        return deleteFor(url, null, clazz);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Class<T> clazz) throws Exception {
        return deleteFor(url, parameters, null, clazz);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Class<T> clazz) throws Exception {
        return deleteFor(url, parameters, headers, null, 0, clazz);
    }

    public <T> T deleteFor(String url, Object payload, Class<T> clazz) throws Exception {
        return deleteFor(url, null, null, payload, 0, clazz);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Object payload, Class<T> clazz) throws Exception {
        return deleteFor(url, parameters, null, payload, 0, clazz);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T deleteFor(String url, TypeReference<T> tr) throws Exception {
        return deleteFor(url, null, tr);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, TypeReference<T> tr) throws Exception {
        return deleteFor(url, parameters, null, tr);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, TypeReference<T> tr) throws Exception {
        return deleteFor(url, parameters, headers, null, 0, tr);
    }

    public <T> T deleteFor(String url, Object payload, TypeReference<T> tr) throws Exception {
        return deleteFor(url, null, null, payload, 0, tr);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Object payload, TypeReference<T> tr) throws Exception {
        return deleteFor(url, parameters, null, payload, 0, tr);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis, TF.constructType(tr));
    }

    public byte[] deleteForRaw(String url) throws Exception {
        return deleteForRaw(url, null);
    }

    public byte[] deleteForRaw(String url, Collection<NameValuePair> parameters) throws Exception {
        return deleteForRaw(url, parameters, null);
    }

    public byte[] deleteForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return deleteForRaw(url, parameters, headers, null, 0);
    }

    public byte[] deleteForRaw(String url, Object payload) throws Exception {
        return deleteForRaw(url, null, null, payload, 0);
    }

    public byte[] deleteForRaw(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return deleteForRaw(url, parameters, null, payload, 0);
    }

    public byte[] deleteForRaw(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public InputStream deleteForStream(String url) throws Exception {
        return deleteForStream(url, null);
    }

    public InputStream deleteForStream(String url, Collection<NameValuePair> parameters) throws Exception {
        return deleteForStream(url, parameters, null);
    }

    public InputStream deleteForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        return deleteForStream(url, parameters, headers, null, 0);
    }

    public InputStream deleteForStream(String url, Object payload) throws Exception {
        return deleteForStream(url, null, null, payload, 0);
    }

    public InputStream deleteForStream(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        return deleteForStream(url, parameters, null, payload, 0);
    }

    public InputStream deleteForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public void deleteVoid(String url) throws Exception {
        deleteVoid(url, null);
    }

    public void deleteVoid(String url, Collection<NameValuePair> parameters) throws Exception {
        deleteVoid(url, parameters, null);
    }

    public void deleteVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers) throws Exception {
        deleteVoid(url, parameters, headers, null, 0);
    }

    public void deleteVoid(String url, Object payload) throws Exception {
        deleteVoid(url, null, null, payload, 0);
    }

    public void deleteVoid(String url, Collection<NameValuePair> parameters, Object payload) throws Exception {
        deleteVoid(url, parameters, null, payload, 0);
    }

    public void deleteVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpDelete.METHOD_NAME, url, parameters, headers, payload, timeoutMillis);
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, Class<T> clazz) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(clazz));
    }

    public <T> T deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, TypeReference<T> tr) throws Exception {
        return executeHttpFor(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis, TF.constructType(tr));
    }

    public byte[] deleteFor(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForRaw(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public InputStream deleteForStream(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForStream(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    public void deleteVoid(String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        executeHttpVoid(HttpPost.METHOD_NAME, url, parameters, headers, formKeyValues, timeoutMillis);
    }

    private <T> T executeHttpFor(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis, JavaType type) throws Exception {
        HttpEntity entity = executeHttpForEntity(method, url, parameters, headers, payload, timeoutMillis);
        return deserializeFromJson(EntityUtils.toByteArray(entity), type);
    }

    private <T> T executeHttpFor(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis, JavaType type) throws Exception {
        HttpEntity entity = executeHttpForEntity(method, url, parameters, headers, formKeyValues, timeoutMillis);
        return deserializeFromJson(EntityUtils.toByteArray(entity), type);
    }

    private <T> T executeHttpFor(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis, JavaType type) throws Exception {
        HttpEntity entity = executeHttpForEntity(method, url, parameters, headers, is, key, timeoutMillis);
        return deserializeFromJson(EntityUtils.toByteArray(entity), type);
    }

    private byte[] executeHttpForRaw(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return EntityUtils.toByteArray(executeHttpForEntity(method, url, parameters, headers, payload, timeoutMillis));
    }

    private byte[] executeHttpForRaw(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return EntityUtils.toByteArray(executeHttpForEntity(method, url, parameters, headers, formKeyValues, timeoutMillis));
    }

    private byte[] executeHttpForRaw(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return EntityUtils.toByteArray(executeHttpForEntity(method, url, parameters, headers, is, key, timeoutMillis));
    }

    private InputStream executeHttpForStream(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        return executeHttpForEntity(method, url, parameters, headers, payload, timeoutMillis).getContent();
    }

    private InputStream executeHttpForStream(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        return executeHttpForEntity(method, url, parameters, headers, formKeyValues, timeoutMillis).getContent();
    }

    private InputStream executeHttpForStream(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        return executeHttpForEntity(method, url, parameters, headers, is, key, timeoutMillis).getContent();
    }

    private void executeHttpVoid(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        EntityUtils.consumeQuietly(executeHttpForEntity(method, url, parameters, headers, payload, timeoutMillis));
    }

    private void executeHttpVoid(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        EntityUtils.consumeQuietly(executeHttpForEntity(method, url, parameters, headers, formKeyValues, timeoutMillis));
    }

    private void executeHttpVoid(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        EntityUtils.consumeQuietly(executeHttpForEntity(method, url, parameters, headers, is, key, timeoutMillis));
    }

    private HttpEntity executeHttpForEntity(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Object payload, int timeoutMillis) throws Exception {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);
        if (parameters != null) parameters.forEach(rb::addParameter);
        if (headers != null) headers.forEach(h -> rb.addHeader(h.getName(), h.getValue()));
        if (payload != null) rb.setEntity(new StringEntity(serializeAsJsonString(payload), ContentType.APPLICATION_JSON));
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        return executeHttp(rb.build()).getEntity();
    }

    private HttpEntity executeHttpForEntity(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, Collection<NameValuePair> formKeyValues, int timeoutMillis) throws Exception {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);
        if (parameters != null) parameters.forEach(rb::addParameter);
        if (headers != null) headers.forEach(h -> rb.addHeader(h.getName(), h.getValue()));
        if (formKeyValues != null) rb.setEntity(new UrlEncodedFormEntity(formKeyValues));
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        return executeHttp(rb.build()).getEntity();
    }

    private HttpEntity executeHttpForEntity(String method, String url, Collection<NameValuePair> parameters, Collection<NameValuePair> headers, InputStream is, String key, int timeoutMillis) throws Exception {
        RequestBuilder rb = RequestBuilder.create(method).setUri(url);
        if (parameters != null) parameters.forEach(rb::addParameter);
        if (headers != null) headers.forEach(h -> rb.addHeader(h.getName(), h.getValue()));
        if (StringUtils.isNotBlank(key) && is != null) {
            rb.setEntity(
                    MultipartEntityBuilder.create()
                            .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                            .addBinaryBody(key, is, ContentType.APPLICATION_FORM_URLENCODED, key)
                            .build()
            );
        }
        if (timeoutMillis > 0) {
            RequestConfig rc = RequestConfig.copy(RequestConfig.DEFAULT).setConnectTimeout(timeoutMillis).setSocketTimeout(timeoutMillis).build();
            rb.setConfig(rc);
        }
        return executeHttp(rb.build()).getEntity();
    }

    public abstract HttpResponse executeHttp(HttpUriRequest request) throws Exception;

}