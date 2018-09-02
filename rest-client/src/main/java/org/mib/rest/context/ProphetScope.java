package org.mib.rest.context;

import org.slf4j.MDC;

import static org.mib.rest.utils.Validator.validateObjectNotNull;
import static org.mib.rest.utils.Validator.validateStringNotBlank;

/**
 * Created by hasee on 2018/2/26.
 */
public class ProphetScope {

    private static final ThreadLocal<ProphetContext> CONTEXT_HOLDER = ThreadLocal.withInitial(ProphetContext::new);

    public static ProphetContext getProphetContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
        MDC.remove(ProphetContext.REQUEST_ID_HEADER);
    }

    public static void setProphetContext(ProphetContext pc) {
        validateObjectNotNull(pc, "prophet context");
        CONTEXT_HOLDER.set(pc);
    }

    public static String getToken() {
        return getProphetContext().getToken();
    }

    public static void setUserToken(String token) {
        validateStringNotBlank(token, "user token");
        ProphetContext pc = getProphetContext();
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
        MDC.put(ProphetContext.REQUEST_ID_HEADER, requestId);
    }
}
