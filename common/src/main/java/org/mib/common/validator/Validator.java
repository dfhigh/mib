package org.mib.common.validator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;

@Slf4j
public class Validator {

    public static void validateConditionTrue(boolean condition, String identity) throws IllegalArgumentException {
        if (condition) return;
        throw new IllegalArgumentException(identity + " not true");
    }

    public static void validateConditionFalse(boolean condition, String identity) throws IllegalArgumentException {
        if (!condition) return;
        throw new IllegalArgumentException(identity + " not false");
    }

    public static void validateIntPositive(int num, String identity) throws IllegalArgumentException {
        validateIntRange(num, identity, 1, Integer.MAX_VALUE);
    }

    public static void validateIntNotNegative(int num, String identity) throws IllegalArgumentException {
        validateIntRange(num, identity, 0, Integer.MAX_VALUE);
    }

    public static void validateIntRange(int num, String identity, int lower, int upper) throws IllegalArgumentException {
        if (num < lower || num > upper) {
            throw new IllegalArgumentException(identity + " " + num + " not in range [" + lower + "," + upper + "]");
        }
    }

    public static void validateLongPositive(long num, String identity) throws IllegalArgumentException {
        validateLongRange(num, identity, 1, Long.MAX_VALUE);
    }

    public static void validateLongNotNegative(long num, String identity) throws IllegalArgumentException {
        validateLongRange(num, identity, 0, Long.MAX_VALUE);
    }

    public static void validateLongRange(long num, String identity, long lower, long upper) throws IllegalArgumentException {
        if (num < lower || num > upper) {
            throw new IllegalArgumentException(identity + " " + num + " not in range [" + lower + "," + upper + "]");
        }
    }

    public static void validateDoublePositive(double num, String identity) throws IllegalArgumentException {
        if (num <= 0.0d) {
            throw new IllegalArgumentException(identity + " " + num + " must be positive");
        }
    }

    public static void validateDoubleNotNegative(double num, String identity) throws IllegalArgumentException {
        validateDoubleRange(num, identity, 0.0d, Double.MAX_VALUE);
    }

    public static void validateDoubleRange(double num, String identity, double lower, double upper) throws IllegalArgumentException {
        if (num < lower || num > upper) {
            throw new IllegalArgumentException(identity + " " + num + " not in range [" + lower + "," + upper + "]");
        }
    }

    public static void validateStringNotBlank(String str, String identity) throws IllegalArgumentException {
        if (StringUtils.isBlank(str)) {
            throw new IllegalArgumentException(identity + " can't be blank");
        }
    }

    public static void validateStringNoLongerThan(String str, String identity, int len) throws IllegalArgumentException {
        if (str == null) return;
        validateIntRange(str.length(), identity + " length", 0, len);
    }

    public static void validateStringNotBlankNoLongerThan(String str, String identity, int len) throws IllegalArgumentException {
        validateStringNotBlank(str, identity);
        validateStringNoLongerThan(str, identity, len);
    }

    public static void validateObjectNotNull(Object obj, String identity) throws IllegalArgumentException {
        if (obj == null) {
            throw new IllegalArgumentException(identity + " can't be null");
        }
    }

    public static void validateCollectionNotEmptyContainsNoNull(Collection<?> collection, String identity) throws IllegalArgumentException {
        validateCollectionNotEmptyContainsNoNullNoLargerThan(collection, identity, Integer.MAX_VALUE);
    }

    public static void validateCollectionNotEmptyContainsNoNullNoLargerThan(Collection<?> collection, String identity, int size) throws IllegalArgumentException {
        validateObjectNotNull(collection, identity);
        validateIntRange(collection.size(), identity + " size", 1, size);
        collection.forEach(e -> validateObjectNotNull(e, identity + " element"));
    }
}
