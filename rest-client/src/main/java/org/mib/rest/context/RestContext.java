package org.mib.rest.context;

import lombok.Data;

@Data
public class RestContext implements Cloneable {

    public static final String PROPHET_AUTH_HEADER = "X-Prophet-Auth";
    public static final String REQUEST_ID_HEADER = "X-Prophet-Tracing-v1";
    public static final String COOKIE_HEADER = "Cookie";
    public static final String TOKEN_KEY = "User-Token";
    public static final String PERMANENT_KEY = "Access-Key";

    private String token;
    private String requestId;
    private String permanentKey;

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public RestContext clone() {
        RestContext pc = new RestContext();
        pc.setToken(token);
        pc.setRequestId(requestId);
        pc.setPermanentKey(permanentKey);
        return pc;
    }
}
