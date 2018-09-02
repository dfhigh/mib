package org.mib.rest.client.callback;

import org.apache.http.HttpResponse;

public abstract class DirectHttpResponseHandler extends HttpResponseHandler<HttpResponse> {

    @Override
    protected HttpResponse convert(HttpResponse response) {
        return response;
    }

}
