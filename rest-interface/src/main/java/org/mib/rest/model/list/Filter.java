package org.mib.rest.model.list;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static org.mib.common.validator.Validator.validateStringNotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Filter {

    private String field;
    private Operator operator;
    private Object value;
    private String jsonKey;

    public static Filter eq(final String field, final Object value) {
        return eq(field, value, null);
    }

    public static Filter eq(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.EQ, value, jsonKey);
    }

    public static Filter ne(final String field, final Object value) {
        return ne(field, value, null);
    }

    public static Filter ne(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.NE, value, jsonKey);
    }

    public static Filter lt(final String field, final Object value) {
        return lt(field, value, null);
    }

    public static Filter lt(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.LT, value, jsonKey);
    }

    public static Filter lte(final String field, final Object value) {
        return lte(field, value, null);
    }

    public static Filter lte(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.LTE, value, jsonKey);
    }

    public static Filter gt(final String field, final Object value) {
        return gt(field, value, null);
    }

    public static Filter gt(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.GT, value, jsonKey);
    }

    public static Filter gte(final String field, final Object value) {
        return gte(field, value, null);
    }

    public static Filter gte(final String field, final Object value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        return new Filter(field, Operator.GTE, value, jsonKey);
    }

    public static Filter like(final String field, final String value) {
        return like(field, value, null);
    }

    public static Filter like(final String field, final String value, final String jsonKey) {
        validateStringNotBlank(field, "field name");
        validateStringNotBlank(value, "field search value");
        return new Filter(field, Operator.LIKE, value, jsonKey);
    }
}
