package org.mib.rest.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.mib.common.validator.Validator.validateStringNotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sorter {

    private String field;
    private Order order;
    private String jsonKey;

    public static Sorter ascending(final String field) {
        return ascending(field, null);
    }

    public static Sorter ascending(final String field, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Sorter(field, Order.ASC, jsonKey);
    }

    public static Sorter descending(final String field) {
        return descending(field, null);
    }

    public static Sorter descending(final String field, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Sorter(field, Order.DESC, jsonKey);
    }
}
