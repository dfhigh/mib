package org.mib.rest.context;

import com.google.common.collect.Maps;
import org.apache.http.protocol.HttpContext;

import java.util.Iterator;
import java.util.Map;

public class IterableHttpContext implements HttpContext, Iterable<Map.Entry<String, Object>> {

    private final Map<String, Object> map;

    public IterableHttpContext() {
        this.map = Maps.newConcurrentMap();
    }

    @Override
    public Object getAttribute(String id) {
        return map.get(id);
    }

    @Override
    public void setAttribute(String id, Object obj) {
        if (obj == null) map.remove(id);
        else map.put(id, obj);
    }

    @Override
    public Object removeAttribute(String id) {
        return map.remove(id);
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return map.entrySet().iterator();
    }

    public void clear() {
        map.clear();
    }

    @Override
    public String toString() {
        return map.toString();
    }
}
