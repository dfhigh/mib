package org.mib.rest.context;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

@Data
public class RestContext implements Cloneable {

    private Map<String, String> contextHeaders = Maps.newHashMap();

    @Override
    public RestContext clone() throws CloneNotSupportedException {
        RestContext rc = (RestContext) super.clone();
        if (contextHeaders != null) rc.setContextHeaders(Maps.newHashMap(contextHeaders));
        return rc;
    }
}
