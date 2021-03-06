package org.mib.rest.model.list;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum Operator {

    LT("<"), LTE("<="), EQ("="), GTE(">="), GT(">"), NE("<>"), LIKE("like");

    private @Getter @JsonValue String expr;

    Operator(final String expr) {
        this.expr = expr;
    }

    static Operator fromExpr(String expr) {
        if (expr == null || expr.isEmpty()) {
            throw new IllegalArgumentException("operator expression can't be empty");
        }
        switch (expr) {
            case "<":
                return LT;
            case "<=":
                return LTE;
            case "=":
                return EQ;
            case ">=":
                return GTE;
            case ">":
                return GT;
            case "<>":
            case "!=":
                return NE;
            case "like":
            case "LIKE":
                return LIKE;
            default:
                throw new IllegalArgumentException("unknown operator " + expr);
        }
    }
}
