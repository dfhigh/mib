package org.mib.rest.context;

import lombok.Data;

/**
 * Created by hasee on 2018/2/26.
 */
@Data
public class ProphetContext implements Cloneable {

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
    public ProphetContext clone() {
        ProphetContext pc = new ProphetContext();
        pc.setToken(token);
        pc.setRequestId(requestId);
        pc.setPermanentKey(permanentKey);
        return pc;
    }
}
