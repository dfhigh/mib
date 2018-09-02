package org.mib.rest.context;

import org.slf4j.MDC;

import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

public class RestScope {

    private static final ThreadLocal<RestContext> CONTEXT_HOLDER = ThreadLocal.withInitial(RestContext::new);

    public static RestContext getProphetContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
        MDC.remove(RestContext.REQUEST_ID_HEADER);
    }

    public static void setProphetContext(RestContext pc) {
        validateObjectNotNull(pc, "prophet context");
        CONTEXT_HOLDER.set(pc);
    }

    public static String getToken() {
        return getProphetContext().getToken();
    }

    public static void setUserToken(String token) {
        validateStringNotBlank(token, "user token");
        RestContext pc = getProphetContext();
        pc.setToken(token);
    }

    public static void setPermanentKey(String permanentKey) {
        validateStringNotBlank(permanentKey, "permanentKey");
        getProphetContext().setPermanentKey(permanentKey);
    }

    public static String getPermanentKey() { return getProphetContext().getPermanentKey(); }

    public static String getRequestId() {
        return getProphetContext().getRequestId();
    }

    public static void setRequestId(String requestId) {
        validateStringNotBlank(requestId, "requestId");
        getProphetContext().setRequestId(requestId);
        MDC.put(RestContext.REQUEST_ID_HEADER, requestId);
    }
}
