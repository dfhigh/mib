package org.mib.rest.model.list;

import lombok.Getter;

public enum Operator {

    LT("<"), LTE("<="), EQ("="), GTE(">="), GT(">"), NE("<>");

    private @Getter String expr;

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
            default:
                throw new IllegalArgumentException("unknown operator " + expr);
        }
    }
}
