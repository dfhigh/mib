package org.mib.rest.context;

import static org.mib.common.validator.Validator.validateObjectNotNull;

public class RestScope {

    private static final ThreadLocal<RestContext> CONTEXT_HOLDER = ThreadLocal.withInitial(RestContext::new);

    public static RestContext getRestContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void clear() {
        CONTEXT_HOLDER.remove();
    }

    public static void setRestContext(RestContext rc) {
        validateObjectNotNull(rc, "rest context");
        CONTEXT_HOLDER.set(rc);
    }

    public static String getContextHeader(String key) {
        validateObjectNotNull(key, "key");
        return getRestContext().getContextHeaders().get(key);
    }

    public static void setContextHeader(String key, String value) {
        validateObjectNotNull(key, "key");
        validateObjectNotNull(value, "value");
        getRestContext().getContextHeaders().put(key, value);
    }
}
