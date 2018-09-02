package org.mib.rest.model;

import org.mib.rest.utils.QueryEscaper;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.Set;

/**
 * Created by dufei on 18/1/24.
 */
@Data
@Builder
public class ListElementRequest {
    private String name;
    private long offset;
    private long limit;
    private String orderBy;
    private String ordering;
    private Map<String, Object> params;
    private Map<String, Set<Object>> ins;

    public ListElementRequest escaped() {
        if (name != null) name = QueryEscaper.sqlEscape(name);
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof String) {
                    String str = (String) entry.getValue();
                    entry.setValue(QueryEscaper.sqlEscape(str));
                }
            }
        }
        if (ins != null) {
            for (Map.Entry<String, Set<Object>> entry : ins.entrySet()) {
                Set<Object> set = Sets.newHashSetWithExpectedSize(entry.getValue().size());
                for (Object obj : entry.getValue()) {
                    if (obj instanceof String) {
                        set.add(QueryEscaper.sqlEscape((String) obj));
                    } else {
                        set.add(obj);
                    }
                }
                entry.setValue(set);
            }
        }
        return this;
    }
}
