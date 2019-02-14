package org.mib.rest.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private String field;
    private Operator operator;
    private Object value;

    public static Filter eq(final String field, final Object value) {
        return new Filter(field, Operator.EQ, value);
    }

    public static Filter ne(final String field, final Object value) {
        return new Filter(field, Operator.NE, value);
    }

    public static Filter lt(final String field, final Object value) {
        return new Filter(field, Operator.LT, value);
    }

    public static Filter lte(final String field, final Object value) {
        return new Filter(field, Operator.LTE, value);
    }

    public static Filter gt(final String field, final Object value) {
        return new Filter(field, Operator.GT, value);
    }

    public static Filter gte(final String field, final Object value) {
        return new Filter(field, Operator.GTE, value);
    }
}
