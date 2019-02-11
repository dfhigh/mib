package org.mib.rest.client.retry;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;

import java.util.Set;
import java.util.concurrent.TimeoutException;

import static org.mib.common.validator.Validator.validateIntNotNegative;
import static org.mib.common.validator.Validator.validateIntPositive;
import static org.mib.common.validator.Validator.validateObjectNotNull;
import static org.mib.common.validator.Validator.validateStringNotBlank;

public class BasicRetryStrategy implements RetryStrategy {

    private final int maxAttemptsAllowed;
    private final int baseRetryIntervalMillis;
    private final double retryIntervalBackoffRate;
    private final Set<String> retryableMethods;
    private final Set<Class<? extends Exception>> retryableExceptions;
    private final Set<Integer> retryableStatusCodes;

    public BasicRetryStrategy(final int maxAttemptsAllowed, final int baseRetryIntervalMillis,
                              final double retryIntervalBackoffRate, final Set<String> retryableMethods,
                              final Set<Class<? extends Exception>> retryableExceptions, final Set<Integer> retryableStatusCodes) {
        validateIntPositive(maxAttemptsAllowed, "max attempts");
        validateIntNotNegative(baseRetryIntervalMillis, "base retry interval milliseconds");
        if (retryIntervalBackoffRate < 1.0d) throw new IllegalArgumentException("retry interval backoff rate can't be smaller than 1");
        validateObjectNotNull(retryableMethods, "retryable request http methods");
        validateObjectNotNull(retryableExceptions, "retryable exception classes");
        validateObjectNotNull(retryableStatusCodes, "retryable http status codes");
        this.maxAttemptsAllowed = maxAttemptsAllowed;
        this.baseRetryIntervalMillis = baseRetryIntervalMillis;
        this.retryIntervalBackoffRate = retryIntervalBackoffRate;
        this.retryableMethods = ImmutableSet.copyOf(retryableMethods);
        this.retryableExceptions = ImmutableSet.copyOf(retryableExceptions);
        this.retryableStatusCodes = ImmutableSet.copyOf(retryableStatusCodes);
    }

    @Override
    public int getRetryIntervalMillis(HttpRequest req, HttpResponse res, Exception e, int attempts) {
        validateObjectNotNull(req, "request");
        validateIntPositive(attempts, "attempt count");
        if (attempts > maxAttemptsAllowed) return -1;
        if (!retryableMethods.contains(req.getRequestLine().getMethod())) return -1;
        if (e != null && !classContained(e.getClass())) return -1;
        if (!retryableStatusCodes.contains(res.getStatusLine().getStatusCode())) return -1;
        return (int) (baseRetryIntervalMillis * Math.pow(retryIntervalBackoffRate, attempts));
    }

    private boolean classContained(Class clazz) {
        while (clazz != Object.class) {
            if (retryableExceptions.contains(clazz)) return true;
            clazz = clazz.getSuperclass();
        }
        return false;
    }

    public static Builder builder() {
        return new Builder();
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Builder {
        private int maxAttemptsAllowed = 3;
        private int baseRetryIntervalMillis = 100;
        private double retryIntervalBackoffRate = 2.0;
        private Set<String> retryableMethods = Sets.newHashSet(HttpGet.METHOD_NAME);
        @SuppressWarnings("unchecked")
        private Set<Class<? extends Exception>> retryableExceptions = Sets.newHashSet(TimeoutException.class);
        private Set<Integer> retryableStatusCodes = Sets.newHashSet(
                // request timeout
                408,
                // too many requests
                429,
                // bad gateway
                502,
                // service unavailable
                503,
                // gateway timeout
                504,
                // insufficient storage
                507,
                // bandwidth limit exceeded
                509
        );

        public RetryStrategy build() {
            return new BasicRetryStrategy(maxAttemptsAllowed, baseRetryIntervalMillis, retryIntervalBackoffRate,
                    retryableMethods, retryableExceptions, retryableStatusCodes);
        }

        public Builder maxAttemptsAllowed(int maxAttemptsAllowed) {
            validateIntPositive(maxAttemptsAllowed, "max attempts allowed");
            this.maxAttemptsAllowed = maxAttemptsAllowed;
            return this;
        }

        public Builder baseRetryIntervalMillis(int baseRetryIntervalMillis) {
            validateIntNotNegative(baseRetryIntervalMillis, "base retry interval milliseconds");
            this.baseRetryIntervalMillis = baseRetryIntervalMillis;
            return this;
        }

        public Builder retryIntervalBackoffRate(double retryIntervalBackoffRate) {
            if (retryIntervalBackoffRate < 1.0d) throw new IllegalArgumentException("retry interval backoff rate can't be smaller than 1");
            this.retryIntervalBackoffRate = retryIntervalBackoffRate;
            return this;
        }

        public Builder retryableMethods(Set<String> retryableMethods) {
            validateObjectNotNull(retryableMethods, "retryable methods");
            if (this.retryableMethods == null) this.retryableMethods = retryableMethods;
            else this.retryableMethods.addAll(retryableMethods);
            return this;
        }

        public Builder retryableMethod(String method) {
            validateStringNotBlank(method, "http method");
            if (retryableMethods == null) retryableMethods = Sets.newHashSet();
            retryableMethods.add(method);
            return this;
        }

        public Builder retryableExceptions(Set<Class<? extends Exception>> retryableExceptions) {
            validateObjectNotNull(retryableExceptions, "retryable exception classes");
            if (this.retryableExceptions == null) this.retryableExceptions = retryableExceptions;
            else this.retryableExceptions.addAll(retryableExceptions);
            return this;
        }

        public Builder retryableException(Class<? extends Exception> clazz) {
            validateObjectNotNull(clazz, "exception class");
            if (retryableExceptions == null) retryableExceptions = Sets.newHashSet();
            retryableExceptions.add(clazz);
            return this;
        }

        public Builder retryableStatusCodes(Set<Integer> retryableStatusCodes) {
            validateObjectNotNull(retryableStatusCodes, "retryable status codes");
            if (this.retryableStatusCodes == null) this.retryableStatusCodes = retryableStatusCodes;
            else this.retryableStatusCodes.addAll(retryableStatusCodes);
            return this;
        }

        public Builder retryableStatusCode(int statusCode) {
            validateIntPositive(statusCode, "http status code");
            if (retryableStatusCodes == null) retryableStatusCodes = Sets.newHashSet();
            retryableStatusCodes.add(statusCode);
            return this;
        }
    }
}
