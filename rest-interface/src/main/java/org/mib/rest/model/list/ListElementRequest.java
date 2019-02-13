package org.mib.rest.model.list;

import org.mib.rest.utils.QueryEscaper;
import com.google.common.collect.Sets;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by dufei on 18/1/24.
 */
@Data
@Builder
public class ListElementRequest {
    private long offset;
    private long limit;
    private String orderBy;
    private String ordering;
    private List<Filter> filters;
    private List<Sorter> sorters;
    private Map<String, Set<Object>> ins;

    public ListElementRequest escaped() {
        if (filters != null) {
            filters.stream().filter(filter -> filter.getValue() instanceof String).forEach(
                    filter -> filter.setValue(QueryEscaper.sqlEscape((String) filter.getValue()))
            );
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
