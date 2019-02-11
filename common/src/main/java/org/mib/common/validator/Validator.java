package org.mib.common.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@Slf4j
public class Validator {

    public static void validateIntPositive(int num, String identity) throws IllegalArgumentException {
        if (num <= 0) {
            throw new IllegalArgumentException(identity + " " + num + " must be positive");
        }
    }

    public static void validateIntNotNegative(int num, String identity) throws IllegalArgumentException {
        if (num < 0) {
            throw new IllegalArgumentException(identity + " " + num + " can't be negative");
        }
    }

    public static void validateLongPositive(long num, String identity) throws IllegalArgumentException {
        if (num <= 0) {
            throw new IllegalArgumentException(identity + " " + num + " must be positive");
        }
    }

    public static void validateLongNotNegative(long num, String identity) throws IllegalArgumentException {
        if (num < 0) {
            throw new IllegalArgumentException(identity + " " + num + " can't be negative");
        }
    }

    public static void validateDoublePositive(double num, String identity) throws IllegalArgumentException {
        if (num <= 0.0d) {
            throw new IllegalArgumentException(identity + " " + num + " must be positive");
        }
    }

    public static void validateDoubleNotNegative(double num, String identity) throws IllegalArgumentException {
        if (num < 0.0d) {
            throw new IllegalArgumentException(identity + " " + num + " can't be negative");
        }
    }

    public static void validateStringNotBlank(String str, String identity) throws IllegalArgumentException {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(identity + " can't be blank");
        }
    }

    public static void validateObjectNotNull(Object obj, String identity) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException(identity + " can't be null");
        }
    }

    public static void validateCollectionNotEmptyContainsNoNull(Collection<?> collection, String identity) throws IllegalArgumentException {
        validateObjectNotNull(collection, identity);
        if (collection.isEmpty()) {
            throw new IllegalArgumentException(identity + " can't be empty");
        }
        for (Object obj : collection) {
            if (obj == null) {
                throw new IllegalArgumentException(identity + " " + collection + " contains null");
            }
        }
    }
}
