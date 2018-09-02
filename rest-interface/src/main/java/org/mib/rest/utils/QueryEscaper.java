package org.mib.rest.utils;

import com.google.common.escape.Escaper;
import com.google.common.escape.Escapers;

/**
 * Created by dufei on 18/3/1.
 */
public class QueryEscaper {

    private static final Escaper SQL_ESCAPER = Escapers.builder()
            .addEscape('\\', "\\\\")
            .addEscape('%', "\\%")
            .addEscape('_', "\\_")
            .addEscape('"', "\"")
            .addEscape('\'', "\\'")
            .build();

    public static String sqlEscape(String original) {
        if (original == null) return null;
        return SQL_ESCAPER.escape(original);
    }
}
