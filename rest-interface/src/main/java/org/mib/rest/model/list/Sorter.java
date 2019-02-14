package org.mib.rest.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sorter {
    private String field;
    private Order order;

    public static Sorter ascending(final String field) {
        return new Sorter(field, Order.ASC);
    }

    public static Sorter descending(final String field) {
        return new Sorter(field, Order.DESC);
    }
}
